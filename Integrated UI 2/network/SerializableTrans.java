package network;
import java.io.Serializable;

public class SerializableTrans implements Serializable{
	private String query;
	private String scope;
	private boolean toCommit;
	private int iso_level;
	
	public SerializableTrans(String query, String scope, int iso_level) {
		super();
		this.query = query;
		this.scope = scope;
		toCommit = true;
		this.iso_level = iso_level;
	}
	
	public SerializableTrans(String query, String scope, boolean toCommit, int iso_level) {
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

	public int getIso_level() {
		return iso_level;
	}

	public void setIso_level(int iso_level) {
		this.iso_level = iso_level;
	}
	
	
	
	
	
	
}	
