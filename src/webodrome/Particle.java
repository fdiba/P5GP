package webodrome;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Particle {
	
	private PApplet pApplet;
	
	private PVector velocity;
	private PVector acceleration;
		  
	protected PVector location;
	protected int lifespan;
	
	//----------- param -----------//
	protected int startLifespan = 50;
	private int maxValue = 60;
	private int minValue = -60;
	private int maxSpeed = 1;
	private float maxforce = (float) 0.05;    //maximum steering force
	
	public Particle(PApplet p, int _lifespan){
		pApplet = p;
		location = new PVector(pApplet.random(640), pApplet.random(480)); 
		lifespan = _lifespan;
		init();
	}
	public Particle(PApplet p, PVector loc, int _lifespan){
		pApplet = p;
		location = loc.get();
	    lifespan = _lifespan;
	    init();
	}
	private void init(){
	    velocity = new PVector(pApplet.random(minValue, maxValue), pApplet.random(minValue, maxValue));
	    acceleration = new PVector();
	}
	private void applyForce(PVector force) {
	    acceleration.add(force);
	}
	private void flock(ArrayList<Particle> particles){
	    
	    PVector sep = separate(particles);
	    //PVector ali = align(particles);
	    //PVector coh = cohesion(particles);
	    
	    //-------- TODO PARAM -----------//
	    sep.mult((float) 1.5);
	    //ali.mult(1.0);
	    //coh.mult(1.0);
	    
	    applyForce(sep);
	    //applyForce(ali);
	    //applyForce(coh);
	    
	}
	
	// craig reynolds flocking behavior
	@SuppressWarnings("unused")
	private PVector cohesion(ArrayList<Particle> particles) {
		
		float neighbordist = 50;
	    PVector sum = new PVector(0,0);
	    int count = 0;
	    
	    for (Particle other : particles) {
	      
	    	float distance = PVector.dist(location, other.location);
	      
	    	if ((distance > 0) && (distance < neighbordist)) {
	    		sum.add(other.location);
	    		count++;
	    	}
	    }
	    
	    if (count > 0) {
	    	sum.div(count);
	    	return seek(sum);
	    } else {
	    	return new PVector();
	    }
	}
	@SuppressWarnings("unused")
	private PVector align (ArrayList<Particle> particles) {
	      
		float neighbordist = 50;
	    PVector sum = new PVector();
	    int count = 0;
	    
	    for (Particle other : particles) {
	      
	   
	    	float distance = PVector.dist(location, other.location);
	      
	    	if ((distance > 0) && (distance < neighbordist)) {
	    		sum.add(other.velocity);
	    		count++;
	    	}
	      
	    }
	    
	    if (count > 0) {
	      
	    	sum.div((float)count);
	    	sum.normalize();
	    	sum.mult(maxSpeed);
	      
	    	PVector steer = PVector.sub(sum, velocity);
	    	steer.limit(maxforce);
	    	return steer;
	      
	    } else {
	      
	    	return new PVector();
	    
	    }
	    
	}
	private PVector separate (ArrayList<Particle> particles) {
	    
	    float desiredseparation = (float) 25.0; //--------- PARAM --------------//
	    PVector steer = new PVector();
	    int count = 0;
	    
	    for (Particle other : particles) {
	      
	    	float distance = PVector.dist(location, other.location);
	      
	    	if ((distance > 0) && (distance < desiredseparation)) {
	        
	    		PVector diff = PVector.sub(location, other.location);
	    		diff.normalize();
	    		diff.div(distance);
	    		steer.add(diff);
	    		count++;    
	    	}
	    
	    }
	    
	    if (count > 0) steer.div((float)count);
	    
	    if (steer.mag() > 0) {      
	      
	    	//implement Reynolds: Steering = Desired - Velocity
	    	steer.normalize();
		    steer.mult(maxSpeed);
		    steer.sub(velocity);
		    steer.limit(maxforce);
	      
	    }
	    
	    return steer;
	    
	}
	
	private PVector seek(PVector target){
		
		PVector desired = PVector.sub(target, location);
	    
	    desired.normalize();
	    desired.mult(maxSpeed);
	    
	    /* a method that calculates and applies a steering force towards a target
	     * steering = Desired minus Velocity
	     */
	     
	    PVector steer = PVector.sub(desired, velocity);
	    steer.limit(maxforce);
	    return steer;
	    
	}
	public void run(ArrayList<Particle> particles, HostImage host){
		flock(particles);
	    update(host);
	}
	private void update(HostImage host){
				
		velocity.add(acceleration);
	    
	    velocity.limit(maxSpeed);
	    
	    location.add(velocity);
	    
	    acceleration.mult(0);
	    
	    //------------------
        
	    setBoundaries();
	    setParticleColorBasedOnHostImage(host);
		//display();
		
	}
	private void setParticleColorBasedOnHostImage(HostImage host){
		
		int w = 640;
		int h = 480;
		
		location.x = PApplet.constrain(location.x, 0, w-1);
		location.y = PApplet.constrain(location.y, 0, h-1);
		
		int x = (int) location.x;
		int y = (int) location.y;
	    
		int loc = x + y * w;
		
		editPixColor(host, loc, true);
		
		//----------- create blur by editing color of closed pixels -----------------//
		
		//editTopRightBottomLeftPixels(host, x, y);
		//editXPixels(host, x, y);
	      
	}
	private void editPixColor(HostImage host, int loc, boolean isCenter){
		
		int hostColor = host.img.pixels[loc];
		int actualColor = pApplet.pixels[loc];
		    
		int hostBlueValue = hostColor & 0xFF;
		int actualBlueValue = actualColor & 0xFF;
   
		if (actualBlueValue >= hostBlueValue) { // same color bad spot
			
			actualBlueValue = hostBlueValue;	
			
			if(isCenter) lifespan -= 5;
	    
		} else { // not same color maybe good spot
	    	
	    	int num = 1;
	    	
	    	if(isCenter) {
	    		num = 3;
	    	} else {
	    		num = 10;
	    	}
	    	
	    	int val = hostBlueValue/num;
	    	
	    	actualBlueValue += val;
	    	if (actualBlueValue > hostBlueValue) actualBlueValue = hostBlueValue;
	    	
	    	if(isCenter) lifespan += (val/(255/3)) * 20; // --------------- TO DO param ---------------------//
	    	
	    	int c = (actualBlueValue << 16) | (actualBlueValue << 8) | actualBlueValue;
	    	pApplet.pixels[loc] = c;
	    }
		
	}
	private void editXPixels(HostImage host, int x, int y){
		
		int w = 640;
		int h = 480;
		
		//-------------- top right ------------------//
		if(x<w-1 && y>0){
			int loc = x+1 + (y-1) * w;
			editPixColor(host, loc, false);
		}
		
		//-------------- top left ------------------//
		if(x>0 && y>0){
			int loc = x-1 + (y-1) * w;
			editPixColor(host, loc, false);
		}
		
		//-------------- bottom right ------------------//
		if(x<w-1 && y>h-1){
			int loc = x+1 + (y+1) * w;
			editPixColor(host, loc, false);
		}
		
		//-------------- bottom left ------------------//
		if(x>0 && y>h-1){
			int loc = x-1 + (y+1) * w;
			editPixColor(host, loc, false);
		}
		
	}
	private void editTopRightBottomLeftPixels(HostImage host, int x, int y){
		
		int w = 640;
		int h = 480;
		
		//-------------- right ------------------//		
		if(x<w-1){ //not last pixel
			int loc = x+1 + y * w;
			editPixColor(host, loc, false);
		}
		
		
		//-------------- left ------------------//
		if(x>0){ //not first pixel
			int loc = x-1 + y * w;
			editPixColor(host, loc, false);    	
		}
		
		//-------------- top ------------------//		
		if(y>0){ //not first row
			int loc = x+1 + (y-1) * w;
			editPixColor(host, loc, false);
		}
		
		//-------------- bottom ------------------//
		if(y<h-1){ //not last row
			int loc = x+1 + (y+1) * w;
			editPixColor(host, loc, false);
		}
		
		
		
	}
	@SuppressWarnings("unused")
	private void display(){
		pApplet.stroke(255, 0, 0);
		pApplet.point(location.x, location.y);
	}
	private void setBoundaries(){
		
		int w = 640;
	    int h = 480;
	    
		if(location.x > w) {
	      location.x = 0;
	    } else if (location.x < 0){
	      location.x = w;
	    }
	    
	    if(location.y > h) {
	      location.y = 0;
	    } else if (location.y < 0){
	      location.y = h;
	    }
	    
	}
	protected boolean isDead() {
	    
		//PApplet.println(lifespan);
		
		if(lifespan <= 0) {
			return true;
	    } else {
	    	return false;
	    }
	  
	}

}
