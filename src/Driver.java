import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javax.swing.JOptionPane;


public class Driver {

	
	public static void main(String[] args){
		final CyclicBarrier gate = new CyclicBarrier(6);
		Transaction1 t1 = new Transaction1(gate, "1");
		t1.setIsolationLevel(Transaction.ISO_READ_UNCOMMITTED);
		Thread x = new Thread(t1);
		
		Transaction1 t11 = new Transaction1(gate,"2");
		t11.setIsolationLevel(Transaction.ISO_READ_UNCOMMITTED);
		Thread x11 = new Thread(t11);
		
		Transaction2 t2 = new Transaction2(gate,"Z1");
		t2.setIsolationLevel(Transaction.ISO_READ_UNCOMMITTED);
		Thread x2 = new Thread(t2);
		
		Transaction2 t22 = new Transaction2(gate,"Z2");
		t22.setIsolationLevel(Transaction.ISO_READ_UNCOMMITTED);
		Thread x22 = new Thread(t22);
		
		Transaction1 t111 = new Transaction1(gate,"3");
		t111.setIsolationLevel(Transaction.ISO_READ_UNCOMMITTED);
		Thread x111 = new Thread(t111);
		
		x.start();
		
		x11.start();
		
		x2.start();
		
		x22.start();
		
		x111.start();
		
		try {
			gate.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (BrokenBarrierException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		/*Server SER = new Server();
        Thread X = new Thread(SER);
        X.start();
        new Client();*/
	}
	
	/*public static void Connect(){
        try{
            final int PORT = 444;
            final String HOST = "192.168.1.6";
            Socket SOCK = new Socket(HOST,PORT);
            System.out.println("You connected to: " + HOST);
            
            ChatClient = new Client(SOCK);
            
            
            PrintWriter OUT = new PrintWriter(SOCK.getOutputStream());
            OUT.println(UserName);
            OUT.flush();
            
            Thread X = new Thread(ChatClient);
            X.start();
        }
        catch(Exception X){
            System.out.println(X);
            JOptionPane.showMessageDialog(null,"Server not reponding");
            System.exit(0);
        }
            
    }*/
}
