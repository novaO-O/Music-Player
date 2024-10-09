package studiplayer.audio;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.File;
import studiplayer.basic.TagReader;


public abstract class AudioFile {

	String filename;
	String pathname;
	String sep = "\\";
	char sep2 = '\\';
	String notSep = "/";
	String author;
	String title;
//	protected long duration;
	protected String album;

	
	
	private boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().indexOf("win") >= 0;
		}
	
	public static String[] removeEmptyElem(String [] input) {
		List<String> result = new ArrayList<>();
			for (String elem : input) {
				if(!elem.isEmpty()) {
					result.add(elem);
				}
			}
		return result.toArray(new String[0]);
			
		}
	
	public AudioFile() {
		this.pathname = "";
		this.filename = "";
		this.author = "";
		this.title = "";
	}
	
	public AudioFile(String path) throws NotPlayableException {
		parsePathname(path);
		parseFilename(filename);
		
//		if (!readingTest.canRead() || readingTest.equals(null)) {
//			throw new NotPlayableException(path, ": Abbruch, Datei nicht lesbar");
//		}

	}

	public void parsePathname(String path) throws NotPlayableException {
		// erase space
		String withoSpace = path.trim();
		
		
		// System is Windows
		if (!isWindows()) {
			sep = "/";
			sep2 = '/';
			notSep = "\\";
		}
		
		// Slash to Backlslash
		String withoSlash = withoSpace.replace(notSep,sep);				
		
		
		// last Char on the right site of \ => filename
		boolean contBackslash = withoSlash.contains(sep);
		if (contBackslash) {
				
			int lastBackslash = withoSlash.lastIndexOf(sep);

			// Backslash remove
			String[] splitPath = withoSlash.split("[/\\\\]");
			String[] remove = removeEmptyElem(splitPath);
				
			String finalPath = String.join(sep, remove);

				
			if (withoSlash.charAt(0) == sep2 && withoSlash.charAt(withoSlash.length()-1) == sep2) {
				pathname = sep + finalPath + sep;				
				filename = withoSlash.substring(lastBackslash+1);
				
			} else if (withoSlash.charAt(withoSlash.length()-1) == sep2) {
				pathname = finalPath + sep;				
				filename = "";
			} else if (withoSlash.charAt(0) == sep2) {
				pathname = sep + finalPath;				
				filename = withoSlash.substring(lastBackslash+1);	
			} else {
				pathname = finalPath;
				filename = withoSlash.substring(lastBackslash+1);
			}
			
			boolean contDott = pathname.contains(":");
			if (!isWindows() && contDott) {
				String[] splitFinalPath = pathname.split(":");
				String[] temp = removeEmptyElem(splitFinalPath);
				String result = String.join("", temp);
				result = sep + result;
				pathname = result;
			}
				
		} else {
			filename = withoSlash;
			pathname = withoSlash;
		}
		filename = filename.trim();
		
		// Lesetest ob Dateipfad auf eine lesbare Datei verweist
		File readingTest = new File(pathname);
		if (!readingTest.canRead()) {
            throw new NotPlayableException(this.pathname, "Can't read file");
        }
		
	}
	
	public void parseFilename(String filename) throws NotPlayableException {
		
			
			try {
				filename = filename.trim();
				
				if (filename.equals("")) {
					title = "";
					author = "";
				} else if (filename.equals("-")) {
					title = "-";
					author = "";
				} else {
				
				boolean hyphenNspace = filename.contains(" - ");
				
				if (hyphenNspace && filename.length() > 3) {
					String[] split = filename.split(" - ");
					String[] remove1 = removeEmptyElem(split);
		
					author = remove1[0];
					String titleTmp = remove1[1];
		
					int lastPoint = titleTmp.lastIndexOf(".");
		
					title = titleTmp.substring(0, lastPoint);
				} else {
					int lastPoint1 = filename.lastIndexOf(".");
					author = "";
					title = filename.substring(0, lastPoint1);
				}
			
			
			
		}
			} catch (RuntimeException e) {
				throw new NotPlayableException(pathname, e);
			}
		
		title = title.trim();
		author = author.trim();
	
	}

	// Getter
	public String getFilename() {
		return this.filename;
	}
	
	public String getPathname() {
		return this.pathname;
	}
	
	public String getAuthor() {
		return this.author;
		}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getAlbum() {
		return this.album;
	}
	
	public String toString() {
		String finalAuthor = getAuthor().trim();
		String finalTitle = getTitle().trim();
		
		if (finalAuthor.equals("") || finalAuthor.equals(null)) {
			return finalTitle;
		} 
		
		if (finalTitle.equals("")) {
			return finalAuthor;
		}
//		String finalString = finalAuthor + " - " + finalTitle;
		return finalAuthor + " - " + finalTitle;
		}

	
	
	
	
	// abstract Methods
	public abstract void play() throws NotPlayableException;
	
	public abstract void togglePause();
	
	public abstract void stop();
	
	public abstract String formatDuration();
	
	public abstract String formatPosition();
}


