package com.util;

import java.util.function.Function;

public class CopyUtil {
  public static <T> void deepCopyMatrix(final T[][] src, T[][] dest, int sx,
      int sy, Function<T, T> copyFunction) {
    if (src == null || dest == null) {
      throw new IllegalArgumentException(
          "Source and destination matrices cannot be null.");
    }
    if (src.length < sy || dest.length < sy || src[0].length < sx
        || dest[0].length < sx) {
      throw new IllegalArgumentException(
          "Source or destination matrices are smaller than specified dimensions.");
    }

    for (int y = 0; y < sy; y++) {
      for (int x = 0; x < sx; x++) {
        dest[y][x] = copyFunction.apply(src[y][x]);
      }
    }
  }

  public static <T> void deepCopyMatrix(final T[][] src, T[][] dest,
      Function<T, T> copyFunction) {
    if (src == null || dest == null) {
      throw new IllegalArgumentException(
          "Source and destination matrices cannot be null.");
    }

    for (int y = 0; y < src.length; y++) {
      for (int x = 0; x < src[0].length; x++) {
        dest[y][x] = copyFunction.apply(src[y][x]);
      }
    }
  }
}
