package fr.epicanard.duckconfig.examples;

import fr.epicanard.duckconfig.annotations.Header;
import fr.epicanard.duckconfig.annotations.Resource;
import fr.epicanard.duckconfig.annotations.ResourceLocation;

@Resource(value = "plop.yml", location = ResourceLocation.CLASS_PATH)
@Header({
    "==============",
    "This is an header",
    "    With even an indent",
    "=============="
})
public class Plop {
  public String name = "toto";
  public String age = "plop";
  public Pat pat = new Pat();

  @Override
  public String toString() {
    return String.format("[Name:%s, Age:%s, Pat:%s]", name, age, pat);
  }
}