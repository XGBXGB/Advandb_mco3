package Network;
import java.io.Serializable;

public class SerializableTrans implements Serializable{
	private String query;
	private String scope;
	private boolean toCommit;
	
	public SerializableTrans(String query, String scope) {
		super();
		this.query = query;
		this.scope = scope;
		toCommit = true;
	}
	
	public SerializableTrans(String query, String scope, boolean toCommit) {
		super();
		this.query = query;
		this.scope = scope;
		this.toCommit = toCommit;
	}
		
	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getQuery() {
		return query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}

	public boolean isToCommit() {
		return toCommit;
	}

	public void setToCommit(boolean toCommit) {
		this.toCommit = toCommit;
	}
	
	
	
	
}	
