/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Page;

import static File.Constants.CONTENT_PANE_HEIGHT;
import static File.Constants.CONTENT_PANE_WIDTH;
import static File.Constants.REMOVE_BUTTON_HEIGHT;
import static File.Constants.REMOVE_BUTTON_WIDTH;
import static File.Constants.REMOVE_COMPONENT_CSS;
import static File.Constants.UPDATE_FOOTER_HEADER_LABEL_CSS;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author eafernandez
 */
public class ParagraphComponent extends Component{
    
    private String paragraphContent;
    private String paragraphFontFamly;
    
    private HBox addParagrapghPane = new HBox();
    private TextArea paragrapghField = new TextArea();
    private Label fontLabel = new Label("Choose a font");
    private ToggleGroup toggleGroup = new ToggleGroup();
    
    private RadioButton Montserrat = new RadioButton("Montserrat");
    private RadioButton Lobster = new RadioButton("Lobster");
    private RadioButton Merriweather = new RadioButton("Merriweather");
    private RadioButton Oswald = new RadioButton("Oswald");
    private RadioButton Arvo = new RadioButton("Arvo");
    
    private Button removeButton;
    private Page page;
    
    public ParagraphComponent(Page p) {
        this.page = p;
        createRemoveBtn();
        paragrapghField.setWrapText(true);
        paragraphFontFamly = "Montserrat";
        paragraphContent = "Insert Text Here";
        constructRadioBtn();
        Montserrat.setSelected(true);
        defaultAttributes();
        handleRemove();
    }
    
    public ParagraphComponent(Page p,String pc, String f) {
        this.page = p;
        this.paragraphContent = pc;
        this.paragraphFontFamly = f;
        createRemoveBtn();
        paragrapghField.setWrapText(true);
        constructRadioBtn();
        
        if(paragraphFontFamly.equals("Montserrat")) {
            Montserrat.setSelected(true);
        } else if(paragraphFontFamly.equals("Lobster")) {
            Lobster.setSelected(true);
        } else if(paragraphFontFamly.equals("Merriweather")) {
            Merriweather.setSelected(true);
        } else if(paragraphFontFamly.equals("Oswald")) {
            Oswald.setSelected(true);
        } else if(paragraphFontFamly.equals("Arvo")) {
            Arvo.setSelected(true);
        }
        defaultAttributes();
        handleRemove();
    }
    
    private void constructRadioBtn() {
        Montserrat.setUserData("Montserrat");
        Lobster.setUserData("Lobster");
        Merriweather.setUserData("Merriweather");
        Oswald.setUserData("Oswald");
        Arvo.setUserData("Arvo");
        System.out.println("REACHED");
        Montserrat.setToggleGroup(toggleGroup);
        Lobster.setToggleGroup(toggleGroup);
        Merriweather.setToggleGroup(toggleGroup);
        Oswald.setToggleGroup(toggleGroup);
        Arvo.setToggleGroup(toggleGroup);
    }
    
    private void createRemoveBtn() {
        removeButton = new Button("Remove");
        removeButton.setPrefSize(REMOVE_BUTTON_WIDTH, REMOVE_BUTTON_HEIGHT);
        removeButton.getStyleClass().add(REMOVE_COMPONENT_CSS);
    }
    
    public void handleRemove() {
        removeButton.setOnAction(e ->{
            page.getComponents().remove(this);
            page.getComponentView().getChildren().remove(addParagrapghPane);
        });
    }
        
    @Override
    public Pane componentView() {
        addParagrapghPane.getStyleClass().add("paragrapgh_component");
        paragrapghField.setMaxWidth(350);
        paragrapghField.setMaxHeight(180);
        fontLabel.getStyleClass().add(UPDATE_FOOTER_HEADER_LABEL_CSS);
        paragrapghField.setText(paragraphContent);
        VBox fontBox = new VBox(10);
        fontBox.getChildren().addAll(Montserrat,Lobster, Merriweather, Oswald, Arvo);
        StackPane removeBtnPane = new StackPane(removeButton);
        addParagrapghPane.getChildren().addAll(paragrapghField, fontBox, removeBtnPane);
        return addParagrapghPane;
    }
    
    
    public void defaultAttributes() {
        addParagrapghPane.setPrefSize(CONTENT_PANE_WIDTH, CONTENT_PANE_HEIGHT);
    }
    
    public String getParagraphContent() {
        return paragraphContent;
    }

    public void setParagraphContent(String paragraphContent) {
        this.paragraphContent = paragraphContent;
    }

    public String getParagraphFontFamly() {
        return paragraphFontFamly;
    }

    public void setParagraphFontFamly(String paragraphFontFamly) {
        this.paragraphFontFamly = paragraphFontFamly;
    }

    
    @Override
    public JsonObject makeJsonObject() {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("type", getType())
                .add("text", paragrapghField.getText())
                .add("font", toggleGroup.getSelectedToggle().getUserData().toString()).build();
        return jsonObject;
    }
    
    @Override
    public String getType() {
        return "paragraph";
    }
    
    
}
