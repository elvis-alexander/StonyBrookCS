/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Page;

import static File.Constants.ADD_HEADER_LABEL_CSS;
import static File.Constants.ADD_HEADER_PANE_CSS;
import static File.Constants.CONTENT_PANE_HEIGHT;
import static File.Constants.CONTENT_PANE_WIDTH;
import static File.Constants.DEFAULT_HEADER;
import static File.Constants.REMOVE_BUTTON_HEIGHT;
import static File.Constants.REMOVE_BUTTON_WIDTH;
import static File.Constants.REMOVE_COMPONENT_CSS;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author Elviis
 */
public class HeaderComponent extends Component {
  
    private String header;
    private final Label headerLabel = new Label("Header Comp: ");
    private final TextField headerField = new TextField();
    private final HBox addHeaderPane = new HBox();
    
    private Label fontLabel = new Label("Choose a font");
    private ToggleGroup toggleGroup = new ToggleGroup();
    
    private RadioButton Montserrat = new RadioButton("Montserrat");
    private RadioButton Lobster = new RadioButton("Lobster");
    private RadioButton Merriweather = new RadioButton("Merriweather");
    private RadioButton Oswald = new RadioButton("Oswald");
    private RadioButton Arvo = new RadioButton("Arvo");
    
    private Button removeButton;
    private Page page;
    
    // Construct Default obj.
    public HeaderComponent(Page p) {
        this.page = p;
        createRemoveBtn();
        header = DEFAULT_HEADER;
        setRadioAttr();
        Montserrat.setSelected(true);
        defaultAttributes();
        initHandler();
        handleRemove();
    }

    // Construct object on load. 
    public HeaderComponent(Page p, String header, String font) {
        this.page = p;
        createRemoveBtn();     
        this.header = header;
        setRadioAttr();
        if(font.equals("Montserrat")) {
            Montserrat.setSelected(true);
        } else if(font.equals("Lobster")) {
            Lobster.setSelected(true);
        } else if(font.equals("Merriweather")) {
            Merriweather.setSelected(true);
        } else if(font.equals("Oswald")) {
            Oswald.setSelected(true);
        } else if(font.equals("Arvo")) {
            Arvo.setSelected(true);
        }
        defaultAttributes(); 
        initHandler();
        handleRemove();
    }
    
    private void createRemoveBtn() {
        removeButton = new Button("Remove");
        removeButton.setPrefSize(REMOVE_BUTTON_WIDTH, REMOVE_BUTTON_HEIGHT);
        removeButton.getStyleClass().add(REMOVE_COMPONENT_CSS);
    }
    
    private void setRadioAttr() {
        Montserrat.setUserData("Montserrat");
        Lobster.setUserData("Lobster");
        Merriweather.setUserData("Merriweather");
        Oswald.setUserData("Oswald");
        Arvo.setUserData("Arvo");

        Montserrat.setToggleGroup(toggleGroup);
        Lobster.setToggleGroup(toggleGroup);
        Merriweather.setToggleGroup(toggleGroup);
        Oswald.setToggleGroup(toggleGroup);
        Arvo.setToggleGroup(toggleGroup);
    }
    
    private void defaultAttributes() {
            headerLabel.getStyleClass().add(ADD_HEADER_LABEL_CSS);
            addHeaderPane.getStyleClass().add(ADD_HEADER_PANE_CSS);
            headerField.setText(header);
            headerField.setMinWidth(350);
            addHeaderPane.setPrefSize(CONTENT_PANE_WIDTH, CONTENT_PANE_HEIGHT/3);
    }

    private void initHandler() {
        // set handler for text field
    }

    @Override
    public Pane componentView() {
        HBox fontBox = new HBox(10);
        fontBox.getChildren().addAll(Montserrat,Lobster, Merriweather, Oswald, Arvo);
        StackPane removeBtnPane = new StackPane(removeButton);
        addHeaderPane.getChildren().addAll(headerLabel, headerField,fontBox, removeBtnPane);
        return addHeaderPane;
    }
    
    public void setHeader(String header) {
        this.header = header;
    }    
    public TextField getHeaderField() {
        return headerField;
    }

    @Override
    public JsonObject makeJsonObject() {
        // Inclue Font later on.
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("type", getType())
                .add("text", headerField.getText())
                .add("font", toggleGroup.getSelectedToggle().getUserData().toString()).build();
        return jsonObject;
    }

    @Override
    public String getType() {
        return "header";
    }
    public void save() {
        this.header = headerField.getText();
    }
    
    public void handleRemove() {
        removeButton.setOnAction(e ->{
            page.getComponents().remove(this);
            page.getComponentView().getChildren().remove(addHeaderPane);
        });
    }
}
