import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.sun.rowset.CachedRowSetImpl;

public class Driver {
	
	public static void printMessage(String message){
		System.out.println(message);
	}
	public static void main(String[] args){
		Runner runner = new Runner();
		Scanner sc = new Scanner(System.in);
		int condition=0;
		try{
		do{
			System.out.println("Menu:");
			System.out.println("[1] Connect to a host");
			System.out.println("[2] Start server");
			System.out.println("[3] Read Palawan transaction");
			System.out.println("[4] Read Marinduque transaction");
			System.out.println("[5] Read Both transaction");
			System.out.println("[6] Write Palawan transaction");
			System.out.println("[7] Write Marinduque transaction");
			System.out.println("[8] Write Both transaction");
			System.out.println("[9] Read data");
			condition = sc.nextInt();
			if(condition==1){
				System.out.println("Input the port of the server to connect to:");
				int port = sc.nextInt();
				System.out.println("Input the IP address of the server to connect to:");
				sc.nextLine();
	            String ip = sc.nextLine();
	            System.out.println("Input name of host:");
	            String hostName = sc.nextLine();
				runner.joinHost(("\"JOIN\" "+runner.getName())+"\u001a", InetAddress.getByName(ip), hostName);
			}
			else if(condition==3){
				runner.executeTransactions("SELECT hpq_hh_id FROM hpq_death WHERE mdeadage>94;", "BOTH", 0, "SELECT hpq_hh_id, id FROM hpq_crop WHERE crop_line=3;", "BOTH", 1);
			}
			else if(condition==2){
				if(runner.getMyServer()==null){
					System.out.println("Input the port of server to be created:");
					int port = sc.nextInt();
					runner.startServer(port);
				}else
					System.out.println("Server is already running.");
			}
			else if(condition==4){
				if(runner.getMyServer()!=null)
					runner.stopServer();
				else
					System.out.println("Server is not running.");
			}else if(condition==5){
				if(runner.getMyClient()!=null){
					//runner.readPalawan();
				}
			}else if(condition==9){
				runner.executeTransactions("SELECT * FROM hpq_death;", "BOTH", 0, "SELECT * FROM hpq_mem;", "BOTH", 1);
			}
		}while(condition != -1);
		}catch(Exception e){
			e.printStackTrace();
			sc.close();
		}
		sc.close();
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
