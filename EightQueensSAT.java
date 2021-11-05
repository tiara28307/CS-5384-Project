import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author Zhichao Cao
 *
 */

public class EightQueensSAT {
	
	public static void main(String[] args) throws Exception {
		
		// Side length of the game board which is square. This is also the
		// number of queens on the board.
		int sideLength = 0;
		
		if(args.length == 1) {
			sideLength = Integer.parseInt(args[0]);
		}
		else {
			System.out.println("Exactly one argument must be supplied, the size of board.");
			return;
		}

		int[][] gameBoard = new int[sideLength][sideLength];
		int counter = 1;
		// Initialization of game board
		for(int i = 0; i < sideLength; i++) {
			for(int j = 0; j < sideLength; j++) {
				// Identifier number for each variable, required by dimacs format
				gameBoard[i][j] = counter;
				counter++;
			}
		}
		
		ArrayList<ArrayList<Integer>> cnfLists = new ArrayList<ArrayList<Integer>>();

		// Generate boolean clauses for each horizontal row
		for(int i = 0; i < sideLength; i++) {
			ArrayList<Integer> literalList = new ArrayList<Integer>();
			for(int j = 0; j < sideLength; j++) {
				literalList.add(gameBoard[i][j]);
			}
			HorizontalRows(literalList.size(), literalList, cnfLists);
		}

		// Generate boolean clauses for each vertical column
		for(int i = 0; i < sideLength; i++) {
			ArrayList<Integer> literalList = new ArrayList<Integer>();
			for(int j = 0; j < sideLength; j++) {
				literalList.add(gameBoard[j][i]);
			}
			VerticalCols(literalList.size(), literalList, cnfLists);
		}

		// All diagonal lines of in the square game board
		AllDiaginalLines(sideLength, gameBoard, cnfLists);
		
		String inputFile = "input" + sideLength + ".cnf";
		
		// Print into a file for Minisat
		PrintFile(inputFile, sideLength, cnfLists);
		// NOTE: redirecting output to the statFile's didn't appear to work, so
		// we opted to run from the command line instead. See runQueens.bash
		
		// Execute Minisat
		// String outputFile = "output" + sideLength;
		// String statFile = "statistics" + sideLength;
		// Process minisat = Runtime.getRuntime().exec("./MiniSat_v1.14_linux " + inputFile + " " + outputFile + " > " + statFile); // Linux call MiniSat
		// Process minisat = Runtime.getRuntime().exec("minisat input.cnf > output");  // Windows call MiniSat
		// minisat.waitFor();
		
		//Scanner s = new Scanner(new File(outputFile));
		//while(s.hasNext()) {
		//	System.out.println(s.nextLine());
		//}
	}
	
	private static void HorizontalRows(int size, ArrayList<Integer> literalList, ArrayList<ArrayList<Integer>> cnfLists)  {
		
		// There is one and only one queen in each row, such as x11 V x12 V x13 V x14 = true
		cnfLists.add(literalList);
		
		// Other clauses, since we only have one queen on each row, so x1i -> !x1j = !x1i V !x1j for any i != j, 
		for(int i = 0; i < size - 1; i++) {
			for(int j = i + 1; j < size; j++) {
				ArrayList<Integer> clauseList = new ArrayList<Integer>();
				clauseList.add(-literalList.get(i));
				clauseList.add(-literalList.get(j));
				cnfLists.add(clauseList);
			}
		}
		
	}
	
	private static void VerticalCols(int size, ArrayList<Integer> literalList, ArrayList<ArrayList<Integer>> cnfLists)  {
		
		// There is one and only one queen in each column, such as x11 V x21 V x31 V x41 = true
		cnfLists.add(literalList);		
		
		// Other clauses, since we only have one queen on each column, so xi1 -> !xj1 = !xi1 V !xj1 for any i != j, 
		for(int i = 0; i < size - 1; i++) {
			for(int j = i + 1; j < size; j++) {
				ArrayList<Integer> clauseList = new ArrayList<Integer>();
				clauseList.add(-literalList.get(i));
				clauseList.add(-literalList.get(j));
				cnfLists.add(clauseList);
			}
		}
		
	}
	
	private static void AllDiaginalLines(int sideLength, int[][] gameBoard, ArrayList<ArrayList<Integer>> cnfLists) {

		// Go through all diagonal lines of the game board square
		for(int i = 0; i < sideLength - 1; i++) {
			ArrayList<Integer> literalList = new ArrayList<Integer>();
			for(int j = 0; j < sideLength - i; j++) {
				literalList.add(gameBoard[j][j + i]);
			}
			DiagonalLine(literalList.size(), literalList, cnfLists);
		}		
		for(int i = 1; i < sideLength - 1; i++) {
			ArrayList<Integer> literalList = new ArrayList<Integer>();
			for(int j = 0; j < sideLength - i; j++) {
				literalList.add(gameBoard[j + i][j]);
			}
			DiagonalLine(literalList.size(), literalList, cnfLists);
		}		
		for(int i = 0; i < sideLength - 1; i++) {
			ArrayList<Integer> literalList = new ArrayList<Integer>();
			for(int j = 0; j < sideLength - i; j++) {
				literalList.add(gameBoard[j][sideLength - 1 - (j + i)]);
			}
			DiagonalLine(literalList.size(), literalList, cnfLists);
		}		
		for(int i = 1; i < sideLength - 1; i++) {
			ArrayList<Integer> literalList = new ArrayList<Integer>();
			for(int j = 0; j < sideLength - i; j++) {
				literalList.add(gameBoard[j + i][sideLength - 1 - j]);
			}
			DiagonalLine(literalList.size(), literalList, cnfLists);
		}
		
	}
	
	private static void DiagonalLine(int size, ArrayList<Integer> literalList, ArrayList<ArrayList<Integer>> cnfLists) {
		
		// There is at most one queen in each diagonal line, which means we do not have to put a queen in diagonal line
		// Therefore, we do not need the clause such as  x11 V x22 V x33 V x44 = true
		for(int i = 0; i < size - 1; i++) {
			for(int j = i + 1; j < size; j++) {
				ArrayList<Integer> clauseList = new ArrayList<Integer>();
				clauseList.add(-literalList.get(i));
				clauseList.add(-literalList.get(j));
				cnfLists.add(clauseList);
			}
		}
		
	}
	
	private static void PrintFile(String path, int sideLength, ArrayList<ArrayList<Integer>> cnfLists) throws IOException {
		
		// Print into a file for Minisat
		File file = new File(path);
		if(!file.exists()) {
			file.createNewFile();
		}
		FileWriter fileWriter = new FileWriter(file);
		StringBuilder sb = new StringBuilder();
		sb.append("c input.cnf from Queens problem for Minisat\n");
		sb.append("c\n");
		sb.append("p cnf " + sideLength * sideLength + " " + cnfLists.size() + "\n");
		System.out.println(cnfLists.size());
		
		for(ArrayList<Integer> list : cnfLists) {
			for(int i : list) {
				sb.append(i + " ");
			}
			sb.append(" 0\n");
		}
		fileWriter.write(sb.toString());
		fileWriter.close();
	}
	// Run `java EightQueensSAT $n`
}
