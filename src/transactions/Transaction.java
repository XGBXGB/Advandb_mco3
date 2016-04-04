package transactions;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public interface Transaction {
	
	public static int ISO_READ_UNCOMMITTED = 1;
	public static int ISO_READ_COMMITTED = 2;
	public static int ISO_REPEATABLE_READ = 3;
	public static int ISO_SERIALIZABLE = 4;
	
	
	public void setIsolationLevel(int iso_level);
	
	public void beginTransaction() throws SQLException;
	
	public ResultSet getResultSet();
	
	public int getLockIdentifier() ;
	
	public String getQuery() ;
	
	public void start();
	
	public void rollback();
	
	public void commit();
	
}
