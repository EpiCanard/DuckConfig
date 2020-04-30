package fr.epicanard.duckconfig.examples;

import fr.epicanard.duckconfig.annotations.Resource;
import fr.epicanard.duckconfig.annotations.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public @Resource(value = "plop.yml", location = ResourceLocation.CLASS_PATH)
class Plop {
  public String name = "toto";
  public String age = "plop";
  public List<Pat> pat = new ArrayList<>();

  public Plop() {}

  @Override
  public String toString() {
    return String.format("[Name:%s, Age:%s, Pat:%s]", name, age, pat);
  }
}
