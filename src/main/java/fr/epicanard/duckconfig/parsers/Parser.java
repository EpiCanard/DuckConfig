package fr.epicanard.duckconfig.parsers;

import java.io.InputStream;
import java.util.Map;

public interface Parser {
  <T> T load(InputStream file, Class<T> clazz);

  <T> Map<String, T> loadMap(InputStream file, Class<T> clazz);

  <T> String dump(T config);
}
