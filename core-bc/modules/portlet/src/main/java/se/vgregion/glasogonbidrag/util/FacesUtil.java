package se.vgregion.glasogonbidrag.util;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.Map;

@Component
@Scope(value = "prototype")
public class FacesUtil {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(FacesUtil.class);

    public ThemeDisplay getThemeDisplay() {
        ExternalContext externalContext =
                FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> requestMap = externalContext.getRequestMap();

        return (ThemeDisplay)requestMap.get(WebKeys.THEME_DISPLAY);
    }

    public Long fetchId(String paramterName) {
        ExternalContext externalContext =
                FacesContext.getCurrentInstance().getExternalContext();

        Map<String, String> parameterMap =
                externalContext.getRequestParameterMap();

        Long id = Long.parseLong(parameterMap.get(paramterName));

        LOGGER.info("FacesUtil - fetchId(): {}", id);

        return id;
    }

    public String fetchProperty(String parameterName) {

        ExternalContext externalContext =
                FacesContext.getCurrentInstance().getExternalContext();

        Map<String, String> parameterMap =
                externalContext.getRequestParameterMap();

        String value = parameterMap.get(parameterName);

        LOGGER.info("ViewBeneficiaryBackingBean - fetchProperty(): {}", value);

        return value;
    }
}
