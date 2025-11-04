package main.sprint3;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import java.io.File;

public class GUI extends Application {
    private Box[][] boxes;

    private Label gameStatus = new Label("Player 1's Turn");

    static private Board board;

    private Text player1SOSCount = new Text();

    private Text player2SOSCount = new Text();

    private String message = "";

    private  Text messageText = new Text();



    private ImageView[] imageStack = {};

    @Override
    public void start(Stage primaryStage){
        BuildGUI GUI = new BuildGUI();

        primaryStage.setTitle("SOS");
        primaryStage.setScene(GUI.getPrimaryScene());
        primaryStage.show();
    }

    public void drawBoard() {
        for (int row = 0; row < board.getBoardSize(); row++){
            for (int column = 0; column < board.getBoardSize(); column++) {
                boxes[row][column].getChildren().clear();
                if (board.getCell(row, column) == Board.Cell.S)
                    boxes[row][column].drawS(row, column);
                else if (board.getCell(row, column) == Board.Cell.O)
                    boxes[row][column].drawO();
            }
        }
    }

    public void clearErrorMessage() {
        message = "";
        messageText.setText(message);
    }

    public void setMessage(String str) {
        message = str;
        messageText.setText(message);
    }

    public class Box extends Pane{
        private final int row, column;

        public Box(int row, int column) {
            this.row = row;
            this.column = column;
            setStyle("-fx-border-color: white");
            this.setPrefSize(500, 500);
            this.setOnMouseClicked(e -> handleMouseClick());
        }

        private void handleMouseClick() {
            if (!board.getGameOver()) {
                message = board.makeMove(row, column);
                if (message.equals("FAIL")) {
                    messageText.setText("Select S or O");
                } else if (message.equals("INVALID")) {
                    messageText.setText("Select a valid box");
                } else {
                    clearErrorMessage();
                    message = board.selectTurn();
                    if (!message.equals("NOSOS")) {
                        setMessage(message);
                    }
                }
                if (board.getTurn().equals("PLAYER1")){
                    player1SOSCount.setText(String.valueOf(board.getPlayer1SOSCount()));
                } else {
                    player2SOSCount.setText(String.valueOf(board.getPlayer2SOSCount()));
                }
                drawBoard();
                displayGameStatus();
            }
        }

        public StackPane imageSelect(String playerSelection, String color) {
            File file = new File("./src/assets/" + playerSelection.toLowerCase() + color + ".png");
            Image imageURL = new Image(file.toURI().toString());
            ImageView image = new ImageView(imageURL);

            ImageView[] temp = new ImageView[imageStack.length + 1];
            System.arraycopy(imageStack, 0, temp, 0, imageStack.length);
            temp[imageStack.length] = image;
            imageStack = temp;

            if (playerSelection.equals("S")){
                image.fitWidthProperty().bind(this.widthProperty().subtract(15));
                image.fitHeightProperty().bind(this.heightProperty().subtract(15));
            } else{
                image.fitWidthProperty().bind(this.widthProperty().subtract(17));
                image.fitHeightProperty().bind(this.heightProperty().subtract(17));
            }
            return (new StackPane(image));
        }

        public void drawS(int row, int column) {
            StackPane sBox = imageSelect("S", board.getColor(row, column).toString());

            sBox.prefWidthProperty().bind(this.widthProperty());
            sBox.prefHeightProperty().bind(this.heightProperty());
            sBox.setAlignment(Pos.CENTER);

            getChildren().add(sBox);
        }

        public void drawO() {
            StackPane oBox = imageSelect("O", board.getColor(row, column).toString());

            oBox.prefWidthProperty().bind(this.widthProperty());
            oBox.prefHeightProperty().bind(this.heightProperty());
            oBox.setAlignment(Pos.CENTER);

            getChildren().add(oBox);
        }

        private void displayGameStatus() {
            if (board.getTurn().equals("PLAYER1")){
                gameStatus.setText("Player 1's Turn");
            }
            else{
                gameStatus.setText("Player 2's Turn");
            }
        }
    }

    public class BuildGUI extends Stage {
        private final Scene primaryScene;
        private GridPane bodyGrid;
        private BorderPane borderPane;
        private int boardSize = 3;
        private boolean started = false;

        public Scene getPrimaryScene() {
            return primaryScene;
        }

        public BuildGUI() {
            if (board == null) { board = new Board(boardSize); }

            bodyGrid = new GridPane();
            bodyGrid.setHgap(20);

            bodyGrid.add(new Text("SOS"), 0,0);

            HBox gameSelect = CreateGameSelect();
            bodyGrid.add(gameSelect, 1,0);

            HBox boardSize = CreateBoardSizeSelect();
            bodyGrid.add(boardSize, 2,0);

            HBox errorTextHBox = new HBox();
            errorTextHBox.setAlignment(Pos.CENTER);
            errorTextHBox.getChildren().add(messageText);
            bodyGrid.add(errorTextHBox, 1, 2);


            HBox startGameButton = CreateStartButton();
            bodyGrid.add(startGameButton, 1, 3);

            borderPane = new BorderPane();
            borderPane.setCenter(bodyGrid);

            primaryScene = new Scene(borderPane, 600, 600);
        }

        public boolean radioSelection(RadioButton currentBtn, RadioButton secondaryBtn, String selection){
            if (currentBtn.isSelected()) {
                if (secondaryBtn.isSelected())
                    secondaryBtn.setSelected(false);
                System.out.println(selection + " is selected");
                return true;
            }
            return false;
        }

        public void selectedGameMode (RadioButton simpleBtn, RadioButton generalBtn) {
            if (board.getGamemodeSelected().equals("SIMPLE")) {
                simpleBtn.setSelected(true);
                generalBtn.setSelected(false);
            }
            if (board.getGamemodeSelected().equals("GENERAL")) {
                simpleBtn.setSelected(false);
                generalBtn.setSelected(true);
            }
        }


        public VBox CreatePlayerSOSelect(String player){
            RadioButton sSelect = new RadioButton();
            RadioButton oSelect = new RadioButton();
            sSelect.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if ( radioSelection(sSelect, oSelect, "S") ){
                        board.setPlayerSelection("S", player);
                    }
                    else{
                        board.setPlayerSelection("", player);
                    }
                }
            });
            oSelect.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if ( radioSelection( oSelect, sSelect, "O") ){
                        board.setPlayerSelection("O", player);
                    }
                    else {
                        board.setPlayerSelection("", player);
                    }
                }
            });

            HBox sSelectHBox = new HBox(new Text("S  "), sSelect);
            sSelectHBox.setMinWidth(100);
            sSelectHBox.setAlignment(Pos.CENTER);
            HBox oSelectHBox = new HBox(new Text("O  "), oSelect);
            oSelectHBox.setMinWidth(100);
            oSelectHBox.setAlignment(Pos.CENTER);

            Text text;

            if (player.equals("PLAYER1")) {text = new Text("Player 1: \n  (RED) ");}
            else {text = new Text("Player 2: \n  (BLUE) ");}

            VBox sOSelect = new VBox();
            sOSelect.setAlignment(Pos.CENTER);
            sOSelect.setSpacing(20);
            sOSelect.getChildren().addAll(text,
                    sSelectHBox, oSelectHBox);


            return (sOSelect);
        }

        public VBox createPlayerMenu(String player) {
            VBox  playerMenu = new VBox();
            if (player.equals("PLAYER1")){
                playerMenu.getChildren().addAll(CreatePlayerSOSelect(player), player1SOSCount);
            } else {
                playerMenu.getChildren().addAll(CreatePlayerSOSelect(player), player2SOSCount);
            }

            playerMenu.setAlignment(Pos.CENTER);
            playerMenu.setSpacing(20);

            return playerMenu;
        }

        public HBox CreateGameSelect (){
            RadioButton simpleGameButton = new RadioButton();
            RadioButton generalGameButton = new RadioButton();
            simpleGameButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (!started){
                        if (radioSelection(simpleGameButton, generalGameButton, "Simple")){
                            board.setGamemodeSelection("SIMPLE");
                        }
                        else {board.setGamemodeSelection("");}
                    } else {
                        selectedGameMode(simpleGameButton, generalGameButton);
                    }
                }
            });
            generalGameButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (!started){
                        if (radioSelection(generalGameButton, simpleGameButton, "General")){
                            board.setGamemodeSelection("GENERAL");
                        }
                        else {board.setGamemodeSelection("");}
                    } else {
                        selectedGameMode(simpleGameButton, generalGameButton);
                    }
                }
            });

            HBox buttonBoxHBox = new HBox();
            buttonBoxHBox.setMinHeight(50);
            buttonBoxHBox.setAlignment(Pos.CENTER);
            buttonBoxHBox.setSpacing(10);
            buttonBoxHBox.getChildren().addAll(new Text("Simple Game"), simpleGameButton,
                    new Text("General Game"), generalGameButton);

            return buttonBoxHBox;
        }

        public HBox CreateBoardSizeSelect(){

            HBox boardSizeHBox = new HBox();
            TextField boardSizeField = new TextField("3");

            boardSizeHBox.setMinWidth(5);
            boardSizeHBox.setMaxWidth(100);
            boardSizeHBox.setSpacing(10);
            boardSizeHBox.setPadding(new Insets(0, 5, 0, 0));
            boardSizeHBox.setAlignment(Pos.CENTER);

            boardSizeHBox.getChildren().addAll(new Text("Board Size"), boardSizeField);

            boardSizeField.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (!started){
                        if (!boardSizeField.getText().isEmpty()){
                            try {
                                boardSize = Integer.parseInt(boardSizeField.getText());
                                board.setBoardSize(boardSize);
                                if (board.validBoardSize()){
                                    System.out.println(boardSize);
                                    clearErrorMessage();
                                }
                                else {
                                    boardSize = 0;
                                    setMessage("Enter a number greater than 2");
                                }
                            } catch (NumberFormatException nfe){
                                System.out.println("Please enter a whole number");
                                boardSize = 0;
                                setMessage("Enter whole number");
                            }
                        }
                        else{ boardSize = 0; }
                    }
                    else {
                        boardSizeField.setText(String.valueOf(boardSize));
                        boardSizeField.setEditable(false);
                        boardSizeField.setDisable(true);
                        boardSizeField.setMouseTransparent(true);
                        boardSizeField.setFocusTraversable(false);
                    }
                }
            });

            return boardSizeHBox;
        }

        public GridPane GameBoard() {

            GridPane gameBoardGid = new GridPane();
            boxes = new Box[board.getBoardSize()][board.getBoardSize()];
            for (int i = 0; i < board.getBoardSize(); i++)
                for (int j = 0; j < board.getBoardSize(); j++)
                    gameBoardGid.add(boxes[i][j] = new Box(i, j), j, i);
            drawBoard();

            return gameBoardGid;
        }

        public HBox CreateStartButton() {
            Button startGameButton = new Button("Start Game");
            HBox startGameHBox = new HBox();
            startGameHBox.setMinHeight(50);
            startGameHBox.setAlignment(Pos.CENTER);
            startGameHBox.getChildren().add(startGameButton);

            startGameButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (board.getGameOver()){
                        String validGame = board.validGame();

                        if (validGame.equals("PASS")) {
                            int size = board.getBoardSize();
                            String gameMode = board.getGamemodeSelected();
                            board = null;
                            board = new Board(size);
                            board.setBoardSize(size);
                            board.setGamemodeSelection(gameMode);
                            board.setGameOver(false);

                            gameStart();
                        } else {
                            if (validGame.equals("BOARDSIZEERROR")) {
                                System.out.println("Enter a board size greater than or equal to 3");
                                setMessage("Enter Valid Board Size");
                            }
                            if (validGame.equals("GAMEMODEERROR")) {
                                System.out.println("Select a Simple or General Game");
                                setMessage("Select Simple or General Game");
                            }
                        }
                    }
                }
            });

            return startGameHBox;
        }

        public void gameStart() {
            if (!message.isEmpty()) {
                clearErrorMessage();
            }

            board.setGameOver(false);

            bodyGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 1);

            player1SOSCount.setText("0");
            player2SOSCount.setText("0");

            VBox player1Menu = createPlayerMenu("PLAYER1");
            bodyGrid.add(player1Menu, 0, 1);

            GridPane gameBoardGid = this.GameBoard();
            bodyGrid.add(gameBoardGid, 1, 1);

            VBox player2Menu = createPlayerMenu("PLAYER2");
            bodyGrid.add(player2Menu, 2, 1);

            borderPane.setBottom(gameStatus);
            if (imageStack.length != 0){
                System.out.println(imageStack.length);
                for (int i = 0; i < imageStack.length; i++){
                    imageStack[i].setImage(null);
                }
            }
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}