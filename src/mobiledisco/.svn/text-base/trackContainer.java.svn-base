package mobiledisco;

import processing.core.PApplet;
import processing.core.PImage;
import ddf.minim.*;

public class trackContainer {
	PApplet parent; // The parent PApplet that we will render ourselves onto
	boolean ISLOADED = false;
	boolean ISREMOVED = false;
	int stopTime = 0;
	int stopLimit = 100; // zeit bis der track anhŠlt
	AudioPlayer player;
	AudioMetaData meta;
	public float volume, fadeVol;
	String trackName;
	private Minim m;
	public int myVol, startTime;
	int position;
	int length;
	int playPointer = 1;
	public int pauseDistance = 260;
	int playhead;
	int updateCounter;
	String author, title;
	
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
			playhead = length / 70;

		}
		ISLOADED = true;
	}

	void pause() {
		ISREMOVED = true;
	}

	void update(float v) {
		if(v > pauseDistance) {
		player.pause();
		((MobileDisco) parent).myPrinter("Paused: " +author +" - " +title);
		} else {
		player.play(); // track abspielen
		((MobileDisco) parent).myPrinter("Playing: " +author +" - " +title);
		}
		stopTime = 0;
		ISREMOVED = false;
		volume = parent.map(v, 0, 250, 0, -30);
		player.shiftGain(player.getGain(), volume, 500);
		myVol = (int) player.getGain();
		position = player.position();
	}

	void drawCursor(float v) {
		updateCounter++;
		if (v >= pauseDistance) { // wenn der track auf pause ist
			parent.image(((MobileDisco) parent).cursor[0], -15, -10);
		}
		else {
		if(updateCounter > 5) {
		if(playPointer <= 5) {
			playPointer++;
			//parent.println(playPointer);

		}
		else {
			playPointer = 1;
			//parent.println("reset");

		}
		updateCounter = 0;
		}

		parent.image(((MobileDisco) parent).cursor[playPointer], -15, -10);
		//parent.println("Loading cursor " +playPointer);
		}
		parent.fill(130);
		//parent.text(trackName, 0 , 35);
		parent.text(author +" - " +title, 0 , 35);  

		//parent.text(player.position(), 0 , 35);
		//parent.println(position);
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