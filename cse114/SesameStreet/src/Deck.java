import java.io.*;
import java.util.*;

public class Deck {
    private Stack<Card> cardList;
    private Random randomizer;

    public Deck() throws Exception {
        this.cardList = new Stack<>();
        initCards();
    }

    private void initCards() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("initcards.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                String character = tokens[0];
                String color = tokens[1];
                String number = tokens[2];
                String imgPath = tokens[3];
                Card card = new Card(Color.valueOf(color), Integer.parseInt(number), Character.valueOf(character), imgPath);
                cardList.push(card);
            }
        }
    }

    public Card draw() {
        return cardList.pop();
    }

    public void shuffle() {
        Collections.shuffle(cardList);
    }

    @Override
    public String toString() {
        return "Deck{" +
                "cardList=" + cardList +
                '}';
    }
}
