package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.PortletURLFactory;
import com.liferay.portlet.PortletURLFactoryUtil;
import org.ocpsoft.prettytime.PrettyTime;
import org.ocpsoft.prettytime.jsf.PrettyTimeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.portal.glasogonbidrag.domain.jpa.InvoiceStatus;
import se.vgregion.service.glasogonbidrag.api.data.InvoiceRepository;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Component(value = "listInvoicesViewBackingBean")
@Scope(value = "request")
public class ListInvoicesViewBackingBean {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private FacesUtil facesUtil;

    private List<Invoice> invoices;
    private Locale locale;
    private PrettyTime prettyTime;
    private Layout registerInvoiceLayout;

    public Layout getRegisterInvoiceLayout() {
        return registerInvoiceLayout;
    }

    public void setRegisterInvoiceLayout(Layout registerInvoiceLayout) {
        this.registerInvoiceLayout = registerInvoiceLayout;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public Locale getLocale() {
        return locale;
    }

    public PrettyTime getPrettyTime() {
        return prettyTime;
    }

    public void setPrettyTime(PrettyTime prettyTime) {
        this.prettyTime = prettyTime;
    }

    @PostConstruct
    protected void init() {

        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();
        locale = themeDisplay.getLocale();
        long userId = themeDisplay.getUserId();
        // Temporary - make sure we always get Swedish locale
        locale = Locale.forLanguageTag("sv-SE");

        prettyTime = new PrettyTime(locale);

        invoices = invoiceRepository.findAllOrderByModificationDate(userId);

        try {
            registerInvoiceLayout = LayoutLocalServiceUtil.getFriendlyURLLayout(themeDisplay.getScopeGroupId(), true, "/registrera-faktura");
        } catch (PortalException e) {
            e.printStackTrace();
        } catch (SystemException e) {
            e.printStackTrace();
        }
    }

    public String formatPrettyTime(Date date) {

        String prettyDateString = prettyTime.format(date);

        return prettyDateString;
    }

    public String createInvoiceUrl(Invoice invoice) {
        String invoiceUrl = "";

        FacesContext facesContext = FacesContext.getCurrentInstance();
        PortletRequest portletRequest = (PortletRequest) facesContext.getExternalContext().getRequest();

        String registerInvoicePortletId = "glasogonbidragcreateinvoice_WAR_glasogonbidragportlet";

        if(registerInvoiceLayout != null) {
            long registerInvoicePlid = registerInvoiceLayout.getPlid();

            PortletURL portletURL = PortletURLFactoryUtil.create(portletRequest, registerInvoicePortletId, registerInvoicePlid, PortletRequest.RENDER_PHASE);
            portletURL.setParameter("_facesViewIdRender", "/views/create_invoice/view_invoice.xhtml");
            portletURL.setParameter("invoiceId", String.valueOf(invoice.getId()));

            invoiceUrl = portletURL.toString();
        }

        return invoiceUrl;
    }


}
