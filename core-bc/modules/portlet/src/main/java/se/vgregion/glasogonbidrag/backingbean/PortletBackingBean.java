package se.vgregion.glasogonbidrag.backingbean;

import com.liferay.portal.theme.ThemeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.portal.glasogonbidrag.domain.jpa.Invoice;
import se.vgregion.service.glasogonbidrag.domain.api.data.InvoiceRepository;
import se.vgregion.service.glasogonbidrag.domain.api.service.InvoiceService;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import java.util.Locale;

/**
 * @author Erik Andersson - Monator Technologies AB
 */
@Component(value = "portletBackingBean")
@Scope(value = "view", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PortletBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(PortletBackingBean.class);

    @Autowired
    private FacesUtil facesUtil;

    // Helpers
    private String portletNamespace;
    private Locale locale;

    // Getter and Setters for Helpers
    public String getPortletNamespace() {
        return portletNamespace;
    }

    public void setPortletNamespace(String portletNamespace) {
        this.portletNamespace = portletNamespace;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    // Initializer
    @PostConstruct
    public void init() {
        LOGGER.info("PortletBackingBean - init()");

        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();
        locale = themeDisplay.getLocale();
        // Temporary - make sure we always get Swedish locale
        locale = Locale.forLanguageTag("sv-SE");

        portletNamespace = FacesContext.getCurrentInstance()
                .getExternalContext().encodeNamespace("");
    }

}
