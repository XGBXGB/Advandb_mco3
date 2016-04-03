package transactions;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Transaction2 implements Transaction, Runnable{
	String condition;
	Connection con;
	Statement stmt;
	CyclicBarrier gate;
	
	public Transaction2(CyclicBarrier gate, String condition){
		this.gate = gate;
		this.condition = condition;
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
		try {
			gate.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (BrokenBarrierException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
		//QUERY CODE BLOCK
		try{
			String lock = "LOCK TABLES people READ;";
			stmt.execute(lock);
			String SQL = "SELECT * FROM people";
			ResultSet rs = stmt.executeQuery(SQL);
			String unlock = "UNLOCK TABLES;";
			stmt.execute(unlock);
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}

	@Override
	public void die() {
		Lock locker = Lock.getInstance();
		locker.unlock(this);
	}

	@Override
	public void setCondition(String condition) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCondition() {
		// TODO Auto-generated method stub
		return null;
	}

}
