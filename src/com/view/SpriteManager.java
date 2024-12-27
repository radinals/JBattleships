package com.view;

import java.util.HashMap;

public class SpriteManager {
  private HashMap<String, Sprite[]> sprites;

  public SpriteManager() {
    sprites = new HashMap<String, Sprite[]>();

    addSprite("hit", 1);
    addSprite("sea", 1);
    addSprite("miss", 1);
    addSprite("cursor", 1);

    addSprite("battleship", 4);
    addSprite("boat", 2);
    addSprite("carrier", 5);
    addSprite("submarine", 3);
    addSprite("destroyer", 3);
  }

  private void addSprite(String name, int n) {
    Sprite[] spriteGroup = new Sprite[n + 1];
    for (int i = 0; i < n; i++) {
      spriteGroup[i] = new Sprite(String.format("/%s_%d.png", name, i));
    }
    sprites.put(name, spriteGroup);
  }

  public Sprite[] getSprite(String spritename) {
    return this.sprites.getOrDefault(spritename, null);
  }
}
