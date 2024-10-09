package studiplayer.audio;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;



public class PlayList implements Iterable<AudioFile> {
	
	protected List<AudioFile> list = new ArrayList<AudioFile>();
	protected String search;
	protected SortCriterion sortCriterion = SortCriterion.DEFAULT;
	private AudioFile currentAudioFile;

	protected ControllablePlayListIterator contIterator = new ControllablePlayListIterator(list);
	
	public PlayList() {
		resetContIterator();
	}
	
	public PlayList(String m3uPathname) {
		
		try {
			loadFromM3U(m3uPathname);
		} catch (NotPlayableException e) {
			throw new RuntimeException(m3uPathname);
		}
	}
	
	public void add(AudioFile file) {
		list.add(file);
		resetContIterator();
	}
	
	public void remove(AudioFile file) {
		list.remove(file);
		resetContIterator();
	}
	
	public int size() {
		return this.list.size();
	}
	
	public SortCriterion getSortCriterion() {
		return this.sortCriterion;
	}
	
	public void setSortCriterion(SortCriterion sortCriterion) {
		this.sortCriterion = sortCriterion;
		resetContIterator();
	}
	
	public String getSearch() {
		return this.search;
	}
	
	public void setSearch(String search) {
		this.search = search;
		resetContIterator();
	}
	
	public AudioFile currentAudioFile() {
		// empty & ouf of bounds
		if(this.list.isEmpty() || contIterator.getCurrent() > list.size()-1) {
			return null;		
		}
		
		return this.currentAudioFile;    
	}
	
	private void resetContIterator() {
		this.contIterator = new ControllablePlayListIterator(list, search, sortCriterion);
		
		if (contIterator.hasNext()) {
			this.currentAudioFile = contIterator.next();
		} else {
			this.currentAudioFile = null;
		}
	}
	
	public void nextSong() {
		if(this.list.isEmpty()) {
			return;
		} else if (this.contIterator.hasNext()) {
			currentAudioFile = this.contIterator.next();
		} else {
			resetContIterator();
		}
	}
	
	public void loadFromM3U(String pathname) throws NotPlayableException {
        list.removeAll(list);        
        
		Scanner scan = null;
        try {
            scan = new Scanner(new File(pathname));
            
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                
                    try {
                    	if (!line.isEmpty() && !line.isBlank() && !line.startsWith("#")) {
                    		AudioFile file = AudioFileFactory.createAudioFile(line);
                    		this.list.add(file);
                    	}  
                    } catch (NotPlayableException e) {
                    	e.printStackTrace();
                    }
                }
        	
        } catch (Exception e) {
            throw new NotPlayableException("File not found :", pathname);
            
        } finally {
            try {
                System.out.println("File " + pathname + "read");
            	scan.close();
            } catch (Exception e) {
            }
        }
        resetContIterator();
    }

	public void saveAsM3U(String pathname) {
        FileWriter writer = null;
        String sep = System.getProperty("line.separator");
        try {
            writer = new FileWriter(pathname);
            for (AudioFile audioFile : this.list) {
                writer.write(audioFile.getPathname() + sep);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to write file " + pathname);
        } finally {
            try {
            	System.out.println("File " + pathname + " written");
                writer.close();
            } catch (Exception e) {
            	
            }
        }
    }
	
	public List<AudioFile> getList() {
		return this.list;
	}
	
	public Iterator<AudioFile> iterator() {
		return new ControllablePlayListIterator(getList(), getSearch(), getSortCriterion());
	}
	
	public void jumpToAudioFile(AudioFile audioFile) {
		contIterator.jumpToAudioFile(audioFile);
		currentAudioFile = audioFile;
	}
	
	public String toString() {
        return getList().toString();	
	}
	
}