package fr.epicanard.duckconfig.parsers;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.stream.Collectors;

public class EntriesConstructor extends Constructor {
  private TypeDescription itemType;

  EntriesConstructor(Class<?> clazz) {
    super();
    this.itemType = new TypeDescription(clazz);
    this.rootTag = new Tag("myRoot");
  }

  @Override
  protected Object constructObject(Node node) {
    if ("myRoot".equals(node.getTag().getValue()) && node instanceof MappingNode) {
      MappingNode mNode = (MappingNode) node;
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

    } else {
      return super.constructObject(node);
    }
  }
}
