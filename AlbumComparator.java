package studiplayer.audio;

import java.util.Comparator;

public class AlbumComparator implements Comparator<AudioFile> {

	@Override
	public int compare(AudioFile o1, AudioFile o2) {
		
		// prüfen ob instanceOf TaggedFile mit return -1, 0, 1
		if(!(o1 instanceof TaggedFile) && !(o2 instanceof TaggedFile)) {
			return 0;
		}
		if(!(o1 instanceof TaggedFile)) {
			return -1;
		}
		if(!(o2 instanceof TaggedFile)) {
			return 1;
		}
		
		if(o1 == null || o2 == null) {
			throw new RuntimeException();
		}
		
		if(o1.getAlbum() == null && o2.getAlbum() == null) {
			return 0;
		}
		
		if(o1.getAlbum() == null) {
			return -1;
		}	
		
		if(o2.getAlbum() == null) {
			return 1;
		}
		
		return o1.getAlbum().compareTo(o2.getAlbum());			
		
	}
	
}
