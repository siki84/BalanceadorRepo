
import java.io.*;
import java.net.*;
import java.util.logging.Logger;


public class HaProxySocket {

	//UpgradeLogger log = new UpgradeLogger();
	//Logger log1 = log.log();
	final private  int portNumber = 10010;
	public static void main(String[] args) throws Exception {
	 // For testing only
	Socket echoSocket = null;
	PrintWriter out = null;
	BufferedReader in = null;
	try {
	    echoSocket = new Socket("54.225.83.19", 10010);
	    out = new PrintWriter(echoSocket.getOutputStream(), true);
	    in = new BufferedReader(new InputStreamReader(
	                                echoSocket.getInputStream()));
	} catch (UnknownHostException e) {
	    System.err.println("Don't know about host: taranis.");
	    System.exit(1);
	} catch (IOException e) {
	    System.err.println("Couldn't get I/O for "
	   + "the connection to");
	    System.exit(1);
	}
	BufferedReader stdIn = new BufferedReader(
                               new InputStreamReader(System.in));
	String userInput;
	String enable = "enable server noVersion/web01";
	String disable ="disable server noVersion/web01";
	String setWeight ="set weight noVersion/web01 1";
	while ((userInput = stdIn.readLine()) != null) {
		if(userInput.equalsIgnoreCase("disable"))
			out.println(disable+"\n");
	else if(userInput.equalsIgnoreCase("enable"))
		out.println(enable+"\n");
	else
		 out.println(setWeight+"\n");
	System.out.println("echo: " + in.readLine());	    
	Thread.sleep(100);	    
	return;
	}	
	out.close();
	in.close();
	stdIn.close();
	echoSocket.close();

}
   public boolean runCommand(String ipAddress,String command){
	   Socket echoSocket = null;
       PrintWriter out = null;
       BufferedReader in = null;
       try {
           echoSocket = new Socket(ipAddress, portNumber);
           out = new PrintWriter(echoSocket.getOutputStream(), true);
           in = new BufferedReader(new InputStreamReader(
                                       echoSocket.getInputStream()));
       } catch (UnknownHostException e) {
           System.err.println("Don't know about host");
           System.exit(1);
       } catch (IOException e) {
           System.err.println("Couldn't get I/O for "
                              + "the connection to");
           System.exit(1);
       }
	BufferedReader stdIn = new BufferedReader(
                                  new InputStreamReader(System.in));
	out.println(command+"\n");
    try {
		String output =  in.readLine();
		System.out.println(output);
		if(output.equals(""))
			return true;
		else return false;
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    try {
		Thread.sleep(100);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	out.close();
	try {
		in.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		stdIn.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		echoSocket.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return false;
   }
}