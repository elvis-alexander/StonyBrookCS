/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Page;

import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javax.json.JsonObject;

/**
 *
 * @author eafernandez
 */
public class ListComponent extends Component {

    private ListArray listArray;
    private Button addSlide = new Button("Add Item");
    private Button removeSlide = new Button("Remove Item");
    private Button moveUp = new Button("Move Up");
    private Button moveDown = new Button("Move Down");
    private VBox listComponentPane = new VBox();
    
    
    private ToggleGroup toggleGroup = new ToggleGroup();
    private RadioButton Montserrat = new RadioButton("Montserrat");
    private RadioButton Lobster = new RadioButton("Lobster");
    private RadioButton Merriweather = new RadioButton("Merriweather");
    private RadioButton Oswald = new RadioButton("Oswald");
    private RadioButton Arvo = new RadioButton("Arvo");
    
    
    private Page page;
    private Button removeButton;

    public ListComponent(Page p) {
        this.page = p;
        createRemoveBtn();
        this.listArray = new ListArray(this);
        setRadioAttr();
        Montserrat.setSelected(true);
        btnHandler();
        handleRemove();
    }

    public ListComponent(Page p, List<ListItem> l, String font) {
        this.page = p;
        createRemoveBtn();
        this.listArray = new ListArray(l, this);
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
        btnHandler();
        handleRemove();
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
    
    private void createRemoveBtn() {
        removeButton = new Button("Remove Component");
    }

    public void btnHandler() {
        this.addSlide.setOnAction(e -> {
            listArray.addItem();
        });
        this.removeSlide.setOnAction(e -> {
            listArray.removeSelectedSlide();
        });
        this.moveUp.setOnAction(e -> {
            listArray.moveSelectedSlideUp();
        });
        this.moveDown.setOnAction(e -> {
            listArray.moveSelectedSlideDown();
        });
    }

    @Override
    public Pane componentView() {
        HBox btnPane = new HBox();
        btnPane.getStyleClass().add("slide_show_btn_pane");
        btnPane.getChildren().addAll(addSlide, removeSlide, moveDown, moveUp, removeButton,Montserrat,Lobster, Merriweather, Oswald, Arvo);
        listComponentPane.getStyleClass().add("page_editor_slide_show_css");
        listComponentPane.getChildren().addAll(btnPane, listArray);
        return listComponentPane;
    }

    @Override
    public JsonObject makeJsonObject() {
        return listArray.constructJson();
    }

    @Override
    public String getType() {
        return "list";
    }
    
    public void handleRemove() {
        removeButton.setOnAction(e ->{
            page.getComponents().remove(this);
            page.getComponentView().getChildren().remove(listComponentPane);
        });
    }

    public ToggleGroup getToggleGroup() {
        return toggleGroup;
    }
    
}
