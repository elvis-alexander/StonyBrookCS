/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package UI;

import static File.EPortfolioFileManager.JSON_EXT;
import static File.EPortfolioFileManager.SLASH;
import static File.FileExporter.BASE_DIR;
import static File.FileExporter.CSS_DIR;
import static File.FileExporter.DATA_DIR;
import static File.FileExporter.HTML_EXT;
import static File.FileExporter.IMG_DIR;
import static File.FileExporter.JS_DIR;
import static File.FileExporter.VID_DIR;
import Page.Component;
import Page.ImageComponent;
import Page.Page;
import Page.Slide;
import Page.SlideShowComponent;
import Page.VideoComponent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

/**
 *
 * @author eafernandez
 */
public class SiteViewer {
    private UI ui;
    private WebView browser;
    private WebEngine webEngine;
    private StackPane stackPane;
    
    public SiteViewer(UI u) throws IOException {
        this.ui = u;
        this.browser = new WebView();
        this.webEngine = browser.getEngine();
        File file = new File(exportSite());
        String str = file.getAbsolutePath();
        this.webEngine.load("file:" + str);
        this.stackPane = new StackPane(browser);
    }
    
    public void viewSite() {
        ui.getCenterPane().setCenter(stackPane);
        
    }

    public String exportSite() throws IOException {
        List<Page> pageList =  ui.getPageEditor().getPageList();
        String sitePath = "siteview/" + pageList.get(0).getNameComponent().getNameField().getText() + SLASH;
        File siteDir = new File(sitePath);
        if (siteDir.exists()) {
            deleteDir(siteDir);
        }
        siteDir.mkdir();
        new File(sitePath + CSS_DIR).mkdir();
        new File(sitePath + JS_DIR).mkdir();
        new File(sitePath + IMG_DIR).mkdir();
        new File(sitePath + DATA_DIR).mkdir();
        new File(sitePath + VID_DIR).mkdir();

        copyJSFile(sitePath);
        copyFontFile(sitePath);
        constructCssDocument(sitePath);

        for (int i = 0; i < pageList.size(); i++) {
            constructDocument(pageList.get(i), sitePath);
            constructColorFiles(pageList.get(i), sitePath);
            copyImageFiles(pageList.get(i), sitePath);
            constructJsonFiles(pageList.get(i), sitePath);
        }
        return sitePath + pageList.get(0).getTitleComponent().getTitleField().getText() + ".html";
    }

    
    
    
    private void constructCssDocument(String sitePath) throws IOException {
        Path srcCss = new File(BASE_DIR + CSS_DIR + "template1.css").toPath();
        Path destCss = new File(sitePath + CSS_DIR + "template1.css").toPath();
        Files.copy(srcCss, destCss);
        
                
        Path srcCss2  = new File(BASE_DIR + CSS_DIR + "template3.css").toPath();
        Path destCss2 = new File(sitePath + CSS_DIR + "template3.css").toPath();
        Files.copy(srcCss2, destCss2);
    }

    private void copyImageFiles(Page page, String sitePath) throws FileNotFoundException, IOException {
        String banner = page.getBannerComponent().getBannerSrc();
        Path srcBanner = new File(banner).toPath();
        File file = new File(sitePath + IMG_DIR + srcBanner.getFileName());
        if (!file.exists()) {
            Path destBanner = new File(sitePath + IMG_DIR + srcBanner.getFileName()).toPath();
            Files.copy(srcBanner, destBanner);
        }

        for (int i = 0; i < page.getComponents().size(); i++) {
            Component component = page.getComponents().get(i);
            if (component.getType().equals("image")) {
                copyFromImageComponent(sitePath, (ImageComponent) page.getComponents().get(i));
            } else if (component.getType().equals("slideshow")) {
                copyFromSlideShowComponent(sitePath, (SlideShowComponent) page.getComponents().get(i));
            } else if (component.getType().equals("video")) {
                constructVideoFiles(sitePath, (VideoComponent) page.getComponents().get(i));
            }
        }
    }

    public void copyFromImageComponent(String sitePath, ImageComponent imageComponent) throws IOException {
        String imageName = imageComponent.getImageSource();
        Path srcImage = new File(imageName).toPath();
        File file = new File(sitePath + IMG_DIR + srcImage.getFileName());
        if (!file.exists()) {
            Path destImage = new File(sitePath + IMG_DIR + srcImage.getFileName()).toPath();
            Files.copy(srcImage, destImage);
        }
    }
    public void copyFromSlideShowComponent(String sitePath, SlideShowComponent slideShowComponent) {
        ///////////////////////////////////////////////////////////////////////
        ////#######################  INCLUDE HERE ###################//////////
        ///////////////////////////////////////////////////////////////////////
        for(int i = 0; i < slideShowComponent.getSlideList().getSlides().size(); i++) {
            Slide slide = slideShowComponent.getSlideList().getSlides().get(i);
            String imageName = slide.getImageSrc();
            Path srcImage = new File(imageName).toPath();
            File file = new File(sitePath + IMG_DIR + srcImage.getFileName());
            if(!file.exists()) {
                try {
                    Path destImage = new File(sitePath + IMG_DIR + srcImage.getFileName()).toPath();
                    Files.copy(srcImage, destImage);
                } catch (IOException ex) {
                    System.out.println("ERROR FROM COPYING SLIDESHOW IMAGES");
                }
            }
        }
    }

    private void constructVideoFiles(String sitePath, VideoComponent videoComponent) throws IOException {
        String videoFile = videoComponent.getVideoSrc();
        System.out.println(videoFile);
        Path srcFile = new File(videoFile).toPath();
        File file = new File(sitePath + VID_DIR + srcFile.getFileName());
        if (!file.exists()) {
            Path destImage = new File(sitePath + VID_DIR + srcFile.getFileName()).toPath();
            Files.copy(srcFile, destImage);
        }
    }


    private void constructDocument(Page p, String sitePath) throws IOException {

        String title = p.getTitleComponent().getTitleField().getText();

        if (p.getLayoutComponent().getTemplateStr().equals("template1")) {
            Path srcPath = new File(BASE_DIR + "template1.html").toPath();
            Path destPath = new File(sitePath + title + HTML_EXT).toPath();
            Files.copy(srcPath, destPath);
        } else if (p.getLayoutComponent().getTemplateStr().equals("template2")) {
            Path srcPath = new File(BASE_DIR + "template2.html").toPath();
            Path destPath = new File(sitePath + title + HTML_EXT).toPath();
            Files.copy(srcPath, destPath);
        } else if (p.getLayoutComponent().getTemplateStr().equals("template3")) {
            Path srcPath = new File(BASE_DIR + "template3.html").toPath();
            Path destPath = new File(sitePath + title + HTML_EXT).toPath();
            Files.copy(srcPath, destPath);

        } else if (p.getLayoutComponent().getTemplateStr().equals("template4")) {
            Path srcPath = new File(BASE_DIR + "template4.html").toPath();
            Path destPath = new File(sitePath + title + HTML_EXT).toPath();
            Files.copy(srcPath, destPath);

        } else if (p.getLayoutComponent().getTemplateStr().equals("template5")) {
            Path srcPath = new File(BASE_DIR + "template5.html").toPath();
            Path destPath = new File(sitePath + title + HTML_EXT).toPath();
            Files.copy(srcPath, destPath);
        } else {
            System.out.println("TEMPLATE: " + p.getLayoutComponent().getTemplateStr());
        }
    }

    private void constructColorFiles(Page p, String sitePath) throws IOException {
        if (p.getColorComponent().getColorStr().equals("bluegray")) {
            File file = new File(sitePath + CSS_DIR + "bluegray.css");
            if (!file.exists()) {
                Path srcPath = new File(BASE_DIR + CSS_DIR + "bluegray.css").toPath();
                Path destPath = new File(sitePath + CSS_DIR + "bluegray.css").toPath();
                Files.copy(srcPath, destPath);
            }

        } else if (p.getColorComponent().getColorStr().equals("gray")) {
            File file = new File(sitePath + CSS_DIR + "gray.css");
            if (!file.exists()) {
                Path srcPath = new File(BASE_DIR + CSS_DIR + "gray.css").toPath();
                Path destPath = new File(sitePath + CSS_DIR + "gray.css").toPath();
                Files.copy(srcPath, destPath);
            }

        } else if (p.getColorComponent().getColorStr().equals("graygreen")) {
            File file = new File(sitePath + CSS_DIR + "graygreen.css");
            if (!file.exists()) {
                Path srcPath = new File(BASE_DIR + CSS_DIR + "graygreen.css").toPath();
                Path destPath = new File(sitePath + CSS_DIR + "graygreen.css").toPath();
                Files.copy(srcPath, destPath);
            }

        } else if (p.getColorComponent().getColorStr().equals("green")) {
            File file = new File(sitePath + CSS_DIR + "green.css");
            if (!file.exists()) {
                Path srcPath = new File(BASE_DIR + CSS_DIR + "green.css").toPath();
                Path destPath = new File(sitePath + CSS_DIR + "green.css").toPath();
                Files.copy(srcPath, destPath);
            }

        } else if (p.getColorComponent().getColorStr().equals("redgray")) {
            File file = new File(sitePath + CSS_DIR + "redgray.css");
            if (!file.exists()) {
                Path srcPath = new File(BASE_DIR + CSS_DIR + "redgray.css").toPath();
                Path destPath = new File(sitePath + CSS_DIR + "redgray.css").toPath();
                Files.copy(srcPath, destPath);
            }
        }
    }

    private void copyJSFile(String sitePath) throws IOException {
        Path srcPath = new File(BASE_DIR + JS_DIR + "eportfolio.js").toPath();
        Path destPath = new File(sitePath + JS_DIR + "eportfolio.js").toPath();
        Files.copy(srcPath, destPath);
    }

    private void copyFontFile(String sitePath) throws IOException {
        Path srcPath = new File(BASE_DIR + CSS_DIR + "textfont.css").toPath();
        Path destPath = new File(sitePath + CSS_DIR + "textfont.css").toPath();
        Files.copy(srcPath, destPath);
    }

    private void constructJsonFiles(Page page, String sitePath) throws FileNotFoundException {
        JsonObject jsonObject = page.createJsonPage();
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        String jsonFilePath = sitePath + DATA_DIR + page.getTitleComponent().getTitleField().getText() + JSON_EXT;
        OutputStream os = new FileOutputStream(jsonFilePath);
        JsonWriter jsonFileWriter = writerFactory.createWriter(os);
        jsonFileWriter.writeObject(jsonObject);
    }

    public void deleteDir(File dir) {
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                deleteDir(f);
                f.delete();
            } else {
                f.delete();
            }
        }
        dir.delete();
    }

    public void copyAllFiles(String sourceFile, String destinationDir) throws IOException {
        File srcDir = new File(sourceFile);
        File[] files = srcDir.listFiles();
        for (File f : files) {
            Path srcPath = f.toPath();
            Path newPath = new File(destinationDir).toPath();
            if (!f.isDirectory()) {
                Files.copy(srcPath, newPath.resolve(srcPath.getFileName()));
            }
        }
    }
}
