package mysudoku;

import java.util.List;
import java.util.Vector;

public class Sudoku {
	private int board[];
	private int sizeBoard;
	private int boxArr[];
	private boolean canBeEdited[];

	Sudoku(int input[],int size) {
		this.board = input;
		this.sizeBoard = size;
		this.boxArr = makeBoxArr(this.sizeBoard);
		this.canBeEdited = new boolean[size*size];
		for(int i = 0;i<Math.pow(this.sizeBoard,2);i++) {
			if(this.board[i] == 0)
				this.canBeEdited[i] = true;
			else
				this.canBeEdited[i] = false;
		}
	}
	
	public void solve() {
		SudokuSolver newSolve = new SudokuSolver(this.board,this.sizeBoard);
		newSolve.solve();
	}
	
	public int getPosVal(int row,int column) {
		return this.board[row*this.sizeBoard + column];
	}
	
	public boolean canBeEdited(int row, int column) {
		return this.canBeEdited[row*this.sizeBoard + column];
	}
	
	public int getSize() {
		return this.sizeBoard;
	}
	
	public void printBoard() {
		
		int value,sqrt = (int)Math.pow(this.sizeBoard, 0.5);
		
		for(int i = 0;i<this.sizeBoard;i++) {
			if(i%sqrt == 0 && i != 0) 
				System.out.print("----------------------------------------\n");
			for(int j=0;j<this.sizeBoard;j++) {
				value = this.board[this.sizeBoard*i+j];
				if(j%sqrt == 0 && j!=0)
					System.out.print("|");
				if(value == 0)
					System.out.print("    ");
				else
					System.out.printf("%3d ",value);
			}
			System.out.print('\n');
		}
	}

	public boolean isFull() {

		for (int i = 0; i < (Math.pow(this.sizeBoard, 2)); i++) {
			if (this.board[i] == 0)
				return false;
		}
		
		return true;
	}

	
	public boolean isSolved() { 
	  
	  int i, j;
	  
	  List<Integer> refList = this.makeRef();
	  
	  if(this.sizeBoard == 0)
		  return true;
	  
	  //Checking rows 
	  for (i = 0; i < this.sizeBoard; i++) { 
		  refList = this.makeRef(); 
		  for(j = 0; j < this.sizeBoard; j++) {
			if(refList.contains(this.board[(this.sizeBoard)*i + j]))
				refList.remove(Integer.valueOf(this.board[(this.sizeBoard)*i + j]));
		  } 
		  if (!refList.isEmpty())
			  return false;
	  }
	   
	  
	  
	  //Checking Columns 
	  for (i = 0; i < this.sizeBoard; i++) { 
		  refList = this.makeRef(); 
		  for(j = 0; j < this.sizeBoard; j++){
			if(refList.contains(this.board[(this.sizeBoard)*j + i]))
				refList.remove(Integer.valueOf(this.board[(this.sizeBoard)*j + i]));
		  } 
		  if (!refList.isEmpty())
			  return false;
	  }
	  
	   
	  //Checking Boxes
	  for(i=0;i<this.sizeBoard;i++) {
		refList = this.makeRef();
		for(j=0;j<this.sizeBoard;j++){
		if(refList.contains(this.board[this.boxArr[(this.sizeBoard)*i + j]]))
			refList.remove(Integer.valueOf(this.board[this.boxArr[(this.sizeBoard)*i + j]]));
	  }
		if (!refList.isEmpty())
			  return false;
	  }
	  
	  return true;
	}
	
	public boolean makeEntry(int entry,int row,int column) {
		
		if(canBeEdited[row*this.sizeBoard + column]) {
			this.board[row*this.sizeBoard + column] = entry;
			return true;
		}
		
		return false;	
	}
	
	public boolean deleteEntry(int row,int column) {
		
		return this.makeEntry(0,row,column);
			
	}
	
	public boolean isValid(int row,int column) {
		
		int boxStart = 0,count = 0;
		int index = this.sizeBoard * row + column;
		int value = this.board[index];
		
		if(value == 0)
			return true;
	
		if(value > this.sizeBoard)
			return false;
		
		for(int i=0;i<this.sizeBoard;i++)
			for(int j = 0;j<this.sizeBoard;j++) {
				if(this.boxArr[this.sizeBoard*i + j] == index)
					boxStart = i;
			}
		
		
		//Check element's row
		for(int i = 0;i<this.sizeBoard;i++)
			if(this.board[this.sizeBoard*row + i] == value)
				count++;
		if(count>1)
			return false;
		
		count = 0;
		
		//Check element's column
		for(int i = 0;i<this.sizeBoard;i++)
			if(this.board[this.sizeBoard*i + column] == value)
				count++;
		if(count>1)
			return false;
				
		count = 0;
		
		//Check element's box
		for(int i = 0;i<this.sizeBoard;i++)
			if(this.board[this.boxArr[boxStart*this.sizeBoard + i]] == value)
				count++;
		if(count>1)
			return false;
		
		return true;
	}

	private List<Integer> makeRef() {

		List<Integer> retVal = new Vector<Integer>(this.sizeBoard);

		for (int i = 1; i <= this.sizeBoard; i++)
			retVal.add(i);

		return retVal;
	}

	public int[] makeBoxArr(int n) {
		int retVal[] = new int[n * n];
		int i, j, start, sqrt = (int) Math.pow(n, 0.5);

		for (i = 0; i < n; i++) {
			start = sqrt * ((i % sqrt) + (i / sqrt) * n);
			for (j = 0; j < n; j++) {
				retVal[n*i+j] = (start + (j % sqrt) + (j / sqrt) * n);
			}
			
		}

		return retVal;
	}

	
	private class SudokuSolver {
		
		public Sudoku sudoku;
		
		SudokuSolver(int[] arr,int size){
			this.sudoku = new Sudoku(arr,size);
		}
		
		public void solve() {
			int i = 0;
			int size = this.sudoku.getSize();
			boolean flag = true;
			int curVal;
			
			while(!this.sudoku.isSolved()) {
				if(!this.sudoku.canBeEdited(i/size,i%size)) {
					if(flag)
						i++;
					else
						i--;
					continue;
				}
				
				curVal = this.sudoku.getPosVal(i/size,i%size);
				
				if(curVal >= size) {
					flag = false;
					this.sudoku.deleteEntry(i/size, i%size);
					i--;
				}
				else {
					flag = true;
					this.sudoku.makeEntry(curVal+1, i/size, i%size);
					//this.sudoku.printBoard();
					/*try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					if(this.sudoku.isValid(i/size,i%size))
						i++;
					else
						continue;;
				}
					
			}
			
		}
	}
	
}
