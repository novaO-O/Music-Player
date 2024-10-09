package studiplayer.audio;
import java.util.Map;

import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile{
	
	public WavFile() {
		super();
//		readAndSetDurationFromFile();
//		computeDuration(0, 0);
		setDuration(0);
	}
	
	public WavFile(String path) throws NotPlayableException {
		super(path);
		readAndSetDurationFromFile();		
		computeDuration(numberOfFrames, frameRate);
	}
		
	public void readAndSetDurationFromFile() throws NotPlayableException {
		
		try {
			WavParamReader.readParams(pathname);
			frameRate = WavParamReader.getFrameRate();
			numberOfFrames = WavParamReader.getNumberOfFrames();			
		} catch (RuntimeException e){
			throw new NotPlayableException(pathname, e);
		}
	}
	

	
	public static long computeDuration(long numberOfFrames, float frameRate) {
		// umformatieren von long zu float		
		// teilen der float Werte
		float result1 = ((long) numberOfFrames / frameRate) * 1000000L;
				
		long result2 = (long) result1;
		return result2;
	}
	
	public long getDuration() {
		return computeDuration(numberOfFrames, frameRate);
	}
	
	public String toString() {
		return super.toString() + " - " + formatDuration();
		
	}
	
}
