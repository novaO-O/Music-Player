package studiplayer.audio;

public class NotPlayableException extends Exception{
	String path;

	public NotPlayableException(String pathname, String msg) {
		super(msg);
		this.path = pathname;
	}
	
	public NotPlayableException(String pathname, Throwable t) {
		super(t);
		this.path = pathname;
	}
	
	public NotPlayableException(String pathname, String msg, Throwable t) {
		super(msg, t);
		this.path = pathname;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.path + super.toString();
	}
	
	
}
