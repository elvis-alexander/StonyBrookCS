/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Page;

import static File.Constants.ADD_VIDEO_COMPONENT_PANE_CSS;
import static File.Constants.ADD_VIDEO_COMPONENT_PANE_LEFT_GRIDPANE_CSS;
import static File.Constants.ADD_VIDEO_COMPONENT_PANE_RIGHT_VBOX_CSS;
import static File.Constants.CAPTION_PROMPT;
import static File.Constants.CONTENT_PANE_HEIGHT;
import static File.Constants.CONTENT_PANE_WIDTH;
import static File.Constants.DEFAULT_VIDEO_CAPTION;
import static File.Constants.DEFAULT_VIDEO_HEIGHT;
import static File.Constants.DEFAULT_VIDEO_SRC;
import static File.Constants.VIDEO_PROMPT;
import static File.Constants.DEFAULT_VIDEO_WIDTH;
import static File.Constants.HEIGHT_PROMPT;
import static File.Constants.REMOVE_BUTTON_HEIGHT;
import static File.Constants.REMOVE_BUTTON_MSG;
import static File.Constants.REMOVE_BUTTON_WIDTH;
import static File.Constants.REMOVE_COMPONENT_CSS;
import static File.Constants.WIDTH_PROMPT;
import UI.PageEditor;
import java.io.File;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
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

//Must allow user to select video file and caption 
//as well as display width/height

public class VideoComponent extends Component {
    private String videoSource;
    private String videoCaption;
    private String videoWidth;
    private String videoHeight;
    
    
    private final Label videoSourceLabel = new Label(VIDEO_PROMPT);
    private final Label videoCaptionLabel = new Label(CAPTION_PROMPT);
    private final Label videoWidthLabel   = new Label(WIDTH_PROMPT+ "   ");
    private final Label videoHeightLabel  = new Label(HEIGHT_PROMPT+ "  ");
    
    private final TextArea videoSourceField = new TextArea();
    private final TextField videoCaptionField = new TextField();
    private final TextField videoWidthField   = new TextField();
    private final TextField videoHeightField  = new TextField();
    
    private final Button searchVideoFileBtn = new Button("Browse My Computer");
    private Button removeButton;
    
    
    // HBox for each layout
    private final HBox videoCaptionHBox = new HBox();
    private final HBox videoWidthHBox = new HBox();
    private final HBox videoHeightHBox = new HBox();
    
    // Pane to hold Caption, Width, & Height
    private final GridPane leftGridPane = new GridPane();
    private final VBox rightVBox = new VBox();
    
    // Main Layout    
    private final HBox addVideoComponentPane = new HBox();
    private Page page;
    
    private String coolPath;
    
    public VideoComponent(Page p) {
        coolPath = "vids//samplevid.mp4";
        this.page = p;
        constructRemoveBtn();
        this.videoSource = "./vids/samplevid.mp4";
        this.videoCaption = DEFAULT_VIDEO_CAPTION;
        this.videoWidth = DEFAULT_VIDEO_WIDTH;
        this.videoHeight = DEFAULT_VIDEO_HEIGHT;
        handleRemove();
        defaultAttributes();
    }

    public VideoComponent(Page p, String videoSrc, String videoCaption, String videoWidth, String videoHeight, String cp) {
        this.page = p;
        this.videoSource = videoSrc;
        this.videoCaption = videoCaption;
        this.videoWidth = videoWidth;
        this.videoHeight = videoHeight;
        this.coolPath = cp;
        constructRemoveBtn();
        handleRemove();
        defaultAttributes();
    }
    
    public void constructRemoveBtn() {
        this.removeButton = new Button(REMOVE_BUTTON_MSG);
        this.removeButton.setPrefSize(REMOVE_BUTTON_WIDTH, REMOVE_BUTTON_HEIGHT);
        this.removeButton.getStyleClass().add(REMOVE_COMPONENT_CSS);
    }
    
    @Override
    public Pane componentView() {
        initHandler();     

        // set up textfield's
        videoCaptionField.setText(videoCaption);
        videoWidthField.setText(videoWidth);
        videoHeightField.setText(videoHeight);
        videoSourceField.setText(videoSource);
        
        // add to GridPane
        leftGridPane.add(videoSourceLabel, 0, 0);
        leftGridPane.add(searchVideoFileBtn, 0, 1);
        leftGridPane.add(videoSourceField, 0, 2);
        
        // add to each HBox
        videoCaptionHBox.getChildren().addAll(videoCaptionLabel, videoCaptionField);
        videoWidthHBox.getChildren().addAll(videoHeightLabel, videoWidthField);
        videoHeightHBox.getChildren().addAll(videoWidthLabel, videoHeightField);
        rightVBox.getChildren().addAll(videoCaptionHBox, videoWidthHBox, videoHeightHBox);
      
        StackPane removeBtnPane = new StackPane(removeButton);
        removeBtnPane.setPadding(new Insets(0,0,0,400));
        addVideoComponentPane.getChildren().addAll(leftGridPane, rightVBox, removeBtnPane);
        return addVideoComponentPane;
    }
    
    private void defaultAttributes() {
        this.videoSourceField.setWrapText(true);
        this.videoSourceField.setMaxWidth(300);
        this.videoSourceField.setMaxHeight(100);
        this.leftGridPane.getStyleClass().add(ADD_VIDEO_COMPONENT_PANE_LEFT_GRIDPANE_CSS);
        this.rightVBox.getStyleClass().add(ADD_VIDEO_COMPONENT_PANE_RIGHT_VBOX_CSS);
        this.addVideoComponentPane.getStyleClass().add(ADD_VIDEO_COMPONENT_PANE_CSS);
        this.addVideoComponentPane.setPrefSize(CONTENT_PANE_WIDTH, CONTENT_PANE_HEIGHT);
    }
    
    private void initHandler() {
        searchVideoFileBtn.setOnAction(src -> {
        
            String initialDir = System.getProperty("user.home");
            FileChooser fileChooser = new  FileChooser();
            fileChooser.setInitialDirectory(new File(initialDir));
            FileChooser.ExtensionFilter mp3Filter = new FileChooser.ExtensionFilter("mp3 files (*.mp3)", "*.mp3");
            FileChooser.ExtensionFilter mp4Filter = new FileChooser.ExtensionFilter("mp4 files (*.mp4)", "*.mp4");
            FileChooser.ExtensionFilter MP3Filter = new FileChooser.ExtensionFilter("MP3 files (*.MP3)", "*.MP3");
            FileChooser.ExtensionFilter MP4Filter = new FileChooser.ExtensionFilter("MP4 files (*.MP4)", "*.MP4");  
            fileChooser.getExtensionFilters().addAll( mp3Filter, mp4Filter, MP3Filter, MP4Filter);
            File file = fileChooser.showOpenDialog(null);
            if(file != null) {
                setVideoSrc(file.getAbsolutePath());
                coolPath = "vids//" + file.getName();
                videoSourceField.setText(file.getAbsolutePath());
            }
            
        });
        
    }
    
    public void handleRemove() {
        removeButton.setOnAction(e ->{
            page.getComponents().remove(this);
            page.getComponentView().getChildren().remove(addVideoComponentPane);
        });
    }
    
    // Setter's for setting image detail's / used for saving
    public void setVideoSrc(String videoSource) {
        this.videoSource = videoSource;
    }
    
    public String getVideoSrc() {
        return videoSource;
    }

    public void setVideoCaption(String videoCaption) {
        this.videoCaption = videoCaption;
    }

    public void setVideoWidth(String videoWidth) {
        this.videoWidth = videoWidth;
    }

    public void setVideoHeight(String videoHeight) {
        this.videoHeight = videoHeight;
    }
    
   // Getter's used for pulling Data
    public TextField getVideoCaptionField() {
        return videoCaptionField;
    }

    public TextField getVideoWidthField() {
        return videoWidthField;
    }

    public TextField getVideoHeightField() {
        return videoHeightField;
    }                                                                                                       

    @Override
    public JsonObject makeJsonObject() {
        System.out.println("COOLPATH: " + coolPath);
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("type", getType())
                .add("src", videoSourceField.getText())
                .add("caption", videoCaptionField.getText())
                .add("width", videoWidthField.getText())
                .add("height", videoHeightField.getText())
                .add("coolpath", coolPath)
                .build();
        return jsonObject;
    }

    @Override
    public String getType() {
        return "video";
    }
}
