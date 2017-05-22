/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Page;

import static File.Constants.ADD_IMAGE_COMPONENT_PANE_CSS;
import static File.Constants.ADD_IMAGE_COMPONENT_PANE_LEFT_VBOX_CSS;
import static File.Constants.ADD_IMAGE_COMPONENT_PANE_RIGHT_VBOX_CSS;
import static File.Constants.CAPTION_PROMPT;
import static File.Constants.CONTENT_PANE_HEIGHT;
import static File.Constants.CONTENT_PANE_WIDTH;
import static File.Constants.DEFAULT_IMAGE_CAPTION;
import static File.Constants.DEFAULT_IMAGE_FLOATTYPE;
import static File.Constants.DEFAULT_IMAGE_HEIGHT;
import static File.Constants.DEFAULT_IMAGE_SRC;
import static File.Constants.DEFAULT_IMAGE_WIDTH;
import static File.Constants.FLOAT_PROMPT;
import static File.Constants.HEIGHT_PROMPT;
import static File.Constants.IMAGE_HEIGHT_FX;
import static File.Constants.IMAGE_WIDTH_FX;
import static File.Constants.REMOVE_BUTTON_HEIGHT;
import static File.Constants.REMOVE_BUTTON_WIDTH;
import static File.Constants.REMOVE_COMPONENT_CSS;
import static File.Constants.WIDTH_PROMPT;
import UI.PageEditor;
import java.io.File;
import java.net.URL;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author eafernandez
 */
public class ImageComponent extends Component {
    // Data
    private String imageSrc;
    private String imageCaption;
    private String imageWidth;
    private String imageHeight;
    private String floatType;
    
    // Label's (Instantiated)
    private final Label imageCaptionLabel = new Label(CAPTION_PROMPT);//DEFAULT_IMAGE_CAPTION);
    private final Label imageWidthLabel = new Label(WIDTH_PROMPT + "   ");
    private final Label imageHeightLabel = new Label(HEIGHT_PROMPT + "  ");
    private final Label floatTypeLabel = new Label(FLOAT_PROMPT);
    
    // Text Field's
    private final TextField imageCaptionField = new TextField();
    private final TextField imageWidthField = new TextField();
    private final TextField imageHeightField = new TextField();    
    
    // Toggle Btns
    private final ToggleGroup toggleGroup = new ToggleGroup();
    private final RadioButton left  = new RadioButton("left");
    private final RadioButton right = new RadioButton("right");
    private final RadioButton none  = new RadioButton("none");

    // Image 
    private StackPane imageStackPane = new StackPane();
    private ImageView imageView;
    
    // Button Remove self
    private Button removeButton;
    
    // HBox for each layout
    private HBox imageCaptionHBox = new HBox();
    private HBox imageWidthHBox = new HBox();
    private HBox imageHeightHBox = new HBox();    
    
    // VBox (for middle/right)
    private VBox leftVBox = new VBox();
    private VBox rightVBox = new VBox();
    
    // Main Layout    
    private HBox addImageComponentPane = new HBox();
    private Page page;
    private String coolPath;
    
    public ImageComponent(Page p) {
        this.page = p;
        coolPath = "imgs//default_ss.png";
        constructRemoveBtn();
        imageSrc = DEFAULT_IMAGE_SRC;
        imageCaption = DEFAULT_IMAGE_CAPTION;
        imageWidth = DEFAULT_IMAGE_WIDTH ;
        imageHeight = DEFAULT_IMAGE_HEIGHT;
        floatType = DEFAULT_IMAGE_FLOATTYPE;
    
        setBtnAttr();
        none.setSelected(true);
        handleRemove();
        defaultAttributes();
        
    }
    
    public ImageComponent(Page p, String src, String ic, String w, String h, String ft, String cp) {
        this.page = p;
        constructRemoveBtn();
        this.imageSrc = src;
        this.imageCaption = ic;
        this.imageWidth = w;
        this.imageHeight = h;
        this.floatType = ft;
        this.coolPath = cp;
        setBtnAttr();
        
        if(floatType.equals("left")) {
            left.setSelected(true);
        } else if(floatType.equals("right")) {
            right.setSelected(true);
        } else if(floatType.equals("none")) {
            none.setSelected(true);
        }
        handleRemove();
        defaultAttributes();
    }
    
    public void setBtnAttr() {
        left.setToggleGroup(toggleGroup);
        right.setToggleGroup(toggleGroup);
        none.setToggleGroup(toggleGroup);
        left.setUserData("left");
        right.setUserData("right");
        none.setUserData("none");
    }
    
    public void constructRemoveBtn() {
        removeButton = new Button("Remove");
        removeButton.setPrefSize(REMOVE_BUTTON_WIDTH, REMOVE_BUTTON_HEIGHT);
        removeButton.getStyleClass().add(REMOVE_COMPONENT_CSS);
    }
    
    @Override
    public HBox componentView() {
        // set up image stackpane
        imageView = new ImageView(new Image("file:" + imageSrc));
        initHandler();
        imageView.setFitWidth(IMAGE_WIDTH_FX);
        imageView.setFitHeight(IMAGE_HEIGHT_FX);
        imageStackPane.getChildren().add(imageView);
        
        // set up textfield's
        imageCaptionField.setText(imageCaption);
        imageWidthField.setText(imageWidth);
        imageHeightField.setText(imageHeight);
        
        // add to each HBox
        imageCaptionHBox.getChildren().addAll(imageCaptionLabel, imageCaptionField);
        imageWidthHBox.getChildren().addAll(imageWidthLabel, imageWidthField);
        imageHeightHBox.getChildren().addAll(imageHeightLabel, imageHeightField);
        
        // Add to left/rght VBox
        leftVBox.getChildren().addAll(imageCaptionHBox, imageWidthHBox, imageHeightHBox);
        rightVBox.getChildren().addAll(floatTypeLabel, left, right, none);
        
        StackPane removeBtnPane = new StackPane(removeButton);
        addImageComponentPane.getChildren().addAll(imageStackPane, leftVBox, rightVBox, removeBtnPane);
        setPaneHandler();
        return addImageComponentPane;
    }
    
    public void setPaneHandler() {
        addImageComponentPane.setOnMouseClicked(e -> {
        });
    }
    
    public void handleRemove() {
        removeButton.setOnAction(e ->{
            page.getComponents().remove(this);
            page.getComponentView().getChildren().remove(addImageComponentPane);
        });
    }
    
    private void defaultAttributes() {
        right.setPadding(new Insets(0,0,0,10));
        left.setPadding(new Insets(0,0,0,10));
        imageStackPane.setPadding(new Insets(0,0,0,30));
        addImageComponentPane.getStyleClass().add(ADD_IMAGE_COMPONENT_PANE_CSS);
        leftVBox.getStyleClass().add(ADD_IMAGE_COMPONENT_PANE_LEFT_VBOX_CSS);
        rightVBox.getStyleClass().add(ADD_IMAGE_COMPONENT_PANE_RIGHT_VBOX_CSS);
        addImageComponentPane.setPrefSize(CONTENT_PANE_WIDTH, CONTENT_PANE_HEIGHT);
    }
        
    public void initHandler() {
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
                coolPath = "imgs//" + file.getName();
                String path = file.getAbsolutePath().substring(0, file.getAbsolutePath().indexOf(file.getName()));
                String fileName = file.getName();
                String imagePath = path + "/" + fileName;
                File imagefile = new File(imagePath);
                try {
                    URL fileURL = imagefile.toURI().toURL();
                    Image image = new Image(fileURL.toExternalForm());
                    imageView.setImage(image);
                    double scaledWidth = IMAGE_WIDTH_FX;
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
    
    // Setter's for setting image detail's / used for saving
    
    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }
    
    public void setImageCaption(String imageCaption) {
        this.imageCaption = imageCaption;
    }

    public void setImageWidth(String width) {
        this.imageWidth = width;
    }
    
    public void setImageHeight(String height) {
        this.imageHeight = height;
    }

    public void setFloatType(String floatType) {
        this.floatType = floatType;
    }
   
    public String getImageSource() {
        return imageSrc;
    }

    // Getter's used for pulling Data
    public TextField getImageCaptionField() {
        return imageCaptionField;
    }

    public TextField getImageWidthField() {
        return imageWidthField;
    }

    public TextField getImageHeightField() {
        return imageHeightField;
    }

    public ToggleGroup getToggleGroup() {
        return toggleGroup;
    }
    
    @Override
    public JsonObject makeJsonObject() {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("type", getType())
                .add("src", getImageSource())
                .add("caption", imageCaptionField.getText())
                .add("width", imageWidthField.getText())
                .add("height", imageHeightField.getText())
                .add("float", getFontStr())
                .add("coolpath", coolPath)
                .build();
        return jsonObject;
    }
    
    @Override
    public String getType() {
        return "image";
    }
    
    public String getFontStr() {
        return toggleGroup.getSelectedToggle().getUserData().toString();
    }
    
    
}
