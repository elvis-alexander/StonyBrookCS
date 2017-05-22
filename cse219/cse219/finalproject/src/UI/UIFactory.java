/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import static File.Constants.ICON_IMG_PATH;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 *
 * @author eafernandez
 */
public class UIFactory {
    
    /* Default Icon */
    public static Image CREATE_ICON() {
        Image icon = new Image(ICON_IMG_PATH);
        return icon;
    }
    
    
    public static Button createButton(Pane toolbar, String message, String imagePath, String style, String toolTipMsg, boolean disabled, double width, double height) {
        /* Local Var's */
        Button newButton = new Button();
        Image imgFile = new Image(imagePath);
        ImageView btnImag = new ImageView(imgFile);
        Tooltip toolTip = new Tooltip(toolTipMsg);

        /* Change Button Attributes */
        if (width != -1) {
            newButton.setMinWidth(width);
        }
        if (height != -1) {
            newButton.setMinHeight(height);
        }
        if (!(style == null)) {
            newButton.getStyleClass().add(style);
        }
        if (!(message == null)) {
            newButton.setText(message);
        }
        newButton.setTooltip(toolTip);
        newButton.setGraphic(btnImag);
        newButton.setDisable(disabled);
        toolbar.getChildren().add(newButton);

        /* return btn */
        return newButton;
    }
}
