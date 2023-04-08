package hr.tvz.pios.modul.build.compatibility;


import com.google.gson.Gson;
import hr.tvz.pios.common.Type;
import hr.tvz.pios.modul.component.Component;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Servis sa statičkim metodama koji radi razne provjere nad medjukompatibilnosti komponenata.
 */
public class CompatibilityService {

  private static final Gson MAPPER = new Gson();

  /**
   * Generalizirana metoda koja provjerava kompatibilnost između dva JSON-a i parametra.
   * @param json1 JSON prve komponente
   * @param json2 JSON druge komponente
   * @param parameter String parametar po kojem se gleda kompatibilnost
   * @return TRUE = OK kompatibilnost
   */
  private static Boolean checkComponentCompatibility(String json1, String json2, String parameter) {
    Map<String, String> map1 = getMapFromJson(json1);
    Map<String, String> map2 = getMapFromJson(json2);

    if (!map1.containsKey(parameter) || !map2.containsKey(parameter)) {
      return Boolean.TRUE;
    }

    return map1.get(parameter).equalsIgnoreCase(map2.get(parameter))
        || splitListAndCheckIfContains(map1.get(parameter), map2.get(parameter), ",")
        || splitListAndCheckIfContains(map2.get(parameter), map1.get(parameter), ",");
  }

  /**
   * Provjerava sadrži li prvi parametar drugi string, nakon što se prvi splita po splitRegex.
   * @param stringToSplit String koji se splita i pretvara u polje
   * @param stringToCheck String koji se provjerava postoji li u listi
   * @param splitRegex Regex po kojem se splita string
   * @return TRUE - nadjen
   */
  private static Boolean splitListAndCheckIfContains(String stringToSplit, String stringToCheck, String splitRegex) {
    return Arrays.stream(stringToSplit.split(splitRegex))
        .anyMatch(value -> value.strip().equalsIgnoreCase(stringToCheck.strip()));
  }

  private static Map<String, String> getMapFromJson(String json) {
    return MAPPER.fromJson(json, Map.class);
  }

  public static Boolean checkCPUAndMotherboardCompatibility(String cpuJson, String motherboardJson) {
    return checkComponentCompatibility(cpuJson, motherboardJson, "Socket");
  }

  public static Boolean checkCPUAndCoolerCompatibility(String cpuJson, String coolerJson) {
    return checkComponentCompatibility(cpuJson, coolerJson, "Socket");
  }

  public static Boolean checkMotherboardAndRAMCompatibility(String ramJson, String motherboardJson) {
    return checkComponentCompatibility(ramJson, motherboardJson, "RAM Type");
  }

  public static Boolean checkMotherboardAndRAMSlotNumberCompatibility(String ramJson, String motherboardJson) {
    String parameter = "Memory Slots";
    Map<String, String> ramMap = getMapFromJson(ramJson);
    Map<String, String> motherboardMap = getMapFromJson(motherboardJson);

    if (!ramMap.containsKey(parameter) || !motherboardMap.containsKey(parameter)) {
      return Boolean.TRUE;
    }

    return Double.parseDouble(ramMap.get(parameter)) <= Double.parseDouble(motherboardMap.get(parameter));
  }

  public static Boolean checkCaseAndMotherboardCompatibility(String caseJson, String motherboardJson) {
    return checkComponentCompatibility(caseJson, motherboardJson, "Form Factor");
  }

  public static Boolean checkMaxPowerAndPSUCompatibility(List<Component> components, String psuJson) {
    String wattageString = "Wattage";

    Map<String, String> psuJsonMap = getMapFromJson(psuJson);

    if (psuJsonMap.get(wattageString) == null) {
      return Boolean.TRUE;
    }
    double psuWattage = Double.parseDouble(psuJsonMap.get(wattageString).replaceAll("[A-z]", ""));
    double componentsWattageSum = components
        .stream()
        .filter(component -> !component.getType().equals(Type.PSU))
        .mapToDouble(component -> {
          Map<String, String> componentJson = getMapFromJson(component.getData());
          return componentJson.containsKey(wattageString)
              ? Double.parseDouble(componentJson.get(wattageString).replaceAll("[A-z]", ""))
              : 0.0;
        })
        .sum();

    return psuWattage >= componentsWattageSum;
  }
}
