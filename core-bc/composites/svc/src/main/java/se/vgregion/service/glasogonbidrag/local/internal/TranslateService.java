package se.vgregion.service.glasogonbidrag.local.internal;

import org.springframework.stereotype.Service;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Aphakia;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Keratoconus;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.None;
import se.vgregion.portal.glasogonbidrag.domain.jpa.diagnose.Special;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class TranslateService {
    private static final Map<String, String> swedish;

    static {
        swedish = new HashMap<>();
        swedish.put("afaki", "diagnose-type-aphakia");
        swedish.put("keratokonus", "diagnose-type-keratoconus");
        swedish.put("special", "diagnose-type-special");
        swedish.put("barn", "diagnose-type-child");

        swedish.put("bilateral", "visual-laterality-bilateral");
        swedish.put("höger", "visual-laterality-right");
        swedish.put("vänster", "visual-laterality-left");
        swedish.put("ingen", "visual-laterality-none");

        swedish.put("ja", "yes");
        swedish.put("nej", "no");
    }

    public String translate(Locale locale, String string) {
        String input = string.toLowerCase();
        if ("sv".equals(locale.getLanguage())) {
            return translateFromSwedish(input);
        } else {
            throw new IllegalStateException("Language not supported");
        }
    }

    private String translateFromSwedish(String string) {
        return swedish.get(string);
    }
}
