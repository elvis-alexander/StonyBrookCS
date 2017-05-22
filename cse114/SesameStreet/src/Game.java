import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class Game {
    private Deck deck;
    private Player human;
    private Player computer;
    private Player currentPlayer;
    private boolean isHumansTurn = false;
    private Stack<Card> discardPile;
    private boolean run = true;
    private Color currentColor;

    public Game() throws Exception {
        init();
    }

    // construct deck and players
    private void init() throws Exception {
        deck = new Deck();
        human = new Player(false);
        computer = new Player(true);
        discardPile = new Stack<>();
        deck.shuffle();
        Card humanDrawingCard = deck.draw();
        Card computerDrawingCard = deck.draw();
        currentPlayer = computer;
        if(humanDrawingCard.getNumber() >= computerDrawingCard.getNumber()) {
            isHumansTurn = true;
            currentPlayer = human;
        }
        deck.shuffle();
    }

    // deals five cards to a computer and a human
    protected void dealFive() {
        for(int i = 0; i < 5; ++i) {
            human.addCard(deck.draw());
            computer.addCard(deck.draw());
        }
    }

    // Turn over the top card of the Draw Pile to make the Discard Pile.
    // If the card drawn is a Wild Card or a Draw 2 Card, continue drawing cards until you find a number card (1-7).
    protected void turnOver() {
        boolean foundRegularCard = false;
        while (!foundRegularCard) {
            Card card = deck.draw();
            if(!card.isSpecial()) {
                // push to discard
                discardPile.push(card);
                // set current color
                currentColor = card.getColor();
                // stop drawing cards
                foundRegularCard = true;
            }
        }
    }

    /**
     * current player draws a card
     */
    protected void drawCard() {
        // current player adds a card
        currentPlayer.addCard(deck.draw());
    }

    // draws one additional cards if player has needs it
    protected void addIfNecessary() {
        /*if (!currentPlayer.hasMatch(discardPile.peek())) {
            drawCard();
        }*/
    }

    private boolean calledUno() {
        List<String> choices = new ArrayList<>();
        choices.add("Yes");
        choices.add("No");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Yes", choices);
        dialog.setTitle("UNO");
        dialog.setHeaderText("Call Uno!");
        dialog.setContentText("Would you like to call UNO?");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            switch (result.get()) {
                case "Yes":
                    return true;
                case "No":
                    return false;
            }
        }
        // shouldn't occur...
        return false;
    }

    /**
     *
     * @param clickedCard
     * @return
     */
    protected boolean makeMove(Card clickedCard) {

        if(currentPlayer.getHand().size() == 2) {
            if(!calledUno()) {
                drawCard();
                drawCard();
            }
        }

        Card top = discardPile.peek();
        if(!Card.isMatch(clickedCard, top)) {
            return false;
        }
        if(clickedCard.isSpecial()) {
            switch (clickedCard.getCharacter()) {
                case ErnieAndBert:
                    drawUnconditional(1);
                    break;
                case OscarTheGrouch:
                    drawUnconditional(2);
                    break;
                case WildCard:
                    // allow user to set current color
                    currentColor = getCurrentColorFromUser();
                    clickedCard.setColor(currentColor);
                    break;
            }
        }
        discardPile.push(clickedCard);
        // dont set color on wildcard
        currentColor = clickedCard.getColor();
        currentPlayer.removeCard(clickedCard);
        if(!clickedCard.isSpecial() || clickedCard.isWildCard())
            swapTurns();
        return true;
    }

    /**
     * swap turns
     */
    protected void swapTurns() {
        if(currentPlayer == human)
            currentPlayer = computer;
        else
            currentPlayer = human;
    }

    /**
     * opposite player draws ntimes cards
     * @param ntimes
     */
    protected void drawUnconditional(int ntimes) {
        Player otherPlayer = human;
        if(currentPlayer == human)
            otherPlayer = computer;
        for(int i = 0; i < ntimes; ++i) {
            otherPlayer.addCard(deck.draw());
        }
    }

    /**
     *
     * @return
     */
    protected Color getCurrentColorFromUser() {
        List<String> choices = new ArrayList<>();
        choices.add("red");
        choices.add("blue");
        choices.add("yellow");
        choices.add("green");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("red", choices);
        dialog.setTitle("Wild Card");
        dialog.setHeaderText("Pick the new color");
        dialog.setContentText("Choose your color:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            System.out.println("Your choice: " + result.get());
            Color color = Color.RED;
            switch (result.get()) {
                case "blue":
                    color = Color.BLUE;
                    break;
                case "yellow":
                    color = Color.YELLOW;
                    break;
                case "green":
                    color = Color.GREEN;
                    break;
            }
            currentColor = color;
            return currentColor;
        }
        // shouldn't occur...
        return null;
    }

    /**
     *
     * @return
     */
    public Player getHuman() {
        return human;
    }

    /**
     *
     * @return
     */
    public Player getComputer() {
        return computer;
    }

    /**
     *
     * @return
     */
    protected Stack<Card> getDiscardPile() {
        return discardPile;
    }

    /**
     *
     * @return
     */
    protected Card getTopCardView() {
        return discardPile.peek();
    }

    /**
     *
     * @return
     */
    protected String logInfo() {
        return currentPlayer == human ? "Players turn" : "Computers Turn";
    }

    /**
     *
     * @return
     */
    protected boolean isPlayersTurn() {
        return currentPlayer == human ? true : false;
    }


    protected boolean isOver() {
        return computer.getHand().size() == 0 || human.getHand().size() == 0;
    }

    protected String getWinner() {
        return human.getHand().size() == 0 ? "Human" : "Computer";
    }
}