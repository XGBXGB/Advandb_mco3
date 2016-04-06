package Network;
import java.io.Serializable;

public class SerializableTrans implements Serializable{
	private String query;
	
	public SerializableTrans(String query) {
		super();
		this.query = query;
	}
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	
	
}	
