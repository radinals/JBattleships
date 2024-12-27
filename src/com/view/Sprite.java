package com.view;

import java.awt.Image;

import javax.imageio.ImageIO;

public class Sprite {
  
  public Image image;
  
  public Sprite(String spriteImage) {
    try {
     image = ImageIO.read(getClass().getResource(spriteImage)); 
     if (image == null) {
       throw new Exception("Cannot Load Sprite (" + spriteImage + ")");
     }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }
    
  }
  
}
