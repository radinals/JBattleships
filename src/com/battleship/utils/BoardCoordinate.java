package com.battleship.utils;

public class BoardCoordinate {
	private int collumn, row;

	public BoardCoordinate() {
		this.collumn = 0;
		this.row = 0;
	}
	
	public BoardCoordinate(BoardCoordinate other) {
		this.collumn = other.getCollumn();
		this.row = other.getRow();
	}
	
	public BoardCoordinate(int x, int y) {
		this.collumn = x;
		this.row = y;
	}
	
   public static boolean coordinateInsideArea(BoardCoordinate point, BoardCoordinate top_left, BoardCoordinate bottom_right) {
	   return coordinateInsideArea(point.getCollumn(), point.getRow(), top_left.getCollumn(), top_left.getRow(), bottom_right.getCollumn(), bottom_right.getRow());
   }

   public static boolean coordinateInsideArea(int x, int y, int top_left_x, int top_left_y, int bottom_right_x, int bottom_right_y) {
		// Determine the bounds of the rectangle
		int xmin = Math.min(top_left_x, bottom_right_x);
		int xmax = Math.max(top_left_x, bottom_right_x);
		int ymin = Math.min(top_left_y, bottom_right_y);
		int ymax = Math.max(top_left_y, bottom_right_y);

		// Check if the point is within the bounds
		return (x >= xmin && x <= xmax) && (y >= ymin && y <= ymax);
	}
   
   	public static BoardCoordinate translateCorodinates(int x,int y, int source_w, int source_h, int target_w, int target_h, int offsetX, int offsetY) {
   		// Scaling factors
   		double scaleX = target_w / (double) source_w;
   		double scaleY = target_h / (double) source_h;

   		// Scaling the coordinate
   		int X_scaled = (int) (x * scaleX);
   		int Y_scaled = (int) (y * scaleY);

   		// Applying translation (offset)
   		int X_lg = X_scaled + offsetX;
   		int Y_lg = Y_scaled + offsetY;	
   		
   		return new BoardCoordinate(X_lg, Y_lg);
   	}


	@Override
	public String toString() {
		return "BoardCoordinate [col=" + collumn + ", row=" + row + "]";
	}

	public int getCollumn() {
		return collumn;
	}

	public void setCollumn(int x) {
		this.collumn = x;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int y) {
		this.row = y;
	}
	
	public void subtractCollumn(int n) {
		collumn -= n;
	}

	public void subtractRow(int n) {
		row -= n;
	}

	public void addCollumn(int n) {
		collumn += n;
	}

	public void addRow(int n) {
		row += n;
	}
	
	public boolean equals(BoardCoordinate other) {
		return this.collumn == other.collumn && this.row == other.row;
	}
	
}
