/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package File;

/**
 *
 * @author eafernandez
 */
public class Constants {
    
    // Main Class ~ Font/Title/Stylesheets
    public static String UI_STYLESHEET = "./CSS/ui_stylesheet.css";
    public static String GOOGLE_FONT = "https://fonts.googleapis.com/css?family=Fjalla+One";
    public static String UI_TITLE = "E-profolio Generator";
    
    // File Toolbar
    public static double FILE_TOOLBAR_BTN_WIDTH = 70;
    public static double FILE_TOOLBAR_BTN_HEIGHT = 40;
    public static String NEW_PORTFOLIO_TOOLTIP = "Create a New E-Portfolio";
    public static String LOAD_PORTFOLIO_TOOLTIP = "Load an Existing E-Portfolio";
    public static String SAVE_PORTFOLIO_TOOLTIP = "Save Current E-Portfolio";
    public static String SAVE_AS_PORTFOLIO_TOOLTIP = "Save As E-Portfolio";
    public static String EXPORT_PORTFOLIO_TOOLTIP = "Export E-Portfolio";
    public static String EXIT_PORTFOLIO_TOOLTIP = "Exit E-Portfolio";
    
    // Workspace toggle 
    public static String WORKSPACE_MSG  = "Workspace";
    public static String SITEVIEWER_MSG = "SiteViewer";

    // Control Pane
    public static String ADD_PAGE_COMP_MSG = "Add Page";
    public static String REMOVE_PAGE_COMP_MSG = "Remove Page";
    public static String ADD_TEXT_COMP_MSG = "Add Text";
    public static String ADD_IMG_COMP_MSG = "Add Image";
    public static String ADD_SS_COMP_MSG = "Add Slide Show";
    public static String ADD_VID_COMP_MSG = "Add Video";
    public static String ADD_HL_MSG = "Add Hyperlink";
    public static String ADD_SLIDE_MSG = "Add Slide";
    public static String REMOVE_SLIDE_MSG = "Remove Slide";
    public static String MOVE_SLIDE_UP_MSG = "Move Slide Up";
    public static String MOVE_SLIDE_DOWN_MSG = "Move Slide Down";
    public static double CONTROL_PANE_BTN_WIDTH = 100;
    public static double CONTROL_PANE_BTN_HEIGHT = 40;

    // CONTENT_PANE AND ACTUAL COMPONENTS ONCE ADDED
    public static double CONTENT_PANE_WIDTH = 1576;//1287;
    public static double CONTENT_PANE_HEIGHT = 230;
    
    // Title Component
    public static String TITLEELABEL = "Title: "; 

    // Textual Components - In General
    public static String FONTFAMILYLABEL = "Font-Family: ";
    public static String FONTSTYLELABEL = "Font-Style: ";
    public static String FONTSIZELABEL = "Font-Size: ";

    // Image Component - In General
    public static final String CAPTION_PROMPT = "Caption: ";
    public static final String WIDTH_PROMPT = "Width: ";
    public static final String HEIGHT_PROMPT = "Height: ";
    public static final String FLOAT_PROMPT = "Float Type: ";
    public static double IMAGE_WIDTH_FX = 220; 
    public static double IMAGE_HEIGHT_FX = 150;

    // Image Component (Specific)
    public static final String DEFAULT_IMAGE_CAPTION = "Caption";
    public static final String DEFAULT_IMAGE_WIDTH = "250";
    public static final String DEFAULT_IMAGE_HEIGHT = "200";
    public static final String DEFAULT_IMAGE_FLOATTYPE = "none";
    
    // Video Components
    public static final String VIDEO_PROMPT = "Video Source:";
    public static final String DEFAULT_VIDEO_SRC = "vids//samplevid.mp4";
    public static final String DEFAULT_VIDEO_CAPTION = "Caption";
    public static final String DEFAULT_VIDEO_WIDTH = "250";
    public static final String DEFAULT_VIDEO_HEIGHT = "200";
    
    // Header Component
    public static final String DEFAULT_HEADER = "New Header Component";
    
    // Layout Component
    public static final String LAYOUT_LABEL_ONE = "Template 1";
    public static final String LAYOUT_LABEL_TWO = "Template 2";
    public static final String LAYOUT_LABEL_THREE = "Template 3";
    public static final String LAYOUT_LABEL_FOUR = "Template 4";
    public static final String LAYOUT_LABEL_FIVE = "Template 5";
    
        
    // CSS Page Editor Pane List
    public static String PAGE_EDITOR_PANE_LIST_CSS = "page_editor_pane_list";
    
    // CSS Classes
    public static String ROOT_PANE_CSS = "root_pane";
    // TOP PANE
    public static String TOP_PANE_CSS = "top_pane";
    // Workspace HBOX
    public static String WORKSPACE_TOGGLE_CSS = "workspace_toggle";
    // File Toolbar
    public static String FILE_TOOLBAR_BTN_CSS_CLASS = "file_toolbar_btn";
    public static String FILE_TOOLBAR_CSS = "file_toolbar";
    public static String WORKSPACE_BTN_CSS = "workspace_toggle_btn";
    // Center Pane
    public static String CENTER_PANE_CSS = "center_pane";
    public static String CONTROL_PANE_CSS = "control_pane";
    // Control Pane
    public static String CONTROL_PANE_BTN_CSS = "control_pane_btn";
    // Add Image Component 
    public final static String ADD_IMAGE_COMPONENT_PANE_CSS            = "add_image_component_pane";
    public final static String ADD_IMAGE_COMPONENT_PANE_LEFT_VBOX_CSS  = "add_image_component_left_vbox";
    public final static String ADD_IMAGE_COMPONENT_PANE_RIGHT_VBOX_CSS = "add_image_component_right_vbox";
    // Add Video Component
    public final static String ADD_VIDEO_COMPONENT_PANE_CSS = "add_video_component_pane";
    public final static String ADD_VIDEO_COMPONENT_PANE_LEFT_GRIDPANE_CSS = "add_video_component_left_gridpane";
    public final static String ADD_VIDEO_COMPONENT_PANE_RIGHT_VBOX_CSS = "add_video_component_right_vbox";
    // Update Title
    public final static String UPDATE_TITLE_HEADER_LABEL_CSS = "updateTitleHeaderLabel";
    public final static String UPDATE_TITLE_PANE_CSS         = "updateTitlePane";
    public final static String DEFAULT_TITLE  = "Title";
    // Update Name
    public final static String UPDATE_NAME_HEADER_LABEL_CSS = "updateNameHeaderLabel";
    public final static String UPDATE_NAME_PANE_CSS         = "updateNamePane";
    public final static String DEFAULT_NAME   = "Name";
    // Update Footer
    public final static String UPDATE_FOOTER_HEADER_LABEL_CSS = "updateFooterHeaderLabel";
    public final static String UPDATE_FOOTER_PANE_CSS         = "updateFooterPane";
    public final static String DEFAULT_FOOTER = "Footer";
    // Header Component
    public final static String ADD_HEADER_LABEL_CSS = "addHeaderLabel";
    public final static String ADD_HEADER_PANE_CSS  = "addHeaderPane";
    // Banner Component
    public final static String BANNER_PANE_CSS  = "banner_pane";
    // Layout Component
    public final static String LAYOUT_COMPONENT_PANE_CSS = "layout_component_pane";
    public final static String LAYOUT_COMPONENT_BUTTONPANE_CSS = "layout_component_buttonpane";
    // Slide Show - Slide Component
    public static String SLIDESHOW_SLIDE_PANE_CSS = "slideshow_slide_pane";
    public static String SLIDESHOW_SLIDE_IMAGE_ATTR_CSS = "slideshow_slide_image_attr";
    public static String SLIDESHOW_CSS = "slide";
    
    
    // Remove Component
    public static String REMOVE_BUTTON_MSG = "Remove";
    public static String REMOVE_COMPONENT_CSS = "remove_component";
    public static double REMOVE_BUTTON_WIDTH = 100;
    public static double REMOVE_BUTTON_HEIGHT = 40;
    
    /* Images */
    public static final String IMG_PATH = "file:./imgs/";
    public static final String ICON_IMG_PATH = IMG_PATH + "icon.jpg";
    public static final String NEW_PORTFOLIO_IMAGEPATH     = IMG_PATH + "new-portfolio.png";
    public static final String LOAD_PORTFOLIO_IMAGEPATH    = IMG_PATH + "load-portfolio.png";
    public static final String SAVE_PORTFOLIO_IMAGEPATH    = IMG_PATH + "save-portfolio.png";
    public static final String SAVE_AS_PORTFOLIO_IMAGEPATH = IMG_PATH + "save-as-portfolio.png";
    public static final String EXPORT_PORTFOLIO_IMAGEPATH  = IMG_PATH + "export-portfolio.png";
    public static final String EXIT_PORTFOLIO_IMAGEPATH    = IMG_PATH + "exit-portfolio.png";
    public static final String WORKSPACE_IMAGEPATH  = IMG_PATH + "workspace.png";
    public static final String SITEVIEWER_IMAGEPATH = IMG_PATH + "siteviewer.png";    
    public static final String ADD_TEXT_COMP_IMAGEPATH = IMG_PATH + "addtext.png";
    public static final String ADD_IMG_COMP_IMAGEPATH = IMG_PATH + "addimage.png";
    public static final String ADD_SS_COMP_IMAGEPATH = IMG_PATH + "addslideshow.png";
    public static final String ADD_VID_COMP_IMAGEPATH = IMG_PATH + "addvideo.png";
    public static final String ADD_HL_IMAGEPATH = IMG_PATH + "addhyperlink.png";
    public static final String ADD_SLIDE_IMAGEPATH = IMG_PATH + "add.png";
    public static final String REMOVE_SLIDE_IMAGEPATH = IMG_PATH + "Remove.png";
    public static final String MOVE_SLIDE_UP_IMAGEPATH = IMG_PATH + "MoveUp.png";
    public static final String MOVE_SLIDE_DOWN_IMAGEPATH = IMG_PATH + "MoveDown.png";
    public static final String DEFAULT_IMAGE_SRC =  "./imgs/default_ss.png";
    public static final String DEFAULT_BANNER_SRC =  "./imgs/banner_def.jpg";
    public static final String TEMPLATE_ONE_IMAGEPATH = IMG_PATH + "template1.png";
    public static final String TEMPLATE_TWO_IMAGEPATH = IMG_PATH + "template2.png";
    public static final String TEMPLATE_THREE_IMAGEPATH = IMG_PATH + "template3.png";
    public static final String TEMPLATE_FOUR_IMAGEPATH = IMG_PATH + "template4.png";
    public static final String TEMPLATE_FIVE_IMAGEPATH = IMG_PATH + "template5.png";
    public static final String ADD_PAGE_IMAGEPATH = IMG_PATH + "add.png";
    public static final String REMOVE_PAGE_IMAGEPATH = IMG_PATH + "no.png";


}
