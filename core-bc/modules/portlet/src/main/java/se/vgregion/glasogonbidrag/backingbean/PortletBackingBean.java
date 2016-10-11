package se.vgregion.glasogonbidrag.backingbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.vgregion.glasogonbidrag.util.FacesUtil;
import se.vgregion.glasogonbidrag.util.LiferayUtil;

import javax.annotation.PostConstruct;
import java.util.Locale;

/**
 * @author Erik Andersson - Monator Technologies AB
 */
@Component(value = "portletBackingBean")
//@Scope(value = "view", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Scope(value = "request")
public class PortletBackingBean {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(PortletBackingBean.class);

    @Autowired
    private FacesUtil facesUtil;

    @Autowired
    private LiferayUtil liferayUtil;

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
        locale = liferayUtil.getLocale();
        portletNamespace = liferayUtil.getPortletNamespace();
    }

}
