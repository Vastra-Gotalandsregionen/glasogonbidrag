package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.User;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.PortletURLFactoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.api.data.InvoiceRepository;

import javax.annotation.PostConstruct;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import java.util.List;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Component(value = "listInvoicesViewBackingBean")
@Scope(value = "view")
public class ListInvoicesViewBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ListInvoicesViewBackingBean.class);

    @Autowired
    private FacesUtil facesUtil;

    @Autowired
    private InvoiceRepository invoiceRepository;

    private long registerInvoicePlid;
    private String createInvoicePortletId;

    private List<Invoice> invoices;

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public String getUserNameById(long userId) {
        String userName = "";

        try {
            User user = UserLocalServiceUtil.getUser(userId);
            userName = user.getFullName();
        } catch (PortalException|SystemException e) {
            LOGGER.error(
                    "Cannot get name for user with id: {}, got exception: {}",
                    userId,
                    e.getMessage());
        }

        return userName;
    }

    public String navigate(long id) {
        PortletRequest request = facesUtil.getRequest();

        PortletURL url = PortletURLFactoryUtil.create(
                request,
                createInvoicePortletId,
                registerInvoicePlid,
                PortletRequest.RENDER_PHASE);
        url.setParameter(
                "_facesViewIdRender",
                "/views/create_invoice/view_invoice.xhtml");
        url.setParameter("invoiceId", Long.toString(id));

        LOGGER.info("navigate: {}", url.toString());

        return url.toString();
    }

    @PostConstruct
    protected void init() {
        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();
        long groupId = themeDisplay.getScopeGroupId();

        fetchInvoices();
        generateLinkIds(groupId);
    }

    private void fetchInvoices() {
        invoices = invoiceRepository.findAll();
    }

    private void generateLinkIds(long groupId) {
        registerInvoicePlid = getPlid(groupId);
        createInvoicePortletId =
                "glasogonbidragcreateinvoice_WAR_glasogonbidragportlet";
    }

    private long getPlid(long groupId) {
        Layout registerLayout = null;
        try {
            registerLayout = LayoutLocalServiceUtil.fetchLayoutByFriendlyURL(
                    groupId, true, "/registrera-faktura");
        } catch (SystemException e) {
            LOGGER.error(
                    "Cannot get registerInvoicePlid of registrera-faktura " +
                    "from layout local service util. Got exception: {}",
                    e.getMessage());
        }

        if (registerLayout != null) {
            return registerLayout.getPlid();
        } else {
            return 0;
        }
    }
}
