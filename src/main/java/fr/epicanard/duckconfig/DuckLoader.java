package fr.epicanard.duckconfig;

import fr.epicanard.duckconfig.annotations.Resource;
import fr.epicanard.duckconfig.annotations.ResourceLocation;
import fr.epicanard.duckconfig.annotations.ResourceWrapper;
import fr.epicanard.duckconfig.exceptions.MissingResourceException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DuckLoader {

  public static <T> T load(final Class<T> configClass) {
    return load(configClass, (String) null);
  }

  public static <T> T load(final Class<T> configClass, final String basePath) {
    final Resource resource = getResource(configClass);
    return load(configClass, new ResourceWrapper(basePath, resource));
  }

  public static <T> T load(final Class<T> configClass, final ResourceWrapper wrapper) {
    final InputStream inputStream = loadFile(wrapper);
    if (inputStream != null) {
      return wrapper.type.parser().load(inputStream, configClass);
    }
    try {
      System.out.println("STREAM NULL");
      return configClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static <T> Map<String, T> loadMap(final Class<T> configClass, final ResourceWrapper wrapper) {
    final InputStream inputStream = loadFile(wrapper);
    if (inputStream != null) {
      return wrapper.type.parser().loadMap(inputStream, configClass);
    }
    return new HashMap<>();

  }

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

  private static <T> Resource getResource(final Class<T> configClass) {
    final Resource resource = configClass.getAnnotation(Resource.class);
    if (resource == null) {
      throw new MissingResourceException(configClass.getName());
    }
    return resource;
  }

  /**
   * Load the file sent in parameter and res
   *
   * @param wrapper Resource wrapper that contains all information of resource
   * @return Return InputStream of file
   */
  private static InputStream loadFile(final ResourceWrapper wrapper) {
    try {
      if (wrapper.location == ResourceLocation.FILE_PATH) {
        return new FileInputStream(new File(wrapper.basePath, wrapper.value));
      } else {
        final String resourcePath = ((wrapper.basePath == null) ? "" : wrapper.basePath + "/") + wrapper.value;
        return DuckLoader.class.getClassLoader().getResourceAsStream(resourcePath);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

}
