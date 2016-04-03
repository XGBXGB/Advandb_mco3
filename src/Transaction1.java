import java.io.Serializable;
import com.sun.rowset.CachedRowSetImpl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Transaction1 implements Transaction, Runnable, Serializable{
	int lockIdentifier;
	String scope;
	String query;
	Connection con;
	Statement stmt;
	CachedRowSetImpl cs;
	
	public Transaction1(String query, String scope, int lockIdentifier){
		this.lockIdentifier = lockIdentifier;
		this.query = query;
		this.scope = scope;
		con = DBConnect.getConnection();
	}
	
	@Override
	public void setIsolationLevel(int iso_level) {
		// TODO Auto-generated method stub
		try {
			switch(iso_level) {
			case ISO_READ_UNCOMMITTED: //ps.setString(1, "READ UNCOMMITTED"); 
					con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				break;
			case ISO_READ_COMMITTED: //ps.setString(1, "READ COMMITTED");
					con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
				break;
			case ISO_REPEATABLE_READ: //ps.setString(1, "REPEATABLE READ");
					con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			 	break;
			case ISO_SERIALIZABLE: //ps.setString(1, "SERIALIZABLE");
					con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				break;
			default: //ps.setString(1, "SERIALIZABLE");
					con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Override
	public void beginTransaction() throws SQLException{
		java.util.Date date= new java.util.Date();
		con.setAutoCommit(false);
		stmt = con.createStatement();
	}
	
	

	@Override
	public void rollback() {
		try{
			con.rollback();
		} catch (SQLException e){
			e.printStackTrace();
		}
		
	}

	@Override
	public void commit() {
		try{
			con.rollback();
		} catch (SQLException e){
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			beginTransaction();
			start();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void start() {
		try{
			String lock="";
			if(lockIdentifier%2==0)
				lock = "LOCK TABLES hpq_death WRITE;";
			else
				lock = "LOCK TABLES hpq_crop WRITE;";
			
			stmt.execute(lock);
			String SQL = query;
			stmt.executeUpdate(SQL);
			String unlock = "UNLOCK TABLES;";
			stmt.execute(unlock);
			
			con.commit();
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}

	@Override
	public void die() {
		Lock locker = Lock.getInstance();
		locker.unlock(this);
	}
	
	public ResultSet getResultSet(){
		return null;
	}

	public int getLockIdentifier() {
		return lockIdentifier;
	}



	public String getQuery() {
		return query;
	}

}
