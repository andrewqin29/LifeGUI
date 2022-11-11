/**
 * Name: Andrew Qin
 * Date: Feb 11, 2022
 * Period 1
 * 
 * Does this lab work? No
 * Any other comments? I am not sure why this does not work. I will get help in class on Monday.
 */

import java.io.*;
import java.util.*;

public class P1_Qin_Andrew_LifeModel extends GridModel<Boolean>{
	
	ArrayList<GenerationListener> listeners;
	public static int currentGeneration;
	
	/**
	 * @param gridData
	 */
	public P1_Qin_Andrew_LifeModel(Boolean[][] gridData) {
		super(gridData);
		listeners = new ArrayList<>();
		currentGeneration = 0;
		
		// TODO Auto-generated constructor stub
	}

	
	public void runLife(int numGenerations) {
		for(int i=0; i<numGenerations; i++) {
			nextGeneration();
//			printBoard();
			
		}
	}
	
	public int rowCount(int row) {
		if(row<0 || row>=getNumRows()) return -1;
		
		int count = 0;
		for(int i=0; i<getNumCols(); i++) {
			if(getValueAt(row, i)==true) count++;
		}
		
		return count;
	}
	
	public int colCount(int col) {
		if(col<0 || col>=getNumCols()) return -1;
		
		int count = 0;
		for(int i=0; i<getNumRows(); i++) {
			if(getValueAt(i, col)==true) count++;
		}
		
		return count;
	}
	
	public int totalCount() {
		int count = 0;
		for(int i=0; i<getNumRows(); i++) {
			for(int j=0; j<getNumCols(); j++) {
				if(getValueAt(i, j)==true) count++;
			}
		}
		
		return count;
	
	}
	
	public void printBoard() {
		int numSpaces = String.valueOf(getNumRows()-1).length();
		String spacing = "";
		for(int i=0; i<numSpaces+1; i++) {
			spacing+=" ";
		}
		
		//horizontal headers
		System.out.print(spacing);
		for(int i=0; i<getNumCols(); i++) {
			System.out.print(i%10);
		}
		System.out.println();
		
		for(int i=0; i<getNumRows(); i++) {
			String format = "%-"+numSpaces+"d";
			System.out.printf(format+" ", i);
			for(int j=0; j<getNumCols(); j++) {
				System.out.print(getValueAt(i, j));
			}
			System.out.println();
			
		}
		
	}
	
	public void nextGeneration() {
		currentGeneration++;
		
		ArrayList<Point> toBlank = new ArrayList<>();
		ArrayList<Point> toStar = new ArrayList<>();
		
		for(int i=0; i<getNumRows(); i++) {
			for(int j=0; j<getNumCols(); j++) {
				boolean conv = this.getValueAt(i, j);
				
				if(conv) {
					if(numNeighbors( i, j)<=1 || numNeighbors( i, j)>=4) {
						toBlank.add(new Point(i, j));

					}
				}else {
					if(numNeighbors( i, j)==3) {
						toStar.add(new Point(i, j));

					}
				}
			}
			
		}
		
		for(int i=0; i<toBlank.size(); i++) {
			Point p = toBlank.get(i);
			this.setValueAt(p.row,  p.col, false);
		}
		
		for(int i=0; i<toStar.size(); i++) {
			Point p = toStar.get(i);
			this.setValueAt(p.row,  p.col, true);
		}
	}
	
	public int numNeighbors(int row, int col) {
		int count = 0;
		
		count+=validNeighbor(row-1, col-1);
		count+=validNeighbor( row-1, col);
		count+=validNeighbor(row-1, col+1);
		count+=validNeighbor( row, col-1);
		count+=validNeighbor(row, col+1);
		count+=validNeighbor( row+1, col-1);
		count+=validNeighbor( row+1, col);
		count+=validNeighbor( row+1, col+1);
		
		return count;

	}
	
	void clear() {
		for(int i=0; i<getNumRows(); i++) {
			for(int j=0; j<getNumCols(); j++) {
				setValueAt(i, j, false);
			}
		}
	}
	
	public int validNeighbor( int row, int col) {
		if(row>=getNumRows() || row<0 || col>=getNumCols() || col<0) return 0;
		if(getValueAt(row, col)) return 1;
		return 0;
	}
	
	void addGenerationListener(GenerationListener l) {
		listeners.add(l);
	}
	
	void removeGenerationListener(GenerationListener l) {
		listeners.remove(l);
	}
	
	void setGeneration(int gen) {
		int old = this.currentGeneration;
		for(int i=this.currentGeneration; i<gen; i++) {
			
			nextGeneration();
		}
		currentGeneration = gen;
		for(GenerationListener j : listeners) {
			j.generationChanged(old, currentGeneration);
		}
	}
	
	int getGeneration() {
		return currentGeneration;
	}

	


}

class Point {
	int row;
	int col;
	
	public Point(int i, int j) {
		row = i;
		col = j;
	}
}
