package fr.epicanard.duckconfig.parsers;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class YamlParser implements Parser {

  private <T> void setField(final Field field, final T obj, final ConfigurationSection section) throws IllegalAccessException {
    final String name = field.getName();
    field.setAccessible(true);
    switch (field.getType().getSimpleName().toLowerCase()) {
      case "string":
        field.set(obj, section.getString(name, (String)field.get(obj)));
        break;
      case "integer":
      case "int":
        field.set(obj, section.getInt(name, field.getInt(obj)));
        break;
      case "double":
        field.set(obj, section.getDouble(name, field.getDouble(obj)));
        break;
      case "boolean":
        field.set(obj, section.getBoolean(name, field.getBoolean(obj)));
        break;
      case "list":
         // CANT DO IT
        break;
      default:
        field.set(obj, loadSection(section.getConfigurationSection(name), field.getType()));
    }
  }

  private <T> T loadSection(final ConfigurationSection section, final Class<T> clazz) {
    try {
       final T obj = clazz.newInstance();
      for (Field field : clazz.getDeclaredFields()) {
        System.out.println(field.getType().getSimpleName());
        setField(field, obj, section);
      }
      return obj;
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public <T> T load(final InputStream file, final Class<T> clazz) {
    final YamlConfiguration yaml = YamlConfiguration.loadConfiguration(new InputStreamReader(file));
    return loadSection(yaml, clazz);
  }

  @Override
  public <T> Map<String, T> loadMap(InputStream file, Class<T> clazz) {
    return null;
  }

  @Override
  public <T> String dump(final T config) {
    return null;
  }
}
