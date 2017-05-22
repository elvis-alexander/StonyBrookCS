/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Page;

import static File.Constants.CONTENT_PANE_HEIGHT;
import static File.Constants.CONTENT_PANE_WIDTH;
import static File.Constants.DEFAULT_FOOTER;
import static File.Constants.PAGE_EDITOR_PANE_LIST_CSS;
import static File.Constants.UPDATE_FOOTER_HEADER_LABEL_CSS;
import static File.Constants.UPDATE_FOOTER_PANE_CSS;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

/**
 *
 * @author Elviis
 */
public class FooterComponent extends DefaultComponent {
  
    private String footer;
    private final Label headerLabel = new Label("Update Footer: ");
    private final TextArea footerField = new TextArea();
    private final HBox updateFooterPane = new HBox();
    
    // Construct Default obj.
    public FooterComponent() {
        footer = DEFAULT_FOOTER;
        footerField.setWrapText(true);
        defaultAttributes();
        initHandler();
    }

    // Construct object on load. 
    public FooterComponent(String f) {
        this.footer = f;
        footerField.setWrapText(true);
        footerField.setText(footer);
        defaultAttributes();
        initHandler();
    }
    
    // All Default Components will have this method to format on loading / creating 
    @Override
    public HBox defaultComponentView() {
        updateFooterPane.getStyleClass().add(PAGE_EDITOR_PANE_LIST_CSS);
        footerField.setMaxWidth(350);
        footerField.setMaxHeight(180);
        headerLabel.getStyleClass().add(UPDATE_FOOTER_HEADER_LABEL_CSS);
        updateFooterPane.getStyleClass().add(UPDATE_FOOTER_PANE_CSS);
        footerField.setText(footer);
        updateFooterPane.getChildren().addAll(headerLabel, footerField);
        return updateFooterPane;
    }
    private void defaultAttributes() {
            updateFooterPane.setPrefSize(CONTENT_PANE_WIDTH, CONTENT_PANE_HEIGHT);
    }
    
    public void initHandler() {
        // set handler for text field
        
    }
    
    public void setFooter(String footer) {
        this.footer = footer;
    }    
    public TextArea getFooterField() {
        return footerField;
    }
}
