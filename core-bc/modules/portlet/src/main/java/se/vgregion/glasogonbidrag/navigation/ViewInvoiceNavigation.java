package se.vgregion.glasogonbidrag.navigation;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.PortletURLFactoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.constants.GbConstants;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.glasogonbidrag.util.LiferayUtil;

import javax.annotation.PostConstruct;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

@Component("viewInvoiceNavigation")
@Scope("request")
public class ViewInvoiceNavigation {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ViewInvoiceNavigation.class);

    @Autowired
    private FacesUtil facesUtil;

    @Autowired
    private LiferayUtil liferayUtil;

    private long registerInvoicePlid;

    public String navigate(long id) {
        PortletRequest request = facesUtil.getRequest();

        PortletURL renderUrl = PortletURLFactoryUtil.create(request, GbConstants.CREATE_INVOICE_PORTLET_ID, registerInvoicePlid, PortletRequest.RENDER_PHASE);
        renderUrl.setParameter("_facesViewIdRender", GbConstants.VIEW_INVOICE_PAGE);
        renderUrl.setParameter("invoiceId", Long.toString(id));

        return renderUrl.toString();
    }

    @PostConstruct
    protected void init() {
        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();
        long groupId = themeDisplay.getScopeGroupId();

        registerInvoicePlid = liferayUtil.getPlidByFriendlyURL(groupId, true, GbConstants.REGISTER_INVOICE_FRIENDLY_URL);
    }
}
