/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//Navigation Bar - a navigation bar provides a named link for each page in the site. 
//Navigation bars are commonly located across the top left portion of the site, or down the left side, 
//or even under the banner across the top middle. 
//Note that you'll need to provide layouts with all three of these navigation bar locations.
package UI;

import static File.Constants.ADD_IMG_COMP_IMAGEPATH;
import static File.Constants.ADD_IMG_COMP_MSG;
import static File.Constants.ADD_PAGE_COMP_MSG;
import static File.Constants.ADD_PAGE_IMAGEPATH;
import static File.Constants.ADD_SS_COMP_IMAGEPATH;
import static File.Constants.ADD_SS_COMP_MSG;
import static File.Constants.ADD_TEXT_COMP_IMAGEPATH;
import static File.Constants.ADD_TEXT_COMP_MSG;
import static File.Constants.ADD_VID_COMP_IMAGEPATH;
import static File.Constants.ADD_VID_COMP_MSG;
import static File.Constants.CENTER_PANE_CSS;
import static File.Constants.CONTROL_PANE_BTN_CSS;
import static File.Constants.CONTROL_PANE_BTN_HEIGHT;
import static File.Constants.CONTROL_PANE_BTN_WIDTH;
import static File.Constants.CONTROL_PANE_CSS;
import static File.Constants.EXIT_PORTFOLIO_IMAGEPATH;
import static File.Constants.EXIT_PORTFOLIO_TOOLTIP;
import static File.Constants.EXPORT_PORTFOLIO_IMAGEPATH;
import static File.Constants.EXPORT_PORTFOLIO_TOOLTIP;
import static File.Constants.FILE_TOOLBAR_BTN_CSS_CLASS;
import File.FileToolbarController;
import static File.Constants.FILE_TOOLBAR_BTN_HEIGHT;
import static File.Constants.FILE_TOOLBAR_BTN_WIDTH;
import static File.Constants.FILE_TOOLBAR_CSS;
import static File.Constants.LOAD_PORTFOLIO_IMAGEPATH;
import static File.Constants.LOAD_PORTFOLIO_TOOLTIP;
import static File.Constants.NEW_PORTFOLIO_IMAGEPATH;
import static File.Constants.NEW_PORTFOLIO_TOOLTIP;
import static File.Constants.REMOVE_PAGE_COMP_MSG;
import static File.Constants.REMOVE_PAGE_IMAGEPATH;
import static File.Constants.ROOT_PANE_CSS;
import static File.Constants.SAVE_AS_PORTFOLIO_IMAGEPATH;
import static File.Constants.SAVE_AS_PORTFOLIO_TOOLTIP;
import static File.Constants.SAVE_PORTFOLIO_IMAGEPATH;
import static File.Constants.SAVE_PORTFOLIO_TOOLTIP;
import static File.Constants.SITEVIEWER_IMAGEPATH;
import static File.Constants.SITEVIEWER_MSG;
import static File.Constants.TOP_PANE_CSS;
import static File.Constants.WORKSPACE_BTN_CSS;
import static File.Constants.WORKSPACE_IMAGEPATH;
import static File.Constants.WORKSPACE_MSG;
import static File.Constants.WORKSPACE_TOGGLE_CSS;
import File.WorkspaceEditorController;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author eafernandez
 */
public class UI {

    private Stage primaryStage;
    private BorderPane rootPane;

    // FILE TOOLBAR / Toogle
    private BorderPane topPane;
    // FILE TOOLBAR  
    private FileToolbarController fileToolbarController;
    private HBox fileToolbarHBox;
    private Button newPortfolio;
    private Button loadPortfolio;
    private Button savePortfolio;
    private Button saveAsPortfolio;
    private Button exportPortfolio;
    private Button exitPortfolio;
    private Button addPage;
    private Button removePage;
    private Button addHeader;
    private Button addList;

    // WorkSpace Toggle
    private HBox workSpaceHBox;
    private Button pageEditorBtn;
    private Button siteViewerBtn;

    // Center Pane
    private BorderPane centerPane;
    private VBox controlPane;

    // Control Pane
    private Button addText;
    private Button addImage;
    private Button addSlideshow;
    private Button addVideo;
    private Button addTextHyperlink;

    // Workspace Pane    
    private PageEditor pageEditor;

    // Initializes App
    public UI(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.rootPane = new BorderPane();
        this.rootPane.getStyleClass().add(ROOT_PANE_CSS);
        this.pageEditor = new PageEditor(this);

        initializeTopPane();
        initializeFileToolbarController();
        initializeCenterPane();
        initializeWorkspaceEditorController();
        initializeEmptyPortfolio();
    }

    // Initializes File Toolbar and WorkSpace Toggle when application is opened
    public void initializeTopPane() {

        // TOP PANE
        this.topPane = new BorderPane();
        this.topPane.getStyleClass().add(TOP_PANE_CSS);

        // FILE TOOLBAR
        this.fileToolbarHBox = new HBox();
        this.fileToolbarHBox.getStyleClass().add(FILE_TOOLBAR_CSS);
        this.newPortfolio = UIFactory.createButton(
                fileToolbarHBox,
                "New",
                NEW_PORTFOLIO_IMAGEPATH,
                FILE_TOOLBAR_BTN_CSS_CLASS,
                NEW_PORTFOLIO_TOOLTIP,
                false,
                FILE_TOOLBAR_BTN_WIDTH,
                FILE_TOOLBAR_BTN_HEIGHT);
        this.loadPortfolio = UIFactory.createButton(
                fileToolbarHBox,
                "Load",
                LOAD_PORTFOLIO_IMAGEPATH,
                FILE_TOOLBAR_BTN_CSS_CLASS,
                LOAD_PORTFOLIO_TOOLTIP,
                false,
                FILE_TOOLBAR_BTN_WIDTH,
                FILE_TOOLBAR_BTN_HEIGHT);
        this.savePortfolio = UIFactory.createButton(
                fileToolbarHBox,
                "Save",
                SAVE_PORTFOLIO_IMAGEPATH,
                FILE_TOOLBAR_BTN_CSS_CLASS,
                SAVE_PORTFOLIO_TOOLTIP,
                true,
                FILE_TOOLBAR_BTN_WIDTH,
                FILE_TOOLBAR_BTN_HEIGHT);
        this.saveAsPortfolio = UIFactory.createButton(
                fileToolbarHBox,
                "Save As",
                SAVE_AS_PORTFOLIO_IMAGEPATH,
                FILE_TOOLBAR_BTN_CSS_CLASS,
                SAVE_AS_PORTFOLIO_TOOLTIP,
                true,
                FILE_TOOLBAR_BTN_WIDTH,
                FILE_TOOLBAR_BTN_HEIGHT);
        this.exportPortfolio = UIFactory.createButton(
                fileToolbarHBox,
                "Export",
                EXPORT_PORTFOLIO_IMAGEPATH,
                FILE_TOOLBAR_BTN_CSS_CLASS,
                EXPORT_PORTFOLIO_TOOLTIP,
                true,
                FILE_TOOLBAR_BTN_WIDTH,
                FILE_TOOLBAR_BTN_HEIGHT);
        this.exitPortfolio = UIFactory.createButton(
                fileToolbarHBox,
                "Exit",
                EXIT_PORTFOLIO_IMAGEPATH,
                FILE_TOOLBAR_BTN_CSS_CLASS,
                EXIT_PORTFOLIO_TOOLTIP,
                false,
                FILE_TOOLBAR_BTN_WIDTH,
                FILE_TOOLBAR_BTN_HEIGHT);

        // Workspace Toggle
        this.workSpaceHBox = new HBox();
        this.workSpaceHBox.getStyleClass().add(WORKSPACE_TOGGLE_CSS);
        this.pageEditorBtn = UIFactory.createButton(workSpaceHBox,
                WORKSPACE_MSG,
                WORKSPACE_IMAGEPATH,
                WORKSPACE_BTN_CSS,
                WORKSPACE_MSG,
                true,
                70,
                40);
        this.siteViewerBtn = UIFactory.createButton(workSpaceHBox,
                SITEVIEWER_MSG,
                SITEVIEWER_IMAGEPATH,
                WORKSPACE_BTN_CSS,
                SITEVIEWER_MSG,
                true,
                70,
                40);

        topPane.setLeft(fileToolbarHBox);
        topPane.setRight(workSpaceHBox);
        rootPane.setTop(topPane);
    }

    // Initializes Controller for File Toolbar btn's
    public void initializeFileToolbarController() {
        this.fileToolbarController = new FileToolbarController(this);

        this.newPortfolio.setOnAction(e -> {
            fileToolbarController.handleNewPortfolioRequest();
        });
        this.loadPortfolio.setOnAction(e -> {
            fileToolbarController.handleLoadPortfolioRequest();
        });
        this.savePortfolio.setOnAction(e -> {
            fileToolbarController.handleSavePortfolioRequest();
        });
        this.saveAsPortfolio.setOnAction(e -> {
            fileToolbarController.handleSaveAsPortfolioRequest();
        });
        this.exportPortfolio.setOnAction(e -> {
            fileToolbarController.handleExportPortfolioRequest();
        });
        this.exitPortfolio.setOnAction(e -> {
            fileToolbarController.handleExitPortfolioRequest();
        });
        this.pageEditorBtn.setOnAction(e -> {
            fileToolbarController.handlePageEditorRequest();
        });
        this.siteViewerBtn.setOnAction(e -> {
            fileToolbarController.handleSiteViewerRequest();
        });
        
        this.primaryStage.setOnCloseRequest(e -> {
            fileToolbarController.handleExitPortfolioRequest();
        });
    }

    // Initializes UI components, does not display
    public void initializeCenterPane() {
        this.centerPane = new BorderPane();
        this.centerPane.getStyleClass().add(CENTER_PANE_CSS);

        this.controlPane = new VBox();
        this.controlPane.getStyleClass().add(CONTROL_PANE_CSS);

        this.addPage = UIFactory.createButton(controlPane, ADD_PAGE_COMP_MSG, ADD_PAGE_IMAGEPATH, CONTROL_PANE_BTN_CSS, ADD_TEXT_COMP_MSG, false, CONTROL_PANE_BTN_WIDTH, CONTROL_PANE_BTN_HEIGHT);
        this.removePage = UIFactory.createButton(controlPane, REMOVE_PAGE_COMP_MSG, REMOVE_PAGE_IMAGEPATH, CONTROL_PANE_BTN_CSS, ADD_TEXT_COMP_MSG, false, CONTROL_PANE_BTN_WIDTH, CONTROL_PANE_BTN_HEIGHT);
        this.addText = UIFactory.createButton(controlPane, "Add Paragrapgh", ADD_TEXT_COMP_IMAGEPATH, CONTROL_PANE_BTN_CSS, "Add Paragrapgh", false, CONTROL_PANE_BTN_WIDTH, CONTROL_PANE_BTN_HEIGHT);
        this.addHeader = UIFactory.createButton(controlPane, "Add Header", ADD_TEXT_COMP_IMAGEPATH, CONTROL_PANE_BTN_CSS, "Add Header", false, CONTROL_PANE_BTN_WIDTH, CONTROL_PANE_BTN_HEIGHT);
        this.addList = UIFactory.createButton(controlPane, "Add List", ADD_TEXT_COMP_IMAGEPATH, CONTROL_PANE_BTN_CSS, "Add List", false, CONTROL_PANE_BTN_WIDTH, CONTROL_PANE_BTN_HEIGHT);
        this.addImage = UIFactory.createButton(controlPane, ADD_IMG_COMP_MSG, ADD_IMG_COMP_IMAGEPATH, CONTROL_PANE_BTN_CSS, ADD_IMG_COMP_MSG, false, CONTROL_PANE_BTN_WIDTH, CONTROL_PANE_BTN_HEIGHT);
        this.addSlideshow = UIFactory.createButton(controlPane, ADD_SS_COMP_MSG, ADD_SS_COMP_IMAGEPATH, CONTROL_PANE_BTN_CSS, ADD_SS_COMP_MSG, false, CONTROL_PANE_BTN_WIDTH, CONTROL_PANE_BTN_HEIGHT);
        this.addVideo = UIFactory.createButton(controlPane, ADD_VID_COMP_MSG, ADD_VID_COMP_IMAGEPATH, CONTROL_PANE_BTN_CSS, ADD_VID_COMP_MSG, false, CONTROL_PANE_BTN_WIDTH, CONTROL_PANE_BTN_HEIGHT);
//        this.addTextHyperlink= UIFactory.createButton(controlPane,ADD_HL_MSG,ADD_HL_IMAGEPATH,CONTROL_PANE_BTN_CSS,ADD_HL_MSG,false,CONTROL_PANE_BTN_WIDTH,CONTROL_PANE_BTN_HEIGHT);

        this.addPage.setPrefWidth(200);
        this.removePage.setPrefWidth(200);
        this.addText.setPrefWidth(200);
        this.addHeader.setPrefWidth(200);
        this.addList.setPrefWidth(200);
        this.addImage.setPrefWidth(200);
        this.addSlideshow.setPrefWidth(200);
        this.addVideo.setPrefWidth(200);
//        this.addTextHyperlink.setPrefWidth(200);

        this.centerPane.setRight(controlPane);
    }

    public void initializeWorkspaceEditorController() {
        WorkspaceEditorController workspaceEditorController = new WorkspaceEditorController(this);
        this.addPage.setOnAction(e -> {
            workspaceEditorController.handleAddPage();
        });
        this.removePage.setOnAction(e -> {
            workspaceEditorController.handleRemovePage();
        });
        this.addText.setOnAction(e -> {
            workspaceEditorController.handleAddParagraghComponent();
        });
        this.addImage.setOnAction(e -> {
            workspaceEditorController.handleAddImageComponent();
        });
        this.addVideo.setOnAction(e -> {
            workspaceEditorController.handleAddVideoComponent();
        });
        this.addSlideshow.setOnAction(e -> {
            workspaceEditorController.handleAddSlideShowComponent();
        });
        this.addHeader.setOnAction(e -> {
            workspaceEditorController.handleAddHeader();
        });
        this.addList.setOnAction(e -> {
            workspaceEditorController.handleAddList();
        });
    }

    // Initialize when Application opens
    public void initializeEmptyPortfolio() {
        StackPane emptyStackPane = new StackPane();
        Label emptyLabel = new Label("EMPTY PORTFOLIO");
        emptyStackPane.getStyleClass().add("empty_portfolio_stackpane");
        emptyLabel.getStyleClass().add("empty_portfolio_label");
        emptyStackPane.getChildren().add(emptyLabel);
        this.rootPane.setCenter(emptyStackPane);
    }

    // New Portfolio Created
    public void newPortfolio() {
        initializeCenterPane();
        initializeWorkspaceEditorController();
        initializeEmptyPortfolio();
        this.rootPane.setCenter(centerPane);
        this.siteViewerBtn.setDisable(false);
    }

    public void turnControlsOff(boolean turnOff) {
        this.addText.setDisable(turnOff);
        this.addImage.setDisable(turnOff);
        this.addSlideshow.setDisable(turnOff);
        this.addVideo.setDisable(turnOff);
        this.removePage.setDisable(turnOff);
        this.addHeader.setDisable(turnOff);
        this.addList.setDisable(turnOff);
    }

    // Grab rootPane, entire UI
    public BorderPane getRootPane() {
        return rootPane;
    }

    public BorderPane getCenterPane() {
        return centerPane;
    }

    // Grabs PageEditor -> which consists list of Pages with details
    public PageEditor getPageEditor() {
        return pageEditor;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPageEditor(PageEditor p) {
        this.pageEditor = p;
    }

    public Button getNewPortfolio() {
        return newPortfolio;
    }

    public Button getLoadPortfolio() {
        return loadPortfolio;
    }

    public Button getSavePortfolio() {
        return savePortfolio;
    }

    public Button getSaveAsPortfolio() {
        return saveAsPortfolio;
    }

    public Button getExportPortfolio() {
        return exportPortfolio;
    }

    public Button getExitPortfolio() {
        return exitPortfolio;
    }

    public Button getPageEditorBtn() {
        return pageEditorBtn;
    }

    public Button getSiteViewerBtn() {
        return siteViewerBtn;
    }

    public Button getAddPage() {
        return addPage;
    }

    public Button getAddHeader() {
        return addHeader;
    }

    public Button getAddList() {
        return addList;
    }

    public Button getAddText() {
        return addText;
    }

    public Button getAddImage() {
        return addImage;
    }

    public Button getAddSlideshow() {
        return addSlideshow;
    }

    public Button getAddVideo() {
        return addVideo;
    }

    public Button getRemovePage() {
        return removePage;
    }
    
    
    
}
