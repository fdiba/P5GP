import java.util.Date;

import processing.core.PApplet;
import webodrome.App;
import webodrome.scene.SeparationScene;

@SuppressWarnings("serial")
public class GenerativePortraits extends PApplet {
	
	//-------- scenes -----------//
	
	private SeparationScene separationScene;
	
	public static void main(String[] args) {
		PApplet.main(GenerativePortraits.class.getSimpleName());

	}
	public void setup(){
		
		size(640, 480);
		frameRate(25);
		background(0);
		//smooth();
		
	}
	public void draw(){
		
		
		int sceneId = App.getSceneId();
		
		switch (sceneId) {
		case 0:
			scene0(); //separation particles
			break;
		default:
			scene0();
			break;
		}
		
		
	}
	private void scene0(){
		
		
		//-------------- init ------------------//
		
		int sceneId = App.getSceneId();
		
		if (sceneId != App.oldSceneId) {
			
			App.oldSceneId = sceneId;
			
			separationScene = new SeparationScene(this);
			App.setActualScene(separationScene);
			
		}
		
		//-------------- draw ------------------//
		
		
		//separationScene.update();
		//separationScene.display();
		
		separationScene.display();
		
		
		
	}
	//------------ mouse ------------//
	public void mouseMoved(){
		
	
		if (App.oldSceneId == 0) {
			if(!separationScene.hasBeenSet){
				separationScene.editBrightnessAndContrast();
			}
		}
	}
	//------------ keyboard ------------//
	public void keyPressed(){
	  
		if (key == ENTER && App.oldSceneId == 0){
		
			if(!separationScene.hasBeenSet){
				noCursor();
				separationScene.startAnim();
			}
	  
		} else if (key == 's'){
		  savePicture();  
		}
	}
	private void savePicture(){
		Date date = new Date();
		String name = "data/images/portraits-"+date.getTime()+".png";
		save(name);
	}

}
