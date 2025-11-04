package main.sprint3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SOSCheckTest {

    private Board board;

    @BeforeEach
    public void setUp() throws Exception{
        board = new Board(3);
    }

    @AfterEach
    public void tearDown() throws Exception{
        board = null;
    }

    @Test
    public void player1SOSSimpleGame() {
        board.setGamemodeSelection("SIMPLE");
        board.setPlayerSelection("S", "PLAYER1");
        board.setPlayerSelection("O", "PLAYER2");

        board.makeMove(0,0);
        board.selectTurn();
        board.makeMove(1,1);
        board.selectTurn();
        board.makeMove(2,2);

        assertEquals("Player 1 Wins", board.selectTurn());
    }

    @Test
    public void player2SOSSimpleGame() {
        board.setGamemodeSelection("SIMPLE");
        board.setPlayerSelection("O", "PLAYER1");
        board.setPlayerSelection("S", "PLAYER2");

        board.makeMove(1,1);
        board.selectTurn();
        board.makeMove(0,0);
        board.selectTurn();
        board.makeMove(0,2);
        board.selectTurn();
        board.makeMove(2,2);


        assertEquals("Player 2 Wins", board.selectTurn());
    }

    @Test
    public void player1SOSGeneralGame() {
        board.setGamemodeSelection("GENERAL");
        board.setPlayerSelection("S", "PLAYER1");
        board.setPlayerSelection("O", "PLAYER2");

        board.makeMove(0,0);
        board.selectTurn();
        board.makeMove(1,1);
        board.selectTurn();
        board.makeMove(2,2);
        board.selectTurn();

        board.setPlayerSelection("O", "PLAYER1");

        for (int row = 0; row < board.getBoardSize(); row++){
            for (int column = 0; column < board.getBoardSize(); column++){
                board.makeMove(row, column);
            }
        }


        assertEquals("Player 1 Wins", board.decideWin());
    }

    @Test
    public void player2SOSGeneralGame() {
        board.setGamemodeSelection("GENERAL");
        board.setPlayerSelection("O", "PLAYER1");
        board.setPlayerSelection("S", "PLAYER2");

        board.makeMove(1,1);
        board.selectTurn();
        board.makeMove(0,0);
        board.selectTurn();
        board.makeMove(0,2);
        board.selectTurn();
        board.makeMove(2,2);
        board.selectTurn();

        board.setPlayerSelection("O", "PLAYER2");

        for (int row = 0; row < board.getBoardSize(); row++){
            for (int column = 0; column < board.getBoardSize(); column++){
                board.makeMove(row, column);
            }
        }


        assertEquals("Player 2 Wins", board.decideWin());
    }

    @Test
    public void NoSOS() {
        board.setPlayerSelection("S", "PLAYER1");
        board.setPlayerSelection("O", "PLAYER2");

        board.makeMove(0,0);
        board.selectTurn();
        board.makeMove(1,1);
        board.selectTurn();
        board.makeMove(2,1);

        assertEquals("NOSOS", board.selectTurn());
    }
}