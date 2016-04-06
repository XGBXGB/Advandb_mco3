package Network;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Christian Gabriel
 */

import java.io.*;
import java.net.*;
import java.sql.ResultSet;
import java.util.Scanner;

import com.sun.rowset.CachedRowSetImpl;

import Controller.Controller;
import Transaction.Transaction2;

public class ServerReturn{
    
    private Scanner INPUT;
    private InputStream extractor;
    private PrintWriter OUT;
    String MESSAGE ="";
    Controller parent;
    byte[] mybytearray=new byte[65000000];
    int current=0;
    String messageType="";
    
    public ServerReturn(Socket X, Controller parent){
    	this.parent = parent;
    	parent.printMessage("Created server return");
        RECEIVE(X);
    }
    
    public synchronized void RECEIVE(Socket X){
    	parent.printMessage("IN RECIEVE");
    	extractByte(X);
    	if(current!=-1){
    		extractHeader(mybytearray);
    		try{
	        	if(messageType.contains("\"READREQUEST\"")){
	        		parent.printMessage("IN READREQUEST");
	        		SerializableTrans st = (SerializableTrans) deserialize(mybytearray);
	        		Transaction2 t = new Transaction2(st.getQuery(),"");
	            	t.beginTransaction();
	            	t.start();
	            	DatabaseObject dbo = new DatabaseObject(t.getResultSet());
	            	String first="\"RETURNREADREQUEST\" ";
	            	byte[] prefix = first.getBytes();
	            	byte[] dboBytes = serialize(dbo);
	            	byte[] finalByte = byteConcat(prefix, dboBytes);
	            	parent.printMessage("ADDRESS IN RETURN REQUEST: "+X.getInetAddress());
	            	parent.sendToHost(finalByte, X.getInetAddress());
	            	
	        	}else if(messageType.contains("\"RETURNREADREQUEST\"")){
	        		DatabaseObject dbo = (DatabaseObject)deserialize(mybytearray);
	        		CachedRowSetImpl rs = dbo.getResultSet();
	        		parent.printResultSet(rs);
	        	}else if(messageType.contains("\"READREQUESTCOMBINE\"")){
	        		SerializableTrans st = (SerializableTrans) deserialize(mybytearray);
	        		Transaction2 t = new Transaction2(st.getQuery(),"");
	            	t.beginTransaction();
	            	t.start();
	            	DatabaseObject dbo = new DatabaseObject(t.getResultSet());
	            	String first="\"RETURNREADREQUESTCOMBINE\" ";
	            	byte[] prefix = first.getBytes();
	            	byte[] dboBytes = serialize(dbo);
	            	byte[] finalByte = byteConcat(prefix, dboBytes);
	            	parent.printMessage("ADDRESS IN RETURN REQUEST: "+X.getInetAddress());
	            	parent.sendToHost(finalByte, X.getInetAddress());
	        	}else if(messageType.contains("\"RETURNREADREQUESTCOMBINE\"")){
	        		DatabaseObject dbo = (DatabaseObject)deserialize(mybytearray);
	        		CachedRowSetImpl rs = dbo.getResultSet();
	        		parent.printCombinedResultSet(rs);
	        		
	        	}
	        	X.close();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    }
    
    public void extractHeader(byte[] X){
        char z;
        boolean condition = false;
        int i=0;
        String temp="";
        int count=0;
        while(condition==false){
            z=(char) X[i];
            temp=temp+z;
            if(z=='"' && count==0){
                count++;
            }
            else if(z=='"' && count==1){
                condition=true;
            }
            i++;
        }
        messageType=temp;
        parent.printMessage("EXTRACT HEADER: "+messageType);
        current=current-(i+1);
        if(current>0){
            byte[] splitter=new byte[current];
            System.arraycopy(mybytearray,i+1,splitter,0,splitter.length);
            mybytearray=splitter;
        }
        System.out.println("EXTRACT HEADER");
    }
    
    
    public void extractByte(Socket X){
        try{
        	parent.printMessage(X.getInetAddress().toString()+" XXX "+X.getInetAddress().getLocalHost().toString());
        	parent.printMessage("in extract byte: "+current);
            int bytesread;
            parent.printMessage("after bytesread");
            extractor = X.getInputStream();
            parent.printMessage("after extractor");
            bytesread = extractor.read(mybytearray, 0, mybytearray.length);
            parent.printMessage("after bytesread");
            current = bytesread;
            
            do{
            	parent.printMessage("bytesread: "+bytesread);
                bytesread=extractor.read(mybytearray,current,mybytearray.length-current);
                if(bytesread >= 0)
                    current += bytesread;
            }while(bytesread>-1);
            parent.printMessage("current: "+current);
        }
        catch(Exception e1){
            e1.printStackTrace();
        }
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
    
	public static byte[] byteConcat(byte[] A, byte[] B) {
        int aLen = A.length;
        int bLen = B.length;
        byte[] C = new byte[aLen + bLen];
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
    }
        
}
