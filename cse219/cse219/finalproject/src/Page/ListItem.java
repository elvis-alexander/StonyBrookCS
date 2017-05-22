/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Page;

import static File.Constants.CONTENT_PANE_HEIGHT;
import static File.Constants.CONTENT_PANE_WIDTH;
import static File.Constants.SLIDESHOW_CSS;
import static File.Constants.SLIDESHOW_SLIDE_IMAGE_ATTR_CSS;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author eafernandez
 */
public class ListItem extends HBox {
    private String imageCaption;
    private Label imageCaptionLabel;
    private TextField imageCaptionField;
    private GridPane  imageAttrPane;
    
    public ListItem() {
        this.imageCaption = "New";
        createView();
    }

    public ListItem(String imageCaption) {
        this.imageCaption = imageCaption;
        createView();
    }
    
    public void createView() {
        this.imageAttrPane = new GridPane();
        this.imageCaptionLabel = new Label("Item:");
        this.imageCaptionField = new TextField();
        this.imageCaptionField.setText(imageCaption);

        this.imageAttrPane.add(imageCaptionLabel, 0, 0);
        this.imageAttrPane.add(imageCaptionField, 0, 1);
        
        super.setWidth(CONTENT_PANE_WIDTH - 750);
        super.setHeight(CONTENT_PANE_HEIGHT - 300);
        this.imageAttrPane.getStyleClass().add(SLIDESHOW_SLIDE_IMAGE_ATTR_CSS);
        super.getStyleClass().add(SLIDESHOW_CSS);
        super.getChildren().addAll(imageAttrPane);
    }
    
    
    public JsonObject makeJsonObject() {
        JsonObject jObject = Json.createObjectBuilder()
                .add("caption", imageCaptionField.getText())
                .build();
        return jObject;
    }
    
    public String getImageCaption() {
        return imageCaption;
    }
}