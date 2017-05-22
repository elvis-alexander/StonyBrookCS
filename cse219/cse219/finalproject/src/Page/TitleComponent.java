/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Page;

import static File.Constants.CONTENT_PANE_HEIGHT;
import static File.Constants.CONTENT_PANE_WIDTH;
import static File.Constants.DEFAULT_TITLE;
import static File.Constants.PAGE_EDITOR_PANE_LIST_CSS;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import static File.Constants.UPDATE_TITLE_HEADER_LABEL_CSS;
import static File.Constants.UPDATE_TITLE_PANE_CSS;

/**
 *
 * @author Elviis
 */
public class TitleComponent extends DefaultComponent implements Selectable {
    private String title;
    private final Label headerLabel = new Label("Update Title: ");
    private final TextField titleField;
    private final HBox updateTitlePane = new HBox();
    private Page page; 
    
    // Construct Default obj.
    public TitleComponent(Page page) {
        this.page = page;
        titleField = new TextField();
        this.title = DEFAULT_TITLE;
        defaultAttributes();
        initHandler();
    }

    // Construct object on load. 
    public TitleComponent(String title) {
        this.title = title;
        titleField = new TextField();
        defaultAttributes(); 
//        initHandler();
    }
    
    // All Default Components will have this method to format on loading / creating 
    @Override
    public HBox defaultComponentView() {
        updateTitlePane.getStyleClass().add(PAGE_EDITOR_PANE_LIST_CSS);
        updateTitlePane.getChildren().addAll(headerLabel, titleField);
        return updateTitlePane;
    }

    private void defaultAttributes() {
        headerLabel.getStyleClass().add(UPDATE_TITLE_HEADER_LABEL_CSS);
        updateTitlePane.getStyleClass().add(UPDATE_TITLE_PANE_CSS);
        titleField.setText(title);
        titleField.setMinWidth(350);
        updateTitlePane.setPrefSize(CONTENT_PANE_WIDTH, CONTENT_PANE_HEIGHT/3);
    }

    private void initHandler() {
        // set handler for text field
        this.titleField.setOnKeyReleased(e->{
            page.setText("" + titleField.getText());
        });
    }
    
    public void actionHandler() {
        this.titleField.setOnKeyReleased(e->{
            page.setText("" + titleField.getText());
        });
    }
    
    public void setPage(Page p) {
        this.page = p;
        initHandler();
    }
    public void setTitle(String title) {
        this.title = title;
    }    
    public TextField getTitleField() {
        return titleField;
    }

    @Override
    public void handleSelect() {
//        updateTitlePane.setOnMouseClicked(e -> {
//            page.setSelected(this);
//            updateTitlePane.getStyleClass().add("selected");
//        });
    }
    
}
