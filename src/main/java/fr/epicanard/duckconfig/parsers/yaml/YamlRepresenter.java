package fr.epicanard.duckconfig.parsers.yaml;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

class YamlRepresenter extends Representer {
  YamlRepresenter() {
    super();
  }

  YamlRepresenter(final DumperOptions options) {
    super(options);
  }

  protected Set<Property> getProperties(final Class<? extends Object> type) {
    final Set<Property> propertySet = getPropertyUtils().getProperties(type);

    final List<String> fields = Arrays.stream(type.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());

    return propertySet.stream()
        .sorted(new BeanPropertyComparator(fields))
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  @Override
  public Node represent(Object data) {
    final Node node = super.represent(data);
    node.setTag(Tag.MAP);
    return node;
  }

  /**
   * Comparator to keep fields in the right order
   */
  static class BeanPropertyComparator implements Comparator<Property> {
    private List<String> fields;

    BeanPropertyComparator(List<String> fields) {
      this.fields = fields;
    }

    public int compare(final Property p1, final Property p2) {
      final int index1 = this.fields.indexOf(p1.getName());
      final int index2 = this.fields.indexOf(p2.getName());
      return Integer.compare(index1, index2);
    }
  }
}