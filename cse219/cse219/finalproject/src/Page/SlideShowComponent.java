/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Page;

import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javax.json.JsonObject;

/**
 *
 * @author eafernandez
 */
public class SlideShowComponent extends Component {

    private SlideList slideList;
    private Button addSlide = new Button("Add Slide");
    private Button removeSlide = new Button("Remove Slide");
    private Button moveUp = new Button("Move Up");
    private Button moveDown = new Button("Move Down");
    private VBox slideShowComponentPane = new VBox();
    
    private Button removeButton;
    private Page page;

    public SlideShowComponent(Page p) {
        this.page = p;
        this.slideList = new SlideList();
        createRemoveBtn();
        btnHandler();
        handleRemove();
    }

    public SlideShowComponent(Page p, List<Slide> slides) {
        this.page = p;
        this.slideList = new SlideList(slides);
        createRemoveBtn();
        btnHandler();
        handleRemove();
    }
    
    
    private void createRemoveBtn() {
        removeButton = new Button("Remove Component");
    }

    public void btnHandler() {
        this.addSlide.setOnAction(e -> {
            slideList.addSlide();

        });
        this.removeSlide.setOnAction(e -> {
            slideList.removeSelectedSlide();
        });
        this.moveUp.setOnAction(e -> {
            slideList.moveSelectedSlideUp();
        });
        this.moveDown.setOnAction(e -> {
            slideList.moveSelectedSlideDown();
        });
    }

    @Override
    public Pane componentView() {
        HBox btnPane = new HBox();
        btnPane.getStyleClass().add("slide_show_btn_pane");
        btnPane.getChildren().addAll(addSlide, removeSlide, moveDown, moveUp, removeButton);
        slideShowComponentPane.getStyleClass().add("page_editor_slide_show_css");
        slideShowComponentPane.getChildren().addAll(btnPane, slideList);
        return slideShowComponentPane;
    }

    @Override
    public JsonObject makeJsonObject() {
        return slideList.constructJson();
    }

    @Override
    public String getType() {
        return "slideshow";
    }
    
    public SlideList getSlideList() {
        return slideList;
    }
    
    public void handleRemove() {
        removeButton.setOnAction(e ->{
            page.getComponents().remove(this);
            page.getComponentView().getChildren().remove(slideShowComponentPane);
        });
    }
}
