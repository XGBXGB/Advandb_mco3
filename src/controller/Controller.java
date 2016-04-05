package controller;

import com.sun.rowset.CachedRowSetImpl;
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
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javax.swing.JOptionPane;

import network.Client;
import network.SerializableTrans;
import network.Server;
import transactions.Transaction;
import transactions.Transaction1;
import transactions.Transaction2;
import view.Driver;

public class Controller {

    private int iso_level = 1;
    private Client myClient;
    private Server myServer;
    private CachedRowSetImpl cs;
    private String name = "";
    private ArrayList<Transaction> transactions;
    private ArrayList<String> queries, scopes;
    Thread client, server;

    public Controller() {
        transactions = new ArrayList();
        myClient = null;
        myServer = null;
        cs = null;
    }

    public void executeTransactions(String query, String scope, String query2, String scope2, boolean isGlobal) {
        try {
            Transaction t1, t2;
            Thread x, x2;
            queries = new ArrayList();
            scopes = new ArrayList();
            /*if (isGlobal) {
             if (query.startsWith("SELECT")) {
             t1 = new Transaction2(query, scope);
             t1.setIsolationLevel(iso_level);
             x = new Thread((Transaction2) t1);

             } else {
             t1 = new Transaction1(query, scope);
             t1.setIsolationLevel(iso_level);
             x = new Thread((Transaction1) t1);
             }
             if (query2.startsWith("SELECT")) {
             t2 = new Transaction2(query2, scope2);
             t2.setIsolationLevel(iso_level);
             x2 = new Thread((Transaction2) t2);
             } else {
             t2 = new Transaction1(query2, scope2);
             t2.setIsolationLevel(iso_level);
             x2 = new Thread((Transaction1) t2);
             }
             x.start();
             x2.start();
             while (true) {
             if (t1.isDonePopulating() && t2.isDonePopulating()) {
             break;
             }
             }
             Driver.printResultSet(t1.getResultSet());
             Driver.printResultSet(t2.getResultSet());

             }*/ //else {
            queries.add("SELECT hpq_hh_id FROM hpq_death WHERE mdeadage>94;");
            scopes.add("BOTH");
            queries.add("SELECT hpq_hh_id FROM hpq_death WHERE mdeadage=94;");
            scopes.add("BOTH");
            Transaction trans;
            for(int i=0; i<queries.size() && i<scopes.size(); i++){
            	if (queries.get(i).startsWith("SELECT")) {
                    if (scopes.get(i).equals("PALAWAN")) {
                    	trans = new Transaction2(queries.get(i), scopes.get(i));
                        trans.setIsolationLevel(iso_level);
                        readPalawan((Transaction2) trans);
                    } else if (scopes.get(i).equals("MARINDUQUE")) {
                    	trans = new Transaction2(queries.get(i), scopes.get(i));
                        trans.setIsolationLevel(iso_level);
                        readMarinduque((Transaction2) trans);
                    } else {
                    	trans = new Transaction2(queries.get(i), scopes.get(i));
                        trans.setIsolationLevel(iso_level);
                        readBoth((Transaction2) trans);
                    }
                } else {//if t1 is write
                    if (scopes.get(i).equals("PALAWAN")) {
                    	trans = new Transaction1(queries.get(i), scopes.get(i));
                        trans.setIsolationLevel(iso_level);
                        writePalawan(trans);
                    } else if (scope.equals("MARINDUQUE")) {
                    	trans = new Transaction1(queries.get(i), scopes.get(i));
                        trans.setIsolationLevel(iso_level);
                        writeMarinduque(trans);
                    } else {
                    	trans = new Transaction1(queries.get(i), scopes.get(i));
                        trans.setIsolationLevel(iso_level);
                        writeBoth(trans);
                    }
                }
            }

            /*for (int i = 0; i < transactions.size(); i++) {
            	
                for (int j = 0; j < queries.size() && j < scopes.size(); j++) {
                	
                    if (queries.get(j).equals("SELECT")) {
                        if (scopes.get(j).equals("PALAWAN")) {
                            readPalawan((Transaction2) transactions.get(i));
                        } else if (scopes.get(j).equals("MARINDUQUE")) {
                            readMarinduque((Transaction2) transactions.get(i));
                        } else {
                            readBoth((Transaction2) transactions.get(i));
                        }
                    } else {//if t1 is write
                        if (scope.equals("PALAWAN")) {
                            writePalawan(t1);
                        } else if (scope.equals("MARINDUQUE")) {
                            writeMarinduque(t1);
                        } else {
                            writeBoth(t1);
                        }
                    }
                }
            }*/

            /* if (query2.startsWith("SELECT")) {//if t2 is read
             t2 = new Transaction2(query2, scope2);
             t2.setIsolationLevel(Transaction.ISO_READ_UNCOMMITTED);

             if (scope2.equals("PALAWAN")) {
             readPalawan((Transaction2) t2);
             } else if (scope2.equals("MARINDUQUE")) {
             readMarinduque((Transaction2) t2);
             } else {
             readBoth((Transaction2) t2);
             }
             } else {//if t2 is write
             t2 = new Transaction1(query2, scope2);
             t2.setIsolationLevel(Transaction.ISO_READ_UNCOMMITTED);

             if (scope2.equals("PALAWAN")) {
             writePalawan(t2);
             } else if (scope2.equals("MARINDUQUE")) {
             writeMarinduque(t2);
             } else {
             writeBoth(t2);
             }
             }*/
            //    }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void writePalawan(Transaction t) {
        if (name.equalsIgnoreCase("CENTRAL")) {
            if (myClient.checkPalawanIfExists()) {
                //return true;
            } else {
                //return false;
            }
        } else if (name.equalsIgnoreCase("PALAWAN")) {
            if (myClient.checkCentralIfExists()) {
                //return true;
            } else {
                //return false;
            }
        } else if (name.equalsIgnoreCase("MARINDUQUE")) {
            if (myClient.checkCentralIfExists() && myClient.checkPalawanIfExists()) {
                //return true;
            } else {
                //return false;
            }
        }
    }

    public void writeMarinduque(Transaction t) {
        if (name.equalsIgnoreCase("CENTRAL")) {
            if (myClient.checkMarinduqueIfExists()) {
                //return true;
            } else {
                //return false;
            }
        } else if (name.equalsIgnoreCase("MARINDUQUE")) {
            if (myClient.checkCentralIfExists()) {
                //return true;
            } else {
                //return false;
            }
        } else if (name.equalsIgnoreCase("PALAWAN")) {
            if (myClient.checkCentralIfExists() && myClient.checkMarinduqueIfExists()) {
                //return true;
            } else {
                //return false;
            }
        }
    }

    public void writeBoth(Transaction t) {
        if (name.equalsIgnoreCase("CENTRAL")) {
            if (myClient.checkMarinduqueIfExists() && myClient.checkPalawanIfExists()) {
                //return true;
            } else {
                //return false;
            }
        } else if (name.equalsIgnoreCase("MARINDUQUE")) {
            if (myClient.checkCentralIfExists() && myClient.checkPalawanIfExists()) {
                //return true;
            } else {
                //return false;
            }
        } else {
            if (myClient.checkCentralIfExists() && myClient.checkMarinduqueIfExists()) {
                //return true;
            } else {
                //return false;
            }
        }
    }

    public void readPalawan(Transaction2 t) throws SQLException {
        if (name.equalsIgnoreCase("PALAWAN")) {
            Thread x = new Thread(t);
            x.start();
            while (true) {
                if (t.isDonePopulating()) {
                    break;
                }
            }
            Driver.printResultSet(t.getResultSet());
        } else if (name.equalsIgnoreCase("CENTRAL")) {
            Thread x = new Thread(t);
            x.start();
            while (true) {
                if (t.isDonePopulating()) {
                    break;
                }
            }
            Driver.printResultSet(t.getResultSet());
        } else if (name.equalsIgnoreCase("MARINDUQUE")) {
            if (myClient.checkCentralIfExists()) {
                try {
                    String message = "\"READREQUEST\" ";
                    byte[] prefix = message.getBytes();
                    SerializableTrans st = new SerializableTrans(t.getQuery());
                    byte[] trans = serialize(st);
                    byte[] fin = byteConcat(prefix, trans);
                    myClient.SEND(fin, myClient.getAddressFromName("CENTRAL"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Driver.printMessage("A NEEDED SERVER IS DOWN");
            }
        }
    }

    public void readMarinduque(Transaction2 t) throws SQLException {
        if (name.equalsIgnoreCase("MARINDUQUE")) {
            Thread x = new Thread(t);
            x.start();
            while (true) {
                if (t.isDonePopulating()) {
                    break;
                }
            }
            Driver.printResultSet(t.getResultSet());
        } else if (name.equalsIgnoreCase("CENTRAL")) {
            Thread x = new Thread(t);
            x.start();
            while (true) {
                if (t.isDonePopulating()) {
                    break;
                }
            }
            Driver.printResultSet(t.getResultSet());
        } else if (name.equalsIgnoreCase("PALAWAN")) {
            if (myClient.checkCentralIfExists()) {
                try {
                    String message = "\"READREQUEST\" ";
                    byte[] prefix = message.getBytes();
                    SerializableTrans st = new SerializableTrans(t.getQuery());
                    byte[] trans = serialize(st);
                    byte[] fin = byteConcat(prefix, trans);
                    myClient.SEND(fin, myClient.getAddressFromName("CENTRAL"));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Driver.printMessage("A NEEDED SERVER IS DOWN");
            }
        }
    }

    public void readBoth(Transaction2 t) throws SQLException {
        if (name.equalsIgnoreCase("CENTRAL")) {
            Thread x = new Thread(t);
            x.start();
            while (true) {
                if (t.isDonePopulating()) {
                    break;
                }
            }
            Driver.printResultSet(t.getResultSet());
        } else {
            if (myClient.checkCentralIfExists()) {
                try {
                    Driver.printMessage("CENTRAL EXISTS");
                    String message = "\"READREQUEST\" ";
                    byte[] prefix = message.getBytes();
                    SerializableTrans st = new SerializableTrans(t.getQuery());
                    byte[] trans = serialize(st);
                    byte[] fin = byteConcat(prefix, trans);
                    myClient.SEND(fin, myClient.getAddressFromName("CENTRAL"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (name.equalsIgnoreCase("MARINDUQUE")) {
                    if (myClient.checkPalawanIfExists()) {
                        try {
                            t.beginTransaction();
                            t.start();
                            cs = t.getResultSet();

                            Driver.printMessage("CENTRAL EXISTS");
                            String message = "\"READREQUESTCOMBINE\" ";
                            byte[] prefix = message.getBytes();
                            SerializableTrans st = new SerializableTrans(t.getQuery());
                            byte[] trans = serialize(st);
                            byte[] fin = byteConcat(prefix, trans);
                            myClient.SEND(fin, myClient.getAddressFromName("PALAWAN"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Driver.printMessage("A NEEDED SERVER IS DOWN");
                    }
                } else {
                    if (myClient.checkMarinduqueIfExists()) {
                        try {
                            t.beginTransaction();
                            t.start();
                            cs = t.getResultSet();

                            Driver.printMessage("CENTRAL EXISTS");
                            String message = "\"READREQUESTCOMBINE\" ";
                            byte[] prefix = message.getBytes();
                            SerializableTrans st = new SerializableTrans(t.getQuery());
                            byte[] trans = serialize(st);
                            byte[] fin = byteConcat(prefix, trans);
                            myClient.SEND(fin, myClient.getAddressFromName("MARINDUQUE"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Driver.printMessage("A NEEDED SERVER IS DOWN");
                    }
                }
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

    public void addNodeName(String name) {
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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void printMessage(String message) {
        Driver.printMessage(message);
    }

    public void sendToHost(byte[] msg, InetAddress receiver) {
        myClient.SEND(msg, receiver);
    }

    public void startClient() {
        myClient = new Client(this);
        client = new Thread(myClient);
        client.start();
    }

    public void startServer(int port) {
        myServer = new Server(port, this);
        server = new Thread(myServer);
        server.start();
    }

    public void stopServer() {
        myServer.setFlag(1);
        myServer = null;
    }

    public void setIsoLevel(int iso_level) {
        this.iso_level = iso_level;
    }

    public void printResultSet(CachedRowSetImpl rs) {
        Driver.printResultSet(rs);
    }

    public void printCombinedResultSet(CachedRowSetImpl rs2) {
        Driver.printResultSet(cs);
        cs = null;
        Driver.printResultSet(rs2);
    }

    public CachedRowSetImpl getCs() {
        return cs;
    }

    public void setCs(CachedRowSetImpl cs) {
        this.cs = cs;
    }

}
