import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class GUI {
    private VBox leftPane;
    private VBox rightPane;
    private VBox infoPane;
    private VBox errorPane;
    private HBox mainPane;
    private Scene scene = null;
    // left pane stuff
    private Label deckLabel = new Label("Deck");
    private Label discardLabel = new Label("Discard Pile");
    private Label infoLabel = new Label("Info Log: None");
    private Label errorLabel = new Label("Error Log: None");
    private ImageView backCard = new ImageView(new Image("file:scans/back.jpg"));
    private ImageView discardCard = new ImageView(new Image("file:scans/back.jpg"));
    // right pane stuff
    private Label playerLabel = new Label("Player");
    private Label computerLabel = new Label("Computer");
    // settings
    private static final int size = 100;
    private Game game;
    private Card discardView;


    public GUI() throws Exception {
        // init
        mainPane = new HBox(60);
        game = new Game();
        game.dealFive();
        game.turnOver();
        constructSettings();
        initUI();
    }

    private void constructSettings() {
        leftPane = new VBox(40);
        rightPane = new VBox(20);
        infoPane = new VBox(20);
        errorPane = new VBox(20);
        // left pane
        deckLabel.setFont(new Font(20));
        discardLabel.setFont(new Font(20));
        infoLabel.setFont(new Font(20));
        errorLabel.setFont(new Font(20));
        backCard.setFitWidth(size);
        backCard.setFitHeight(size);
        discardCard.setFitWidth(size);
        discardCard.setFitHeight(size);
        // right pane stuff
        playerLabel.setFont(new Font(20));
        computerLabel.setFont(new Font(20));
    }

    private void addLeftPane() {
        // add handler to deck
        backCard.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                game.drawCard();
                initUI();
            }
        });

        // left pane
        discardView = game.getTopCardView();
        leftPane.getChildren().clear();
        leftPane.getChildren().add(deckLabel);
        leftPane.getChildren().add(backCard);
        leftPane.getChildren().add(discardLabel);
        leftPane.getChildren().add(discardView);
        System.out.println(discardView);
    }

    private void addHandler(Card card) {
        card.setOnMouseClicked(e -> {
            Card clickedCard = (Card)e.getSource();
            boolean validMove = game.makeMove(clickedCard);
            if(!validMove) {
                errorLabel.setText("No match or invalid color/character/number");
                errorPane.getChildren().clear();
                errorPane.getChildren().add(errorLabel);
                //
            } else {
                System.out.println("valid move!");
                initUI();
            }
        });
    }

    private void addRightPane() {
        // right pane
        // add human cards
        rightPane.getChildren().clear();
        HBox humanView = new HBox();
        for(Card card: game.getHuman().getHand()) {
            if(!game.isPlayersTurn())
                humanView.getChildren().add(new Card(Color.NONE, 0, Character.NONE, "back.jpg"));
            else {
                addHandler(card);
                humanView.getChildren().add(card);
            }

        }
        // add computer cards
        HBox computerView = new HBox();
        for(Card card: game.getComputer().getHand()) {
            if(game.isPlayersTurn()) {
                computerView.getChildren().add(new Card(Color.NONE, 0, Character.NONE, "back.jpg"));
            } else {
                addHandler(card);
                computerView.getChildren().add(card);
            }

        }
        // add human and computer view
        rightPane.getChildren().add(playerLabel);
        rightPane.getChildren().add(humanView);
        rightPane.getChildren().add(computerLabel);
        rightPane.getChildren().add(computerView);
    }

    private void addGameInfo() {
        infoPane.getChildren().clear();
        infoLabel.setText("Info Log: " + game.logInfo());
        infoPane.getChildren().add(infoLabel);
        //
        errorPane.getChildren().clear();
        errorPane.getChildren().add(errorLabel);
    }

    private void initUI() {
        if(game.isOver()) {
            mainPane.getChildren().clear();
            mainPane.getChildren().add(new Label("Congratulations " + game.getWinner() + " has won"));
            return;
        }



        addLeftPane();
        addRightPane();
        addGameInfo();
        // end button handler
        Button endTurnBtn = new Button("End Turn");
        endTurnBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                game.swapTurns();
                initUI();
            }
        });
        // set main pane
        mainPane.getChildren().clear();
        mainPane.getChildren().add(leftPane);
        mainPane.getChildren().add(rightPane);
        mainPane.getChildren().add(infoPane);
        mainPane.getChildren().add(errorPane);
        mainPane.getChildren().add(endTurnBtn);

        if(scene == null) {
            scene = new Scene(mainPane);
        }
    }

    public Scene getScene() {
        return scene;
    }
}
