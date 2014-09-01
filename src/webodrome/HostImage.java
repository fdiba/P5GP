package webodrome;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class HostImage {
	
	private PApplet pApplet;
	public PImage img;
	private PImage imgEdited;
	private PVector location;
	
	protected boolean[] pix;
	
	//---------- params ---------//
	public float contrast = 1;
	public float brightness = 1;
	
	public HostImage(PApplet p, String imgFile){
		
		pApplet = p;
		
		location = new PVector();
		img = pApplet.loadImage(imgFile);
		
		setPix();
		checkImageSize();
		
	}
	public void removeColors(){
	    
		img.loadPixels();
	    
	    for (int x=0; x<img.width; x++) {
	      
	    	for (int y=0; y<img.height; y++) {

	    		int loc = x + y*img.width;

	    		int c = img.pixels[loc];

		        //float r = red(img.pixels[loc]);
		        int r = (c >> 16) & 0xFF;
		        int g = (c >> 8) & 0xFF;
		        int b = c & 0xFF;
		        
		        int value = (r+g+b)/3;
		        
		        //img.pixels[loc] = color(value);
		        img.pixels[loc] = 0xff000000 | (value << 16) | (value << 8) | value;
	      
	    	}
	    
	    }
	    
	    img.updatePixels();
	    
	}
	private void checkImageSize(){

		int w = 640;
		int h = 480;
		
		int scale = 1;
		
	    if(img.width > img.height && img.width > w){      
	      scale = img.width/w;
	    } else if (img.height > img.width && img.height > h){
	      scale = img.height/h;
	    }
	    
	    if(scale != 1){
	      img.resize(img.width/scale, img.height/scale);
	    }
	    
	    centerImage(w, h);
	
	}
	private void setPix(){
		
		int w = 640;
		int h = 480;
		
		pix = new boolean[w*h];
		
		for (int i=0; i<pix.length; i++){
			pix[i] = false;
		}
		
	}
	private void centerImage(int w, int h){ 
	    location.x = w/2 - img.width/2;
	    location.y = h/2 - img.height/2;
	}
	public void editLuminosity(){
	    
		imgEdited = img.get(0, 0, img.width, img.height); 
	    
	    imgEdited.loadPixels();
	    
	    for (int x=0; x<imgEdited.width; x++) {
	      
	    	for (int y=0; y<imgEdited.height; y++) {

	        
	    		int loc = x + y*imgEdited.width;

	    		int c = imgEdited.pixels[loc];

	    		//float r = red(img.pixels[loc]);
		        int r = (c >> 16) & 0xFF;
		        int g = (c >> 8) & 0xFF;
		        int b = c & 0xFF;
		    
		        r = (int) (r*contrast + brightness);
		        g = (int) (g*contrast + brightness);
		        b = (int) (b*contrast + brightness);

		        //r = constrain(r, 0, 255);
		        r = r < 0 ? 0 : r > 255 ? 255 : r;
		        g = g < 0 ? 0 : g > 255 ? 255 : g;
		        b = b < 0 ? 0 : b > 255 ? 255 : b;
		        
		        int value = (r+g+b)/3;
		        
		        //img.pixels[loc] = color(value);
		        imgEdited.pixels[loc] = 0xff000000 | (value << 16) | (value << 8) | value;
	      
	    	}
	    
	    }
	    
	    imgEdited.updatePixels();
	    
	}
	public void display(){
		pApplet.image(imgEdited, location.x, location.y);
	}
}
