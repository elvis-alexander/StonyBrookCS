/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Page;

import static File.Constants.CAPTION_PROMPT;
import static File.Constants.CONTENT_PANE_HEIGHT;
import static File.Constants.CONTENT_PANE_WIDTH;
import static File.Constants.DEFAULT_IMAGE_CAPTION;
import static File.Constants.DEFAULT_IMAGE_HEIGHT;
import static File.Constants.DEFAULT_IMAGE_SRC;
import static File.Constants.DEFAULT_IMAGE_WIDTH;
import static File.Constants.HEIGHT_PROMPT;
import static File.Constants.IMAGE_HEIGHT_FX;
import static File.Constants.IMAGE_WIDTH_FX;
import static File.Constants.SLIDESHOW_CSS;
import static File.Constants.SLIDESHOW_SLIDE_IMAGE_ATTR_CSS;
import static File.Constants.WIDTH_PROMPT;
import java.io.File;
import java.net.URL;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author eafernandez
 */
public class Slide extends HBox {
    private String imageSrc;
    private String imageCaption;
    private String imageWidth;
    private String imageHeight;

    private Label imageCaptionLabel;
    private Label imageWidthLabel;
    private Label imageHeightLabel; 
    private TextField imageCaptionField;
    private TextField imageWidthField;
    private TextField imageHeightField; 
    private ImageView imageView;
    private GridPane  imageAttrPane;
    
    private String coolPath;
    
    
    public Slide() {
        imageSrc = DEFAULT_IMAGE_SRC;
        coolPath = "imgs//default_ss.png";
        this.imageCaption = DEFAULT_IMAGE_CAPTION;
        this.imageWidth = DEFAULT_IMAGE_WIDTH;
        this.imageHeight = DEFAULT_IMAGE_HEIGHT;
        createView();
    }

    public Slide(String imageSrc, String imageCaption, String imageWidth, String imageHeight, String cp) {
        this.imageSrc = imageSrc;
        this.imageCaption = imageCaption;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.coolPath = cp;
        createView();
    }
    
    public void createView() {
//        this.view = new HBox();
        this.imageAttrPane = new GridPane();
        this.imageView = new ImageView(new Image("file:" + imageSrc));
        this.imageView.setFitWidth(IMAGE_WIDTH_FX - 50);
        this.imageView.setFitHeight(IMAGE_HEIGHT_FX);
        handler();
        this.imageCaptionLabel = new Label(CAPTION_PROMPT);
        this.imageWidthLabel = new Label(WIDTH_PROMPT);
        this.imageHeightLabel = new Label(HEIGHT_PROMPT);
        this.imageCaptionField = new TextField();
        this.imageWidthField = new TextField();
        this.imageHeightField = new TextField();
        this.imageCaptionField.setText(imageCaption);
        this.imageWidthField.setText(imageWidth);
        this.imageHeightField.setText(imageHeight);

        this.imageAttrPane.add(imageCaptionLabel, 0, 0);
        this.imageAttrPane.add(imageCaptionField, 0, 1);
        this.imageAttrPane.add(imageWidthLabel, 0, 2);
        this.imageAttrPane.add(imageWidthField, 0, 3);
        this.imageAttrPane.add(imageHeightLabel, 1, 0);
        this.imageAttrPane.add(imageHeightField, 1, 1);
        
        super.setWidth(CONTENT_PANE_WIDTH - 750);
        super.setHeight(CONTENT_PANE_HEIGHT - 300);
        this.imageAttrPane.getStyleClass().add(SLIDESHOW_SLIDE_IMAGE_ATTR_CSS);
        super.getStyleClass().add(SLIDESHOW_CSS);
        super.getChildren().addAll(imageView, imageAttrPane);
    }
    
    private void handler() {
        imageView.setOnMousePressed(src -> {
            String initialDir = System.getProperty("user.home");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(initialDir));
            FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
            FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
            FileChooser.ExtensionFilter gifFilter = new FileChooser.ExtensionFilter("gif files (*.gif)", "*.gif");  
            FileChooser.ExtensionFilter PNGFilter = new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
            FileChooser.ExtensionFilter JPGFilter = new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
            FileChooser.ExtensionFilter GIFFilter = new FileChooser.ExtensionFilter("GIF files (*.GIF)", "*.GIF");  
            fileChooser.getExtensionFilters().addAll(jpgFilter,pngFilter,gifFilter, PNGFilter, JPGFilter, GIFFilter);
            File file = fileChooser.showOpenDialog(null);
            if(file != null) {
                setImageSrc(file.getAbsolutePath());
                coolPath = file.getName();
                String path = file.getPath().substring(0, file.getPath().indexOf(file.getName()));
                String fileName = file.getName();
                String imagePath = path + "/" + fileName;
                File imagefile = new File(imagePath);
                try {
                    URL fileURL = imagefile.toURI().toURL();
                    Image image = new Image(fileURL.toExternalForm());
                    imageView.setImage(image);
                    double scaledWidth = IMAGE_WIDTH_FX - 50;
                    double perc = scaledWidth / image.getWidth();
        	    double scaledHeight = image.getHeight() * perc;
                    imageView.setFitWidth(scaledWidth);
                    imageView.setFitHeight(scaledHeight);
                } catch(Exception e) { 
                    Alert alertDialog = new Alert(AlertType.WARNING, "Unkown Error");
                    alertDialog.showAndWait();
                }
            }
        });
    }
    
    
    public JsonObject makeJsonObject() {
        JsonObject jObject = Json.createObjectBuilder()
                .add("src", getImageSrc())
                .add("caption", imageCaptionField.getText())
                .add("width", imageWidthField.getText())
                .add("height", imageHeightField.getText())
                .add("coolpath", coolPath)
                .build();
        return jObject;
    }
    
    public String getImageSrc() {
        return imageSrc;
    }
    public String getImageCaption() {
        return imageCaption;
    }
    public String getImageWidth() {
        return imageWidth;
    }
    public String getImageHeight() {
        return imageHeight;
    }
    
    public void setImageSrc(String src) {
        this.imageSrc = src;
    }
    public void setImageCaption(String caption) {
        this.imageCaption = caption;
    }
    public void setImageWidth(String width) {
        this.imageWidth = width;
    }
    public void setImageHeight(String height) {
        this.imageHeight = height;
    }
}