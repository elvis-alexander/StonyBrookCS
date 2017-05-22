/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Page;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author eafernandez
 */
public //Layout Class
class Layout {
    
    private String name;
    private String imageSrc;
    private ImageView imageView;
    
    public Layout(String imageSrc, String name) {
        this.name = name;
        this.imageSrc = imageSrc;
        this.imageView = new ImageView(new Image(imageSrc));
    }
    
    public String getImageSrc() {
        return imageSrc;
    }
    
    public ImageView getImageView() {
        return imageView;
    }
    
    public String getName() {
        return name;
    }
    
    
}