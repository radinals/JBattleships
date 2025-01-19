package main.com.util.rng;

import java.util.HashMap;
import java.util.Map;

public class SimpleBiasedRandomChooser implements BiasedRandomChooser {

  private HashMap<Object, Double> options;
  private RandomNumberGenerator rng;
  private Double optionWeightTotal;

  public SimpleBiasedRandomChooser(RandomNumberGenerator rng) {
    this.options = new HashMap<Object, Double>();
    this.rng = rng;
    this.optionWeightTotal = 0.0;
  }

  public void setRng(RandomNumberGenerator rng) {
    this.rng = rng;
  }

  @Override
  public void clearOptions() {
    options.clear();
    this.optionWeightTotal = 0.0;
  }

  @Override
  public void addOption(Object option, Double weight) {
    options.put(option, weight);
    this.optionWeightTotal += weight;
  }

  @Override
  public Object randomPick() {
    if (options.isEmpty())
      return null;
    while (true) {
      Double rand = rng.rangeD(-optionWeightTotal, optionWeightTotal + 1.0);
      for (Map.Entry<Object, Double> e : options.entrySet()) {
        if (rand > e.getValue()) {
          rand -= e.getValue();
        } else if (rand <= e.getValue()) {
          return e.getKey();
        }
      }
    }

  }

}
