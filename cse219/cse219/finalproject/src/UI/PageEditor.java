//tabPane.getStyleClass().add("e");
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import Page.Page;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author eafernandez
 */
public class PageEditor {

    private UI ui;
    private List<Page> pageList;
    private ScrollPane scrollPane;
    private VBox contentScrollPane;
    private Page selectedPage;
    private TabPane tabPane;
    private boolean saved;

    public PageEditor(UI ui) {
        this.ui = ui;
        saved = true;
    }

    public PageEditor(UI ui, List<Page> pageList) {
        this.ui = ui;
        this.pageList = pageList;
        this.tabPane = new TabPane();
        saved = true;

        for (Page p : pageList) {
            System.out.println("ADDING");
            tabPane.getTabs().add(p);
            this.tabPane.setTabMinWidth(150);
            this.tabPane.setTabMinHeight(20);
        }
        tabPane.getSelectionModel().selectLast();
        setSelectedPage(pageList.get(pageList.size() - 1));
        ui.getCenterPane().setCenter(tabPane);
        ui.getSiteViewerBtn().setDisable(false);
        ui.getSavePortfolio().setDisable(false);
        ui.getSaveAsPortfolio().setDisable(false);
        ui.getExportPortfolio().setDisable(false);
    }

    public void handleNewEPortfolio() {
        this.pageList = new ArrayList();
        this.tabPane = new TabPane();
        this.tabPane.setTabMinWidth(150);
        this.tabPane.setTabMinHeight(20);
        this.ui.turnControlsOff(true);
        ui.getSiteViewerBtn().setDisable(true);
        saved = true;
    }

    public void addPage() {
        Page newPage = new Page(this);
        tabPane.getTabs().add(newPage);
        tabPane.getSelectionModel().select(newPage);
        setSelectedPage(newPage);
        pageList.add(newPage);
        this.ui.turnControlsOff(false);
        ui.getCenterPane().setCenter(tabPane);
        ui.getSiteViewerBtn().setDisable(false);
        ui.getSavePortfolio().setDisable(false);
        ui.getSaveAsPortfolio().setDisable(false);
        ui.getExportPortfolio().setDisable(false);
        saved = false;
    }

    public void removeSelectedPage() {
        tabPane.getTabs().remove(getSelectedPage());
        pageList.remove(getSelectedPage());
        setSelectedPage((Page) tabPane.getSelectionModel().getSelectedItem());
        if (getSelectedPage() == null) {
            ui.turnControlsOff(true);
            ui.getSiteViewerBtn().setDisable(true);
            ui.getSavePortfolio().setDisable(true);
            ui.getSaveAsPortfolio().setDisable(true);
            ui.getExportPortfolio().setDisable(true);
            saved = true;
        }
    }

    public void setSelectedPage(Page p) {
        this.selectedPage = p;
    }

    public Page getSelectedPage() {
        return selectedPage;
    }

    public List<Page> getPageList() {
        return pageList;
    }

    public UI getUI() {
        return ui;
    }

    public boolean getSaved() {
        return saved;
    }
    
    public void setSaved(boolean s) {
        this.saved = s;
    }

    public TabPane getTabPane() {
        return tabPane;
    }
    

}
