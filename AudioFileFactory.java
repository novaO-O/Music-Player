package studiplayer.audio;
public class AudioFileFactory {

		
	public static String getExt(String path) {
		int i = path.lastIndexOf("."); 
		String e = path.substring(i+1);
		System.out.println(e);
		return e;
		
	}
	
	
	public static AudioFile createAudioFile(String path) throws NotPlayableException {
		String ext = getExt(path);
		
		if(ext.equalsIgnoreCase("wav")) {
			return new WavFile(path);
		} else if(ext.equalsIgnoreCase("ogg") || ext.equalsIgnoreCase("mp3")) {
			return new TaggedFile(path);
		} else {
			throw new NotPlayableException(path, "Unknown suffix for AudioFile");
		}
		
	}
}
