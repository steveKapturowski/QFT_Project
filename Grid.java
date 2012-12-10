package simulation;

import java.awt.Point;



public abstract class Grid {
  
	protected double[][] _points;
	protected double _ß = 1;
	protected int _D;
	
	public double getß(){
		return _ß;
	}
	public void setß(double ß){
		_ß = ß;
	}
	public Point getRandomPosition(){
		int  x = (int)Math.round( (Math.random()*(_D)) - .5 );
		int  y = (int)Math.round( (Math.random()*(_D)) - .5 );
		
		return new Point(x,y);
	}
	public int getSize(){
		return _D;
	}
	public void setValue(int x, int y, double value){
		_points[x][y] = value;
	}
	public double getValue(int x, int y){
		return _points[x][y];
	}
	
	public abstract void clusterUpdate();
	public abstract void metropolisUpdate();
	
	public abstract double getMagnetization();
	public abstract double getHamiltonian();
	
	public abstract Grid getBlockedGrid();
}
