package simulation;

import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;

  public class Grid_XY extends Grid{

		private LinkedList<Point> _neighbors;
		//public static final double _criticalTemperature = TODO ;
		
		public Grid_XY(int D){
			_D = D;
			_points = new double[D][D];
			
			_neighbors = new LinkedList<Point>();
			_neighbors.add(new Point(0,1));
			_neighbors.add(new Point(0,-1));
			_neighbors.add(new Point(1,0));
			_neighbors.add(new Point(-1,0));
			
			
			// Initialize lattice with random spins
			for(int i=0; i<D; i++){
				for(int j=0; j<D; j++){
					 _points[i][j] = Math.random()*2*Math.PI;
				}
			}
			
			
		}
		
		/*
		 * Grow spin chain as specified in the Wolff Cluster algorithm
		 * 
		 */
		private HashSet<Point> growChain(Point p0, double Ø){
			
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
							&& Math.random() < (1 - Math.exp(-4*_ß*Math.cos(_points[p.x][p.y]-_points[t.x][t.y]))) ){
							
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
		
		
		/*
		 * Flips each spin variable in the chain about an axis specified by Ø 
		 * 
		 */
		private void flipChain(HashSet<Point> chain, double Ø){
			double k;
			for (Point p : chain){
				k = Math.PI + 2*Ø - this._points[p.x][p.y];
				
				while (k > 2*Math.PI){
					k -= 2*Math.PI;
				}
				this._points[p.x][p.y] = k;
			}
		}
		
		/*
		 * Uses the Cluster Algorithm to sample new spin configuration
		 * 
		 */
		public void clusterUpdate(){
			
			double Ø = Math.random()*2*Math.PI;
			this.flipChain(
				this.growChain(
					this.getRandomPosition()
						   	   ,Ø)
						   ,Ø);
			
		}
		
		
		public void metropolisUpdate(){
			// TODO
		}
		
		/*
		 * Computes Magnetization for the current spin configuration
		 * 
		 */
		public double getMagnetization(){
			
			double Mx=0,
				My=0;
			for (int x=0; x<_D; x++){
				for (int y=0; y<_D; y++){
					
					Mx +=Math.cos(this.getValue(x, y));
					My +=Math.sin(this.getValue(x, y));
				}
			}
			return Math.sqrt(Mx*Mx+My*My);
			
			
		}
		
		/*
		 * Computes the Hamiltonian for the current spin configuration
		 * 
		 */
		public double getHamiltonian(){
			double Hamiltonian = 0;
			
			for (int i=0; i<_D; i++){
				for (int j=0; j<_D; j++){
					if (i<_D-1){
						Hamiltonian += Math.cos(_points[i][j]-_points[i+1][j]);
					}
					if (j<_D-1){
						Hamiltonian += Math.cos(_points[i][j]*_points[i][j+1]);
					}
				}
			}
			
			return Hamiltonian;
			
		}
		
		/* 
		 * Returns a new Grid_XY object that is produced by applying a block transformation
		 * to this Grid_XY object
		 * 
		 */
		public Grid getBlockedGrid(){
			
			Grid_XY blockedGrid = new Grid_XY(_D/2);
			double value;
			
			for (int i = 0; i<_D/2; i++){
				for (int j = 0; j<_D/2; j++){
					
					value = (this.getValue(2*i, 2*j)
							+this.getValue(2*i+1, 2*j)
							+this.getValue(2*i, 2*j+1)
							+this.getValue(2*i+1, 2*j+1)
							)/4;
					blockedGrid.setValue(i, j, value);
					
				}
			}
			
			
			return blockedGrid;
			
		}
	}

