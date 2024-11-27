package com.battleship.utils;

public class Vec2D {
	private int x, y;

	public Vec2D() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vec2D(Vec2D other) {
		this.x = other.getX();
		this.y = other.getY();
	}
	
	public Vec2D(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
   public static boolean isPointInsideRectangle(Vec2D point, Vec2D top_left, Vec2D bottom_right) {
	   return isPointInsideRectangle(point.getX(), point.getY(), top_left.getX(), top_left.getY(), bottom_right.getX(), bottom_right.getY());
   }

   public static boolean isPointInsideRectangle(int x, int y, int top_left_x, int top_left_y, int bottom_right_x, int bottom_right_y) {
		// Determine the bounds of the rectangle
		int xmin = Math.min(top_left_x, bottom_right_x);
		int xmax = Math.max(top_left_x, bottom_right_x);
		int ymin = Math.min(top_left_y, bottom_right_y);
		int ymax = Math.max(top_left_y, bottom_right_y);

		// Check if the point is within the bounds
		return (x >= xmin && x <= xmax) && (y >= ymin && y <= ymax);
	}
   
   	public static Vec2D translateCorodinates(int x,int y, int source_w, int source_h, int target_w, int target_h, int offsetX, int offsetY) {
   		// Scaling factors
   		double scaleX = target_w / (double) source_w;
   		double scaleY = target_h / (double) source_h;

   		// Scaling the coordinate
   		int X_scaled = (int) (x * scaleX);
   		int Y_scaled = (int) (y * scaleY);

   		// Applying translation (offset)
   		int X_lg = X_scaled + offsetX;
   		int Y_lg = Y_scaled + offsetY;	
   		
   		return new Vec2D(X_lg, Y_lg);
   	}


	@Override
	public String toString() {
		return "Vec2D [x=" + x + ", y=" + y + "]";
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void subtractX(int n) {
		x -= n;
	}

	public void subtractY(int n) {
		y -= n;
	}

	public void addX(int n) {
		x += n;
	}

	public void addY(int n) {
		y += n;
	}
	
	public boolean equals(Vec2D other) {
		return this.x == other.x && this.y == other.y;
	}
	
}
