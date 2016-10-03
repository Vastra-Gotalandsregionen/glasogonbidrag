package se.vgregion.service.glasogonbidrag.local.internal;

import org.springframework.stereotype.Service;
import se.vgregion.service.glasogonbidrag.local.api.AreaCodeLookupService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Martin Lind - Monator Technologies AB
 */
@Service
public class AreaCodeLookupServiceImpl implements AreaCodeLookupService {
    private static final Map<String, List<String>> countyToMunicipal;
    static {
        countyToMunicipal = new HashMap<>();

        // Stockholm's county
        countyToMunicipal.put("01", Arrays.asList(
                "0114", "0115", "0117", "0120", "0123", "0125", "0126",
                "0127", "0128", "0136", "0138", "0139", "0140", "0160",
                "0162", "0163", "0180", "0181", "0182", "0183", "0184",
                "0186", "0187", "0188", "0191", "0192"));
        // Uppsala's county
        countyToMunicipal.put("03", Arrays.asList(
                "0305", "0319", "0330", "0331", "0360", "0380", "0381",
                "0382"));
        // Södermanland's county
        countyToMunicipal.put("04", Arrays.asList(
                "0428", "0461", "0480", "0481", "0482", "0483", "0484",
                "0486", "0488"));
        // Östergötland's county
        countyToMunicipal.put("05", Arrays.asList(
                "0509", "0512", "0513", "0560", "0561", "0562", "0563",
                "0580", "0581", "0582", "0583", "0584", "0586"));
        // Jönköping's county
        countyToMunicipal.put("06",Arrays.asList(
                "0604", "0617", "0642", "0643", "0662", "0665", "0680",
                "0682", "0683", "0684", "0685", "0686", "0687"));
        // Kronoberg's county
        countyToMunicipal.put("07",Arrays.asList(
                "0760", "0761", "0763", "0764", "0765", "0767", "0780",
                "0781"));
        // Kalmar's county
        countyToMunicipal.put("08",Arrays.asList(
                "0821", "0834", "0840", "0860", "0861", "0862", "0880",
                "0881", "0882", "0883", "0884", "0885"));
        // Gotland's county
        countyToMunicipal.put("09",Arrays.asList(
                "0980"));
        // Blekinge's county
        countyToMunicipal.put("10", Arrays.asList(
                "1060", "1080", "1081", "1082", "1083"));
        // Skåne's county
        countyToMunicipal.put("12", Arrays.asList(
                "1214", "1230", "1231", "1233", "1256", "1257", "1260",
                "1261", "1262", "1263", "1264", "1265", "1266", "1267",
                "1270", "1272", "1273", "1275", "1276", "1277", "1278",
                "1280", "1281", "1282", "1283", "1284", "1285", "1286",
                "1287", "1290", "1291", "1292", "1293"));
        // Hallands's county
        countyToMunicipal.put("13", Arrays.asList(
                "1315", "1380", "1381", "1382", "1383", "1384"));
        // Västra Götaland's county
        countyToMunicipal.put("14", Arrays.asList(
                "1401", "1402", "1407", "1415", "1419", "1421", "1427",
                "1430", "1435", "1438", "1439", "1440", "1441", "1442",
                "1443", "1444", "1445", "1446", "1447", "1452", "1460",
                "1461", "1462", "1463", "1465", "1466", "1470", "1471",
                "1472", "1473", "1480", "1481", "1482", "1484", "1485",
                "1486", "1487", "1488", "1489", "1490", "1491", "1492",
                "1493", "1494", "1495", "1496", "1497", "1498", "1499"));
        // Värmland's county
        countyToMunicipal.put("17", Arrays.asList(
                "1715", "1730", "1737", "1760", "1761", "1762", "1763",
                "1764", "1765", "1766", "1780", "1781", "1782", "1783",
                "1784", "1785"));
        // Örebro's county
        countyToMunicipal.put("18", Arrays.asList(
                "1814", "1860", "1861", "1862", "1863", "1864", "1880",
                "1881", "1882", "1883", "1884", "1885"));
        // Västmanland's county
        countyToMunicipal.put("19", Arrays.asList(
                "1904", "1907", "1960", "1961", "1962", "1980", "1981",
                "1982", "1983", "1984"));
        // Dalarna's county
        countyToMunicipal.put("20", Arrays.asList(
                "2021", "2023", "2026", "2029", "2031", "2034", "2039",
                "2061", "2062", "2080", "2081", "2082", "2083", "2084",
                "2085"));
        // Gävleborg's county
        countyToMunicipal.put("21", Arrays.asList(
                "2101", "2104", "2121", "2132", "2161", "2180", "2181",
                "2182", "2183", "2184"));
        // Västernorrland's county
        countyToMunicipal.put("22", Arrays.asList(
                "2260", "2262", "2280", "2281", "2282", "2283", "2284"));
        // Jämtland's county
        countyToMunicipal.put("23", Arrays.asList(
                "2303", "2305", "2309", "2313", "2321", "2326", "2361",
                "2380"));
        // Västerbotten's county
        countyToMunicipal.put("24", Arrays.asList(
                "2401", "2403", "2404", "2409", "2417", "2418", "2421",
                "2422", "2425", "2460", "2462", "2463", "2480", "2481",
                "2482"));
        // Norrbotten's county
        countyToMunicipal.put("25", Arrays.asList(
                "2505", "2506", "2510", "2513", "2514", "2518", "2521",
                "2523", "2560", "2580", "2581", "2582", "2583", "2584"));
    }

    private static final Map<String, String> countyNames;
    static {
        countyNames = new HashMap<>();
        countyNames.put("01", "stockholms län");
        countyNames.put("03", "uppsala län");
        countyNames.put("04", "södermanlands län");
        countyNames.put("05", "östergötlands län");
        countyNames.put("06", "jönköpings län");
        countyNames.put("07", "kronobergs län");
        countyNames.put("08", "kalmar län");
        countyNames.put("09", "gotlands län");
        countyNames.put("10", "blekinge län");
        countyNames.put("12", "skåne län");
        countyNames.put("13", "hallands län");
        countyNames.put("14", "västra Götalands län");
        countyNames.put("17", "värmlands län");
        countyNames.put("18", "örebro län");
        countyNames.put("19", "västmanlands län");
        countyNames.put("20", "dalarnas län");
        countyNames.put("21", "gävleborgs län");
        countyNames.put("22", "västernorrlands län");
        countyNames.put("23", "jämtlands län");
        countyNames.put("24", "västerbottens län");
        countyNames.put("25", "norrbottens län");
    }

    private static final Map<String, String> municipallyNames;
    static {
        municipallyNames = new HashMap<>();
        municipallyNames.put("0114", "upplands väsby");
        municipallyNames.put("0115", "vallentuna");
        municipallyNames.put("0117", "österåker");
        municipallyNames.put("0120", "värmdö");
        municipallyNames.put("0123", "järfälla");
        municipallyNames.put("0125", "ekerö");
        municipallyNames.put("0126", "huddinge");
        municipallyNames.put("0127", "botkyrka");
        municipallyNames.put("0128", "salem");
        municipallyNames.put("0136", "haninge");
        municipallyNames.put("0138", "tyresö");
        municipallyNames.put("0139", "upplands-bro");
        municipallyNames.put("0140", "nykvarn");
        municipallyNames.put("0160", "täby");
        municipallyNames.put("0162", "danderyd");
        municipallyNames.put("0163", "sollentuna");
        municipallyNames.put("0180", "stockholm");
        municipallyNames.put("0181", "södertälje");
        municipallyNames.put("0182", "nacka");
        municipallyNames.put("0183", "sundbyberg");
        municipallyNames.put("0184", "solna");
        municipallyNames.put("0186", "lidingö");
        municipallyNames.put("0187", "vaxholm");
        municipallyNames.put("0188", "norrtälje");
        municipallyNames.put("0191", "sigtuna");
        municipallyNames.put("0192", "nynäshamn");
        municipallyNames.put("0305", "håbo");
        municipallyNames.put("0319", "älvkarleby");
        municipallyNames.put("0330", "knivsta");
        municipallyNames.put("0331", "heby");
        municipallyNames.put("0360", "tierp");
        municipallyNames.put("0380", "uppsala");
        municipallyNames.put("0381", "enköping");
        municipallyNames.put("0382", "östhammar");
        municipallyNames.put("0428", "vingåker");
        municipallyNames.put("0461", "gnesta");
        municipallyNames.put("0480", "nyköping");
        municipallyNames.put("0481", "oxelösund");
        municipallyNames.put("0482", "flen");
        municipallyNames.put("0483", "katrineholm");
        municipallyNames.put("0484", "eskilstuna");
        municipallyNames.put("0486", "strängnäs");
        municipallyNames.put("0488", "trosa");
        municipallyNames.put("0509", "ödeshög");
        municipallyNames.put("0512", "ydre");
        municipallyNames.put("0513", "kinda");
        municipallyNames.put("0560", "boxholm");
        municipallyNames.put("0561", "åtvidaberg");
        municipallyNames.put("0562", "finspång");
        municipallyNames.put("0563", "valdemarsvik");
        municipallyNames.put("0580", "linköping");
        municipallyNames.put("0581", "norrköping");
        municipallyNames.put("0582", "söderköping");
        municipallyNames.put("0583", "motala");
        municipallyNames.put("0584", "vadstena");
        municipallyNames.put("0586", "mjölby");
        municipallyNames.put("0604", "aneby");
        municipallyNames.put("0617", "gnosjö");
        municipallyNames.put("0642", "mullsjö");
        municipallyNames.put("0643", "habo");
        municipallyNames.put("0662", "gislaved");
        municipallyNames.put("0665", "vaggeryd");
        municipallyNames.put("0680", "jönköping");
        municipallyNames.put("0682", "nässjö");
        municipallyNames.put("0683", "värnamo");
        municipallyNames.put("0684", "sävsjö");
        municipallyNames.put("0685", "vetlanda");
        municipallyNames.put("0686", "eksjö");
        municipallyNames.put("0687", "tranås");
        municipallyNames.put("0760", "uppvidinge");
        municipallyNames.put("0761", "lessebo");
        municipallyNames.put("0763", "tingsryd");
        municipallyNames.put("0764", "alvesta");
        municipallyNames.put("0765", "älmhult");
        municipallyNames.put("0767", "markaryd");
        municipallyNames.put("0780", "växjö");
        municipallyNames.put("0781", "ljungby");
        municipallyNames.put("0821", "högsby");
        municipallyNames.put("0834", "torsås");
        municipallyNames.put("0840", "mörbylånga");
        municipallyNames.put("0860", "hultsfred");
        municipallyNames.put("0861", "mönsterås");
        municipallyNames.put("0862", "emmaboda");
        municipallyNames.put("0880", "kalmar");
        municipallyNames.put("0881", "nybro");
        municipallyNames.put("0882", "oskarshamn");
        municipallyNames.put("0883", "västervik");
        municipallyNames.put("0884", "vimmerby");
        municipallyNames.put("0885", "borgholm");
        municipallyNames.put("0980", "gotland");
        municipallyNames.put("1060", "olofström");
        municipallyNames.put("1080", "karlskrona");
        municipallyNames.put("1081", "ronneby");
        municipallyNames.put("1082", "karlshamn");
        municipallyNames.put("1083", "sölvesborg");
        municipallyNames.put("1214", "svalöv");
        municipallyNames.put("1230", "staffanstorp");
        municipallyNames.put("1231", "burlöv");
        municipallyNames.put("1233", "vellinge");
        municipallyNames.put("1256", "östra göinge");
        municipallyNames.put("1257", "örkelljunga");
        municipallyNames.put("1260", "bjuv");
        municipallyNames.put("1261", "kävlinge");
        municipallyNames.put("1262", "lomma");
        municipallyNames.put("1263", "svedala");
        municipallyNames.put("1264", "skurup");
        municipallyNames.put("1265", "sjöbo");
        municipallyNames.put("1266", "hörby");
        municipallyNames.put("1267", "höör");
        municipallyNames.put("1270", "tomelilla");
        municipallyNames.put("1272", "bromölla");
        municipallyNames.put("1273", "osby");
        municipallyNames.put("1275", "perstorp");
        municipallyNames.put("1276", "klippan");
        municipallyNames.put("1277", "åstorp");
        municipallyNames.put("1278", "båstad");
        municipallyNames.put("1280", "malmö");
        municipallyNames.put("1281", "lund");
        municipallyNames.put("1282", "landskrona");
        municipallyNames.put("1283", "helsingborg");
        municipallyNames.put("1284", "höganäs");
        municipallyNames.put("1285", "eslöv");
        municipallyNames.put("1286", "ystad");
        municipallyNames.put("1287", "trelleborg");
        municipallyNames.put("1290", "kristianstad");
        municipallyNames.put("1291", "simrishamn");
        municipallyNames.put("1292", "ängelholm");
        municipallyNames.put("1293", "hässleholm");
        municipallyNames.put("1315", "hylte");
        municipallyNames.put("1380", "halmstad");
        municipallyNames.put("1381", "laholm");
        municipallyNames.put("1382", "falkenberg");
        municipallyNames.put("1383", "varberg");
        municipallyNames.put("1384", "kungsbacka");
        municipallyNames.put("1401", "härryda");
        municipallyNames.put("1402", "partille");
        municipallyNames.put("1407", "öckerö");
        municipallyNames.put("1415", "stenungsund");
        municipallyNames.put("1419", "tjörn");
        municipallyNames.put("1421", "orust");
        municipallyNames.put("1427", "sotenäs");
        municipallyNames.put("1430", "munkedal");
        municipallyNames.put("1435", "tanum");
        municipallyNames.put("1438", "dals-ed");
        municipallyNames.put("1439", "färgelanda");
        municipallyNames.put("1440", "ale");
        municipallyNames.put("1441", "lerum");
        municipallyNames.put("1442", "vårgårda");
        municipallyNames.put("1443", "bollebygd");
        municipallyNames.put("1444", "grästorp");
        municipallyNames.put("1445", "essunga");
        municipallyNames.put("1446", "karlsborg");
        municipallyNames.put("1447", "gullspång");
        municipallyNames.put("1452", "tranemo");
        municipallyNames.put("1460", "bengtsfors");
        municipallyNames.put("1461", "mellerud");
        municipallyNames.put("1462", "lilla edet");
        municipallyNames.put("1463", "mark");
        municipallyNames.put("1465", "svenljunga");
        municipallyNames.put("1466", "herrljunga");
        municipallyNames.put("1470", "vara");
        municipallyNames.put("1471", "götene");
        municipallyNames.put("1472", "tibro");
        municipallyNames.put("1473", "töreboda");
        municipallyNames.put("1480", "göteborg");
        municipallyNames.put("1481", "mölndal");
        municipallyNames.put("1482", "kungälv");
        municipallyNames.put("1484", "lysekil");
        municipallyNames.put("1485", "uddevalla");
        municipallyNames.put("1486", "strömstad");
        municipallyNames.put("1487", "vänersborg");
        municipallyNames.put("1488", "trollhättan");
        municipallyNames.put("1489", "alingsås");
        municipallyNames.put("1490", "borås");
        municipallyNames.put("1491", "ulricehamn");
        municipallyNames.put("1492", "åmål");
        municipallyNames.put("1493", "mariestad");
        municipallyNames.put("1494", "lidköping");
        municipallyNames.put("1495", "skara");
        municipallyNames.put("1496", "skövde");
        municipallyNames.put("1497", "hjo");
        municipallyNames.put("1498", "tidaholm");
        municipallyNames.put("1499", "falköping");
        municipallyNames.put("1715", "kil");
        municipallyNames.put("1730", "eda");
        municipallyNames.put("1737", "torsby");
        municipallyNames.put("1760", "storfors");
        municipallyNames.put("1761", "hammarö");
        municipallyNames.put("1762", "munkfors");
        municipallyNames.put("1763", "forshaga");
        municipallyNames.put("1764", "grums");
        municipallyNames.put("1765", "årjäng");
        municipallyNames.put("1766", "sunne");
        municipallyNames.put("1780", "karlstad");
        municipallyNames.put("1781", "kristinehamn");
        municipallyNames.put("1782", "filipstad");
        municipallyNames.put("1783", "hagfors");
        municipallyNames.put("1784", "arvika");
        municipallyNames.put("1785", "säffle");
        municipallyNames.put("1814", "lekeberg");
        municipallyNames.put("1860", "laxå");
        municipallyNames.put("1861", "hallsberg");
        municipallyNames.put("1862", "degerfors");
        municipallyNames.put("1863", "hällefors");
        municipallyNames.put("1864", "ljusnarsberg");
        municipallyNames.put("1880", "örebro");
        municipallyNames.put("1881", "kumla");
        municipallyNames.put("1882", "askersund");
        municipallyNames.put("1883", "karlskoga");
        municipallyNames.put("1884", "nora");
        municipallyNames.put("1885", "lindesberg");
        municipallyNames.put("1904", "skinnskatteberg");
        municipallyNames.put("1907", "surahammar");
        municipallyNames.put("1960", "kungsör");
        municipallyNames.put("1961", "hallstahammar");
        municipallyNames.put("1962", "norberg");
        municipallyNames.put("1980", "västerås");
        municipallyNames.put("1981", "sala");
        municipallyNames.put("1982", "fagersta");
        municipallyNames.put("1983", "köping");
        municipallyNames.put("1984", "arboga");
        municipallyNames.put("2021", "vansbro");
        municipallyNames.put("2023", "malung-sälen");
        municipallyNames.put("2026", "gagnef");
        municipallyNames.put("2029", "leksand");
        municipallyNames.put("2031", "rättvik");
        municipallyNames.put("2034", "orsa");
        municipallyNames.put("2039", "älvdalen");
        municipallyNames.put("2061", "smedjebacken");
        municipallyNames.put("2062", "mora");
        municipallyNames.put("2080", "falun");
        municipallyNames.put("2081", "borlänge");
        municipallyNames.put("2082", "säter");
        municipallyNames.put("2083", "hedemora");
        municipallyNames.put("2084", "avesta");
        municipallyNames.put("2085", "ludvika");
        municipallyNames.put("2101", "ockelbo");
        municipallyNames.put("2104", "hofors");
        municipallyNames.put("2121", "ovanåker");
        municipallyNames.put("2132", "nordanstig");
        municipallyNames.put("2161", "ljusdal");
        municipallyNames.put("2180", "gävle");
        municipallyNames.put("2181", "sandviken");
        municipallyNames.put("2182", "söderhamn");
        municipallyNames.put("2183", "bollnäs");
        municipallyNames.put("2184", "hudiksvall");
        municipallyNames.put("2260", "ånge");
        municipallyNames.put("2262", "timrå");
        municipallyNames.put("2280", "härnösand");
        municipallyNames.put("2281", "sundsvall");
        municipallyNames.put("2282", "kramfors");
        municipallyNames.put("2283", "sollefteå");
        municipallyNames.put("2284", "örnsköldsvik");
        municipallyNames.put("2303", "ragunda");
        municipallyNames.put("2305", "bräcke");
        municipallyNames.put("2309", "krokom");
        municipallyNames.put("2313", "strömsund");
        municipallyNames.put("2321", "åre");
        municipallyNames.put("2326", "berg");
        municipallyNames.put("2361", "härjedalen");
        municipallyNames.put("2380", "östersund");
        municipallyNames.put("2401", "nordmaling");
        municipallyNames.put("2403", "bjurholm");
        municipallyNames.put("2404", "vindeln");
        municipallyNames.put("2409", "robertsfors");
        municipallyNames.put("2417", "norsjö");
        municipallyNames.put("2418", "malå");
        municipallyNames.put("2421", "storuman");
        municipallyNames.put("2422", "sorsele");
        municipallyNames.put("2425", "dorotea");
        municipallyNames.put("2460", "vännäs");
        municipallyNames.put("2462", "vilhelmina");
        municipallyNames.put("2463", "åsele");
        municipallyNames.put("2480", "umeå");
        municipallyNames.put("2481", "lycksele");
        municipallyNames.put("2482", "skellefteå");
        municipallyNames.put("2505", "arvidsjaur");
        municipallyNames.put("2506", "arjeplog");
        municipallyNames.put("2510", "jokkmokk");
        municipallyNames.put("2513", "överkalix");
        municipallyNames.put("2514", "kalix");
        municipallyNames.put("2518", "övertorneå");
        municipallyNames.put("2521", "pajala");
        municipallyNames.put("2523", "gällivare");
        municipallyNames.put("2560", "älvsbyn");
        municipallyNames.put("2580", "luleå");
        municipallyNames.put("2581", "piteå");
        municipallyNames.put("2582", "boden");
        municipallyNames.put("2583", "haparanda");
        municipallyNames.put("2584", "kiruna");
    }

    @Override
    public String lookupCountyFromCode(String code) {
        return countyNames.get(code);
    }

    @Override
    public String lookupCodeForCounty(String name) {
        for (String countyCode : countyNames.keySet()) {
            String countyName = countyNames.get(countyCode);

            if (countyName.startsWith(name)) {
                return countyCode;
            }
        }

        return "";
    }

    @Override
    public String lookupMunicipalityFromCode(String code) {
        return municipallyNames.get(code);
    }

    @Override
    public String lookupCodeForMunicipality(String name) {
        for (String municipallyCode : municipallyNames.keySet()) {
            String municipallyName = municipallyNames.get(municipallyCode);

            if (municipallyName.startsWith(name)) {
                return municipallyCode;
            }
        }

        return "";
    }

    @Override
    public String lookupCountyBelonging(String municipalityCode) {
        for (String countyCode : countyToMunicipal.keySet()) {
            List<String> municipalities = countyToMunicipal.get(countyCode);
            if (municipalities.contains(municipalityCode)) {
                return countyCode;
            }
        }

        return "";
    }

    @Override
    public boolean isMunicipalityPartOfCounty(String countyCode,
                                              String municipalityCode) {
        List<String> municipalities = countyToMunicipal.get(countyCode);

        return municipalities.contains(municipalityCode);
    }
}
