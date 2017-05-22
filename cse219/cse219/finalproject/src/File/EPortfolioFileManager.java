/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package File;

import static File.Constants.DEFAULT_VIDEO_SRC;
import Page.BannerComponent;
import Page.ColorComponent;
import Page.Component;
import Page.FontComponent;
import Page.FooterComponent;
import Page.HeaderComponent;
import Page.ImageComponent;
import Page.LayoutComponent;
import Page.ListComponent;
import Page.ListItem;
import Page.NameComponent;
import Page.Page;
import Page.ParagraphComponent;
import Page.Slide;
import Page.SlideShowComponent;
import Page.TitleComponent;
import Page.VideoComponent;
import UI.PageEditor;
import UI.UI;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

/**
 *
 * @author eafernandez
 */
public class EPortfolioFileManager {

    public static String SLASH = "/";
    public static String JSON_EXT = ".json";
    public static String DATA_FOL = "./data";
    
    public void saveAsPortfolio(UI ui, String file) {
        StringWriter sw = new StringWriter();
        PageEditor pageEditor = ui.getPageEditor();
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (int i = 0; i < pageEditor.getPageList().size(); i++) {//Page page : pageEditor.getPageList()) {
            System.out.println("PAGELIST SIZE: " + pageEditor.getPageList());
            JsonObject jsonObject = pageEditor.getPageList().get(i).createJsonPage();
            jsonArrayBuilder.add(jsonObject);
        }
        JsonArray jsonArray = jsonArrayBuilder.build();
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeArray(jsonArray);
        jsonWriter.close();
        String jsonFilePath = file;

        try {
            OutputStream outputStream = new FileOutputStream(jsonFilePath);
            JsonWriter jsonFileWriter = Json.createWriter(outputStream);
            jsonFileWriter.writeArray(jsonArray);
            String prettyPrinted = sw.toString();
            PrintWriter pw = new PrintWriter(jsonFilePath);
            pw.write(prettyPrinted);
            pw.close();
            System.out.println(prettyPrinted);
        } catch (FileNotFoundException ex) {
            System.out.println("Save Error");
        }
    }
    

    public void savePortfolio(UI ui) {
        StringWriter sw = new StringWriter();
        PageEditor pageEditor = ui.getPageEditor();
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for (int i = 0; i < pageEditor.getPageList().size(); i++) {//Page page : pageEditor.getPageList()) {
            System.out.println("PAGELIST SIZE: " + pageEditor.getPageList());
            JsonObject jsonObject = pageEditor.getPageList().get(i).createJsonPage();
            jsonArrayBuilder.add(jsonObject);
        }
        JsonArray jsonArray = jsonArrayBuilder.build();
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);

        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeArray(jsonArray);
        jsonWriter.close();
        
        String path = pageEditor.getPageList().get(0).getNameComponent().getNameField().getText();
        String jsonFilePath = DATA_FOL + SLASH + path + JSON_EXT;

        try {
            OutputStream outputStream = new FileOutputStream(jsonFilePath);
            JsonWriter jsonFileWriter = Json.createWriter(outputStream);
            jsonFileWriter.writeArray(jsonArray);
            String prettyPrinted = sw.toString();
            PrintWriter pw = new PrintWriter(jsonFilePath);
            pw.write(prettyPrinted);
            pw.close();
            System.out.println(prettyPrinted);
        } catch (FileNotFoundException ex) {
            System.out.println("Save Error");
        }
    }

    public void loadPortfolio(UI ui, String path) {
        List<Page> pageList = new ArrayList<Page>();
        try {
            JsonArray jArray = loadJSONFile(path);
            for (int i = 0; i < jArray.size(); i++) {
                JsonObject jObject = jArray.getJsonObject(i);
                TitleComponent tc = new TitleComponent(jObject.getString("title"));
                FontComponent fc = new FontComponent(jObject.getString("font"));
                NameComponent nc = new NameComponent(jObject.getString("name"));
                LayoutComponent lc = new LayoutComponent(jObject.getString("layout"));
                ColorComponent cc = new ColorComponent(jObject.getString("color"));
                JsonObject jsonBanner = jObject.getJsonObject("banner");
                BannerComponent bc = createBannerComponent(jsonBanner);
                FooterComponent foc = new FooterComponent(jObject.getString("footer"));
                Page newPage = new Page(tc, fc, nc, lc,cc, bc, foc);
                tc.setPage(newPage);
                JsonArray components = jObject.getJsonArray("components");
                createComponents(newPage, components);
                pageList.add(newPage);
            }
            PageEditor pageEditor = new PageEditor(ui, pageList);
            ui.setPageEditor(pageEditor);
            for(int i = 0; i < pageEditor.getPageList().size(); i++) {
                pageEditor.getPageList().get(i).setPageEditorHandler(pageEditor);
            }

        } catch (IOException ex) {
            System.out.println("LOAD ERROR");
        }
    }
    
    private JsonArray loadJSONFile(String jsonFilePath) throws IOException {
        InputStream inputStream = new FileInputStream(jsonFilePath);
        JsonReader jsonReader = Json.createReader(inputStream);
        JsonArray jsonObject = jsonReader.readArray();
        jsonReader.close();
        inputStream.close();
        return jsonObject;
    }
    
    private BannerComponent createBannerComponent(JsonObject jObject) {
        BannerComponent bc = new BannerComponent(jObject.getString("src"), jObject.getString("include"), jObject.getString("coolpath"));
        return bc;
    }

    private void createComponents(Page page, JsonArray components) {
        List<Component> componentList = new ArrayList<>();
        for (int i = 0; i < components.size(); i++) {
            JsonObject jObject = components.getJsonObject(i);
            Component newComponent;

            if (jObject.getString("type").equals("header")) {
                page.addComponent(createHeaderComponent(page, jObject));
            } else if (jObject.getString("type").equals("image")) {
                page.addComponent(createImageComponent(page, jObject));
            } else if (jObject.getString("type").equals("paragraph")) {
                page.addComponent(createParagraphComponent(page, jObject));
            } else if (jObject.getString("type").equals("video")) {
                page.addComponent(createVideoComponent(page, jObject));
            } else if (jObject.getString("type").equals("slideshow")) {
                page.addComponent(createSlideShowComponent(page, jObject));
            } else if(jObject.getString("type").equals("list")) {
                page.addComponent(createListComponent(page, jObject));
            }
        }
    }

    private ListComponent createListComponent(Page page, JsonObject jObject) {
        List<ListItem> newList = new ArrayList<ListItem>();
        JsonArray listObj = jObject.getJsonArray("listitems");
        for(int i = 0; i < listObj.size(); i++) {
            JsonObject listItemObj = listObj.getJsonObject(i);
            String caption = listItemObj.getString("caption");
            ListItem newItem = new ListItem(caption);
            newList.add(newItem);
        }   
        String font = jObject.getString("font");
        ListComponent listComponent = new ListComponent(page, newList, font);
        return listComponent;
    }
    
    
    private Component createSlideShowComponent(Page page, JsonObject slideShowObj) {
        List<Slide> newSlideShow = new ArrayList<Slide>();
        JsonArray slideListObj = slideShowObj.getJsonArray("slidelist");
        for(int i = 0; i < slideListObj.size(); i++) {
            JsonObject slideJsonObj = slideListObj.getJsonObject(i);
            String src = slideJsonObj.getString("src");
            String caption = slideJsonObj.getString("caption");
            String width = slideJsonObj.getString("width");
            String height = slideJsonObj.getString("height");
            String coolPath = slideJsonObj.getString("coolpath");
            Slide newSlide = new Slide(src, caption, width, height, coolPath);
            newSlideShow.add(newSlide);
        }
        SlideShowComponent slideShowComponent = new SlideShowComponent(page, newSlideShow);
        return slideShowComponent;
    }
    
    private HeaderComponent createHeaderComponent(Page page, JsonObject jObject) {
        String header = jObject.getString("text");
        String font = jObject.getString("font");
        HeaderComponent newComponent = new HeaderComponent(page, header, font);
        return newComponent;
    }

    private ImageComponent createImageComponent(Page page, JsonObject jObject) {
        String src = jObject.getString("src");
        String caption = jObject.getString("caption");
        String width = jObject.getString("width");
        String height = jObject.getString("height");
        String floatType = jObject.getString("float");
        String coolPath = jObject.getString("coolpath");
        ImageComponent imageComponent = new ImageComponent(page, src, caption, width, height, floatType, coolPath);
        return imageComponent;
    }

    private ParagraphComponent createParagraphComponent(Page page, JsonObject jObject) {
        String text = jObject.getString("text");
        String font = jObject.getString("font");
        ParagraphComponent paragraphComponent = new ParagraphComponent(page, text, font);
        return paragraphComponent;
    }

    private VideoComponent createVideoComponent(Page page, JsonObject jObject) {
        String src = jObject.getString("src");
        String caption = jObject.getString("caption");
        String width = jObject.getString("width");
        String height = jObject.getString("height");
        String coolPath = jObject.getString("coolpath");
        VideoComponent videoComponent = new VideoComponent(page, src, caption, width, height, coolPath);
        return videoComponent;
    }
    
}