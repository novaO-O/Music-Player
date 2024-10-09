package studiplayer.audio;

import java.util.Comparator;

public class TitleComparator implements Comparator<AudioFile> {

	@Override
	public int compare(AudioFile o1, AudioFile o2) {
		try {
			return o1.getTitle().compareTo(o2.getTitle());
		} catch (RuntimeException e) {
			throw new RuntimeException();
		}
	}


}
