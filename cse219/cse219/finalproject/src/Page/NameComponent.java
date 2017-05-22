/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Page;

import static File.Constants.CONTENT_PANE_HEIGHT;
import static File.Constants.CONTENT_PANE_WIDTH;
import static File.Constants.DEFAULT_NAME;
import static File.Constants.PAGE_EDITOR_PANE_LIST_CSS;
import static File.Constants.UPDATE_NAME_HEADER_LABEL_CSS;
import static File.Constants.UPDATE_NAME_PANE_CSS;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 *
 * @author Elviis
 */
public class NameComponent extends DefaultComponent implements Selectable {
  
    private String name;
    private final Label headerLabel = new Label("Update Name: ");
    private final TextField nameField = new TextField();
    private final HBox updateNamePane = new HBox();
    
    public NameComponent() {
        name = DEFAULT_NAME;
        defaultAttributes();
        initHandler();
    }

    // Construct object on load. 
    public NameComponent(String name) {
        this.name = name;
        defaultAttributes();
        initHandler();
    }
    
    // All Default Components will have this method to format on loading / creating 
    @Override
    public HBox defaultComponentView() {
        updateNamePane.getStyleClass().add(PAGE_EDITOR_PANE_LIST_CSS);
        nameField.setText(name);
        updateNamePane.getChildren().addAll(headerLabel, nameField);
        return updateNamePane;
    }
    
    private void defaultAttributes() {
        nameField.setText(name);
        nameField.setMinWidth(350);
        updateNamePane.setPrefSize(CONTENT_PANE_WIDTH, CONTENT_PANE_HEIGHT/3);
        updateNamePane.getStyleClass().add(UPDATE_NAME_PANE_CSS);
        headerLabel.getStyleClass().add(UPDATE_NAME_HEADER_LABEL_CSS);
    }
    
    public void initHandler() {
        // set handler for text field
        
    }
    
    public void setName(String name) {
        this.name = name;
    }    
    public String getName() {
        return name;
    }
    public TextField getNameField() {
        return nameField;
    }

    @Override
    public void handleSelect() {
//        updateNamePane.setOnMouseClicked(e -> {
//            page.setSelected(this);
//            updateNamePane.getStyleClass().add("selected");
//        });
    }
}
