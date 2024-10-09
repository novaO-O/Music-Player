package studiplayer.audio;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ControllablePlayListIterator implements Iterator<AudioFile> {

	private List<AudioFile> files;
	private int currentIndex = -1;
    private AudioFile currentAudioFile;
	
	public ControllablePlayListIterator(List<AudioFile> files) {
		this.files = files;
	}
	
	public ControllablePlayListIterator(List<AudioFile> files, String search, SortCriterion sort) {
		List<AudioFile> filteredFiles = filterSearch(files, search);
        this.files = filterSort(filteredFiles, sort);
	}
	
	public int getCurrent() {
		return this.currentIndex;
	}
	
	public void setCurrent(int current) {
		this.currentIndex = current;
	}
	
	public boolean hasNext() {
		return this.files.size()-1 > getCurrent();
	}

	public AudioFile next() {	
		// out of bounds, current = 0
		if(getCurrent() > files.size()-1) {
			setCurrent(0);
			return this.files.get(getCurrent()); 
		} else {			
			setCurrent(getCurrent()+1);
			return this.files.get(getCurrent());
		}
	} 
	
	public AudioFile jumpToAudioFile(AudioFile file) {
		setCurrent(this.files.indexOf(file));
		return files.get(getCurrent());
	}
	
	public List<AudioFile> filterSearch(List<AudioFile> files, String search) {
		List<AudioFile> filteredList = new LinkedList<>();
		
		if(search == null || search.isEmpty()) {
			filteredList = files;
		
		} else {
			for(AudioFile elem : files) {
				if(elem.getAuthor().contains(search) 
						|| elem.getTitle().contains(search) 
						|| (elem instanceof TaggedFile && ((TaggedFile) elem).getAlbum().contains(search))) {
					filteredList.add(elem);
				}
			}
		}
		return filteredList;
	}
	
	public static List<AudioFile> filterSort(List<AudioFile> files, SortCriterion sort) {
		List<AudioFile> filteredList = new LinkedList<>(files);
		
		if(sort != SortCriterion.DEFAULT && sort != null) {
			Comparator<AudioFile> comparator = null;
			ControllablePlayListIterator cont = new ControllablePlayListIterator(filteredList);
		
			switch (sort) {
				case AUTHOR:
					comparator = new AuthorComparator();
					break;
				case ALBUM:
					comparator = new AlbumComparator();
					break;
				case TITLE:
					comparator = new TitleComparator();
					break;
				case DURATION:
					comparator = new DurationComparator();
					break;
			}
			filteredList.sort(comparator); 
		}
		return filteredList;

	}
}
