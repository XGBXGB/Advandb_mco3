package view;
import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.sun.rowset.CachedRowSetImpl;

import controller.Controller;

public class Driver {
	static final int PORT=2020;
	
	public static void main(String[] args){
		Controller runner = new Controller();
		boolean isLocal = true;
		runner.setName("PALAWAN");
		runner.startServer(PORT);
		runner.startClient();
		Scanner sc = new Scanner(System.in);
		int condition=0;
		
		try{
			do{
				System.out.println("Menu:");
				System.out.println("[1] Read read transaction");
				System.out.println("[2] Set to global");
				System.out.println("[3] Set to local");
				condition = sc.nextInt();
				if(condition==1){
					runner.executeTransactions("SELECT hpq_hh_id FROM hpq_death WHERE mdeadage>94;", "BOTH", "SELECT hpq_hh_id FROM hpq_death WHERE mdeadage=94;", "BOTH", isLocal);
				}else if(condition==2){
					isLocal = true;
				}else if(condition==3){
					isLocal = false;
				}
			}while(condition != -1);
		}catch(Exception e){
			e.printStackTrace();
			sc.close();
		}
		sc.close();
	}
	
	public static void printMessage(String message){
		System.out.println(message);
	}
	
	public static void printResultSet(CachedRowSetImpl rs){
		try{
			System.out.println("TRANSACTION BATCH:!!!!!!!!!");
			while(rs.next()){
				System.out.println("RS: "+rs.getString(1));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
}
