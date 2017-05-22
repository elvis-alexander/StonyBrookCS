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

/**
 *
 * @author eafernandez
 */
public class ColorComponent extends DefaultComponent{
    
    private String color;
    private Label selectColorLabel = new Label("Color Combo:  ");
    private ToggleGroup group = new ToggleGroup();
    
    private RadioButton bluegray;
    private RadioButton gray;
    private RadioButton graygreen;
    private RadioButton green;
    private RadioButton redgray;
    
    private Pane labelPane  = new Pane();
    private HBox radioButtonPane = new HBox();
    private HBox selectColorPane = new HBox();
    
    public ColorComponent() {
        this.color = "bluegray";
        constructRadioBtns();
        bluegray.setSelected(true);
        defaultAttributes();
    }
    
    public ColorComponent(String c) {
        this.color = c;
        constructRadioBtns();

        if(color.equals("bluegray")) {
            bluegray.setSelected(true);
        } else if(color.equals("gray")) {
            gray.setSelected(true);
        } else if(color.equals("graygreen")) {
            graygreen.setSelected(true);
        } else if(color.equals("green")) {
            green.setSelected(true);
        } else if(color.equals("redgray")) {
            redgray.setSelected(true);
        }
        defaultAttributes();
    }
    
    public void constructRadioBtns() {
        bluegray = new RadioButton("bluegray");
        gray = new RadioButton("gray");
        graygreen = new RadioButton("graygreen");
        green = new RadioButton("green");        
        redgray = new RadioButton("redgray");  
        
        bluegray.setUserData("bluegray");
        gray.setUserData("gray");
        graygreen.setUserData("graygreen");
        green.setUserData("green");
        redgray.setUserData("redgray");
    }
    
    public void defaultAttributes() {
        bluegray.setToggleGroup(group);
        gray.setToggleGroup(group);
        graygreen.setToggleGroup(group);
        green.setToggleGroup(group);
        redgray.setToggleGroup(group);  
        selectColorLabel.getStyleClass().add("select_color_label");
        selectColorPane.getStyleClass().add("select_color_pane");
        selectColorPane.setPrefSize(CONTENT_PANE_WIDTH, CONTENT_PANE_HEIGHT/3);
    }

    @Override
    public Pane defaultComponentView() {
        selectColorPane.getStyleClass().add(PAGE_EDITOR_PANE_LIST_CSS);
        labelPane.getChildren().add(selectColorLabel);
        radioButtonPane.getChildren().addAll(bluegray, gray, graygreen, green, redgray);
        selectColorPane.getChildren().addAll(labelPane, radioButtonPane);
        return selectColorPane;
    }
    
    public String getColorStr() {
        return group.getSelectedToggle().getUserData().toString();
    }
    
}