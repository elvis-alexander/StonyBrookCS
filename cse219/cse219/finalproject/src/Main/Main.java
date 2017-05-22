/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import static File.Constants.GOOGLE_FONT;
import static File.Constants.UI_STYLESHEET;
import static File.Constants.UI_TITLE;
import UI.UI;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author eafernandez
 */
public class Main extends Application {
    
    
    @Override
    public void start(Stage primaryStage) {
        try {
            /* Initialize Program */
            UI root = new UI(primaryStage);
            Scene mainScene = new Scene(root.getRootPane());
            mainScene.getStylesheets().add(UI_STYLESHEET);
            mainScene.getStylesheets().add(GOOGLE_FONT);
            primaryStage.setTitle(UI_TITLE);
            primaryStage.setScene(mainScene);
//            primaryStage.getIcons().add(CREATE_ICON());
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
            primaryStage.setWidth(bounds.getWidth());
            primaryStage.setHeight(bounds.getHeight());
            primaryStage.show();
            
        } catch(Exception e) { 
            e.printStackTrace(); 
        }
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}
