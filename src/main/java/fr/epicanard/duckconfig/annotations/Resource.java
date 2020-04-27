package fr.epicanard.duckconfig.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Resource {
  String value() default "";
  ResourceType type() default ResourceType.YAML;
  ResourceLocation location() default ResourceLocation.FILE_PATH;
}
