import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Card extends ImageView {
    private Color color;
    private int number;
    private Character character;
    /*private ImageView imageView;*/
    private static final int size = 100;


    public Card(Color color, int number, Character character, String imgPath) {
        this.color = color;
        this.number = number;
        this.character = character;
        /*this.imageView = new ImageView(new Image("file:scans/" + imgPath));
        this.imageView.setFitHeight(size);
        this.imageView.setFitWidth(size);*/
        setImage(new Image("file:scans/" + imgPath));
        setFitHeight(size);
        setFitWidth(size);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public boolean isWildCard() { return character == Character.WildCard; }

    /*public ImageView getImageView() { return imageView; }

    public void setImageView(ImageView imageView) { this.imageView = imageView; }*/

    public boolean isSpecial() {
        return character == Character.ErnieAndBert ||
                character == Character.OscarTheGrouch || character == Character.WildCard;
    }

    public static boolean isMatch(Card c1, Card c2) {
        if(c1.isWildCard())
            return true;
        if (c1.getCharacter() == c2.getCharacter() || c1.getColor() == c2.getColor())
            return true;
        else if(!c1.isSpecial() && !c2.isSpecial())
            return c1.getNumber() == c2.getNumber();
        else
            return false;
    }

    @Override
    public String toString() {
        return "Card{" +
                "color=" + color +
                ", number=" + number +
                ", character=" + character +
                '}';
    }
}
