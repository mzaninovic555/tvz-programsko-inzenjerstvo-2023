package hr.tvz.pios.modul.user.register;

/**
 * Enum vrijednosti za stanja aktivacije.
 */
public enum ActivationResult {
  //potrebno je aktivirati racun
  ACTIVATION_REQUIRED("activation_required"),
  ALREADY_ACTIVATED("already_activated"),
  ACTIVATION_SUCCESS("activation_success"),
  ACTIVATION_ERROR("activation_error");

  private final String type;

  ActivationResult(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
