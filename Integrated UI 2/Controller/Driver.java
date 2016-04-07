package Controller;
import java.sql.SQLException;
import java.util.Scanner;

import Controller.Controller;
import View.MainFrame;

import com.sun.rowset.CachedRowSetImpl;

public class Driver {
	
	
	public static void main(String args[]) {
		final int PORT=2020;
		
		Controller runner = new Controller();
		boolean isLocal = true;
		runner.setName("PALAWAN");
		runner.startServer(PORT);
		runner.startClient();
		Scanner sc = new Scanner(System.in);
		int condition=0;
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
