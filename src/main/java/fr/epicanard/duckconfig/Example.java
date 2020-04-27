package fr.epicanard.duckconfig;

import fr.epicanard.duckconfig.annotations.Resource;
import fr.epicanard.duckconfig.annotations.ResourceLocation;
import fr.epicanard.duckconfig.annotations.ResourceWrapper;

import java.util.Map;

class Pat {
  public String plop = "tutu";

  @Override
  public String toString() {
    return String.format("[Plop:%s]", plop);
  }
}

@Resource(value = "plop.yml", location = ResourceLocation.CLASS_PATH)
class Plop {
  public String name = "toto";
  public String age = "plop";
  public Pat pat = new Pat();

  @Override
  public String toString() {
    return String.format("[Name:%s, Age:%s, Pat:%s]", name, age, pat);
  }
}

public class Example {
  static void loadPlop() {
    // Load
    final Plop plop = DuckLoader.load(Plop.class);

    // Print
    System.out.println(plop);

    // Save
    DuckLoader.save(plop, new ResourceWrapper(null, "plop.yml", ResourceLocation.FILE_PATH));

  }

  static void loadEntriesPlop() {
    // Load
    final Map<String, Plop> entries = DuckLoader.loadMap(Plop.class, new ResourceWrapper(null, "entries_plop.yml", ResourceLocation.CLASS_PATH));

    // Print
    entries.forEach((key, value) -> {
      System.out.println(value);
    });

    // Save
    DuckLoader.save(entries, new ResourceWrapper(null, "plop_entries.yml", ResourceLocation.FILE_PATH));
  }

  public static void main(String[] args) {
    loadPlop();
    System.out.println("=============");
    loadEntriesPlop();
  }
}
