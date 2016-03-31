import java.util.ArrayList;

public class Lock {
	private boolean isWriting;
	private int readers;
	private ArrayList<Transaction> transactions;
	private static Lock instance = null;
	
	public static Lock getInstance() {
        if (instance == null) {
            instance = new Lock();
        }
        return instance;
    }
	
	
	public Lock() {
		isWriting = false;
		readers = 0;
		transactions = new ArrayList();
	}

	public synchronized void readLock(Transaction t){
		Transaction head = null;
		//System.out.println("Transaction"+t.getTimeStamp()+" is REQUESTING for a readlock..");
		if(isWriting){
			head = transactions.get(0);
		}
		while(isWriting){
			/*if(t.getTimeStamp() < head.getTimeStamp()){
				head.die();
				head.start();
			}else{
				try{
					System.out.println("Transaction"+t.getTimeStamp()+" is WAITING for a readlock..");
					synchronized(t){
						t.wait();
					}
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}*/
			
		}
		
		readers++;
		transactions.add(t);
		//System.out.println("Transaction"+t.getTimeStamp()+" got the readlock!");
	}
	
	public synchronized void writeLock(Transaction t){
		Transaction head = null;
		//System.out.println("Transaction"+t.getTimeStamp()+" is REQUESTING for a writelock..");
		if(isWriting || readers>0){
			head = transactions.get(0);
		}
			
		while(isWriting || readers>0){
			/*if(t.getTimeStamp() < head.getTimeStamp()){
				head.die();
				head.start();
			}else{
				try{
					System.out.println("Transaction"+t.getTimeStamp()+" is WAITING for writelock..");
					synchronized(t){
						t.wait();
					}
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}*/
		}
		isWriting = true;
		transactions.add(t);
		//System.out.println("Transaction"+t.getTimeStamp()+" got the writelock!");
	}
	
	public synchronized void unlock(Transaction t){
		if(transactions.contains(t)){
			//System.out.println("Transaction"+t.getTimeStamp()+" locks unlocked!");
			transactions.remove(t);
			if(isWriting){
				isWriting = false;
			}else{
				readers--;
			}
			notifyAll();
		}else{
			//System.out.println("Transaction"+t.getTimeStamp()+" is not in the lockowners list!");
		}
	}
}
