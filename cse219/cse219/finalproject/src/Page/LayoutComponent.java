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
public class LayoutComponent extends DefaultComponent{
    
    private String template;
    private Label selectTemplateLabel = new Label("Template:  ");
    private ToggleGroup group = new ToggleGroup();
    
    private RadioButton template1;
    private RadioButton template2;
    private RadioButton template3;
    private RadioButton template4;
    private RadioButton template5;
    
    private Pane labelPane  = new Pane();
    private HBox radioButtonPane = new HBox();
    private HBox selectTemplatePane = new HBox();
    
    public LayoutComponent() {
        this.template = "template1";
        constructRadioBtns();
        template1.setSelected(true);
        defaultAttributes();
    }
    
    public LayoutComponent(String t) {
        this.template = t;
        constructRadioBtns();

        if(template.equals("template1")) {
            template1.setSelected(true);
        } else if(template.equals("template2")) {
            template2.setSelected(true);
        } else if(template.equals("template3")) {
            template3.setSelected(true);
        } else if(template.equals("template4")) {
            template4.setSelected(true);
        } else if(template.equals("template5")) {
            template5.setSelected(true);
        }
        defaultAttributes();
    }
    
    public void constructRadioBtns() {
        template1 = new RadioButton("template1");
        template2 = new RadioButton("template2");
        template3 = new RadioButton("template3");
        template4 = new RadioButton("template4");        
        template5 = new RadioButton("template5");  
        
        template1.setUserData("template1");
        template2.setUserData("template2");
        template3.setUserData("template3");
        template4.setUserData("template4");
        template5.setUserData("template5");
    }
    
    public void defaultAttributes() {
        template1.setToggleGroup(group);
        template2.setToggleGroup(group);
        template3.setToggleGroup(group);
        template4.setToggleGroup(group);
        template5.setToggleGroup(group);  
        selectTemplateLabel.getStyleClass().add("select_color_label");
        selectTemplatePane.getStyleClass().add("select_color_pane");
        selectTemplatePane.setPrefSize(CONTENT_PANE_WIDTH, CONTENT_PANE_HEIGHT/3);
    }

    @Override
    public Pane defaultComponentView() {
        selectTemplatePane.getStyleClass().add(PAGE_EDITOR_PANE_LIST_CSS);
        labelPane.getChildren().add(selectTemplateLabel);
        radioButtonPane.getChildren().addAll(template1, template2, template3, template4, template5);
        selectTemplatePane.getChildren().addAll(labelPane, radioButtonPane);
        return selectTemplatePane;
    }
    
    public String getTemplateStr() {
        return group.getSelectedToggle().getUserData().toString();
    }
    
}