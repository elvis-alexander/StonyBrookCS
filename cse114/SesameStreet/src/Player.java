import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Card> hand;
    private boolean isComputer;

    /**
     *
     * @param isComputer
     */
    public Player(boolean isComputer) {
        this.isComputer = isComputer;
        this.hand = new ArrayList<>();
    }

    /**
     *
     * @return
     */
    public List<Card> getHand() {
        return hand;
    }

    /**
     *
     * @param hand
     */
    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    /**
     *
     * @return
     */
    public boolean isComputer() {
        return isComputer;
    }

    /**
     *
     * @param computer
     */
    public void setComputer(boolean computer) {
        isComputer = computer;
    }

    /**
     *
     * @param card
     */
    public void addCard(Card card) {
        this.hand.add(card);
    }

    public boolean hasMatch(Card topOfDiscard) {
        for(Card card : hand) {
            if(card.isWildCard())
                return true;
            if (card.getCharacter() == topOfDiscard.getCharacter() || card.getColor() == topOfDiscard.getColor())
                return true;
            else if(!card.isSpecial() && !topOfDiscard.isSpecial())
                if(card.getNumber() == topOfDiscard.getNumber())
                    return true;
        }
        return false;
    }

        public void removeCard(Card card) {
        this.hand.remove(card);
    }

    public boolean uno() {
        return hand.size() == 0;
    }

    @Override
    public String toString() {
        return "Player{" +
                "hand=" + hand +
                ", isComputer=" + isComputer +
                '}';
    }
}
