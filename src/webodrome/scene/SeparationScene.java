package webodrome.scene;

import processing.core.PApplet;
import webodrome.HostImage;
import webodrome.ParticleSystem;

public class SeparationScene extends Scene {
	
	private String[] imageFiles = {"emma-watson-158356_w1000.jpg", "Scarlett-Johansson-faces.jpg"};
	private HostImage host;
	private ParticleSystem ps;
	public boolean hasBeenSet;

	public SeparationScene(PApplet _pApplet) {
		
		super(_pApplet);
		
		//int id = (int) pApplet.random(imageFiles.length);
		//host = new HostImage(this, imageFiles[id]);
		host = new HostImage(pApplet, imageFiles[1]);
		host.removeColors();
		
		ps = new ParticleSystem(pApplet);

	}
	public void startAnim(){
		  
		//println("contrast: " + host.contrast + " | brightness: " + host.brightness);
		hasBeenSet = true;
		  
		host.img = pApplet.get(0, 0, pApplet.width, pApplet.height); //update img width and height contrast and brightness
		host.img.loadPixels();
		pApplet.background(0); 
		
	}
	public void editBrightnessAndContrast(){
		host.contrast = 5f*(pApplet.mouseX/(float)pApplet.width); //0 to 5
		host.brightness = (float) (256*(pApplet.mouseY/(float)pApplet.height-0.5)); //-128 to +128
	}
	
	public void display(){
		
		
		if(!hasBeenSet) {
			
			pApplet.fill(0);
		    pApplet.rect(0, 0, pApplet.width, pApplet.height);
		    host.editLuminosity();
		    host.display();  
		  
		} else {
		
			pApplet.loadPixels();  
		    ps.run(host);
		    pApplet.updatePixels();
		  
		  }
		
	}
}
