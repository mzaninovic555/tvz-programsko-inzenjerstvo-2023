package hr.tvz.pios.modul.build.compatibility;


import com.google.gson.Gson;
import hr.tvz.pios.modul.component.Component;
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

    if (map1.get(parameter) == null || map2.get(parameter) == null) {
      return Boolean.TRUE;
    }

    return map1.get(parameter).equalsIgnoreCase(map2.get(parameter));
  }

  private static Map<String, String> getMapFromJson(String json) {
    return MAPPER.fromJson(json, Map.class);
  }

  public static Boolean checkCPUAndMotherboardCompatibility(String cpuJson, String motherboardJson) {
    return checkComponentCompatibility(cpuJson, motherboardJson, "Socket");
  }

  public static Boolean checkMotherboardAndRAMCompatibility(String ramJson, String motherboardJson) {
    return checkComponentCompatibility(ramJson, motherboardJson, "RAM Type");
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
    double psuWattage = Double.parseDouble(psuJsonMap.get(wattageString));
    double componentsWattageSum = components
        .stream()
        .mapToDouble(component -> {
          Map<String, String> mapJson = getMapFromJson(component.getData());
          return mapJson.get(wattageString) != null ? Double.parseDouble(mapJson.get(wattageString)) : 0.0;
        })
        .sum();

    return psuWattage >= componentsWattageSum;
  }
}
