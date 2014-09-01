package webodrome;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class ParticleSystem {
	
	private PApplet pApplet;
	
	//------------ param -------------//
	private int particlesMaxNumber = 20;
	private int lifespanToGiveBirth = 80;
	
	public ArrayList<Particle> particles;
	
	public ParticleSystem(PApplet p){
		
		pApplet = p;
		
		particles = new ArrayList<Particle>();
		
		for (int i=0; i<particlesMaxNumber; i++){
			particles.add(new Particle(pApplet, 50));
		}
		
	}
	public void run(HostImage host){
		
		
		
		int numParticles = particles.size();
	    PApplet.println(numParticles);
		
		
	    for(int i=numParticles-1; i>=0; i--){
	      
	    	Particle p = (Particle) particles.get(i);
	      
	    	p.run(particles, host);
	    	
	    	
	    	if (p.isDead()) {   
	  	      
	    		particles.remove(i);
	      
	    	} else if (p.lifespan >= lifespanToGiveBirth) {   
	    		
	    		int count = 0;
	        
	    		for (Particle other : particles) {
	          
	    			float distance = PVector.dist(p.location, other.location);
	    			if ((distance > 0) && (distance < 50)) count++;
	        
	    		}
	        
	    		int maxClosePoints = (int) pApplet.random(5)+10;
	        
	    		if(count < maxClosePoints){
	          
	    			//set child location
	    			PVector randLoc = new PVector(pApplet.random(-1,1), pApplet.random(-1,1));
	    			PVector newLoc = PVector.add(p.location, randLoc);
		          
	    			if(particles.size() < 700) {
	            
	    				particles.add(new Particle(pApplet, newLoc, p.startLifespan/2));
	            
	    				p.lifespan = p.startLifespan/2;
	          
	    			}
	        
	    		}
	    		
	    	}
	    	
	    }
	    
	    
		
		
	}
}
