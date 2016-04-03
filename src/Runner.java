import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javax.swing.JOptionPane;

import com.sun.rowset.CachedRowSetImpl;


public class Runner {
	
	private String iso_level="";
	private Client myClient;
	private Server myServer;
	private final String name = "PALAWAN";
	Thread client, server;
	
	public void executeTransactions(String query, String scope, int lockIdentifier, String query2, String scope2, int lockIdentifier2){
		
		Transaction t1, t2;
		if(query.startsWith("SELECT")){//if t1 is read
			t1 = new Transaction2(query, scope, lockIdentifier);
			t1.setIsolationLevel(Transaction.ISO_READ_UNCOMMITTED);
			
			if(scope.equals("PALAWAN")){
				readPalawan((Transaction2)t1);
			}
			else if(scope.equals("MARINDUQUE")){
				//readMarinduque(t1);
			}
			else{
				readBoth((Transaction2)t1);
			}
		}else{//if t1 is write
			t1 = new Transaction1(query, scope, lockIdentifier);
			t1.setIsolationLevel(Transaction.ISO_READ_UNCOMMITTED);
			
			if(scope.equals("PALAWAN")){
				writePalawan(t1);
			}
			else if(scope.equals("MARINDUQUE")){
				writeMarinduque(t1);
			}
			else{
				writeBoth(t1);
			}
		}
		
		if(query2.startsWith("SELECT")){//if t2 is read
			t2 = new Transaction2(query2, scope2, lockIdentifier2);
			t2.setIsolationLevel(Transaction.ISO_READ_UNCOMMITTED);
			
			if(scope2.equals("PALAWAN")){
				//readPalawan(t2);
			}
			else if(scope2.equals("MARINDUQUE")){
				//readMarinduque(t2);
			}
			else {
				//readBoth(t2);
			}
		}else{//if t2 is write
			t2 = new Transaction1(query2, scope2, lockIdentifier2);
			t2.setIsolationLevel(Transaction.ISO_READ_UNCOMMITTED);
			
			if(scope2.equals("PALAWAN")){
				writePalawan(t2);
			}
			else if(scope2.equals("MARINDUQUE")){
				writeMarinduque(t2);
			}
			else {
				writeBoth(t2);
			}
		}
		
		
	}
	
	public void writePalawan(Transaction t){
		if(name.equalsIgnoreCase("CENTRAL")){
			if(myServer.checkPalawanExists()){
				//return true;
			}else{
				//return false;
			}
		}
		else if(name.equalsIgnoreCase("PALAWAN")){
			if(myServer.checkCentralExists()){
				//return true;
			}else{
				//return false;
			}
		}else if(name.equalsIgnoreCase("MARINDUQUE")){
			if(myServer.checkCentralExists() && myServer.checkPalawanExists()){
				//return true;
			}else{
				//return false;
			}
		}
	}
	
	public void writeMarinduque(Transaction t){
		if(name.equalsIgnoreCase("CENTRAL")){
			if(myServer.checkMarinduqueExists()){
				//return true;
			}else{
				//return false;
			}
		}
		else if(name.equalsIgnoreCase("MARINDUQUE")){
			if(myServer.checkCentralExists()){
				//return true;
			}else{
				//return false;
			}
		}else if(name.equalsIgnoreCase("PALAWAN")){
			if(myServer.checkCentralExists() && myServer.checkMarinduqueExists()){
				//return true;
			}else{
				//return false;
			}
		}
	}
	
	public void writeBoth(Transaction t){
		if(name.equalsIgnoreCase("CENTRAL")){
			if(myServer.checkMarinduqueExists() && myServer.checkPalawanExists()){
				//return true;
			}else{
				//return false;
			}
		}
		else if(name.equalsIgnoreCase("MARINDUQUE")){
			if(myServer.checkCentralExists() && myServer.checkPalawanExists()){
				//return true;
			}else{
				//return false;
			}
		}else {
			if(myServer.checkCentralExists() && myServer.checkMarinduqueExists()){
				//return true;
			}else{
				//return false;
			}
		}
	}
	
	public void readPalawan(Transaction2 t){
		if(name.equalsIgnoreCase("PALAWAN")){
			Thread x = new Thread(t);
			x.start();
			while(true){
				if(t.getResultSet()!=null)
					break;
			}
			Driver.printResultSet(t.getResultSet());
				//execute from self
		}
		else if(name.equalsIgnoreCase("CENTRAL")){
			Thread x = new Thread(t);
			x.start();
			Driver.printResultSet(t.getResultSet());
			//return result to driver
		}
		else if(name.equalsIgnoreCase("MARINDUQUE")){
			if(myClient.checkCentralIfExists()){
				try{
					String message = "\"READREQUEST\" ";
					byte[] prefix = message.getBytes();
					byte[] trans = serialize(t);
					byte[] fin = byteConcat(prefix,trans);
					myClient.SEND(fin, myClient.getAddressFromName("CENTRAL"));
				}catch(IOException e){
					e.printStackTrace();
				}
			}else{
				Driver.printMessage("A NEEDED SERVER IS DOWN");
			}
		}
	}
	
	public void readMarinduque(Transaction2 t){
		if(name.equalsIgnoreCase("MARINDUQUE")){
			Thread x = new Thread(t);
			x.start();
			Driver.printResultSet(t.getResultSet());
		}
		else if(name.equalsIgnoreCase("CENTRAL")){
			//execute read from self
			Thread x = new Thread(t);
			x.start();
			Driver.printResultSet(t.getResultSet());
		}
		
		else if(name.equalsIgnoreCase("PALAWAN")){
			if(myClient.checkCentralIfExists()){
				try{
					String message = "\"READREQUEST\" ";
					byte[] prefix = message.getBytes();
					byte[] trans = serialize(t);
					byte[] fin = byteConcat(prefix,trans);
					myClient.SEND(fin, myClient.getAddressFromName("CENTRAL"));
				}catch(IOException e){
					e.printStackTrace();
				}
			}else{
				Driver.printMessage("A NEEDED SERVER IS DOWN");
			}
		}
	}
	
	public void readBoth(Transaction2 t){
		if(name.equalsIgnoreCase("CENTRAL")){
			Thread x = new Thread(t);
			x.start();
			Driver.printResultSet(t.getResultSet());
		}else{
			if(true){
				try{
					Driver.printMessage("CENTRAL EXISTS");
					String message = "\"READREQUEST\" ";
					byte[] prefix = message.getBytes();
					SerializableTrans st = new SerializableTrans(t.getQuery(),t.getLockIdentifier());
					byte[] trans = serialize(st);
					byte[] fin = byteConcat(prefix,trans);
					myClient.SEND(fin, myClient.getAddressFromName("CENTRAL"));
				}catch(IOException e){
					e.printStackTrace();
				}
			}else{
				Driver.printMessage("A NEEDED SERVER IS DOWN");
			}
		}
	}
	
	public static byte[] byteConcat(byte[] A, byte[] B) {
        int aLen = A.length;
        int bLen = B.length;
        byte[] C = new byte[aLen + bLen];
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
    }
	
	public void addNodeName(String name){
		myServer.addNodeName(name);
	}
	
	public static byte[] serialize(Object obj) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(obj);
	    return out.toByteArray();
	}
	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
	    ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return is.readObject();
	}
	
	
	
	
	public Client getMyClient() {
		return myClient;
	}

	public Server getMyServer() {
		return myServer;
	}

	public String getName(){
		return name;
	}
	
	public void printMessage(String message){
		Driver.printMessage(message);
	}
	
	public void sendToHost(byte[] msg, InetAddress receiver){
		myClient.SEND(msg, receiver);
	}
	
	public void joinHost(String msg, InetAddress ip, String hostName){
		if(myClient==null){
			myClient = new Client(this);
			client = new Thread(myClient);
			client.start();
			myClient.JOIN(name, ip, hostName);
			Driver.printMessage("MYCLIENT IS NULL");
		}else{
			myClient.JOIN(name, ip, hostName);
		}
	}
	
	
	/*public void connectToHost (String host, int port){
		if(myClient==null){
			myClient = new Client(this);
			client = new Thread(myClient);
			client.start();
			myClient.addHost(host, port);
		}else{
			myClient.addHost(host, port);
		}
	}*/
	
	/*public void disconnectFromHosts(){
		try{
			//myClient.DISCONNECT();
			myClient.setFlag(1);
			myClient = null;
		}catch (IOException e){
			e.printStackTrace();
		}
	}*/
	
	public void startServer(int port){
		myServer = new Server(port, this);
		server = new Thread(myServer);
		server.start();
	}
	
	public void stopServer(){
		myServer.setFlag(1);
		myServer = null;
	}
	
	public void setIsoLevel(String iso_level){
		this.iso_level = iso_level;
		/*
		 case ISO_READ_UNCOMMITTED: //ps.setString(1, "READ UNCOMMITTED"); 
						con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
					break;
				case ISO_READ_COMMITTED: //ps.setString(1, "READ COMMITTED");
						con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
					break;
				case ISO_REPEATABLE_READ: //ps.setString(1, "REPEATABLE READ");
						con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
				 	break;
				case ISO_SERIALIZABLE:
		 */
	}
	
	public void printResultSet(CachedRowSetImpl rs){
		Driver.printResultSet(rs);
	}
	
}
