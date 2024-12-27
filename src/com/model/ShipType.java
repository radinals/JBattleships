package com.model;

public enum ShipType {
  CARRIER(5), BATTLESHIP(4), SUBMARINE(3), DESTROYER(3), BOAT(2);

  private int length;

  private ShipType(int length) {
    this.length = length;
  }

  public int getLength() {
    return length;
  }
}
