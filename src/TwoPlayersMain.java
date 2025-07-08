import java.util.*;

public class TwoPlayersMain {
    static String[][] grid = new String[3][3];
    static Random random = new Random();
    static Scanner scanner = new Scanner(System.in);
    static HashMap<Integer, String> players = new HashMap<>();
    static String starter;
    static String actualPlayer;
    static List<Integer> numCoord;
    static int counter = 0;
    static boolean isWin = false;
    static boolean isCoordCorrect;

    static final int MAX_MOVES = 9;
    static final String PLAYER_X = "X";
    static final String PLAYER_O = "O";

    public static void main(String[] args) {
        players.put(1, PLAYER_X);
        players.put(2, PLAYER_O);
        printGrid();
        starter = players.get(random.nextInt(1, 3));
        actualPlayer = starter;
        System.out.println("Player " + starter + " will start.");
        do {
            do {
                isCoordCorrect = askForCoordinates();
            } while (!isCoordCorrect);
            System.out.println();
            if (checkIfMoveIsAvail(numCoord)) {
                grid[numCoord.get(0) - 1][numCoord.get(1) - 1] = actualPlayer;
                counter++;
                printGrid();
            } else {
                do {
                    System.out.println("This move is not available");
                    do {
                        isCoordCorrect = askForCoordinates();
                    } while (!isCoordCorrect);
                } while (!checkIfMoveIsAvail(numCoord));
                grid[numCoord.get(0) - 1][numCoord.get(1) - 1] = actualPlayer;
                counter++;
                printGrid();
            }
            if (counter >= 5) {
                isWin = checkForWin();
                if(isWin){
                    System.out.printf("Player %s wins.%n", actualPlayer);
                    if(!askIfPlayAgain()){
                        break;
                    }

                } else if(counter == MAX_MOVES && !isWin){
                    System.out.println("It's a draw");
                    if(!askIfPlayAgain()){
                        break;
                    }

                }
            }
            togglePlayer();
        } while (!isWin);

    }

    public static void printGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == null) {
                    grid[i][j] = " ";
                }
                if (j != grid[i].length - 1) {
                    System.out.print(grid[i][j] + " | ");
                } else {
                    System.out.println(grid[i][j]);
                }
            }
            if (i != grid.length - 1) {
                System.out.println("_ . _ . _");
            }
        }
    }

    public static boolean askForCoordinates() {
        System.out.print("Write the coordinates separated by a comma: ");
        try {
            numCoord = registerCoordinates(scanner.nextLine());
            if (numCoord.size() < 2) {
                throw new IndexOutOfBoundsException();
            }
            for (int n : numCoord) {
                if ((1 > n) || (n > 3)) {
                    throw new IndexOutOfBoundsException();
                }
            }
        } catch (Exception e) {
            System.out.println("Please write the coordinates as two integers between 1 and 3 separated by a comma.");
            return false;
        }
        return true;
    }

    public static List<Integer> registerCoordinates(String coordinates) {
        String[] coord = coordinates.split(",");
        List<Integer> numCoord = new ArrayList<>();
        for (String c : coord) {
            numCoord.add(Integer.parseInt(c.trim()));
        }
        return numCoord;
    }

    public static boolean checkIfMoveIsAvail(List<Integer> numCoord) {
        return grid[numCoord.get(0) - 1][numCoord.get(1) - 1].equals(" ");
    }

    public static boolean checkForWin() {
        for (String[] row : grid) {
            if (!row[0].equals(" ") && row[0].equals(row[1]) && row[0].equals(row[2])) {
                return true;
            }
        }
        for (int j = 0; j < grid.length; j++) {
            if (!grid[0][j].equals(" ") && grid[0][j].equals(grid[1][j]) && grid[0][j].equals(grid[2][j])) {
                return true;
            }
        }
        if (!grid[0][0].equals(" ") && grid[0][0].equals(grid[1][1]) && grid[0][0].equals(grid[2][2])) {
            return true;
        }
        if (!grid[0][2].equals(" ") && grid[0][2].equals(grid[1][1]) && grid[0][2].equals(grid[2][0])) {
            return true;
        }
        return false;
    }

    public static void togglePlayer() {
        actualPlayer = actualPlayer.equals(PLAYER_X) ? PLAYER_O : PLAYER_X;
    }

    public static boolean askIfPlayAgain(){
        System.out.println("Play again? (y/n) : ");
        if(scanner.nextLine().trim().equalsIgnoreCase("y")){
            resetGame();
            return true;
        }
        return false;
    }

    public static void resetGame(){
        counter = 0;
        isWin = false;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = " ";
            }
        }
        printGrid();
        starter = players.get(random.nextInt(1, 3));
        actualPlayer = starter;
        System.out.println("Player " + starter + " will start.");
    }
}