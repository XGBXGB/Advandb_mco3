import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Client implements Runnable{
    Socket SOCK;
    int port = 1414;
    ArrayList<InetAddress> hosts;
    ArrayList<String> names;
    Scanner INPUT;
    Scanner SEND = new Scanner(System.in);
    PrintWriter OUT;
    Runner parent;
    int flag=0;
    
    public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
    
    public int getHostsSize(){
    	return hosts.size();
    }
    
    public Client(Runner parent){
        this.parent = parent;
        hosts = new ArrayList();
        names = new ArrayList();
    }
    
    public void run(){
        try{
            try{
                CheckStream(); 
            }
            finally{
                //DISCONNECT();
            }
        }
        catch(Exception X){
        	X.printStackTrace();
            System.out.print(X);
        }
    }
    
    public void addHost(String host, int port, String name){
    	Socket temp_sock = null;
		try {
			temp_sock = new Socket(host, port);
			OUT = new PrintWriter(temp_sock.getOutputStream());
			OUT.println(parent.getName()+": has connected to your server.");
            OUT.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//hosts.add(temp_sock);
    	names.add(name);
        parent.printMessage("(CLIENT) You connected to: " + host);
    }
    
   /* public void DISCONNECT() throws IOException{
    	for(int i=0; i<hosts.size(); i++){
    		Socket temp = hosts.get(i);
    		INPUT = new Scanner(temp.getInputStream());
	        OUT = new PrintWriter(temp.getOutputStream());
	        OUT.println(parent.getName() + "has disconnected");
	        OUT.flush();
	        temp.close();
    	}
    	hosts.clear();
    }*/
    
    public void CheckStream(){
        while(true){
        	if(flag==1){
        		parent.printMessage("CLIENT STOPPED");
        		break;
        	}
            RECEIVE();
        }
    }
    
    public void RECEIVE(){
        
    }
    
    public void SEND (byte[] msg, InetAddress receiver){
    	try{
			Socket temp = new Socket(receiver,port);
			OutputStream output = temp.getOutputStream();
	        //OUT = new PrintWriter(temp.getOutputStream());
	        output.write(msg, 0, msg.length);
	        //temp.getOutputStream().close();
	        //OUT.flush();
	        output.flush();
	        temp.close();
            output.close();
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }
    
    public void JOIN (String name, InetAddress receiver, String hostName){
    	try{
    		String msg = "\"JOIN\" "+name+"\u001a";
			Socket temp = new Socket(receiver,port);
			OutputStream output = temp.getOutputStream();
			parent.printMessage("derived inet: "+temp.getInetAddress());
			parent.printMessage("inet: "+receiver);
			hosts.add(receiver);
			names.add(hostName);
	        //OUT = new PrintWriter(temp.getOutputStream());
	        output.write(msg.getBytes(), 0, msg.getBytes().length);
	        
	        output.flush();
	        temp.close();
            output.close();
	        //temp.getOutputStream().close();
	        //OUT.flush();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public boolean checkMarinduqueIfExists(){
    	try{
    		Socket s = new Socket(hosts.get(names.indexOf("MARINDUQUE")), port);
    	}catch(IOException e){
    		e.printStackTrace();
    		return false;
    	}
    	return true;
    }
    
    public boolean checkCentralIfExists(){
    	try{
    		Socket s = new Socket(hosts.get(names.indexOf("CENTRAL")), port);
    		s.close();
    	}catch(IOException e){
    		e.printStackTrace();
    		return false;
    	}
    	return true;
    }
    
    public boolean checkPalawanIfExists(){
    	try{
    		Socket s = new Socket(hosts.get(names.indexOf("PALAWAN")), port);
    		
    	}catch(IOException e){
    		e.printStackTrace();
    		return false;
    	}
    	return true;
    }
    
    public InetAddress getAddressFromName(String name){
    	return hosts.get(names.indexOf(name));
    }
            
}
