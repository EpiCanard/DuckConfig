package fr.epicanard.duckconfig.parsers.yaml;

import fr.epicanard.duckconfig.parsers.Parser;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

public class YamlParser implements Parser {
  private <T> T load(final InputStream file, final Class<T> clazz, final Constructor constructor) {
    final Representer representer = new YamlRepresenter();
    representer.getPropertyUtils().setSkipMissingProperties(true);
    final Yaml yaml = new Yaml(constructor, representer);

    return yaml.load(file);
  }

  @Override
  public <T> T load(final InputStream file, final Class<T> clazz) {
    return load(file, clazz, new Constructor(clazz));
  }

  @Override
  public <T> Map<String, T> loadMap(InputStream file, Class<T> clazz) {
    return load(file, Map.class, new YamlConstructor(clazz));
  }

  @Override
  public <T> String dump(final T config) {
    final Representer representer = new YamlRepresenter();
    if (config instanceof Map) {
      final Iterator it = ((Map)config).values().iterator();
      if (it.hasNext()) {
        representer.addClassTag(it.next().getClass(), Tag.MAP);
      }
    }
    final Yaml yaml = new Yaml(representer);

    return yaml.dumpAs(config, Tag.MAP, DumperOptions.FlowStyle.BLOCK);
  }
}
