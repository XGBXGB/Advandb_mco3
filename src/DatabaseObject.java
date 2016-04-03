import java.io.Serializable;
import java.sql.ResultSet;

import com.sun.rowset.CachedRowSetImpl;

public class DatabaseObject implements Serializable {
	CachedRowSetImpl rs;
	
	public DatabaseObject(CachedRowSetImpl rs){
		this.rs = rs;
	}
	
	public void setResultSet(CachedRowSetImpl rs){
		this.rs = rs;
	}
	
	public CachedRowSetImpl getResultSet(){
		return rs;
	}
}
