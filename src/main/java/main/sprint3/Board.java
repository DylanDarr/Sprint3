package main.sprint3;

public class Board {
    public enum Cell {EMPTY, S, O};
    private Cell[][] gameGrid;
    public enum Color {RED, BLUE, PURPLE, BLACK}
    private Color[][] colorGrid;
    private int BOARDSIZE;
    private String turn;
    private int unsetSOS = 0;
    private boolean gameOver = true;
    private String gamemodeSelected = "";
    private String player1Selection = "";
    private String player2Selection = "";
    private int player1SOSCount;
    private int player2SOSCount;

    public Board(int bSize) {
        BOARDSIZE = bSize;
        gameGrid = new Cell[BOARDSIZE][BOARDSIZE];
        colorGrid = new Color[BOARDSIZE][BOARDSIZE];
        createBoard();
    }

    public void createBoard() {
        for (int row = 0; row < BOARDSIZE; row++){
            for (int column = 0; column < BOARDSIZE; column++){
                gameGrid[row][column] = Cell.EMPTY;
                colorGrid[row][column] = Color.BLACK;
            }
        }
        turn = "PLAYER1";
    }

    public Cell getCell(int row, int column) {
        if (row >= 0 && row < BOARDSIZE && column >= 0 && column < BOARDSIZE)
            return gameGrid[row][column];
        else
            return null;
    }

    public Color getColor(int row, int column) {
        if (row >= 0 && row < BOARDSIZE && column >= 0 && column < BOARDSIZE)
            return colorGrid[row][column];
        else
            return null;
    }

    public void setBoardSize(int number){
        BOARDSIZE = number;
    }

    public int getBoardSize() {return BOARDSIZE;}

    //public void setTurn(String str) {turn = str;}

    public String getTurn() {
        return turn;
    }

    public void setGameOver(boolean over) {gameOver = over;}

    public boolean getGameOver() {return gameOver;}

    public int getPlayer1SOSCount() {return player1SOSCount;}

    public int getPlayer2SOSCount() {return player2SOSCount;}

    public void setGamemodeSelection(String gamemode) {gamemodeSelected = gamemode;}

    public String getGamemodeSelected() {return gamemodeSelected;}

    public boolean validGameMode() {
        return gamemodeSelected.equals("SIMPLE") || gamemodeSelected.equals("GENERAL");
    }

    public boolean validBoardSize() {
        return BOARDSIZE >= 3;
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

    public int gameSSOS(int oRowCheck, int oColumnCheck, int sRow, int sColumn){
        if (gameGrid[oRowCheck][oColumnCheck] == Cell.O) {
            int RowIndex = oRowCheck - (sRow - oRowCheck);
            int ColumnIndex = oColumnCheck - (sColumn - oColumnCheck);
            if (RowIndex < 0 || ColumnIndex < 0)
                return 0;
            if (RowIndex > BOARDSIZE - 1 || ColumnIndex > BOARDSIZE - 1)
                return 0;
            if (gameGrid[oRowCheck - (sRow - oRowCheck)][oColumnCheck - ( sColumn- oColumnCheck)]
                    == Cell.S){
                int[] rowArray = {sRow, oRowCheck, oRowCheck - (sRow - oRowCheck)};
                int[] columnArray = {sColumn, oColumnCheck, oColumnCheck - ( sColumn- oColumnCheck)};

                for (int i = 0; i < rowArray.length; i++){
                    if (turn.equals("PLAYER1")){
                        if (colorGrid[rowArray[i]][columnArray[i]] == Color.BLUE ||
                                colorGrid[rowArray[i]][columnArray[i]] == Color.PURPLE){
                            colorGrid[rowArray[i]][columnArray[i]] = Color.PURPLE;
                        } else {
                            colorGrid[rowArray[i]][columnArray[i]] = Color.RED;
                        }
                    } else {
                        if (colorGrid[rowArray[i]][columnArray[i]] == Color.RED ||
                                colorGrid[rowArray[i]][columnArray[i]] == Color.PURPLE){
                            colorGrid[rowArray[i]][columnArray[i]] = Color.PURPLE;
                        } else {
                            colorGrid[rowArray[i]][columnArray[i]] = Color.BLUE;
                        }
                    }
                }
                return 1;
            }
        }
        return 0;
    }

    public int gameOSOS(int sRowCheck, int sColumnCheck, int oRow, int oColumn){
        if (gameGrid[sRowCheck][sColumnCheck] == Cell.S) {
            int tempRowIndex = oRow - (sRowCheck - oRow);
            int tempColumnIndex = oColumn - (sColumnCheck - oColumn);
            if (tempRowIndex < 0 || tempColumnIndex < 0)
                return 0;
            if (tempRowIndex > BOARDSIZE - 1 || tempColumnIndex > BOARDSIZE - 1)
                return 0;
            if (gameGrid[oRow - (sRowCheck - oRow)][oColumn - (sColumnCheck - oColumn)]
                    == Cell.S) {
                int[] rowArray = {sRowCheck, oRow, oRow - (sRowCheck - oRow)};
                int[] columnArray = {sColumnCheck, oColumn, oColumn - (sColumnCheck - oColumn)};

                for (int i = 0; i < rowArray.length; i++){
                    if (turn.equals("PLAYER1")){
                        if (colorGrid[rowArray[i]][columnArray[i]] == Color.BLUE ||
                                colorGrid[rowArray[i]][columnArray[i]] == Color.PURPLE){
                            colorGrid[rowArray[i]][columnArray[i]] = Color.PURPLE;
                        } else {
                            colorGrid[rowArray[i]][columnArray[i]] = Color.RED;
                        }
                    } else {
                        if (colorGrid[rowArray[i]][columnArray[i]] == Color.RED ||
                                colorGrid[rowArray[i]][columnArray[i]] == Color.PURPLE){
                            colorGrid[rowArray[i]][columnArray[i]] = Color.PURPLE;
                        } else {
                            colorGrid[rowArray[i]][columnArray[i]] = Color.BLUE;
                        }
                    }
                }
                return 1;
            }
        }
        return 0;
    }

    public int checkBoardMin(int value){
        if (value - 1 <= 0) {return value;}
        return (value - 1);
    }

    public int checkBoardMax(int value){
        if (value + 1 >= BOARDSIZE) {return value;}
        return (value + 1);
    }
    
    public int scanBoard (int row, int column, String selectedCell){
        int SOS = 0;
        for (int rowCheck = checkBoardMin(row); rowCheck <= checkBoardMax(row); rowCheck++) {
            for (int columnCheck = checkBoardMin(column); columnCheck <= checkBoardMax(column); columnCheck++) {
                if (selectedCell.equals("S")) {SOS = SOS + gameSSOS(rowCheck, columnCheck, row, column);}
                if (selectedCell.equals("O")) {SOS = SOS + gameOSOS(rowCheck, columnCheck, row, column);}
            }
        }
        return (SOS);
    }

    public String makeMove(int row, int column){
        if (turn.equals("PLAYER1")){
            if (player1Selection.equals("S") || player1Selection.equals("O")) {
                if (row >= 0 && row < BOARDSIZE && column >= 0 && column < BOARDSIZE && gameGrid[row][column] == Cell.EMPTY) {
                    gameGrid[row][column] = (player1Selection.equals("S")) ? Cell.S : Cell.O;
                    unsetSOS = scanBoard(row, column, gameGrid[row][column].name());
                    //turn = "PLAYER2";
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
                if (row >= 0 && row < BOARDSIZE && column >= 0 && column < BOARDSIZE && gameGrid[row][column] == Cell.EMPTY) {
                    gameGrid[row][column] = (player2Selection.equals("S")) ? Cell.S : Cell.O;
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

    public boolean checkFullBoard(){
        for (int row = 0; row < BOARDSIZE; row++) {
            for (int column = 0; column < BOARDSIZE; column++) {
                if (gameGrid[row][column] == Cell.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public String decideWin() {
        gameOver = true;
        if (player1SOSCount == player2SOSCount) {
            System.out.println("Tie Game");
            return "Tie Game";
        } else {
            if (player1SOSCount > player2SOSCount) {
                System.out.println("Player 1 Wins");
                return "Player 1 Wins";
            } else {
                System.out.println("Player 2 Wins");
                return "Player 2 Wins";
            }
        }
    }

    public String selectTurn() {
        boolean fullBoard = checkFullBoard();
        if (unsetSOS > 0){
            if (gamemodeSelected.equals("SIMPLE")){
                gameOver = true;
                if (turn.equals("PLAYER1")){
                    System.out.println("Player 1 Wins");
                    return "Player 1 Wins";
                } else {
                    System.out.println("Player 2 Wins");
                    return "Player 2 Wins";
                }
            } else {
                if (turn.equals("PLAYER1")){
                    player1SOSCount = player1SOSCount + unsetSOS;
                } else {
                    player2SOSCount = player2SOSCount + unsetSOS;
                }
                System.out.println(player1SOSCount + "  " + player2SOSCount);
                unsetSOS = 0;
            }
            if (fullBoard) {
                return decideWin();
            }
        } else {
            if (fullBoard) {
                return decideWin();
            }
            if (turn.equals("PLAYER1")){
                turn = "PLAYER2";
            } else {
                turn = "PLAYER1";
            }
        }
        return "NOSOS";
    }

}
