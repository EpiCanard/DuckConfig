package fr.epicanard.duckconfig.parsers;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;
import java.util.Map;

public class YamlParser implements Parser {
  private <T> T load(final InputStream file, final Class<T> clazz, final Constructor constructor) {
    final Representer representer = new Representer();

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
    return load(file, Map.class, new EntriesConstructor(clazz));
  }

  @Override
  public <T> String dump(final T config) {
    final Yaml yaml = new Yaml(new YamlRepresenter());

    return yaml.dumpAs(config, Tag.MAP, DumperOptions.FlowStyle.BLOCK);
  }
}
