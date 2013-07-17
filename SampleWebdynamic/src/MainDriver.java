/*****************************************************
 * 
 * 08-722 Data Structures for Application Programmers
 * Homework 5: Building Index using BST
 * 
 * DO NOT MODIFY THIS CLASS!
 * ALL OF YOUR WORK SHOULD BE DONE IN OTHER CLASSES!
 * 
 *****************************************************/
import java.io.*;
import java.util.Scanner;
import java.util.logging.Logger;


public class MainDriver {
	
	public static void main(String[] args) throws IOException {
		// Create an object of class Upgrade Logger and use 
		UpgradeLogger log = new UpgradeLogger();
		Logger log1 = log.log();	
	 
		Scanner in = new Scanner(System.in); 
		System.out.println("Enter time interval of upgrade in minutes :");
		int upgradeInvervalInput = in.nextInt();
		log1.info("Upgrade Interval is"+upgradeInvervalInput);
		UpgradeController ug = new UpgradeController(upgradeInvervalInput);
		ug.loadData(log1);
		System.out.println("Start thread for upgrade?");
		String inputstart = in.next();
		if(inputstart.equalsIgnoreCase("Y"))
		{
			log1.info("Starting Upgrade thread");
			(new Thread(new UpgradeController())).start();	
		}
		//System.out.println("Upgardes"+ug.getNumOfUpgrades());
		ug.startUpgarde();
		// Close logger object
		log.close();
	}
	
}

