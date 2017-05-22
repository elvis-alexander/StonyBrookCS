import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Driver extends Application {


    public static void main(String[] args) {
        launch(args);
        /*initGame();
        dealFive();
        turnOver();
        while (run) {
            drawCard();
            if(discardPile.peek().getCharacter() != Character.WildCard)
                swapTurns();
        }*/
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // left pane deck and
        GUI gui = new GUI();
        primaryStage.setTitle("Sesame Street UNO");
        // set size
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        primaryStage.setScene(gui.getScene());
        primaryStage.show();
    }
}






















