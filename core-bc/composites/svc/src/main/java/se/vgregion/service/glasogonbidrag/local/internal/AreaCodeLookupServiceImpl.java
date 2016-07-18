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
        municipallyNames.put("0114", "Upplands Väsby");
        municipallyNames.put("0115", "Vallentuna");
        municipallyNames.put("0117", "Österåker");
        municipallyNames.put("0120", "Värmdö");
        municipallyNames.put("0123", "Järfälla");
        municipallyNames.put("0125", "Ekerö");
        municipallyNames.put("0126", "Huddinge");
        municipallyNames.put("0127", "Botkyrka");
        municipallyNames.put("0128", "Salem");
        municipallyNames.put("0136", "Haninge");
        municipallyNames.put("0138", "Tyresö");
        municipallyNames.put("0139", "Upplands-Bro");
        municipallyNames.put("0140", "Nykvarn");
        municipallyNames.put("0160", "Täby");
        municipallyNames.put("0162", "Danderyd");
        municipallyNames.put("0163", "Sollentuna");
        municipallyNames.put("0180", "Stockholm");
        municipallyNames.put("0181", "Södertälje");
        municipallyNames.put("0182", "Nacka");
        municipallyNames.put("0183", "Sundbyberg");
        municipallyNames.put("0184", "Solna");
        municipallyNames.put("0186", "Lidingö");
        municipallyNames.put("0187", "Vaxholm");
        municipallyNames.put("0188", "Norrtälje");
        municipallyNames.put("0191", "Sigtuna");
        municipallyNames.put("0192", "Nynäshamn");
        municipallyNames.put("0305", "Håbo");
        municipallyNames.put("0319", "Älvkarleby");
        municipallyNames.put("0330", "Knivsta");
        municipallyNames.put("0331", "Heby");
        municipallyNames.put("0360", "Tierp");
        municipallyNames.put("0380", "Uppsala");
        municipallyNames.put("0381", "Enköping");
        municipallyNames.put("0382", "Östhammar");
        municipallyNames.put("0428", "Vingåker");
        municipallyNames.put("0461", "Gnesta");
        municipallyNames.put("0480", "Nyköping");
        municipallyNames.put("0481", "Oxelösund");
        municipallyNames.put("0482", "Flen");
        municipallyNames.put("0483", "Katrineholm");
        municipallyNames.put("0484", "Eskilstuna");
        municipallyNames.put("0486", "Strängnäs");
        municipallyNames.put("0488", "Trosa");
        municipallyNames.put("0509", "Ödeshög");
        municipallyNames.put("0512", "Ydre");
        municipallyNames.put("0513", "Kinda");
        municipallyNames.put("0560", "Boxholm");
        municipallyNames.put("0561", "Åtvidaberg");
        municipallyNames.put("0562", "Finspång");
        municipallyNames.put("0563", "Valdemarsvik");
        municipallyNames.put("0580", "Linköping");
        municipallyNames.put("0581", "Norrköping");
        municipallyNames.put("0582", "Söderköping");
        municipallyNames.put("0583", "Motala");
        municipallyNames.put("0584", "Vadstena");
        municipallyNames.put("0586", "Mjölby");
        municipallyNames.put("0604", "Aneby");
        municipallyNames.put("0617", "Gnosjö");
        municipallyNames.put("0642", "Mullsjö");
        municipallyNames.put("0643", "Habo");
        municipallyNames.put("0662", "Gislaved");
        municipallyNames.put("0665", "Vaggeryd");
        municipallyNames.put("0680", "Jönköping");
        municipallyNames.put("0682", "Nässjö");
        municipallyNames.put("0683", "Värnamo");
        municipallyNames.put("0684", "Sävsjö");
        municipallyNames.put("0685", "Vetlanda");
        municipallyNames.put("0686", "Eksjö");
        municipallyNames.put("0687", "Tranås");
        municipallyNames.put("0760", "Uppvidinge");
        municipallyNames.put("0761", "Lessebo");
        municipallyNames.put("0763", "Tingsryd");
        municipallyNames.put("0764", "Alvesta");
        municipallyNames.put("0765", "Älmhult");
        municipallyNames.put("0767", "Markaryd");
        municipallyNames.put("0780", "Växjö");
        municipallyNames.put("0781", "Ljungby");
        municipallyNames.put("0821", "Högsby");
        municipallyNames.put("0834", "Torsås");
        municipallyNames.put("0840", "Mörbylånga");
        municipallyNames.put("0860", "Hultsfred");
        municipallyNames.put("0861", "Mönsterås");
        municipallyNames.put("0862", "Emmaboda");
        municipallyNames.put("0880", "Kalmar");
        municipallyNames.put("0881", "Nybro");
        municipallyNames.put("0882", "Oskarshamn");
        municipallyNames.put("0883", "Västervik");
        municipallyNames.put("0884", "Vimmerby");
        municipallyNames.put("0885", "Borgholm");
        municipallyNames.put("0980", "Gotland");
        municipallyNames.put("1060", "Olofström");
        municipallyNames.put("1080", "Karlskrona");
        municipallyNames.put("1081", "Ronneby");
        municipallyNames.put("1082", "Karlshamn");
        municipallyNames.put("1083", "Sölvesborg");
        municipallyNames.put("1214", "Svalöv");
        municipallyNames.put("1230", "Staffanstorp");
        municipallyNames.put("1231", "Burlöv");
        municipallyNames.put("1233", "Vellinge");
        municipallyNames.put("1256", "Östra Göinge");
        municipallyNames.put("1257", "Örkelljunga");
        municipallyNames.put("1260", "Bjuv");
        municipallyNames.put("1261", "Kävlinge");
        municipallyNames.put("1262", "Lomma");
        municipallyNames.put("1263", "Svedala");
        municipallyNames.put("1264", "Skurup");
        municipallyNames.put("1265", "Sjöbo");
        municipallyNames.put("1266", "Hörby");
        municipallyNames.put("1267", "Höör");
        municipallyNames.put("1270", "Tomelilla");
        municipallyNames.put("1272", "Bromölla");
        municipallyNames.put("1273", "Osby");
        municipallyNames.put("1275", "Perstorp");
        municipallyNames.put("1276", "Klippan");
        municipallyNames.put("1277", "Åstorp");
        municipallyNames.put("1278", "Båstad");
        municipallyNames.put("1280", "Malmö");
        municipallyNames.put("1281", "Lund");
        municipallyNames.put("1282", "Landskrona");
        municipallyNames.put("1283", "Helsingborg");
        municipallyNames.put("1284", "Höganäs");
        municipallyNames.put("1285", "Eslöv");
        municipallyNames.put("1286", "Ystad");
        municipallyNames.put("1287", "Trelleborg");
        municipallyNames.put("1290", "Kristianstad");
        municipallyNames.put("1291", "Simrishamn");
        municipallyNames.put("1292", "Ängelholm");
        municipallyNames.put("1293", "Hässleholm");
        municipallyNames.put("1315", "Hylte");
        municipallyNames.put("1380", "Halmstad");
        municipallyNames.put("1381", "Laholm");
        municipallyNames.put("1382", "Falkenberg");
        municipallyNames.put("1383", "Varberg");
        municipallyNames.put("1384", "Kungsbacka");
        municipallyNames.put("1401", "Härryda");
        municipallyNames.put("1402", "Partille");
        municipallyNames.put("1407", "Öckerö");
        municipallyNames.put("1415", "Stenungsund");
        municipallyNames.put("1419", "Tjörn");
        municipallyNames.put("1421", "Orust");
        municipallyNames.put("1427", "Sotenäs");
        municipallyNames.put("1430", "Munkedal");
        municipallyNames.put("1435", "Tanum");
        municipallyNames.put("1438", "Dals-Ed");
        municipallyNames.put("1439", "Färgelanda");
        municipallyNames.put("1440", "Ale");
        municipallyNames.put("1441", "Lerum");
        municipallyNames.put("1442", "Vårgårda");
        municipallyNames.put("1443", "Bollebygd");
        municipallyNames.put("1444", "Grästorp");
        municipallyNames.put("1445", "Essunga");
        municipallyNames.put("1446", "Karlsborg");
        municipallyNames.put("1447", "Gullspång");
        municipallyNames.put("1452", "Tranemo");
        municipallyNames.put("1460", "Bengtsfors");
        municipallyNames.put("1461", "Mellerud");
        municipallyNames.put("1462", "Lilla Edet");
        municipallyNames.put("1463", "Mark");
        municipallyNames.put("1465", "Svenljunga");
        municipallyNames.put("1466", "Herrljunga");
        municipallyNames.put("1470", "Vara");
        municipallyNames.put("1471", "Götene");
        municipallyNames.put("1472", "Tibro");
        municipallyNames.put("1473", "Töreboda");
        municipallyNames.put("1480", "Göteborg");
        municipallyNames.put("1481", "Mölndal");
        municipallyNames.put("1482", "Kungälv");
        municipallyNames.put("1484", "Lysekil");
        municipallyNames.put("1485", "Uddevalla");
        municipallyNames.put("1486", "Strömstad");
        municipallyNames.put("1487", "Vänersborg");
        municipallyNames.put("1488", "Trollhättan");
        municipallyNames.put("1489", "Alingsås");
        municipallyNames.put("1490", "Borås");
        municipallyNames.put("1491", "Ulricehamn");
        municipallyNames.put("1492", "Åmål");
        municipallyNames.put("1493", "Mariestad");
        municipallyNames.put("1494", "Lidköping");
        municipallyNames.put("1495", "Skara");
        municipallyNames.put("1496", "Skövde");
        municipallyNames.put("1497", "Hjo");
        municipallyNames.put("1498", "Tidaholm");
        municipallyNames.put("1499", "Falköping");
        municipallyNames.put("1715", "Kil");
        municipallyNames.put("1730", "Eda");
        municipallyNames.put("1737", "Torsby");
        municipallyNames.put("1760", "Storfors");
        municipallyNames.put("1761", "Hammarö");
        municipallyNames.put("1762", "Munkfors");
        municipallyNames.put("1763", "Forshaga");
        municipallyNames.put("1764", "Grums");
        municipallyNames.put("1765", "Årjäng");
        municipallyNames.put("1766", "Sunne");
        municipallyNames.put("1780", "Karlstad");
        municipallyNames.put("1781", "Kristinehamn");
        municipallyNames.put("1782", "Filipstad");
        municipallyNames.put("1783", "Hagfors");
        municipallyNames.put("1784", "Arvika");
        municipallyNames.put("1785", "Säffle");
        municipallyNames.put("1814", "Lekeberg");
        municipallyNames.put("1860", "Laxå");
        municipallyNames.put("1861", "Hallsberg");
        municipallyNames.put("1862", "Degerfors");
        municipallyNames.put("1863", "Hällefors");
        municipallyNames.put("1864", "Ljusnarsberg");
        municipallyNames.put("1880", "Örebro");
        municipallyNames.put("1881", "Kumla");
        municipallyNames.put("1882", "Askersund");
        municipallyNames.put("1883", "Karlskoga");
        municipallyNames.put("1884", "Nora");
        municipallyNames.put("1885", "Lindesberg");
        municipallyNames.put("1904", "Skinnskatteberg");
        municipallyNames.put("1907", "Surahammar");
        municipallyNames.put("1960", "Kungsör");
        municipallyNames.put("1961", "Hallstahammar");
        municipallyNames.put("1962", "Norberg");
        municipallyNames.put("1980", "Västerås");
        municipallyNames.put("1981", "Sala");
        municipallyNames.put("1982", "Fagersta");
        municipallyNames.put("1983", "Köping");
        municipallyNames.put("1984", "Arboga");
        municipallyNames.put("2021", "Vansbro");
        municipallyNames.put("2023", "Malung-Sälen");
        municipallyNames.put("2026", "Gagnef");
        municipallyNames.put("2029", "Leksand");
        municipallyNames.put("2031", "Rättvik");
        municipallyNames.put("2034", "Orsa");
        municipallyNames.put("2039", "Älvdalen");
        municipallyNames.put("2061", "Smedjebacken");
        municipallyNames.put("2062", "Mora");
        municipallyNames.put("2080", "Falun");
        municipallyNames.put("2081", "Borlänge");
        municipallyNames.put("2082", "Säter");
        municipallyNames.put("2083", "Hedemora");
        municipallyNames.put("2084", "Avesta");
        municipallyNames.put("2085", "Ludvika");
        municipallyNames.put("2101", "Ockelbo");
        municipallyNames.put("2104", "Hofors");
        municipallyNames.put("2121", "Ovanåker");
        municipallyNames.put("2132", "Nordanstig");
        municipallyNames.put("2161", "Ljusdal");
        municipallyNames.put("2180", "Gävle");
        municipallyNames.put("2181", "Sandviken");
        municipallyNames.put("2182", "Söderhamn");
        municipallyNames.put("2183", "Bollnäs");
        municipallyNames.put("2184", "Hudiksvall");
        municipallyNames.put("2260", "Ånge");
        municipallyNames.put("2262", "Timrå");
        municipallyNames.put("2280", "Härnösand");
        municipallyNames.put("2281", "Sundsvall");
        municipallyNames.put("2282", "Kramfors");
        municipallyNames.put("2283", "Sollefteå");
        municipallyNames.put("2284", "Örnsköldsvik");
        municipallyNames.put("2303", "Ragunda");
        municipallyNames.put("2305", "Bräcke");
        municipallyNames.put("2309", "Krokom");
        municipallyNames.put("2313", "Strömsund");
        municipallyNames.put("2321", "Åre");
        municipallyNames.put("2326", "Berg");
        municipallyNames.put("2361", "Härjedalen");
        municipallyNames.put("2380", "Östersund");
        municipallyNames.put("2401", "Nordmaling");
        municipallyNames.put("2403", "Bjurholm");
        municipallyNames.put("2404", "Vindeln");
        municipallyNames.put("2409", "Robertsfors");
        municipallyNames.put("2417", "Norsjö");
        municipallyNames.put("2418", "Malå");
        municipallyNames.put("2421", "Storuman");
        municipallyNames.put("2422", "Sorsele");
        municipallyNames.put("2425", "Dorotea");
        municipallyNames.put("2460", "Vännäs");
        municipallyNames.put("2462", "Vilhelmina");
        municipallyNames.put("2463", "Åsele");
        municipallyNames.put("2480", "Umeå");
        municipallyNames.put("2481", "Lycksele");
        municipallyNames.put("2482", "Skellefteå");
        municipallyNames.put("2505", "Arvidsjaur");
        municipallyNames.put("2506", "Arjeplog");
        municipallyNames.put("2510", "Jokkmokk");
        municipallyNames.put("2513", "Överkalix");
        municipallyNames.put("2514", "Kalix");
        municipallyNames.put("2518", "Övertorneå");
        municipallyNames.put("2521", "Pajala");
        municipallyNames.put("2523", "Gällivare");
        municipallyNames.put("2560", "Älvsbyn");
        municipallyNames.put("2580", "Luleå");
        municipallyNames.put("2581", "Piteå");
        municipallyNames.put("2582", "Boden");
        municipallyNames.put("2583", "Haparanda");
        municipallyNames.put("2584", "Kiruna");
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
