package studiplayer.audio;
import java.io.File;

import studiplayer.basic.BasicPlayer;

public abstract class SampledFile extends AudioFile {

	float frameRate;
	long numberOfFrames;
	protected long duration;  
	
	public SampledFile() {
		super();
	}
	
	public SampledFile(String path) throws NotPlayableException {
		super(path);
	}
	
	public void play() throws NotPlayableException {
		try {
			BasicPlayer.play(pathname);
		} catch (RuntimeException e) {
			throw new NotPlayableException(pathname, e);
		}
	}
	
	
	public void togglePause () {
		BasicPlayer.togglePause();
	}
	
	public void stop() {
		BasicPlayer.stop();
	}
	
	public String formatDuration() {		
		return timeFormatter(getDuration());
	}
	
	public String formatPosition() {
		return timeFormatter(BasicPlayer.getPosition());
	}
	
	public static String timeFormatter(long timeInMicroSeconds) {
		// RuntimeException bei negativen zahlen und overflow		
		if(timeInMicroSeconds < 0) {
			throw new RuntimeException("Abbruch des Programms, Zeit im negativen Bereich");
		}
		
		long secTemp = timeInMicroSeconds / 1000000;
		long min = secTemp / 60;

		if(min > 99) {
			throw new RuntimeException("Abbruch des Programms, Overflow");
		}
		
		// sec die "übrigbleiben"
		long sec = secTemp % 60;
		
		String formatted = String.format("%02d:%02d", min, sec);
		return formatted;
	}
	
	public long getDuration() {
		return this.duration;
	}
	
	// Setter
	public void setAuthor(String author) {
		this.author = author.trim();
	}
	
	public void setTitle(String tile) {
		this.title = title;
	}
	
	public void setDuration(long duration) {
		this.duration = duration;
	}
}
