package main.com.util;

public interface BiasedRandomChooser {
  public void clearOptions();
  public void addOption(Object option, Double weight);
  public Object randomPick();
}
