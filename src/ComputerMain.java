import java.util.*;
import java.util.stream.Collectors;

public class ComputerMain {
    static HashMap<Integer, String> grid = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();
    static boolean isWin = false;
    static boolean isDraw = false;
    static Set<Integer> availableNumbers;

    static final int MAX_MOVES = 9;
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    static final int[][] WINNING_COMBOS = {
            {1,2,3},{4,5,6},{7,8,9},
            {1,4,7},{2,5,8},{3,6,9},
            {1,5,9},{3,5,7}
    };

    public static void main(String[] args) {
        do {
            resetGrid();
            printGrid();
            if(askWhoStarts()){
                do {
                    askMove();
                    isWin = checkWinFor("O");
                    isDraw = checkIfDraw();
                    if(isDraw){
                        break;
                    }
                    if(isWin){
                        System.out.println("You win");
                        break;
                    }
                    computerMove();
                    isWin = checkWinFor("X");
                    isDraw = checkIfDraw();
                    if(isDraw){
                        break;
                    }
                    if(isWin){
                        System.out.println("I win");
                        break;
                    }
                } while(!isDraw);
            } else {
                do {
                    computerMove();
                    isWin = checkWinFor("X");
                    isDraw = checkIfDraw();
                    if(isDraw){
                        break;
                    }
                    if(isWin){
                        System.out.println("I win");
                        break;
                    }
                    askMove();
                    isWin = checkWinFor("O");
                    isDraw = checkIfDraw();
                    if(isDraw){
                        break;
                    }
                    if(isWin){
                        System.out.println("You win");
                        break;
                    }
                } while(!isDraw);
            }
        } while (askPlayAgain());
    }

    static void resetGrid() {
        grid.put(1, "1");
        grid.put(2, "2");
        grid.put(3, "3");
        grid.put(4, "4");
        grid.put(5, "5");
        grid.put(6, "6");
        grid.put(7, "7");
        grid.put(8, "8");
        grid.put(9, "9");
        availableNumbers = grid.keySet().stream().collect(Collectors.toSet());
    }

    static void printGrid() {
        for (int i = 1; i <= MAX_MOVES ; i++) {
            if(grid.get(i).equals("O")){
                System.out.printf(BLUE + grid.get(i) + RESET);
            } else if(grid.get(i).equals("X")){
                System.out.printf(RED + grid.get(i) + RESET);
            } else {
                System.out.print(grid.get(i));
            }
            if(i == MAX_MOVES){
                System.out.println();
            } else if(i % 3 == 0){
                System.out.println();
                System.out.println("_ . _ . _");
            } else {
                System.out.print(" | ");
            }
        }
    }

    static boolean askWhoStarts(){
        System.out.println("You're O and I'm X.");
        System.out.print("Do you want to make the first move? (y/n): ");
        String ans = scanner.nextLine().trim().toLowerCase();
        return ans.equals("y");
    }

    static void askMove(){
        boolean isNumberValid = true;
        int ans = 0;
        do{
            try {
                System.out.print("Write the number where you want to put O: ");
                ans = Integer.parseInt(scanner.nextLine());
                if(!availableNumbers.contains(ans)){
                    throw new RuntimeException();
                }
                isNumberValid = true;
            } catch(Exception e) {
                System.out.println("Write a valid number from the grid");
                isNumberValid = false;
            }
        } while(!isNumberValid);
        availableNumbers.remove(ans);
        grid.put(ans, "O");
        printGrid();
    }

    static void computerMove() {
        if (tryWin()) {
            System.out.println("\nThis is my move:");
            printGrid();
            return;
        }
        if (blockPlayer()) {
            System.out.println("\nThis is my move:");
            printGrid();
            return;
        }
        int bestMove = findBestMove();
        grid.put(bestMove, "X");
        availableNumbers.remove(bestMove);
        System.out.println("\nThis is my move:");
        printGrid();
    }

    static int findBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int move = -1;

        for (int pos : availableNumbers) {
            String prev = grid.get(pos);
            grid.put(pos, "X");

            Set<Integer> nextAvailable = new HashSet<>(availableNumbers);
            nextAvailable.remove(pos);

            int score = minimax(false, nextAvailable);

            grid.put(pos, prev);
            if (score > bestScore) {
                bestScore = score;
                move = pos;
            }
        }

        return move;
    }

    static int minimax(boolean isMaximizing, Set<Integer> currentAvailable) {
        if (checkWinFor("X")) return 1;
        if (checkWinFor("O")) return -1;
        if (currentAvailable.isEmpty()) return 0;

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int pos : currentAvailable) {
            String symbol = isMaximizing ? "X" : "O";
            String prev = grid.get(pos);
            grid.put(pos, symbol);

            Set<Integer> nextAvailable = new HashSet<>(currentAvailable);
            nextAvailable.remove(pos);

            int score = minimax(!isMaximizing, nextAvailable);
            grid.put(pos, prev);  // Annulla la mossa

            if (isMaximizing) {
                bestScore = Math.max(score, bestScore);
            } else {
                bestScore = Math.min(score, bestScore);
            }
        }
        return bestScore;
    }

    static boolean checkWinFor(String player) {
        for (int[] combo : WINNING_COMBOS) {
            if (grid.get(combo[0]).equals(player) &&
                    grid.get(combo[1]).equals(player) &&
                    grid.get(combo[2]).equals(player)) {
                return true;
            }
        }
        return false;
    }

    static boolean checkIfDraw() {
        boolean isGridFull = true;
        for (int i = 1; i <= MAX_MOVES; i++) {
            String value = grid.get(i);
            if (!value.equals("X") && !value.equals("O")) {
                isGridFull = false;
                break;
            }
        }

        if (isGridFull && !isWin) {
            System.out.println("It's a draw");
            return true;
        }
        return false;
    }

    static boolean makeStrategicMove(String player) {
        for (int[] combo : WINNING_COMBOS) {
            String a = grid.get(combo[0]);
            String b = grid.get(combo[1]);
            String c = grid.get(combo[2]);

            if (a.equals(b) && a.equals(player) && availableNumbers.contains(combo[2])) {
                grid.put(combo[2], "X");
                availableNumbers.remove(combo[2]);
                return true;
            }
            if (a.equals(c) && a.equals(player) && availableNumbers.contains(combo[1])) {
                grid.put(combo[1], "X");
                availableNumbers.remove(combo[1]);
                return true;
            }
            if (b.equals(c) && b.equals(player) && availableNumbers.contains(combo[0])) {
                grid.put(combo[0], "X");
                availableNumbers.remove(combo[0]);
                return true;
            }
        }
        return false;
    }

    static boolean tryWin(){
        return makeStrategicMove("X");
    }

    static boolean blockPlayer(){
        return makeStrategicMove("O");
    }

    static boolean askPlayAgain(){
        System.out.println();
        System.out.print("Play again? (y/n): ");
        return scanner.nextLine().trim().equalsIgnoreCase("y");
    }
}
