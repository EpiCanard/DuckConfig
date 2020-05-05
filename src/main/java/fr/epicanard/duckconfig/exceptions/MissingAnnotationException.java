package fr.epicanard.duckconfig.exceptions;

public class MissingAnnotationException extends RuntimeException {
  public MissingAnnotationException(final String annotationName, final String className) {
    super("Missing annotation @" + annotationName + " on the class '" + className +"'");
  }
}
