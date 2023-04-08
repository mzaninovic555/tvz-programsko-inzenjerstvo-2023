package hr.tvz.pios.modul.build.compatibility;

public enum CompatibilityError {
  MOTHERBOARD_CPU("CPU and Motherboard sockets don't match"),
  CPU_COOLER("The cooler isn't compatible with the chosen CPU"),
  MOTHERBOARD_RAM("Motherboard and RAM memory type doesn't match"),
  MOTHERBOARD_RAM_SLOTS("The Motherboard you chose doesn't support that many RAM sticks"),
  MOTHERBOARD_CASE("The Motherboard you chose won't fit in the case"),
  MAX_POWER_PSU("Wattage of all components exceeds max PSU wattage");

  private final String errorText;

  CompatibilityError(String errorText) {
    this.errorText = errorText;
  }

  public String getErrorText() {
    return errorText;
  }
}
