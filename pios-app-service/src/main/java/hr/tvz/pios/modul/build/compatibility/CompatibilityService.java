package hr.tvz.pios.modul.build.compatibility;


import com.google.gson.Gson;
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
   * @param parameter parametar po kojem se gleda kompatibilnost
   * @return TRUE = OK kompatibilnost
   */
  private static Boolean checkComponentCompatibility(String json1, String json2, String parameter) {
    Map<String, String> map1 = MAPPER.fromJson(json1, Map.class);
    Map<String, String> map2 = MAPPER.fromJson(json2, Map.class);

    if (map1.get(parameter) == null || map2.get(parameter) == null) {
      return Boolean.TRUE;
    }

    return map1.get(parameter).equalsIgnoreCase(map2.get(parameter));
  }

  public static Boolean checkCPUAndMotherboardCompatibility(String cpuJson, String motherboardJson) {
    return checkComponentCompatibility(cpuJson, motherboardJson, "Socket");
  }

  public static Boolean checkMotherboardAndRAMCompatibility(String ramJson, String motherboardJson) {
    return checkComponentCompatibility(ramJson, motherboardJson, ""); // TODO
  }

  public static Boolean checkCaseAndMotherboardCompatibility(String caseJson, String motherboardJson) {
    return checkComponentCompatibility(caseJson, motherboardJson, "Form Factor");
  }
}
