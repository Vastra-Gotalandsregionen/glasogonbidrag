package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.domain.api.service.InvoiceService;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

/**
 * @author Erik Andersson - Monator Technologies AB
 */
@Component(value = "invoiceBackingBean")
@Scope(value = "view", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class InvoiceBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(InvoiceBackingBean.class);

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private FacesUtil facesUtil;

    // Main objects
    private Invoice invoice;

    // Editable fields
    private boolean noteEditing;
    private String note;

    // Getter and Setters for Main objects
    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getHtmlNote() {
        return note.replaceAll("\\n", "<br>");
    }

    public void setHtmlNote(String note) {
        this.note = note.replaceAll("<br>", "\n");
    }

    public boolean isNoteEditing() {
        return noteEditing;
    }

    public void setNoteEditing(boolean noteEditing) {
        this.noteEditing = noteEditing;
    }

    public boolean checkMayNewGrantsBeAdded(Invoice curInvoice) {
        boolean mayNewGrantsBeAdded = false;

        boolean isStatusInProgress = (curInvoice.getStatus() == InvoiceStatus.IN_PROGRESS);
        boolean isStillAmountLeft = (curInvoice.calculateDifferenceAsKrona().compareTo(BigDecimal.ZERO) != 0);

        mayNewGrantsBeAdded = isStatusInProgress && isStillAmountLeft;

        return mayNewGrantsBeAdded;
    }

    public boolean checkMayInvoiceBeChanged(Invoice curInvoice) {
        boolean mayInvoiceBeChanged = false;

        boolean isStatusInProgress = (curInvoice.getStatus() == InvoiceStatus.IN_PROGRESS);

        mayInvoiceBeChanged = isStatusInProgress;

        return mayInvoiceBeChanged;
    }

    public boolean checkMayInvoiceBeMarkedCompleted(Invoice curInvoice) {
        boolean mayInvoiceBeMarkedCompleted = false;

        boolean isStatusInProgress = (curInvoice.getStatus() == InvoiceStatus.IN_PROGRESS);
        boolean isStillAmountLeft = (curInvoice.calculateDifferenceAsKrona().compareTo(BigDecimal.ZERO) != 0);

        mayInvoiceBeMarkedCompleted = isStatusInProgress && !isStillAmountLeft;

        return mayInvoiceBeMarkedCompleted;
    }

    // Ajax listeners

    public void startEditNoteListener() {
        note = invoice.getNotes();

        noteEditing = true;
    }

    public void stopEditNoteListener() {
        note = invoice.getNotes();

        noteEditing = false;
    }

    public void saveInvoiceNoteListener() {
        invoice.setNotes(note);

        noteEditing = false;

        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();
        String caseWorker = themeDisplay.getUser().getScreenName();

        invoiceService.update(caseWorker, invoice);
    }

    // Initializer

    @PostConstruct
    public void init() {
        long invoiceId = facesUtil.fetchId("invoiceId");
        invoice = invoiceService.findWithParts(invoiceId);

        noteEditing = false;
        note = invoice.getNotes();
    }

}
