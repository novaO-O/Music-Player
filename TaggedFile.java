package studiplayer.audio;
import java.io.File;
import java.util.Map;

import studiplayer.basic.TagReader;

public class TaggedFile extends SampledFile {

	protected String album; 
	
	
	public TaggedFile() {
		super();
//		readAndStoreTags();
	}
	
	public TaggedFile(String path) throws NotPlayableException {
		super(path);
		this.readAndStoreTags();
	}
	
	public String getAlbum() {
		Map<String, Object> tag = TagReader.readTags(getPathname());
		String temp = (String)tag.get("album");
		if(temp == (null)) {
			return this.album = "";
		} else {			
			return this.album = temp.trim();
		}
	}
	
	public void readAndStoreTags() throws NotPlayableException {
		try {	
			Map<String, Object> tagMap = TagReader.readTags(getPathname());
			if (tagMap.containsKey("title")) {
				title = (String)tagMap.get("title");
				if (title != null && !title.equals("")) {
					title = title.trim();
				}
			}
		
			if (tagMap.containsKey("author")) {
				if ((String)tagMap.get("author")!= null) {
					setAuthor((String)tagMap.get("author"));
				}
			}
				
			if (tagMap.containsKey("album")) {
				album = (String)tagMap.get("album");
				if (album != null && !album.equals("")) {
					this.album = ((String)tagMap.get("author"));
				}
			}
				
			if (tagMap.containsKey("duration")) {
				setDuration((long)tagMap.get("duration"));
			}
			
		} catch (RuntimeException e) {
            throw new NotPlayableException("can't read or store Tags :", pathname);
        }	
	}
	
		public String toString() {
			String finalAuthor = getAuthor();
			String finalTitle = getTitle();
			
			String finalString;
			if (getAlbum().equals("")) {
				if (finalAuthor.equals("")) {
					finalString = finalTitle;
				} else {
					finalString = finalAuthor + " - " + finalTitle;
				}
			} else {
				if (finalAuthor.equals("")) {
					finalString = finalTitle + " - " + getAlbum();
				} else {
					finalString = finalAuthor + " - " + getTitle() + " - " + getAlbum();
				}

			}
			return finalString + " - " + formatDuration();
	}

}	
