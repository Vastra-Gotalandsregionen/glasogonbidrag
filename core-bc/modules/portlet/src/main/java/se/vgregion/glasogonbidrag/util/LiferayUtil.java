package se.vgregion.glasogonbidrag.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletRequest;
import java.util.Locale;
import java.util.Map;

@Component
@Scope(value = "prototype")
public class LiferayUtil {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(LiferayUtil.class);

    @Autowired
    private FacesUtil facesUtil;

    public String getUserNameById(long userId) {

        String userName = "";

        try {
            User user = UserLocalServiceUtil.getUser(userId);

            userName = user.getFullName();
        } catch (PortalException | SystemException e) {
            e.printStackTrace();
        }

        return userName;
    }

    public Long getPlidByFriendlyURL(long groupId , boolean privateLayout, String friendlyURL) {

        Long plid = null;

        try {
            Layout layout = LayoutLocalServiceUtil.fetchLayoutByFriendlyURL(
                    groupId, privateLayout, friendlyURL);

            plid = layout.getPlid();
        } catch (SystemException e) {
            e.printStackTrace();
        }

        return plid;
    }

    public Locale getLocale() {
        //Locale locale = null;
        ThemeDisplay themeDisplay = facesUtil.getThemeDisplay();
        Locale locale = themeDisplay.getLocale();
        // Temporary - make sure we always get Swedish locale
        locale = Locale.forLanguageTag("sv-SE");

        return locale;
    }

    public String getPortletNamespace() {
        return FacesContext.getCurrentInstance().getExternalContext().encodeNamespace("");
    }


}
