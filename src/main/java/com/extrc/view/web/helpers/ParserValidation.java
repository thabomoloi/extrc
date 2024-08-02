package com.extrc.view.web.helpers;

public class ParserValidation {
  public final boolean valid;
  public final String message;

  public ParserValidation(boolean valid, String message) {
    this.valid = valid;
    this.message = message;
  }

  public boolean getValid() {
    return valid;
  }

  public String getMessage() {
    return message;
  }
}
