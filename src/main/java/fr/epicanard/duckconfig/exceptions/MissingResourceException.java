package fr.epicanard.duckconfig.exceptions;

public class MissingResourceException extends RuntimeException {
  public MissingResourceException(final String className) {
    super("Missing annotation @Resource on the class '" + className +"'");
  }
}
