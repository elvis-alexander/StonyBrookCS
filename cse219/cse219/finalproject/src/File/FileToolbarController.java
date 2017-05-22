/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package File;

import UI.SiteViewer;
import UI.UI;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

/**
 *
 * @author eafernandez
 */
public class FileToolbarController {

    private UI ui;

    public FileToolbarController(UI ui) {
        this.ui = ui;
    }

    public void handleNewPortfolioRequest() {
        if (ui.getPageEditor().getSaved() == false) {
            promptToSave();
        }
        ui.newPortfolio();
        ui.getPageEditor().handleNewEPortfolio();
    }

    public void promptToSave() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Save");
        alert.setHeaderText("Would you like to save?");
        alert.setContentText("Would you like to save?");
        ButtonType buttonTypeOne = new ButtonType("Yes");
        ButtonType buttonTypeTwo = new ButtonType("No");
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne) {
            EPortfolioFileManager ePortfolioFileManager = new EPortfolioFileManager();
            ePortfolioFileManager.savePortfolio(ui);
            Alert savedAlert = new Alert(AlertType.INFORMATION);
            savedAlert.setTitle("Information Dialog");
            savedAlert.setHeaderText("Saved!");
            savedAlert.setContentText("Saved!");
            savedAlert.showAndWait();
        }
    }

    public void handleLoadPortfolioRequest() {
        String initialDir = System.getProperty("user.home");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./data"));
        FileChooser.ExtensionFilter json = new FileChooser.ExtensionFilter("json files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(json);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            ui.newPortfolio();
            String path = file.getAbsolutePath();
            EPortfolioFileManager ePortfolioFileManager = new EPortfolioFileManager();
            ePortfolioFileManager.loadPortfolio(ui, path);
        }
    }

    public void handleSavePortfolioRequest() {
        EPortfolioFileManager ePortfolioFileManager = new EPortfolioFileManager();
        ePortfolioFileManager.savePortfolio(ui);
        saveAlert();
    }

    public void handleSaveAsPortfolioRequest() {
        String initialDir = System.getProperty("user.home");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(initialDir));
        FileChooser.ExtensionFilter json = new FileChooser.ExtensionFilter("json files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(json);
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            EPortfolioFileManager ePortfolioFileManager = new EPortfolioFileManager();
            ePortfolioFileManager.saveAsPortfolio(ui, file.getAbsolutePath());
            saveAlert();
        }
    }

    public void saveAlert() {
        Alert savedAlert = new Alert(AlertType.INFORMATION);
        savedAlert.setTitle("Information Dialog");
        savedAlert.setHeaderText("Saved!");
        savedAlert.setContentText("Saved!");
        savedAlert.showAndWait();
    }

    public void handleExportPortfolioRequest() {
        try {
            FileExporter fileExporter = new FileExporter(ui);
            fileExporter.exportSite();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleExitPortfolioRequest() {
        if (ui.getPageEditor().getSaved() == false) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Save");
            alert.setHeaderText("Would you like to save?");
            alert.setContentText("Would you like to save?");
            ButtonType buttonTypeOne = new ButtonType("Yes");
            ButtonType buttonTypeTwo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOne) {
                exitSave();
            } else if (result.get() == buttonTypeTwo) {
                ui.getPrimaryStage().close();
            }
        } else {
            ui.getPrimaryStage().close();
        }
    }

    public void exitSave() {

    }

    public void handleSiteViewerRequest() {
        try {
            System.out.println("TRIGGER");
            ui.getSiteViewerBtn().setDisable(true);
            ui.getPageEditorBtn().setDisable(false);
            disableFileToolbar(true);
            SiteViewer siteViewer = new SiteViewer(ui);
            siteViewer.viewSite();
        } catch (Exception ex) {
            ex.printStackTrace();
//            System.out.println("SITE VIEWER ERROR");
        }
    }

    public void handlePageEditorRequest() {
        ui.getPageEditorBtn().setDisable(true);
        ui.getSiteViewerBtn().setDisable(false);
        disableFileToolbar(false);
        ui.getCenterPane().setCenter(ui.getPageEditor().getTabPane());
    }

    public void disableFileToolbar(boolean turnOff) {
        this.ui.getNewPortfolio().setDisable(turnOff);
        this.ui.getLoadPortfolio().setDisable(turnOff);
        this.ui.getSavePortfolio().setDisable(turnOff);
        this.ui.getSaveAsPortfolio().setDisable(turnOff);
        this.ui.getExportPortfolio().setDisable(turnOff);
        this.ui.getExitPortfolio().setDisable(turnOff);
        this.ui.getAddPage().setDisable(turnOff);
        this.ui.getRemovePage().setDisable(turnOff);
        this.ui.getAddText().setDisable(turnOff);
        this.ui.getAddHeader().setDisable(turnOff);
        this.ui.getAddList().setDisable(turnOff);
        this.ui.getAddImage().setDisable(turnOff);
        this.ui.getAddVideo().setDisable(turnOff);
        this.ui.getAddSlideshow().setDisable(turnOff);
    }

}
