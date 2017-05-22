    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Page;

import static File.Constants.CONTENT_PANE_HEIGHT;
import static File.Constants.CONTENT_PANE_WIDTH;
import static File.Constants.PAGE_EDITOR_PANE_LIST_CSS;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author eafernandez
 */
public class FontComponent extends DefaultComponent{
    private String font;
    
    private Label selectFontLabel = new Label("Font: ");
    
    private ToggleGroup group = new ToggleGroup();
    private RadioButton Montserrat;
    private RadioButton Lobster;
    private RadioButton Merriweather;
    private RadioButton Oswald;
    private RadioButton Arvo;
    
    private Pane labelPane  = new Pane();
    private HBox radioButtonPane = new HBox();
    private HBox selectFontPane = new HBox();
    
    public FontComponent() {
        constructRadioButtons();
        Montserrat.setSelected(true);
        font = "Montserrat";
        defaultAttributes();
    }
    
    public FontComponent(String f) {
        this.font = f;
        constructRadioButtons();
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
    }
    
    public void constructRadioButtons() {
        Montserrat = new RadioButton("Montserrat");
        Lobster = new RadioButton("Lobster");
        Merriweather = new RadioButton("Merriweather");
        Oswald = new RadioButton("Oswald");        
        Arvo = new RadioButton("Arvo");  
        Montserrat.setUserData("Montserrat");
        Lobster.setUserData("Lobster");
        Merriweather.setUserData("Merriweather");
        Oswald.setUserData("Oswald");
        Arvo.setUserData("Arvo");
    }
    
    public void defaultAttributes() {
        Montserrat.setToggleGroup(group);
        Lobster.setToggleGroup(group);
        Merriweather.setToggleGroup(group);
        Oswald.setToggleGroup(group);
        Arvo.setToggleGroup(group);  
        selectFontLabel.getStyleClass().add("select_color_label");
        selectFontPane.getStyleClass().add("select_color_pane");
        selectFontPane.setPrefSize(CONTENT_PANE_WIDTH, CONTENT_PANE_HEIGHT/3);
    }

    @Override
    public Pane defaultComponentView() {
        selectFontPane.getStyleClass().add(PAGE_EDITOR_PANE_LIST_CSS);
        labelPane.getChildren().add(selectFontLabel);
        radioButtonPane.getChildren().addAll(Montserrat, Lobster, Merriweather, Oswald, Arvo);
        selectFontPane.getChildren().addAll(labelPane, radioButtonPane);
        return selectFontPane;
    }
    
    public ToggleGroup getToggleGroup() {
        return group;
    }
    
    
    public String getFontStr() {
        return group.getSelectedToggle().getUserData().toString();
    }
}