package controller;
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
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javax.swing.JOptionPane;

import com.sun.rowset.CachedRowSetImpl;

import network.Client;
import network.SerializableTrans;
import network.Server;
import transactions.Transaction;
import transactions.Transaction1;
import transactions.Transaction2;
import view.Driver;


public class Controller {
	
	private int iso_level=1;
	private Client myClient;
	private Server myServer;
	private String name = "";
	Thread client, server;
	
	public void executeTransactions(String query, String scope, int lockIdentifier, String query2, String scope2, int lockIdentifier2){
		try{
		Transaction t1, t2;
		if(query.startsWith("SELECT")){
			t1 = new Transaction2(query, scope, lockIdentifier);
			t1.setIsolationLevel(iso_level);
			
			if(scope.equals("PALAWAN")){
				readPalawan((Transaction2)t1);
			}
			else if(scope.equals("MARINDUQUE")){
				readMarinduque((Transaction2)t1);
			}
			else{
				readBoth((Transaction2)t1);
			}
		}else{//if t1 is write
			t1 = new Transaction1(query, scope, lockIdentifier);
			t1.setIsolationLevel(iso_level);
			
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
				readPalawan((Transaction2)t2);
			}
			else if(scope2.equals("MARINDUQUE")){
				readMarinduque((Transaction2)t2);
			}
			else {
				readBoth((Transaction2)t2);
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
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}
	
	public void writePalawan(Transaction t){
		if(name.equalsIgnoreCase("CENTRAL")){
			if(myClient.checkPalawanIfExists()){
				//return true;
			}else{
				//return false;
			}
		}
		else if(name.equalsIgnoreCase("PALAWAN")){
			if(myClient.checkCentralIfExists()){
				//return true;
			}else{
				//return false;
			}
		}else if(name.equalsIgnoreCase("MARINDUQUE")){
			if(myClient.checkCentralIfExists() && myClient.checkPalawanIfExists()){
				//return true;
			}else{
				//return false;
			}
		}
	}
	
	public void writeMarinduque(Transaction t){
		if(name.equalsIgnoreCase("CENTRAL")){
			if(myClient.checkMarinduqueIfExists()){
				//return true;
			}else{
				//return false;
			}
		}
		else if(name.equalsIgnoreCase("MARINDUQUE")){
			if(myClient.checkCentralIfExists()){
				//return true;
			}else{
				//return false;
			}
		}else if(name.equalsIgnoreCase("PALAWAN")){
			if(myClient.checkCentralIfExists() && myClient.checkMarinduqueIfExists()){
				//return true;
			}else{
				//return false;
			}
		}
	}
	
	public void writeBoth(Transaction t){
		if(name.equalsIgnoreCase("CENTRAL")){
			if(myClient.checkMarinduqueIfExists() && myClient.checkPalawanIfExists()){
				//return true;
			}else{
				//return false;
			}
		}
		else if(name.equalsIgnoreCase("MARINDUQUE")){
			if(myClient.checkCentralIfExists() && myClient.checkPalawanIfExists()){
				//return true;
			}else{
				//return false;
			}
		}else {
			if(myClient.checkCentralIfExists() && myClient.checkMarinduqueIfExists()){
				//return true;
			}else{
				//return false;
			}
		}
	}
	
	public void readPalawan(Transaction2 t)throws SQLException{
		if(name.equalsIgnoreCase("PALAWAN")){
			t.beginTransaction();
			t.start();
			
			Driver.printResultSet(t.getResultSet());
				//execute from self
		}
		else if(name.equalsIgnoreCase("CENTRAL")){
			t.beginTransaction();
			t.start();
			
			Driver.printResultSet(t.getResultSet());
			//return result to driver
		}
		else if(name.equalsIgnoreCase("MARINDUQUE")){
			if(myClient.checkCentralIfExists()){
				try{
					String message = "\"READREQUEST\" ";
					byte[] prefix = message.getBytes();
					SerializableTrans st = new SerializableTrans(t.getQuery(), t.getLockIdentifier());
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
	
	public void readMarinduque(Transaction2 t) throws SQLException{
		if(name.equalsIgnoreCase("MARINDUQUE")){
			t.beginTransaction();
			t.start();
			
			Driver.printResultSet(t.getResultSet());
		}
		else if(name.equalsIgnoreCase("CENTRAL")){
			t.beginTransaction();
			t.start();
			
			Driver.printResultSet(t.getResultSet());
		}
		
		else if(name.equalsIgnoreCase("PALAWAN")){
			if(myClient.checkCentralIfExists()){
				try{
					String message = "\"READREQUEST\" ";
					byte[] prefix = message.getBytes();
					SerializableTrans st = new SerializableTrans(t.getQuery(), t.getLockIdentifier());
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
	
	public void readBoth(Transaction2 t)throws SQLException{
		if(name.equalsIgnoreCase("CENTRAL")){
			t.beginTransaction();
			t.start();
			Driver.printResultSet(t.getResultSet());
		}else{
			if(myClient.checkCentralIfExists()){
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
	
	public Client getMyClient() {
		return myClient;
	}

	public Server getMyServer() {
		return myServer;
	}

	public void setName(String name){
		this.name = name;
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
	
	
	public void startClient(){
		myClient = new Client(this);
		client = new Thread(myClient);
		client.start();
	}
	
	public void startServer(int port){
		myServer = new Server(port, this);
		server = new Thread(myServer);
		server.start();
	}
	
	public void stopServer(){
		myServer.setFlag(1);
		myServer = null;
	}
	
	public void setIsoLevel(int iso_level){
		this.iso_level = iso_level;
	}
	
	public void printResultSet(CachedRowSetImpl rs){
		Driver.printResultSet(rs);
	}
	
}
