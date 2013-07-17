

import java.io.*;
import java.net.*;

/**
*
* @author Javier
*/
public class HaProxySocket {


   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) throws Exception {
       // TODO code application logic here
       String sentence;
       String modifiedSentence;
       BufferedReader inFromUser = new BufferedReader(new
InputStreamReader(System.in));

       Socket clientSocket = new Socket("10.202.143.81", 10010);
       DataOutputStream outToServer = new
DataOutputStream(clientSocket.getOutputStream());
       BufferedReader inFromServer = new BufferedReader(new
InputStreamReader(clientSocket.getInputStream()));

       sentence = inFromUser.readLine();
    //   outToServer.writeBytes(sentence);
       outToServer.writeUTF(sentence);

       while (!inFromServer.ready());
   //    modifiedSentence = inFromServer.readLine();
       char[] cbuf = new char[100];
       int a  = inFromServer.read(cbuf);
       System.out.println(a);
       clientSocket.close();
   }

}
