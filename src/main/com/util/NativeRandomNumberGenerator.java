package main.com.util;

import java.util.random.RandomGenerator;

public class NativeRandomNumberGenerator implements RandomNumberGenerator {

  @Override
  public Integer rangeI(int min, int max) {
    RandomGenerator randomGenerator = RandomGenerator.of("Random");
    return randomGenerator.nextInt(min, max);
  }

  @Override
  public Double rangeD(double min, double max) {
    RandomGenerator randomGenerator = RandomGenerator.of("Random");
    return randomGenerator.nextDouble(min, max);
  }

}
