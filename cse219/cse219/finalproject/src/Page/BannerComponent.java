/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Page;

import static File.Constants.BANNER_PANE_CSS;
import static File.Constants.CONTENT_PANE_HEIGHT;
import static File.Constants.CONTENT_PANE_WIDTH;
import static File.Constants.DEFAULT_BANNER_SRC;
import static File.Constants.DEFAULT_IMAGE_SRC;
import static File.Constants.IMAGE_HEIGHT_FX;
import static File.Constants.IMAGE_WIDTH_FX;
import static File.Constants.PAGE_EDITOR_PANE_LIST_CSS;
import java.io.File;
import java.net.URL;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author eafernandez
 */
public class BannerComponent extends DefaultComponent {

    // Data
    private String bannerSrc;
    
    // Label's (Instantiated)
    private final Label bannerLabel = new Label("Banner: ");
    
    // Image 
    private StackPane imageStackPane = new StackPane();
    private ImageView imageView;
    
    // VBox (for middle/right)
    private VBox imageLabelPane = new VBox();
    
    // Main Layout    
    private HBox bannerPane = new HBox();
    
    private ToggleGroup toggleGroup = new ToggleGroup();
    private RadioButton yes = new RadioButton("yes");
    private RadioButton no = new RadioButton("no");
    private Label includeBannerLabel = new Label("Include Banner?");
    
    private String includeBannerString;
    private String coolPath;
    
    public BannerComponent() {
        bannerSrc = DEFAULT_BANNER_SRC;
        coolPath = "imgs//banner_def.jpg";
        includeBannerString = "yes";
        yes.setSelected(true);
        constructRadioBtn();
        defaultAttributes();
    }
    
    public BannerComponent(String bannerSrc,String ibs, String cp) {
        this.bannerSrc = bannerSrc;
        this.includeBannerString  = ibs;
        this.coolPath = cp;

        if(ibs.equals("yes")) {
            yes.setSelected(true);
        } else if(ibs.equals("no")) {
            no.setSelected(true);
        }
        constructRadioBtn();
        defaultAttributes();
    }
    
    public void constructRadioBtn() {
        yes.setUserData("yes");
        no.setUserData("no");
        yes.setToggleGroup(toggleGroup);
        no.setToggleGroup(toggleGroup);
    }
    
    @Override
    public Pane defaultComponentView() {
        bannerPane.getStyleClass().add(PAGE_EDITOR_PANE_LIST_CSS);
        imageView = new ImageView(new Image("file:" + bannerSrc));
        initHandler();
        imageView.setFitWidth(IMAGE_WIDTH_FX);
        imageView.setFitHeight(IMAGE_HEIGHT_FX);
        imageStackPane.getChildren().add(imageView);
        
        VBox rbPane = new VBox(12);
        Label label = new Label("Include Banner?");
        rbPane.getChildren().addAll(label, yes, no);
        rbPane.setStyle("-fx-font-size: 20px");
        bannerPane.getChildren().addAll(bannerLabel,imageStackPane,rbPane);
        return bannerPane;
    }
    
    
    // Add CSS /  Set Size for HBOX (Main) Layout
    
    private void defaultAttributes() {
        bannerPane.getStyleClass().add(BANNER_PANE_CSS);
        bannerPane.setPrefSize(CONTENT_PANE_WIDTH, CONTENT_PANE_HEIGHT);
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
                setBannerSrc(file.getAbsolutePath());
                coolPath = "img//" + file.getName();
                String path = file.getPath().substring(0, file.getPath().indexOf(file.getName()));
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
    
    public void setBannerSrc(String bannerSrc) {
        this.bannerSrc = bannerSrc;
    }
    
    public String getBannerSrc() {
        return bannerSrc;
    }
    
    public JsonObject makeJsonObject() {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("src", getBannerSrc())
                .add("coolpath", coolPath)
                .add("include", toggleGroup.getSelectedToggle().getUserData().toString()).build();
        return jsonObject;
    }
    
    public String coolPath() {
        return coolPath;
    }
}
