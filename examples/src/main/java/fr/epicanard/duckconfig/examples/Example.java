package fr.epicanard.duckconfig.examples;

import fr.epicanard.duckconfig.DuckLoader;
import fr.epicanard.duckconfig.annotations.ResourceLocation;
import fr.epicanard.duckconfig.annotations.ResourceWrapper;

import java.util.Map;

public class Example {
  static void loadPlop() {
    // Load
    final Plop plop = DuckLoader.load(Plop.class);

    // Print
    System.out.println(plop);

    // Save
    DuckLoader.save(plop, new ResourceWrapper("examples/target", "plop.yml", ResourceLocation.FILE_PATH));

  }

  static void loadEntriesPlop() {
    // Load
    final Map<String, Plop> entries = DuckLoader.loadMap(Plop.class, new ResourceWrapper(null, "entries_plop.yml", ResourceLocation.CLASS_PATH));

    // Print
    entries.forEach((key, value) -> {
      System.out.println(value);
    });

    // Save
    DuckLoader.save(entries, new ResourceWrapper("examples/target", "plop_entries.yml", ResourceLocation.FILE_PATH));
  }

  static void loadEmptyEntries() {
    // Load
    final Map<String, Plop> entries = DuckLoader.loadMap(Plop.class, new ResourceWrapper(null, "empty_entries.yml", ResourceLocation.CLASS_PATH));

    // Print
    if (entries == null) {
      System.out.println("SHOULD NOT BE NULL");
    } else {
      System.out.println("Is empty as expected");
    }

    // Save
    DuckLoader.save(entries, new ResourceWrapper("examples/target", "empty_entries.yml", ResourceLocation.FILE_PATH));
  }

  public static void main(String[] args) {
    loadPlop();
    System.out.println("=============");
    loadEntriesPlop();
    System.out.println("=============");
    loadEmptyEntries();
  }
}
