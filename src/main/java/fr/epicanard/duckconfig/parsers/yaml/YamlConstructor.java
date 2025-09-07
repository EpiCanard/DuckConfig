package fr.epicanard.duckconfig.parsers.yaml;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.stream.Collectors;

class YamlConstructor extends Constructor {
  private static final String PREFIX = "tag:duckloader,2020:";
  public static final Tag ENTRIES = new Tag(PREFIX + "entries");

  private TypeDescription itemType;

  YamlConstructor(Class<?> clazz) {
    super(new LoaderOptions());
    this.itemType = new TypeDescription(clazz);
    this.rootTag = ENTRIES;
  }

  @Override
  protected Object constructObject(Node node) {
    if (ENTRIES.equals(node.getTag()) && node instanceof MappingNode) {
      return this.mapEntriesNode(node);
    } else {
      return super.constructObject(node);
    }
  }

  private Object mapEntriesNode(final Node node) {
    final MappingNode mNode = (MappingNode) node;
    return mNode.getValue().stream().collect(
        Collectors.toMap(
            t -> super.constructObject(t.getKeyNode()),
            t -> {
              Node child = t.getValueNode();
              child.setType(itemType.getType());
              return super.constructObject(child);
            }
        )
    );
  }
}
