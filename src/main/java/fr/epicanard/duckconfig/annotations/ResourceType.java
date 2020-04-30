package fr.epicanard.duckconfig.annotations;

import fr.epicanard.duckconfig.parsers.Parser;
import fr.epicanard.duckconfig.parsers.yaml.YamlParser;

public enum ResourceType {
  YAML(YamlParser.class);

  private Class<? extends Parser> parserClass;

  ResourceType(Class<? extends Parser> parserClass) {
    this.parserClass = parserClass;
  }

  /**
   * Return new instance of associated parser
   *
   * @return New instance of parse
   */
  public Parser parser() {
    try {
      return this.parserClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    };
    return null;
  }
}
