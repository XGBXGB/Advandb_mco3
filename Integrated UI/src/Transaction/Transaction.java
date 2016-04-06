package Transaction;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.sun.rowset.CachedRowSetImpl;

public interface Transaction {
	
	public static int ISO_READ_UNCOMMITTED = 1;
	public static int ISO_READ_COMMITTED = 2;
	public static int ISO_REPEATABLE_READ = 3;
	public static int ISO_SERIALIZABLE = 4;
	public static String ISO_READ_UNCOMMITTED_LBL = "Read Uncommitted";
	public static String ISO_READ_COMMITTED_LBL = "Read Committed";
	public static String ISO_REPEATABLE_READ_LBL = "Repeatable Read";
	public static String ISO_SERIALIZABLE_LBL = "Serializable";
	
	public void setIsolationLevel(int iso_level);
	
	public void beginTransaction() throws SQLException;
	
	public CachedRowSetImpl getResultSet();
	
	public boolean isDonePopulating();
	
	public String getQuery() ;
	
	public void start();
	
	public void rollback();
	
	public void commit();
	
}
