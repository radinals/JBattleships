package main.com.util.rng;

public interface BiasedRandomChooser {
  public void clearOptions();

  public void addOption(Object option, Double weight);

  public Object randomPick();
}
