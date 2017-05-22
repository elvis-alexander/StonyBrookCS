/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Page;

import javafx.scene.layout.Pane;
import javax.json.JsonObject;

/**
 *
 * @author eafernandez
 */
public abstract class Component extends SuperComponent{
    
    public abstract Pane componentView();
    
    public abstract JsonObject makeJsonObject();
    
    public abstract String getType();
}
