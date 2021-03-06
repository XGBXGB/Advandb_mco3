package transactions;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.sun.rowset.CachedRowSetImpl;

import database.DBConnect;

public class Transaction2 implements Transaction, Serializable, Runnable{
	String scope;
	String query;
	Connection con;
	Statement stmt;
	volatile CachedRowSetImpl cs;
	volatile boolean donePopulating;
	
	public Transaction2(String query, String scope){
		this.scope = scope;
		cs = null;
		this.query = query;
		con = DBConnect.getConnection();
		donePopulating = false;
	}
	
	
	


	public String getQuery() {
		return query;
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
	public void start() {
		//QUERY CODE BLOCK
		try{
			String lock="";
			lock = "LOCK TABLES hpq_death READ;";
			stmt.execute(lock);
			ResultSet rs = stmt.executeQuery(query);
			cs = new CachedRowSetImpl();
			cs.populate(rs);
			String unlock = "UNLOCK TABLES;";
			Statement stmt2 = con.createStatement();
			stmt2.execute(unlock);
			donePopulating = true;
			System.out.println("DONE EXECUTING TRANS2 "+query);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public boolean isDonePopulating(){
		return donePopulating;
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
	
	public void setQuery(String query){
		this.query = query;
	}
	
	public CachedRowSetImpl getResultSet(){
		return cs;
	}


}
