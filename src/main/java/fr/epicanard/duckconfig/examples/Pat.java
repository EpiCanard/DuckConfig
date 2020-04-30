package fr.epicanard.duckconfig.examples;

public class Pat {
  public String plop = "tutu";

  @Override
  public String toString() {
    return String.format("[Plop:%s]", plop);
  }
}
