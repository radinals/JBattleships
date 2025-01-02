package main.com.model;

public class GameBoardCell {

  public GameBoardMark mark;
  public ShipType ship;

  public GameBoardCell() {
    this.mark = GameBoardMark.NONE;
  }

  public GameBoardCell(GameBoardCell other) {
    this.mark = other.mark;
    this.ship = other.ship;
  }

  public GameBoardCell removeShip() {
    ship = null;
    return this;
  }

  public boolean containsAShip() {
    return ship != null;
  }

  public GameBoardCell setShip(ShipType ship) {
    this.ship = ship;
    return this;
  }

  public GameBoardCell setMarkHit() {
    this.mark = GameBoardMark.HIT;
    return this;
  }

  public GameBoardCell setMarkMiss() {
    this.mark = GameBoardMark.MISS;
    return this;
  }

  public GameBoardCell setMarkNone() {
    this.mark = GameBoardMark.NONE;
    return this;
  }

  public boolean isMarkHit() {
    return mark == GameBoardMark.HIT;
  }

  public boolean isMarkMiss() {
    return mark == GameBoardMark.MISS;
  }

  public boolean isMarkNone() {
    return mark == GameBoardMark.NONE;
  }
}
