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
public class ListArray extends VBox {

    private List<ListItem> listItems;
    private ListItem selectedItem;
    private ListComponent listComponent;

    public ListArray(ListComponent lc) {
        this.listComponent = lc;
        listItems = new ArrayList<ListItem>();
    }

    public ListArray(List<ListItem> l, ListComponent lc) {
        this.listItems = new ArrayList<ListItem>();
        this.listComponent = lc;
        for(int i = 0; i < l.size(); i++) {
            addItem(l.get(i));
        }
    }
    
    public void addItem(ListItem newItem) {
        setSlideHandler(newItem);
        listItems.add(newItem);
        setSelectedItem(newItem);
        super.getChildren().clear();
        highlightSelectedSlide();
    }

    public void addItem() {
        ListItem newItem = new ListItem();
        setSlideHandler(newItem);
        listItems.add(newItem);
        setSelectedItem(newItem);
        super.getChildren().clear();
        highlightSelectedSlide();
    }

    public void setSlideHandler(ListItem newItem) {
        newItem.setOnMouseClicked(e -> {
            setSelectedItem(newItem);
            super.getChildren().clear();
            highlightSelectedSlide();
        });
    }
    

    public void highlightSelectedSlide() {
        for (int i = 0; i < listItems.size(); i++) {
            if (listItems.get(i) == getSelected()) {
                listItems.get(i).setStyle("-fx-background-color: lightblue;");
                super.getChildren().add(listItems.get(i));
            } else {
                listItems.get(i).setStyle("-fx-background-color: #0FB493;");
                super.getChildren().add(listItems.get(i));
            }
        }
    }

    public void removeSelectedSlide() {
        listItems.remove(selectedItem);
        listItems.remove(null);
        super.getChildren().clear();
        if(!listItems.isEmpty()) {
            setSelectedItem(listItems.get(listItems.size() - 1));
            highlightSelectedSlide();
        }
    }

    public void moveSelectedSlideUp() {
        int index = listItems.indexOf((ListItem) getSelected());
        if (index > 0) {
            ListItem itemTmp = listItems.get(index);
            listItems.set(index, listItems.get(index - 1));
            listItems.set(index - 1, itemTmp);
            
            super.getChildren().clear();
            if(!listItems.isEmpty()) {
                setSelectedItem(listItems.get(index - 1));
                highlightSelectedSlide();
            }
        }
    }

    public void moveSelectedSlideDown() {
        int index = listItems.indexOf((ListItem) getSelected());
        if (index < (listItems.size() - 1)) {
            ListItem listTmp = listItems.get(index);
            listItems.set(index, listItems.get(index + 1));
            listItems.set(index + 1, listTmp);
            
            super.getChildren().clear();
            if(!listItems.isEmpty()) {
                setSelectedItem(listItems.get(index+1));
                highlightSelectedSlide();
            }
        }
        
    }

    public void setSelectedItem(ListItem l) {
        this.selectedItem = l;
    }

    public ListItem getSelected() {
        return selectedItem;
    }

    public List<ListItem> getSlides() {
        return listItems;
    }
    
    public JsonObject constructJson() {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (ListItem item : listItems) {
            jsonArrayBuilder.add(item.makeJsonObject());
        }
        JsonArray slideListJson = jsonArrayBuilder.build();
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("type", "list")
                .add("font", listComponent.getToggleGroup().getSelectedToggle().getUserData().toString())
                .add("listitems", slideListJson).build();
        return jsonObject;
    }

}
