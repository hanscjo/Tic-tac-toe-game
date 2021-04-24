package game;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;

public class TicTacToe implements Files {
	
	private String symbols = "         ";
	private char currentPlayer = 'x';
	private char[] validChars = { ',', '1', '2', '3' };
	
	Stack<String> regretStack = new Stack<String>();
	Stack<String> redoStack = new Stack<String>();
	
	char getCell(int x, int y) {
		int index = (((y-1)*3)+x)-1;
		return symbols.charAt(index);
	}
	
	private boolean setCell(char c, int x, int y) {
		if (! isOccupied(x,y)) {
			int index = (((y-1)*3)+x)-1;
			this.regretStack.push(this.symbols + "-" + c);
			while (! redoStack.empty()) {
				redoStack.pop();
			}
			char[] symbolsArray = this.symbols.toCharArray();
			//System.out.println(symbolsArray);
			symbolsArray[index] = c;
			//System.out.println(symbolsArray);
			//System.out.println("===========");
			this.symbols = new String(symbolsArray);
			return true;
		}
		return false;
	}
	
	boolean isOccupied(int x, int y) {
		int index = (((y-1)*3)+x)-1;
		return (symbols.charAt(index) != ' ');
	}
	
	char getCurrentPlayer() {
		return this.currentPlayer;
	}
	
	void play(int x, int y) {
		if (! isFinished()) {
			if (! isOccupied(x, y)) {
				setCell(getCurrentPlayer(), x, y);
				if (hasWinner()) {
					System.out.println("Spillet er ferdig. Vinneren er: " + this.getCurrentPlayer());
				}
				else {
					if (this.currentPlayer == 'x') {
						this.currentPlayer = 'o';
					}
					else {
						this.currentPlayer = 'x';
					}
				}
				
			}
			else {
				throw new IllegalArgumentException("Feltet er allerede spilt");
			}
		}
		else {
			throw new IllegalStateException("Spillet er ferdig.");
		}
			
	}
	
	boolean hasWinner() {
		if (symbols.charAt(0) != ' ' && symbols.charAt(0) == symbols.charAt(1) && symbols.charAt(0) == symbols.charAt(2)) { //Første rad
			return true;
		}
		else if (symbols.charAt(3) != ' ' && symbols.charAt(3) == symbols.charAt(4) && symbols.charAt(3) == symbols.charAt(5)) { //Andre rad
			return true;
		}
		else if (symbols.charAt(6) != ' ' && symbols.charAt(6) == symbols.charAt(7) && symbols.charAt(6) == symbols.charAt(8)) { //Tredje rad
			return true;
		}
		else if (symbols.charAt(0) != ' ' && symbols.charAt(0) == symbols.charAt(3) && symbols.charAt(0) == symbols.charAt(6)) { //Første kolonne
			return true;
		}
		else if (symbols.charAt(1) != ' ' && symbols.charAt(1) == symbols.charAt(4) && symbols.charAt(1) == symbols.charAt(7)) { //Andre kolonne
			return true;
		}
		else if (symbols.charAt(2) != ' ' && symbols.charAt(2) == symbols.charAt(5) && symbols.charAt(2) == symbols.charAt(8)) { //Tredje kolonne
			return true;
		}
		else if (symbols.charAt(0) != ' ' && symbols.charAt(0) == symbols.charAt(4) && symbols.charAt(0) == symbols.charAt(8)) { //Diagonal fra x = 1, y = 1
			return true;
		}
		else if (symbols.charAt(6) != ' ' && symbols.charAt(6) == symbols.charAt(4) && symbols.charAt(6) == symbols.charAt(2)) { //Diagonal fra x = 1, y = 3
			return true;
		}
		else {
			return false;
		}
	}
	
	boolean isWinner(char c) {
		if (hasWinner()) {
			return (this.getCurrentPlayer() == c);
		}
		return false;
	}
	
	boolean isFinished() {
		if (hasWinner()) {
			return true;
		}
		else if (this.symbols.indexOf(' ') == -1) {
			return true;
		}
		else {
			return false;
		}
	}
	
	void getInput(String in) {
		//List<char[]> list = Arrays.asList(this.validChars);
		if (in.length() != 3) {
			throw new IllegalArgumentException("Koordinatene skrives 'x,y' med tre tegn til sammen. Unngå tegn og mellomrom som ikke er et komma eller tallene 1-3.");
		}
		
		int k = 0;
		int m = 0;
		for (int i = 0; i < in.length(); i++) {
			if (in.charAt(i) == ',') {
				k++;
			}
			if (in.charAt(i) == ' ') {
				m++;
			}
		}
		if (k != 1 || m > 0) {
			throw new IllegalArgumentException("Koordinatene må separeres med ett komma og uten noen mellomrom slik: x,y");
		}
		
		int t = 0;
		for (int h = 0; h < in.length(); h++) {
			for (int j = 0; j < validChars.length; j++) {
				if (in.charAt(h) == validChars[j]) {
					t++;
				}
			}
			//if (! (list.contains(in.charAt(h)))) {
				//throw new IllegalArgumentException("Vennligst ikke skriv inn tegn som ikke er et komma, eller tallene 1-3.");
			//}
		}
		if (t != 3) {
			throw new IllegalArgumentException("Koordinatene skrives 'x,y' med tre tegn til sammen. Unngå tegn og mellomrom som ikke er et komma eller tallene 1-3.");
		}
		
		
		String[] coordinates = in.split(",");
		
		int x = Integer.parseInt(coordinates[0]);
		int y = Integer.parseInt(coordinates[1]);
		if ((x > 3 || x < 0) && (y > 3 || y < 0)) {
			throw new IllegalArgumentException("Hverken av de to oppgitte koordinatene passer for 3x3-brettet. Bruk verdier fra 1-3.");
		}
		if (x > 3 || x < 0) {
			throw new IllegalArgumentException("Den oppgitte x-koordinaten passer ikke for 3x3-brettet. Bruk verdier fra 1-3.");
		}
		if (y > 3 || y < 0) {
			throw new IllegalArgumentException("Den oppgitte y-koordinaten passer ikke for 3x3-brettet. Bruk verdier fra 1-3.");
		}
		
		
		
		play(x,y);
	}
	
	public String toString() {
		String returnString = "";
		for (int y = 3; y > 0; y--) {
			for (int x = 1; x < 4; x++) {
				int index = (((y-1)*3)+x)-1;
				returnString = returnString + this.symbols.charAt(index) + "    ";
			}
			returnString = returnString + "\r\n";
		}
		if (hasWinner()) {
			returnString = returnString + "\r\nSpillet er ferdig.  Vinneren er:  " + this.getCurrentPlayer();
		}
		else if (isFinished()) {
			returnString = returnString + "\r\nSpillet er ferdig.  Uavgjort.";
		}
		//returnString = returnString + "=====";
		return returnString;
	}
	
	
	
	//public void addRegret(String gameState) {
		//this.regretStack.push(gameState);
	//}
	
	public void undo() {
		if (this.regretStack.empty()) {
			throw new IllegalStateException("Det er ingen handlinger igjen å angre");
		}
		String stackTop = this.regretStack.pop();
		this.redoStack.push(this.symbols + "-" + this.getCurrentPlayer());
		String[] parts = stackTop.split("-");
		this.symbols = parts[0];
		this.currentPlayer = parts[1].charAt(0);
	}
	
	public void redo() {
		if (this.redoStack.empty()) {
			throw new IllegalStateException("Det er ingen handlinger igjen å gjenta");
		}
		String stackTop = this.redoStack.pop();
		this.regretStack.push(this.symbols + "-" + this.getCurrentPlayer());
		String[] parts = stackTop.split("-");
		this.symbols = parts[0];
		this.currentPlayer = parts[1].charAt(0);
	}
	
	public void save(String FileName) {
		//Her kan vi legge til ulik funksjonalitet basert på filtype, dersom vi hadde benyttet flere filtyper.
		String format = getFileFormat(FileName);
		System.out.println(format);
		if (format == "txt") {
			try {
				PrintWriter outFile = new PrintWriter(FileName);
				outFile.println(this.symbols + "-" + this.getCurrentPlayer());
				System.out.println(this.symbols + "-" + this.getCurrentPlayer());
				outFile.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.err.println("Feil: Filen '" + FileName + "' kunne ikke leses.");
				//System.exit(1);
			}
		}
	}

	public void load(String FileName) {
		//Her kan vi legge til ulik funksjonalitet basert på filtype, dersom vi hadde benyttet flere filtyper.
		String format = getFileFormat(FileName);
		if (format == "txt") {
			try {
				Scanner in = new Scanner(new FileReader(FileName));
				String output = in.nextLine();
				in.close();
				String[] parts = output.split("-");
				this.symbols = parts[0];
				this.currentPlayer = parts[1].charAt(0);
				while(! this.regretStack.empty()) {
					this.regretStack.pop();
				}
				while(! this.redoStack.empty()) {
					this.redoStack.pop();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.err.println("Feil: Filen '" + FileName + "' kunne ikke leses.");
			}
		}
	}
	
	public String getFileFormat(String FileName) {
		if (FileName.contains(".")) {
			String[] parts = FileName.split(".");
			System.out.println(FileName.split("."));
			return "txt";//parts[1];
		}
		throw new IllegalArgumentException("Filnavn må mates inn slik: eksempel.txt");
	}
	
	public static void main(String[] args) {
		TicTacToe tic = new TicTacToe();
		tic.play(1, 1);
		System.out.println(tic);
		tic.play(1, 3);
		System.out.println(tic);
		tic.play(2, 2);
		System.out.println(tic);
		tic.play(2, 3);
		System.out.println(tic);
		tic.play(3, 2);
		System.out.println(tic);
		tic.play(3, 3);
		System.out.println(tic);
		System.out.println(tic.isOccupied(1, 3));
	}

	

}
