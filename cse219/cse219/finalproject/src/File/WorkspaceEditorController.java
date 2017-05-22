/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package File;

import Page.HeaderComponent;
import Page.ImageComponent;
import Page.ListComponent;
import Page.ParagraphComponent;
import Page.SlideShowComponent;
import Page.VideoComponent;
import UI.UI;

/**
 *
 * @author eafernandez
 */
public class WorkspaceEditorController {
    
    private UI ui;
    
    public WorkspaceEditorController(UI ui) {
        this.ui = ui;
    }
    
    public void handleAddPage() {
        ui.getPageEditor().addPage();
    }
    
    public void handleRemovePage() {
        ui.getPageEditor().removeSelectedPage();
    }
    
    public void handleAddImageComponent() {
        this.ui.getPageEditor().getSelectedPage().addComponent(new ImageComponent(ui.getPageEditor().getSelectedPage()));
    }
    
    public void handleAddList() {
        this.ui.getPageEditor().getSelectedPage().addComponent(new ListComponent(ui.getPageEditor().getSelectedPage()));
    }
   
    public void handleAddHeader() {
        this.ui.getPageEditor().getSelectedPage().addComponent(new HeaderComponent(ui.getPageEditor().getSelectedPage()));
    }
    
    public void handleAddVideoComponent() {
        this.ui.getPageEditor().getSelectedPage().addComponent(new VideoComponent(ui.getPageEditor().getSelectedPage()));
    }
    
    public void handleAddSlideShowComponent() {
        this.ui.getPageEditor().getSelectedPage().addComponent(new SlideShowComponent(ui.getPageEditor().getSelectedPage()));
    }
    
    public void handleAddParagraghComponent() {
        this.ui.getPageEditor().getSelectedPage().addComponent(new ParagraphComponent(ui.getPageEditor().getSelectedPage()));
    }
    
}
