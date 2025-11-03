package main.sprint3;

public class Board {
    public enum Cell {EMPTY, S, O};
    private Cell[][] grid;
    private int TOTALROWS;
    private int TOTALCOLUMNS;
    private String turn;
    private String gamemodeSelected = "";
    private String player1Selection = "";
    private String player2Selection = "";

    public Board(int rowColumn) {
        TOTALCOLUMNS = rowColumn;
        TOTALROWS = rowColumn;
        grid = new Cell[TOTALROWS][TOTALCOLUMNS];
        createBoard();
    }

    public void createBoard() {
        for (int row = 0; row < TOTALROWS; row++){
            for (int column = 0; column < TOTALCOLUMNS; column++){
                grid[row][column] = Cell.EMPTY;
            }
        }
        turn = "PLAYER1";
    }

    public Cell getCell(int row, int column) {
        if (row >= 0 && row < TOTALROWS && column >= 0 && column < TOTALCOLUMNS)
            return grid[row][column];
        else
            return null;
    }

    public void setRowColumn (int number){
        TOTALCOLUMNS = number;
        TOTALROWS = number;
    }

    public int getTotalRowColumn() {return TOTALROWS;}

    public void setTurn(String str) {turn = str;}

    public String getTurn() {
        return turn;
    }

    public void setGamemodeSelection(String gamemode) {gamemodeSelected = gamemode;}

    public boolean validGameMode() {
        return gamemodeSelected.equals("SIMPLE") || gamemodeSelected.equals("GENERAL");
    }

    public boolean validBoardSize() {
        return TOTALROWS >= 3;
    }

    public void setPlayerSelection(String currentSelected, String player) {
        if (player.equals("PLAYER1")) { player1Selection = currentSelected;}
        else {
            player2Selection = currentSelected;}
    }

    public String validGame(){
        if (!validGameMode()) {return "GAMEMODEERROR" ;}
        if (!validBoardSize()) { return "BOARDSIZEERROR" ;}
        return ("PASS");
    }

    public String makeMove(int row, int column){
        if (turn.equals("PLAYER1")){
            if (player1Selection.equals("S") || player1Selection.equals("O")) {
                if (row >= 0 && row < TOTALROWS && column >= 0 && column < TOTALCOLUMNS && grid[row][column] == Cell.EMPTY) {
                    grid[row][column] = (player1Selection.equals("S")) ? Cell.S : Cell.O;
                    turn = "PLAYER2";
                    return ("SUCCESS");
                }
                else {return ("INVALID");}
            }
            else{
                System.out.println("Player 1 select S or O");
            }
        }
        else if (turn.equals("PLAYER2")){
            if (player2Selection.equals("S") || player2Selection.equals("O")) {
                if (row >= 0 && row < TOTALROWS && column >= 0 && column < TOTALCOLUMNS && grid[row][column] == Cell.EMPTY) {
                    grid[row][column] = (player2Selection.equals("S")) ? Cell.S : Cell.O;
                    turn = "PLAYER1";
                    return ("SUCCESS");
                }
                else {return ("INVALID");}
            }
            else{
                System.out.println("Player 2 select S or O");
            }
        }
        return ("FAIL");
    }
}
