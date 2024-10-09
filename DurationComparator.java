package studiplayer.audio;

import java.util.Comparator;

public class DurationComparator implements Comparator<AudioFile> {

	public int compare(AudioFile o1, AudioFile o2) {
		try {
			Long durration1 = (long) 0;
			if (o1 instanceof SampledFile) {
				durration1 = ((SampledFile) o1).getDuration();
			}
			
			Long durration2 = (long) 0;
			if (o2 instanceof SampledFile) {
				durration2 = ((SampledFile) o2).getDuration();
			}
			return Long.compare(durration1, durration2);
		} catch (RuntimeException e) {
			throw new RuntimeException();
		}
	}

}
