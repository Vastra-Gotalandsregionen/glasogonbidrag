package se.vgregion.service.glasogonbidrag.local.internal;

import org.springframework.stereotype.Service;
import se.vgregion.service.glasogonbidrag.local.api.RegionResponsibilityLookupService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class RegionResponsibilityLookupServiceImpl
        implements RegionResponsibilityLookupService {
    private static Map<String, Integer> responsibility = new HashMap<>();
    static {
        responsibility.put("skövde", 35020);
        responsibility.put("göteborg", 34020);
        responsibility.put("borås", 33020);
        responsibility.put("vänersborg", 31020);
    }

    private static Map<String, List<String>> regionLists = new HashMap<>();
    static {
        regionLists.put(
                "skövde",
                Arrays.asList(
                        "essunga", "falköping", "grästorp", "gullspång",
                        "götene", "hjo", "karlsborg", "lidköping",
                        "mariestad", "skara", "skövde", "tibro", "tidaholm",
                        "töreboda", "vara"));
        regionLists.put(
                "göteborg",
                Arrays.asList(
                        "ale", "göteborg", "härryda", "kungälv",
                        "mölndal", "partille", "stenungsund", "tjörn",
                        "öckerö"));
        regionLists.put(
                "borås",
                Arrays.asList(
                        "alingsås", "bollebygd", "borås", "herrljunga",
                        "lerum", "lilla edet", "mark", "svenljunga", "tranemo",
                        "ulricehamn", "vårgårda"));
        regionLists.put(
                "vänersborg",
                Arrays.asList(
                        "bengtsfors", "dals-ed", "färgelanda", "lysekil",
                        "mellerud", "munkedal", "orust", "strömstad",
                        "sotenäs", "tanum", "trollhättan", "uddevalla",
                        "vänersborg", "åmål"));
    }

    public int lookupResponsibility(String municipality) {
        String region = lookupRegion(municipality);

        if (region != null) {
            return responsibility.get(region.toLowerCase());
        } else {
            return 0;
        }
    }

    public String lookupRegion(String municipality) {
        String result = null;

        Iterator<String> regionIterator = regionLists.keySet().iterator();

        while (result == null && regionIterator.hasNext()) {
            String region = regionIterator.next();

            List<String> municipalities = regionLists.get(region);

            if (municipalities.contains(municipality.toLowerCase())) {
                result = region;
            }
        }

        return result;
    }
}
