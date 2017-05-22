/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Page;

import static File.Constants.DEFAULT_TITLE;
import UI.PageEditor;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 *
 * @author eafernandez
 */
public class Page extends Tab {
    private List<Component> components;
    private FontComponent fontComponent;
    private TitleComponent titleComponent;
    private LayoutComponent layoutComponent;
    private NameComponent nameComponent;
    private ColorComponent colorComponent;

    private BannerComponent bannerComponent;
    private FooterComponent footerComponent;
    private VBox componentView;
    private PageEditor pageEditor;
    private ScrollPane scroll;
    private SuperComponent selectedComponent;

    public Page(PageEditor pageEditor) {
        this.pageEditor = pageEditor;
        setText(DEFAULT_TITLE);
        setClosable(false);
        tabHandler();
        this.titleComponent = new TitleComponent(this);
        this.fontComponent = new FontComponent();
        this.nameComponent = new NameComponent();
        this.layoutComponent = new LayoutComponent();
        this.colorComponent = new ColorComponent();
        this.bannerComponent = new BannerComponent();
        this.footerComponent = new FooterComponent();
        this.components = new ArrayList<Component>(200);
        this.componentView = new VBox();
        this.componentView.getChildren().addAll(titleComponent.defaultComponentView(), 
                                                fontComponent.defaultComponentView(), 
                                                nameComponent.defaultComponentView(),
                                                layoutComponent.defaultComponentView(), 
                                                colorComponent.defaultComponentView(), 
                                                bannerComponent.defaultComponentView(), 
                                                footerComponent.defaultComponentView());
        componentView.getStyleClass().add("main_scrollpane");
        scroll = new ScrollPane(componentView);
        setContent(scroll);
    }
    
    
    public Page(TitleComponent tc, FontComponent fc, NameComponent nc,LayoutComponent lc, ColorComponent cc, BannerComponent bc, FooterComponent foc) {
//        this.pageEditor = pe;
        setText(tc.getTitleField().getText());
        setClosable(false);
//        tabHandler();
        this.titleComponent = tc;
        this.fontComponent = fc;
        this.nameComponent = nc;
        this.layoutComponent = lc;
        this.colorComponent = cc;
        this.bannerComponent = bc;
        this.footerComponent = foc;
        this.components = new ArrayList<Component>(200);
        this.componentView = new VBox();
        this.componentView.getChildren().addAll(titleComponent.defaultComponentView(),
                                                fontComponent.defaultComponentView(),
                                                nameComponent.defaultComponentView(),
                                                layoutComponent.defaultComponentView(),
                                                colorComponent.defaultComponentView(),
                                                bannerComponent.defaultComponentView(),
                                                footerComponent.defaultComponentView()
                                                );
        componentView.getStyleClass().add("main_scrollpane");
        scroll = new ScrollPane(componentView);
        setContent(scroll);
        
    }
    
    public void setPageEditorHandler(PageEditor p) {
        this.pageEditor = p;
        tabHandler();
    }

    
    
    public void tabHandler() {
        setOnSelectionChanged(e -> {
            pageEditor.setSelectedPage(this);
//            System.out.println("SELECTED PAGE TITLE: " + pageEditor.getSelectedPage().getTitleComponent().getTitleField().getText());
        });
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public NameComponent getNameComponent() {
        return nameComponent;
    }

    public TitleComponent getTitleComponent() {
        return titleComponent;
    }
    
    public FontComponent getFontComponent() {
        return fontComponent;
    }

    public FooterComponent getFooterComponent() {
        return footerComponent;
    }
    
    public LayoutComponent getLayoutComponent() {
        return layoutComponent;
    }

    public void addComponent(Component component) {
        //////////////////////////////////////////////////
        //#######UNESECARRY KEEP FULL PROOFING  ########//
        //////////////////////////////////////////////////
//        this.pageEditor.setSaved(false);
//        this.pageEditor.getUI().getSavePortfolio().setDisable(false);
//        this.pageEditor.getUI().getSaveAsPortfolio().setDisable(false);
//        this.pageEditor.getUI().getExportPortfolio().setDisable(false);
        this.components.add(component);
        this.componentView.getChildren().add(component.componentView());
    }

    public VBox getComponentView() {
        return componentView;
    }

    public void setSelected(SuperComponent sc) {
        this.selectedComponent = sc;
    }

    public BannerComponent getBannerComponent() {
        return bannerComponent;
    }
    
    public ColorComponent getColorComponent() {
        return colorComponent;
    }

    public SuperComponent getSelected() {
        return selectedComponent;
    }

    public JsonObject createJsonPage() {
        JsonObject jsonObject = null;

        boolean containsBanner = true;
        if (containsBanner) {
            jsonObject = Json.createObjectBuilder()
                    .add("title", getTitleComponent().getTitleField().getText())
                    .add("name", getNameComponent().getNameField().getText())
                    .add("font",  fontComponent.getFontStr())
                    .add("layout", layoutComponent.getTemplateStr())
                    .add("color", colorComponent.getColorStr())
                    .add("banner", bannerComponent.makeJsonObject())
                    .add("footer", footerComponent.getFooterField().getText())
                    .add("components", jsonComponentArray())
                    .add("links", constructNavbarArray())
                    .build();
        } else {

        }
        return jsonObject;
    }

    public JsonArray jsonComponentArray() {
        JsonArray componentArray = null;
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for(Component component : components) {
            jsonArrayBuilder.add(component.makeJsonObject());
        }
        componentArray = jsonArrayBuilder.build();
        return componentArray;
    }
    
    public JsonArray constructNavbarArray() {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        JsonObject jsonObject = null;
        Page tempPage;
        for(int i = 0; i < pageEditor.getPageList().size(); i++) {
            tempPage = pageEditor.getPageList().get(i);
            if(tempPage == this) {
                jsonObject = Json.createObjectBuilder()
                    .add("src", tempPage.getTitleComponent().getTitleField().getText())
                    .add("type", "self").build();
            } else {
                jsonObject = Json.createObjectBuilder()
                    .add("src", tempPage.getTitleComponent().getTitleField().getText())
                    .add("type", "navbar").build();
            }
            jsonArrayBuilder.add(jsonObject);
        }
        JsonArray linkArray = jsonArrayBuilder.build();
        return linkArray;
    }
}