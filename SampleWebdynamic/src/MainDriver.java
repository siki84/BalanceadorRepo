/*
 * Main class for testing the backend
 */
import java.io.*;
import java.util.Scanner;
import java.util.logging.Logger;


public class MainDriver {
	
	public static void main(String[] args) throws IOException {
		// Create an object of class Upgrade Logger and use 
	//	UpgradeLogger log = new UpgradeLogger();
		//Logger log1 = log.log();	
	 
		Scanner in = new Scanner(System.in); 
		System.out.println("Enter time interval of upgrade in minutes :");
		int upgradeInvervalInput = in.nextInt();
		System.out.println("Upgrade Interval is"+upgradeInvervalInput);
		UpgradeController ug = new UpgradeController(upgradeInvervalInput);
		//System.out.println(log1);
		ug.loadData();
		System.out.println("Start thread for upgrade?");
		String inputstart = in.next();
		if(inputstart.equalsIgnoreCase("Y"))
		{
			System.out.println("Starting Upgrade thread");
			(new Thread(new UpgradeController())).start();	
		}
		
		ug.reset();
		
		// Close logger object
		//log.close();
	}
	
}

