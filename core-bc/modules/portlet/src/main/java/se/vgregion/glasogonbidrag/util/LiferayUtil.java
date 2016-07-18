package se.vgregion.glasogonbidrag.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.User;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


}
