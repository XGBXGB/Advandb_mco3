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
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;


public class Server implements Runnable{
    public static ArrayList<Socket> ConnectionArray = new ArrayList<Socket>();
    public static ArrayList<String> CurrentUsers = new ArrayList<String>();
    
    
    
    public void run(){
        try{
        	final int PORT = 444;
            ServerSocket SERVER = new ServerSocket(PORT);
            System.out.println("Waiting for clients");
            
            while(true){
            	Socket SOCK = SERVER.accept();
                ConnectionArray.add(SOCK);
                
                System.out.println("Client connected from: "+SOCK.getLocalAddress().getHostName());
                
                AddUserName(SOCK);
                
                ServerReturn CHAT = new ServerReturn(SOCK);
                Thread X = new Thread(CHAT);
                X.start();
                UpdateUsers(); 
            }
        }catch(IOException X){
                X.printStackTrace();
        }
   }
    
    public static void AddUserName(Socket X) throws IOException {
        Scanner INPUT = new Scanner(X.getInputStream());
        String UserName = INPUT.nextLine();
        CurrentUsers.add(UserName);
        
        for(int i=1; i<= Server.ConnectionArray.size(); i++){
            Socket TEMP_SOCK = (Socket) Server.ConnectionArray.get(i-1);
            PrintWriter OUT = new PrintWriter(TEMP_SOCK.getOutputStream());
            OUT.println("#?!" + CurrentUsers);
            OUT.flush();
        }
        
    }
    
    public static void UpdateUsers() throws IOException {
        for(int i=0;i<Server.ConnectionArray.size();i++){
            Socket TEMPSOCK = (Socket) Server.ConnectionArray.get(i);
            PrintWriter OUT = new PrintWriter(TEMPSOCK.getOutputStream());
            OUT.println("User \""+Server.CurrentUsers.get(Server.CurrentUsers.size()-1)+"\" has joined"+"\nNew set of users:");
            //OUT.flush();
            for(int j=0;j<Server.ConnectionArray.size();j++){
                OUT.println("["+j+"]"+Server.CurrentUsers.get(j));
                
                //OUT.flush();
            }
            OUT.flush();
        }
    }
}
