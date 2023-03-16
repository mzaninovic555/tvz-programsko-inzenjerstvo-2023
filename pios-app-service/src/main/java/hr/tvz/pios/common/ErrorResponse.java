package hr.tvz.pios.common;

/**
 * Odgovor za slučajeve kada se desi iznimka.
 *
 * @param message Error poruka.
 * @param errors Opcionalna lista detaljnih.
 */
public record ErrorResponse(String message, ErrorField[] errors) {
  /**
   * Rekord za pojedinosti o erroru.
   *
   * @param name Ime polja za koje je vezana greška.
   * @param message Poruka o grešci.
   */
  public record ErrorField(String name, String message) {}

  public ErrorResponse(String message) {
    this(message, null);
  }
}
