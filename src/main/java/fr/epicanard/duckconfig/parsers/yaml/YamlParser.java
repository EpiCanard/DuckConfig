package fr.epicanard.duckconfig.parsers.yaml;

import static fr.epicanard.duckconfig.annotations.AnnotationHandler.getBaseClass;
import fr.epicanard.duckconfig.parsers.Parser;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;

public class YamlParser implements Parser {
  private <T> T load(final InputStream file, final Class<T> clazz, final Constructor constructor) {
    final Representer representer = new YamlRepresenter();
    representer.getPropertyUtils().setSkipMissingProperties(true);
    final Yaml yaml = new Yaml(constructor, representer);

    return yaml.loadAs(file, clazz);
  }

  @Override
  public <T> T load(final InputStream file, final Class<T> clazz) {
    return load(file, clazz, new CustomClassLoaderConstructor(clazz.getClassLoader()));
  }

  @Override
  public <T> Map<String, T> loadMap(InputStream file, Class<T> clazz) {
    return load(file, Map.class, new YamlConstructor(clazz));
  }

  @Override
  public <T> Writer dump(final T config, final Writer baseWriter) {
    final Representer representer = new YamlRepresenter();
    representer.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    representer.addClassTag(getBaseClass(config), Tag.MAP);

    final Yaml yaml = new Yaml(representer);

    yaml.dump(config, baseWriter);

    return baseWriter;
  }
}
