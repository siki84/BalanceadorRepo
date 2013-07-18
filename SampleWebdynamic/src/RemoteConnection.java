

import java.io.*;
import java.net.*;

/**
*
* @author Javier
*/
public class RemoteConnection {
   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) throws Exception {

	   Process p = Runtime.getRuntime().exec("ssh -i /Users/sdhawan/Downloads/nicta.pem  ubuntu@54.225.83.19 -o StrictHostKeyChecking=no ");
	   PrintWriter out = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(p.getOutputStream())), true);
	   out.println("mkdir sakshiTes");
	   //p.waitFor();

       String line;

       BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
       while((line = error.readLine()) != null){
           System.out.println(line);
       }
       error.close();

      BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
       while((line=input.readLine()) != null){
           System.out.println(line);
       }

       input.close();
       
       OutputStream outputStream = p.getOutputStream();
       PrintStream printStream = new PrintStream(outputStream);
       printStream.println();
       printStream.flush();
       printStream.close();
       out.println("exit");
   }
   
   public boolean runCommand(String connect, String command)
   {
	   Process p;
	try {
		p = Runtime.getRuntime().exec(connect);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(p.getOutputStream())), true);
		 out.println(command);
	  
	   //p.waitFor();
	   String line;

       BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
       //while((line = error.readLine()) != null){
         //  System.out.println(line);
       //}
       error.close();

      BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
    //   while((line=input.readLine()) != null){
      //     System.out.println(line);
       //}

       input.close();
       
       OutputStream outputStream = p.getOutputStream();
       PrintStream printStream = new PrintStream(outputStream);
       printStream.println();
       printStream.flush();
       printStream.close();
       out.println("exit");
	} catch (IOException e) {
		e.printStackTrace();
		return false;
		// TODO Auto-generated catch block

	}  
       return true;
   }

public void newVersion() {
	// Copy the new version of the application to the tomcat folder
	// Use runCommand to copy and rename folders
	
}

}
