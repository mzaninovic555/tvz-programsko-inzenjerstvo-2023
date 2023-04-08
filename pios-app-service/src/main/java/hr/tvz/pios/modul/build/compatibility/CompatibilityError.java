package hr.tvz.pios.modul.build.compatibility;

public enum CompatibilityError {
  MOTHERBOARD_CPU("CPU and Motherboard sockets don't match"),
  MOTHERBOARD_RAM("Motherboard and RAM memory type doesn't match"),
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
