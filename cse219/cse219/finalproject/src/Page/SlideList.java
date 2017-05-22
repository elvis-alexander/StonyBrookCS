/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Page;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.VBox;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 *
 * @author eafernandez
 */
public class SlideList extends VBox {

    private List<Slide> slides;
    private Slide selectedSlide;

    public SlideList() {
        slides = new ArrayList<Slide>();
    }

    public SlideList(List<Slide> s) {
        this.slides = new ArrayList<Slide>();
        for(int i = 0; i < s.size(); i++) {
            addSlide(s.get(i));
        }
    }
    
    public void addSlide(Slide newSlide) {
        setSlideHandler(newSlide);
        slides.add(newSlide);
        setSelectedSlide(newSlide);
        super.getChildren().clear();
        highlightSelectedSlide();
    }

    public void addSlide() {
        Slide newSlide = new Slide();
        setSlideHandler(newSlide);
        slides.add(newSlide);
        setSelectedSlide(newSlide);
        super.getChildren().clear();
        highlightSelectedSlide();
    }

    public void setSlideHandler(Slide newSlide) {
        newSlide.setOnMouseClicked(e -> {
            setSelectedSlide(newSlide);
            super.getChildren().clear();
            highlightSelectedSlide();
        });
    }

    public void highlightSelectedSlide() {
        for (int i = 0; i < slides.size(); i++) {
            if (slides.get(i) == getSelected()) {
                slides.get(i).setStyle("-fx-background-color: lightblue;");
                super.getChildren().add(slides.get(i));
            } else {
                slides.get(i).setStyle("-fx-background-color: #0FB493;");
                super.getChildren().add(slides.get(i));
            }
        }
    }

    public void removeSelectedSlide() {
        slides.remove(selectedSlide);
        slides.remove(null);
        super.getChildren().clear();
        if(!slides.isEmpty()) {
            setSelectedSlide(slides.get(slides.size() - 1));
            highlightSelectedSlide();
        }
    }

    public void moveSelectedSlideUp() {
        int index = slides.indexOf((Slide) getSelected());
        if (index > 0) {
            Slide slideTmp = slides.get(index);
            slides.set(index, slides.get(index - 1));
            slides.set(index - 1, slideTmp);
            
            super.getChildren().clear();
            if(!slides.isEmpty()) {
                setSelectedSlide(slides.get(index - 1));
                highlightSelectedSlide();
            }
        }
    }

    public void moveSelectedSlideDown() {
        int index = slides.indexOf((Slide) getSelected());
        if (index < (slides.size() - 1)) {
            Slide slideTmp = slides.get(index);
            slides.set(index, slides.get(index + 1));
            slides.set(index + 1, slideTmp);
            
            super.getChildren().clear();
            if(!slides.isEmpty()) {
                setSelectedSlide(slides.get(index+1));
                highlightSelectedSlide();
            }
        }
        
    }

    public void setSelectedSlide(Slide s) {
        this.selectedSlide = s;
    }

    public Slide getSelected() {
        return selectedSlide;
    }

    public JsonObject constructJson() {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (Slide slide : slides) {
            jsonArrayBuilder.add(slide.makeJsonObject());
        }
        JsonArray slideListJson = jsonArrayBuilder.build();
        JsonObject jsonObject = Json.createObjectBuilder().add("type", "slideshow").add("slidelist", slideListJson).build();
        return jsonObject;
    }

    public List<Slide> getSlides() {
        return slides;
    }
}
