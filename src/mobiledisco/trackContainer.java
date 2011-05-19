package mobiledisco;

import processing.core.PApplet;
import processing.core.PImage;
import ddf.minim.*;

public class trackContainer {
	PApplet parent; // The parent PApplet that we will render ourselves onto
	boolean ISLOADED = false;
	boolean ISREMOVED = false;
	boolean ISPAUSED = false;
	int stopTime = 0;
	int stopLimit = 100; // zeit bis der track anhŠlt
	AudioPlayer player;
	AudioMetaData meta;
	public float volume, fadeVol;
	String trackName;
	private Minim m;
	public int myVol, startTime;
	float position;
	float length;
	int playPointer = 1;
	public int pauseDistance = 260;
	float playhead;
	int updateCounter;
	String author, title;
	int arcRadius = 15;
	
	
	trackContainer(PApplet p, Minim m, String n) {
		parent = p;
		trackName = n;
		this.m = m;
	}

	void load() {
		if (ISLOADED == false) {
			player = m.loadFile(trackName);
			length = player.length();
			player.setGain(-40);
			parent.println(author +" - " +title + " loaded...");
			meta = player.getMetaData();
			author = meta.author();
			title = meta.title();
			((MobileDisco) parent).myPrinter("Loaded: " +author +" - " +title);
			//player.play(startTime*1000); // track abspielen
			player.cue(0);
		}
		ISLOADED = true;
	}

	void pause() {
		ISREMOVED = true;
	}

	void update(float v) {
		if(v > pauseDistance) {
		ISPAUSED = true;
		player.pause();
		((MobileDisco) parent).myPrinter("Paused: " +author +" - " +title);
		} else {
		ISPAUSED = false;
		player.play(); // track abspielen
		((MobileDisco) parent).myPrinter("Playing: " +author +" - " +title);
		}
		stopTime = 0;
		ISREMOVED = false;
		volume = parent.map(v, 0, 250, 0, -30);
		player.shiftGain(player.getGain(), volume, 500);
		myVol = (int) player.getGain();
				
	}

	void drawCursor() {
		if(ISLOADED == true){ 
		position = player.position();
		playhead = position/length;
		parent.pushMatrix();
		parent.fill(130);  
		parent.rotate(parent.PI);
		int speed = 5000; // normalwert 360
		segment(100,0,0-(playhead*speed),9);
		parent.popMatrix();
		}
		parent.pushMatrix();
		parent.rotate(playhead*1000);
		parent.imageMode(PApplet.CENTER);
		if(ISPAUSED) {
			parent.image(((MobileDisco) parent).cursor[0], 0, 0);

		} else {
			parent.image(((MobileDisco) parent).cursor[1], 0, 0);
		}
		parent.popMatrix();
		
		parent.fill(130);
		parent.text(author +" - " +title, 0 , 35);  
	}
	
	void segment(int resolution,float start, float end, float radius) {
		  parent.beginShape(PApplet.QUAD_STRIP);
		  float strokeWidth = 1.5f;
		  for(int i=0;i<resolution;i++) {
		    float d=parent.radians(parent.map(i,0,resolution-1,start,end));
		    parent.vertex(parent.sin(d)*radius,parent.cos(d)*radius);
		    parent.vertex(parent.sin(d)*radius*strokeWidth,parent.cos(d)*radius*strokeWidth);		  }
		  parent.endShape();
		}
	
	void fader() {
		if (ISREMOVED == true) {
			stopTime = stopTime + 1; // counter zŠhlen
			if (stopTime >= stopLimit / 2) {
				fadeVol = parent.map(stopTime, stopLimit / 2, stopLimit, volume, -30);
				//player.setGain(fadeVol); // volume faden
				player.shiftGain(player.getGain(), -40, 2000);
				// println("fading out...");
				

			}
			// println(stopTime);
			if (stopTime >= stopLimit) {
				player.pause(); // track anhalten
				parent.println("Paused: " +author +" - " +title);
				((MobileDisco) parent).myPrinter("Paused: " +author +" - " +title);
				
				((MobileDisco) parent).playList.add(author +" - " +title); // adding to playlist

				ISREMOVED = false;
			}
		}
	}
}