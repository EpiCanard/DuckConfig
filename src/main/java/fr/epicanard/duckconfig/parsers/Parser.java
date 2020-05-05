package fr.epicanard.duckconfig.parsers;

import java.io.InputStream;
import java.io.Writer;
import java.util.Map;

public interface Parser {
  <T> T load(InputStream file, Class<T> clazz);

  <T> Map<String, T> loadMap(InputStream file, Class<T> clazz);

  <T> Writer dump(T config, Writer baseWriter);
}
