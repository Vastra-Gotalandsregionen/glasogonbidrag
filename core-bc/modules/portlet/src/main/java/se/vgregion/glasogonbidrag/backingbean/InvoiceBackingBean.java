package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.theme.ThemeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.flow.AddGrantFlowState;
import se.vgregion.glasogonbidrag.flow.CreateInvoiceAddGrantPidFlow;
import se.vgregion.glasogonbidrag.flow.action.AddGrantAction;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.glasogonbidrag.util.GrantUtil;
import se.vgregion.glasogonbidrag.util.TabUtil;
import se.vgregion.glasogonbidrag.validator.PersonalNumberValidator;
import se.vgregion.glasogonbidrag.viewobject.PrescriptionVO;
import se.vgregion.portal.glasogonbidrag.domain.DiagnoseType;
import se.vgregion.portal.glasogonbidrag.domain.InvoiceStatus;
import se.vgregion.portal.glasogonbidrag.domain.VisualLaterality;
import se.vgregion.portal.glasogonbidrag.domain.jpa.*;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Aphakia;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Keratoconus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Special;
import se.vgregion.portal.glasogonbidrag.domain.jpa.identification.Personal;
import se.vgregion.service.glasogonbidrag.domain.api.data.BeneficiaryRepository;
import se.vgregion.service.glasogonbidrag.domain.api.data.GrantRepository;
import se.vgregion.service.glasogonbidrag.domain.api.data.IdentificationRepository;
import se.vgregion.service.glasogonbidrag.domain.api.data.InvoiceRepository;
import se.vgregion.service.glasogonbidrag.domain.api.service.BeneficiaryService;
import se.vgregion.service.glasogonbidrag.domain.api.service.DiagnoseService;
import se.vgregion.service.glasogonbidrag.domain.api.service.GrantService;
import se.vgregion.service.glasogonbidrag.domain.api.service.InvoiceService;
import se.vgregion.service.glasogonbidrag.domain.exception.GrantAlreadyExistException;
import se.vgregion.service.glasogonbidrag.domain.exception.NoIdentificationException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Erik Andersson - Monator Technologies AB
 */
@Component(value = "invoiceBackingBean")
@Scope(value = "view", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class InvoiceBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(InvoiceBackingBean.class);

    @Autowired
    private InvoiceRepository invoiceRepository;

    private InvoiceService invoiceService;

    @Autowired
    private FacesUtil facesUtil;

    // Main objects
    private Invoice invoice;

    // Getter and Setters for Main objects
    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public boolean checkMayNewGrantsBeAdded(Invoice curInvoice) {
        LOGGER.info("InvoiceBackingBean - checkMayNewGrantsBeAdded()");

        boolean mayNewGrantsBeAdded = false;

        boolean isStatusInProgress = (curInvoice.getStatus() == InvoiceStatus.IN_PROGRESS);
        boolean isStillAmountLeft = (curInvoice.calculateDifferenceExcludingVatAsKrona().compareTo(BigDecimal.ZERO) != 0);

        mayNewGrantsBeAdded = isStatusInProgress && isStillAmountLeft;

        return mayNewGrantsBeAdded;
    }

    public boolean checkMayInvoiceBeChanged(Invoice curInvoice) {
        LOGGER.info("InvoiceBackingBean - checkMayInvoiceBeChanged()");

        boolean mayInvoiceBeChanged = false;

        boolean isStatusInProgress = (curInvoice.getStatus() == InvoiceStatus.IN_PROGRESS);

        mayInvoiceBeChanged = isStatusInProgress;

        return mayInvoiceBeChanged;
    }

    public boolean checkMayInvoicesBeMarkedCompleted(Invoice curInvoice) {
        LOGGER.info("InvoiceBackingBean - checkMayInvoicesBeMarkedCompleted()");

        boolean mayInvoicesBeMarkedCompleted = false;

        boolean isStatusInProgress = (curInvoice.getStatus() == InvoiceStatus.IN_PROGRESS);
        boolean isStillAmountLeft = (curInvoice.calculateDifferenceExcludingVatAsKrona().compareTo(BigDecimal.ZERO) != 0);

        mayInvoicesBeMarkedCompleted = isStatusInProgress && !isStillAmountLeft;

        return mayInvoicesBeMarkedCompleted;
    }


    // Initializer

    @PostConstruct
    public void init() {
        LOGGER.info("InvoiceBackingBean - init()");

        long invoiceId = facesUtil.fetchId("invoiceId");
        invoice = invoiceRepository.findWithParts(invoiceId);
    }

}
