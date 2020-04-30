package fr.epicanard.duckconfig;

import fr.epicanard.duckconfig.annotations.Resource;
import fr.epicanard.duckconfig.annotations.ResourceLocation;
import fr.epicanard.duckconfig.annotations.ResourceWrapper;
import fr.epicanard.duckconfig.exceptions.MissingResourceException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DuckLoader {

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
    final Resource resource = getResource(configClass);
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
    if (inputStream != null) {
      return wrapper.type.parser().loadMap(inputStream, configClass);
    }
    return new HashMap<>();
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
      byte[] bytes = wrapper.type.parser().dump(config).getBytes();
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
   * Get the resource annotation of class or throw an exception if missing
   *
   * @param configClass Class to analyze
   * @return Return Resource found or throw a MissingResourceException
   */
  private static Resource getResource(final Class<?> configClass) {
    final Resource resource = configClass.getAnnotation(Resource.class);
    if (resource == null) {
      throw new MissingResourceException(configClass.getName());
    }
    return resource;
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
      e.printStackTrace();
      return null;
    }
  }

}
