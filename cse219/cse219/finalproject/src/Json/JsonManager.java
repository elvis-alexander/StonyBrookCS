/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Json;

import Page.Component;
import UI.UI;
import java.io.IOException;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;

/**
 *
 * @author eafernandez
 */


public class JsonManager {
    
    public static String JSON_IMAGE_PATH = "image_path";
    

    public JsonManager(UI ui) throws IOException, NullPointerException {
//        StringWriter sw = new StringWriter();
//        JsonArray pageComponentList = constructPage(ui.getPageEditor().getPageList().get(0).getComponents());
//        JsonObject pageComponent = Json.createObjectBuilder().add("componentlist", pageComponentList).build();
//        Map<String, Object> properties = new HashMap<>(1);
//        properties.put(JsonGenerator.PRETTY_PRINTING, true);
//
//        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
//        JsonWriter jsonWriter = writerFactory.createWriter(sw);
//        jsonWriter.writeObject(pageComponent);
//        jsonWriter.close();
        
//        String pageTitle = "" + ui.getPageEditor().;
        
        
        
    }
    
    private JsonArray constructPage(List<Component> page) {
        
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        for(Component component : page) {
            jsonArrayBuilder.add(component.makeJsonObject());
        }
        JsonArray jsonArray = jsonArrayBuilder.build();
        return jsonArray;
    }
 
    
}
