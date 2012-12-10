package simulation;

import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;

public class Grid_Ising extends Grid{

  private LinkedList<Point> _neighbors;
	public static final double _criticalTemperature = 2 / Math.log(1 + Math.sqrt(2));
	
	public Grid_Ising(int D) {
		_D = D;
		_points = new double[D][D];
		
		_neighbors = new LinkedList<Point>();
		_neighbors.add(new Point(0,1));
		_neighbors.add(new Point(0,-1));
		_neighbors.add(new Point(1,0));
		_neighbors.add(new Point(-1,0));
		
		
		
		for(int i=0; i<D; i++){
			for(int j=0; j<D; j++){
				double P = Math.random();
				if(P>.5){
					this._points[i][j] = -1;
				}else{
					this._points[i][j] = 1;
				}
			}
		}
		
		
	}
	
	//<><><><><><><><><><><><><><><><><> Begin Cluster Algorithm <><><><><><><><><><><><><><><><><><><>
	private HashSet<Point> growChain(Point p0){
		LinkedList<Point> newSites = new LinkedList<Point>(),
		 				 addedSites = new LinkedList<Point>();
		HashSet<Point> chain = new HashSet<Point>();
		chain.add(p0);
		addedSites.add(p0);
		Point t;
		while(addedSites.size() > 0){
			newSites.clear();
			
			for (Point p : addedSites){
				for(Point n : _neighbors){
					
					t = new Point(p.x+n.x, p.y+n.y);
					
					if (t.x >= 0
					    && t.y >= 0
					    && t.x < _D
					    && t.y < _D
					    
					    && !chain.contains(t)
						&& Math.random() < (1 - Math.exp(-2*_ß*this.getValue(t.x, t.y)*this.getValue(p.x, p.y))) ){
						
						newSites.addFirst(t);
						chain.add(t);
					}
					
				}
				
			}
			addedSites.clear();
			addedSites.addAll(newSites);
		}
		
		return chain;
	}
	
	private void flipChain(HashSet<Point> chain){
		
		for (Point p : chain){
			this._points[p.x][p.y] *= -1;
		}
	}
	
	public void clusterUpdate(){
		this.flipChain(
			this.growChain(
				this.getRandomPosition()
					   	   )
					   );
		
	}
	//<><><><><><><><><><><><><><><><><><> End Cluster Algorithm <><><><><><><><><><><><><><><><><><><>
	
	public void metropolisUpdate(){
		
		Point p = this.getRandomPosition();
		
		double Hi = this.getHamiltonian(),
			   Hf;
		
		_points[p.x][p.y] *= -1;
		
		Hf = this.getHamiltonian();
		
		if (Math.random() > Math.min(1, Math.exp(_ß*(Hf-Hi) ) ) ){
			_points[p.x][p.y] *= -1;
		}
	}
		
	public double getHamiltonian(){	
		double Hamiltonian = 0;
		
		for (int i=0; i<_D; i++){
			for (int j=0; j<_D; j++){
				if (i<_D-1){
					Hamiltonian += _points[i][j]*_points[i+1][j];
				}
				if (j<_D-1){
					Hamiltonian += _points[i][j]*_points[i][j+1];
				}
			}
		}
		
		return Hamiltonian;
	}
	
	public double getMagnetization(){
		int M=0;
		for (int x=0; x<_D; x++){
			for (int y=0; y<_D; y++){
				
				M+=this.getValue(x, y);
			}
		}
		return M;
	}
	
	// Returns a 2x2 blocked copy of this grid
	public Grid getBlockedGrid(){
		
		Grid_Ising blockedGrid = new Grid_Ising(_D/2);
		double value;
		
		for (int i = 0; i<_D/2; i++){
			for (int j = 0; j<_D/2; j++){
				
				value = this.getValue(2*i, 2*j)
						+this.getValue(2*i+1, 2*j)
						+this.getValue(2*i, 2*j+1)
						+this.getValue(2*i+1, 2*j+1);
				
				if (value > 0) {value = 1;}
				else if (value < 0) {value = -1;}
				else {value = -1 + 2*Math.round(Math.random());}
				
				
				blockedGrid.setValue(i, j, value);
				
			}
		}
		
		
		return blockedGrid;
		
	}
}
