package main.sprint3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import main.sprint3.Board;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TieGameTest {

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
    public void tieSimpleGame() {
        board.setGamemodeSelection("SIMPLE");
        board.setPlayerSelection("S", "PLAYER1");
        board.setPlayerSelection("S", "PLAYER2");

        for (int row = 0; row < board.getBoardSize(); row++){
            for (int column = 0; column < board.getBoardSize(); column++){
                board.makeMove(row, column);
            }
        }

        assertEquals("Tie Game", board.decideWin());
    }

    @Test
    public void tieGeneralGame() {
        board.setGamemodeSelection("GENERAL");
        board.setPlayerSelection("S", "PLAYER1");
        board.setPlayerSelection("S", "PLAYER2");

        for (int row = 0; row < board.getBoardSize(); row++){
            for (int column = 0; column < board.getBoardSize(); column++){
                board.makeMove(row, column);
            }
        }

        assertEquals("Tie Game", board.decideWin());
    }
}