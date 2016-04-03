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

public class ServerReturn{
    
    private Scanner INPUT;
    private InputStream extractor;
    private PrintWriter OUT;
    String MESSAGE ="";
    Runner parent;
    byte[] mybytearray=new byte[65000000];
    int current=0;
    String messageType="";
    
    public ServerReturn(Socket X, Runner parent){
    	this.parent = parent;
    	parent.printMessage("WEEEEEEEEEEE");
        RECEIVE(X);
        
    }
    
    public void RECEIVE(Socket X){
    	parent.printMessage("IN RECIEVE");
    	extractByte(X);
    	if(current!=-1){
    		System.out.println("INSIDE CURRENT!");
    		extractHeader(mybytearray);
    		try{
	        	if(messageType.contains("\"READREQUEST\"")){
	        		parent.printMessage("IN READREQUEST");
	        		SerializableTrans st = (SerializableTrans) deserialize(mybytearray);
	        		Transaction2 t = new Transaction2(st.getQuery(),"",st.getLockIdentifier());
	            	Thread x = new Thread(t);
	            	x.start();
	            	while(true){
	            		if(t.getResultSet()!=null)
	            			break;
	            	}
	            	DatabaseObject dbo = new DatabaseObject(t.getResultSet());
	            	String first="\"RETURNREADREQUEST\" ";
	            	String msg = "\"JOIN\" nameeeee"+"\u001a";
	            	byte[] prefix = first.getBytes();
	            	byte[] dboBytes = serialize(dbo);
	            	byte[] finalByte = byteConcat(prefix, dboBytes);
	            	parent.printMessage("ADDRESS IN RETURN REQUEST: "+X.getInetAddress());
	            	parent.sendToHost(msg.getBytes(), X.getInetAddress());
	            	
	        	}else if(messageType.contains("\"RETURNREADREQUEST\"")){
	        		DatabaseObject dbo = (DatabaseObject)deserialize(mybytearray);
	        		CachedRowSetImpl rs = dbo.getResultSet();
	        		while(rs.next()){
	        			parent.printResultSet(rs);;
	        		}
	        	}else if(messageType.contains("\"JOIN\"")){
	        		String name = extractName(mybytearray);
	        		parent.printMessage(name);
	        		parent.addNodeName(extractName(mybytearray));
	        	}
	        	X.close();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}
    }
    
    public String extractName(byte[] X){
        char z;
        String name="";
        boolean condition = false;
        int i=0;
        String temp="";
        while(condition==false){
            z=(char) X[i];
            
            if(z=='\u001a'){
                name=temp;
                condition=true;
            }
            else{
                temp=temp+z;
            }
            i++;
        }
        current=current-i;
        System.out.println("Name: "+name);
        if(current>0){
            byte[] splitter=new byte[current];
            System.arraycopy(mybytearray,i,splitter,0,splitter.length);
            mybytearray=splitter;
        }
        return name;
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
        Driver.printMessage("EXTRACT HEADER: "+messageType);
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
    
    /*public void CheckConnection() throws IOException{
        if(!SOCK.isConnected()){
            for(int i=1; i<=Server.ConnectionArray.size();i++){
                if(Server.ConnectionArray.get(i)==SOCK){
                    Server.ConnectionArray.remove(i);
                    Server.CurrentNodes.remove(i);
                }
            }
            
            for(int i=1; i<=Server.ConnectionArray.size(); i++){
                Socket TEMP_SOCK = (Socket) Server.ConnectionArray.get(i-1);
                PrintWriter TEMP_OUT = new PrintWriter(TEMP_SOCK.getOutputStream());
                TEMP_OUT.println(TEMP_SOCK.getLocalAddress().getHostName()+ "disconnected!");
                TEMP_OUT.flush();
                
                parent.printMessage(TEMP_SOCK.getLocalAddress().getHostName() +"disconnected!");
            }
        }
    }*/
    
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
    
    /*public void run(){
        try{
            try{
                INPUT = new Scanner(SOCK.getInputStream());
                OUT = new PrintWriter(SOCK.getOutputStream());
                
                while(true){
                    CheckConnection();
                    if(!INPUT.hasNext())
                        return;
                    extractByte(SOCK);
                    if(current!=-1){
                    	extractHeader(mybytearray);
                    	if(messageType.contains("\"READREQUEST\"")){
                    		Transaction2 t = (Transaction2) deserialize(mybytearray);
   		                 	Thread x = new Thread(t);
   		                 	x.start();
   		                 	
   		                 	DatabaseObject dbo = new DatabaseObject(t.getResultSet());
   		                 	String first="\"RETURNREADREQUEST\" ";
   		                 	byte[] prefix = first.getBytes();
   		                 	byte[] dboBytes = serialize(dbo);
   		                 	
   		                 	byte[] finalByte = byteConcat(prefix, dboBytes);
   		                 	
   		                 	parent.
   		                 	SOCK.getOutputStream().write(finalByte, 0, finalByte.length);
   		                 	SOCK.getOutputStream().flush();
                    	}
                    	
                    }
                    MESSAGE = INPUT.nextLine();
                    
                    parent.printMessage(SOCK.getLocalAddress().getHostName()+" said: "+ MESSAGE);
                    
                    for(int i=0; i<Server.ConnectionArray.size(); i++){
                    	parent.printMessage("i: "+i+" ConnectionArray size: "+Server.ConnectionArray.size());
                        Socket TEMP_SOCK = (Socket) Server.ConnectionArray.get(i);
                        PrintWriter TEMP_OUT = new PrintWriter(TEMP_SOCK.getOutputStream());
                        TEMP_OUT.println(MESSAGE);
                        TEMP_OUT.flush();
                        parent.printMessage("Sent to: "+ TEMP_SOCK.getLocalAddress().getHostName());
                    }
                }
            }
            finally{
                SOCK.close();
            }
        }
        catch(Exception X){
        	X.printStackTrace();
            System.out.print("IN SERVER RETURN: "+X);
        }
    }*/
            
}
