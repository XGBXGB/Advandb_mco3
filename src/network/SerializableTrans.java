package network;
import java.io.Serializable;

public class SerializableTrans implements Serializable{
	private String query;
	private int lockIdentifier;
	
	public SerializableTrans(String query, int lockIdentifier) {
		super();
		this.query = query;
		this.lockIdentifier = lockIdentifier;
	}
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public int getLockIdentifier() {
		return lockIdentifier;
	}
	public void setLockIdentifier(int lockIdentifier) {
		this.lockIdentifier = lockIdentifier;
	}
	
	
}	
