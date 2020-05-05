package fr.epicanard.duckconfig;

import fr.epicanard.duckconfig.annotations.Header;
import fr.epicanard.duckconfig.annotations.Resource;
import fr.epicanard.duckconfig.annotations.ResourceLocation;
import fr.epicanard.duckconfig.annotations.ResourceWrapper;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static fr.epicanard.duckconfig.annotations.AnnotationHandler.*;

public class DuckLoader {
  private static String LINE_BREAK = "\n";
  private static String COMMENT = "# ";

  /**
   * Load a class from a yaml. Use Resource annotation to find where load yaml
   *
   * @param configClass Class to deserialize into
   * @param <T>         Type of class
   * @return Return a new instance of class deserialized from yaml
   */
  public static <T> T load(final Class<T> configClass) {
    return load(configClass, (String) null);
  }

  /**
   * Load a class from a yaml. Use Resource annotation to find where load yaml
   *
   * @param configClass Class to deserialize into
   * @param basePath    Base path of file to search
   * @param <T>         Type of class
   * @return Return a new instance of class deserialized from yaml
   */
  public static <T> T load(final Class<T> configClass, final String basePath) {
    final Resource resource = getAnnotationOrThrow(AnnotationType.RESOURCE, configClass);
    return load(configClass, new ResourceWrapper(basePath, resource));
  }

  /**
   * Load a class from a yaml
   *
   * @param configClass Class to deserialize into
   * @param wrapper     Resource wrapper that contains load information
   * @param <T>         Type of class
   * @return Return a new instance of class deserialized from yaml
   */
  public static <T> T load(final Class<T> configClass, final ResourceWrapper wrapper) {
    final InputStream inputStream = loadFile(wrapper, configClass.getClassLoader());
    if (inputStream != null) {
      return wrapper.type.parser().load(inputStream, configClass);
    }
    try {
      return configClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Load a generic map of subclass from yaml
   *
   * @param configClass Sub class contained in map
   * @param wrapper     Resource wrapper that contains load information
   * @param <T>         Type of subclass
   * @return Return the map of subclass deserialized from yaml
   */
  public static <T> Map<String, T> loadMap(final Class<T> configClass, final ResourceWrapper wrapper) {
    final InputStream inputStream = loadFile(wrapper, configClass.getClassLoader());
    return Optional.ofNullable(inputStream)
        .map(stream -> wrapper.type.parser().loadMap(stream, configClass))
        .orElseGet(HashMap::new);
  }

  /**
   * Save the config class to yaml
   *
   * @param config  Config class to save
   * @param wrapper Resource wrapper that contains information to save the file
   * @param <T>     Type of config
   */
  public static <T> void save(final T config, final ResourceWrapper wrapper) {
    try {
      final FileOutputStream stream = new FileOutputStream(new File(wrapper.basePath, wrapper.value));
      final Writer buffer = setHeader(config, new StringWriter());
      byte[] bytes = wrapper.type.parser().dump(config, buffer).toString().getBytes();
      stream.write(bytes, 0, bytes.length);
      stream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /* ===========
   *    TOOLS
   * =========== */

  /**
   * Write the header of the file
   *
   * @param config Config class that contains the header
   * @param writer Base writer to use to write header
   * @param <T>    Type of config
   * @return Modified writer
   */
  private static <T> Writer setHeader(final T config, final Writer writer) {
    final Header header = getAnnotation(AnnotationType.HEADER, getBaseClass(config));
    if (header != null) {
      final String headerStr = Arrays.stream(header.value()).reduce("", (acc, line) -> acc + COMMENT + line + LINE_BREAK);
      if (headerStr.length() > 0) {
        try {
          writer.write(headerStr);
          writer.write(LINE_BREAK);
        } catch (IOException e) {
        }
      }
    }
    return writer;
  }

  /**
   * Load the file sent in parameter in resource wrapper
   *
   * @param wrapper     Resource wrapper that contains all information of resource
   * @param classLoader ClassLoader to use to load inner file
   * @return Return InputStream of file
   */
  private static InputStream loadFile(final ResourceWrapper wrapper, final ClassLoader classLoader) {
    try {
      if (wrapper.location == ResourceLocation.FILE_PATH) {
        return new FileInputStream(new File(wrapper.basePath, wrapper.value));
      } else {
        final String resourcePath = ((wrapper.basePath == null) ? "" : wrapper.basePath + "/") + wrapper.value;
        return classLoader.getResourceAsStream(resourcePath);
      }
    } catch (FileNotFoundException e) {
      return null;
    }
  }

}
