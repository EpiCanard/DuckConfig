package fr.epicanard.duckconfig.annotations;

import fr.epicanard.duckconfig.exceptions.MissingAnnotationException;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Map;

public class AnnotationHandler {

  public static <T> Class<?> getBaseClass(final T config) {
    if (config instanceof Map) {
      final Iterator it = ((Map)config).values().iterator();
      if (it.hasNext()) {
        return it.next().getClass();
      }
    }
    return config.getClass();
  }

  public static <A extends Annotation> A getAnnotation(final AnnotationType annotationType, final Class<?> clazz) {
    return (A) clazz.getAnnotation(annotationType.annotation);
  }

  public static <A extends Annotation> A getAnnotationOrThrow(final AnnotationType annotationType, final Class<?> clazz) {
    final A annotation = getAnnotation(annotationType, clazz);
    if (annotation == null) {
      throw new MissingAnnotationException(annotationType.getName(), clazz.getName());
    }
    return annotation;
  }

  public enum AnnotationType {
    HEADER(Header.class),
    RESOURCE(Resource.class);

    Class<? extends Annotation> annotation;

    AnnotationType(Class<? extends Annotation> annotation) {
      this.annotation = annotation;
    }

    String getName() {
      return this.annotation.getName();
    }
  }
}
