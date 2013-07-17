/* This is the main class that provides the interface to the webservice
 * 
 */
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class UpgradeController  implements Runnable {
	// Class variables
	static int upgradeInterval;
	// true represents upgrade one by once from 1st server
	// false represents upgrade server from one load balancer and then from the other
	// Default is true
	static boolean sequentialSequence=true;
	
	static ArrayList<LoadBalancer> loadbalancers= new ArrayList<LoadBalancer>();
	static int numOfUpgrades= 0;
	
	// Object pass 
	// static variables
	public UpgradeController(int upgradeInvervalInput)
	{
		this.upgradeInterval = upgradeInvervalInput;
		this.sequentialSequence = true;
	
	}
	public  int getNumOfUpgrades()
	{
		System.out.println(this.numOfUpgrades);
		
		return this.numOfUpgrades;
		
	}
	public UpgradeController() {
		// TODO Auto-generated constructor stub
	}

	public boolean loadData(Logger log) {
	 /* Return true if the data is loaded successfully 
	  * else return false
	  * Check for the correct format
	  * empty file etc
	  * send appropriate message
	  */

		ArrayList<String> fullinfo = new ArrayList<String>();
		Scanner scanner = null;
		LoadBalancer lb = null;
		Server server = null;
		String[] loadBalancerInfo = null;
		String[] serverInfo = null;
		try {
			scanner = new Scanner(new File("TestConfig.txt"));
			while(scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				//log.info(line);
				if(line.contains("/*") || line.equals(""))
				{
					// Ignore the line beginning with /* - comments
					continue;
				}
				else
				{
					// Save the remaining content in an array
					fullinfo.add(line);	
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Saved into the proper file
		System.out.println("Size is"+ fullinfo.size());
		int num = fullinfo.size()-1;
	//	System.out.println("Upgrades"+ numOfUpgrades);
		setUpgrades(num);
		
		// Iterate through the list and fill the classes
		int count = 0;
		for(Iterator<String> iterator = fullinfo.iterator(); iterator.hasNext();)
		{
			String info = iterator.next().toString();
			// Load balancer found add Servers to the LB
			
			if(info.contains("Loadbalancer"))
			{	count++;
				if(count==1)
				{
					loadBalancerInfo = info.split(" ");
					lb = new LoadBalancer(loadBalancerInfo[0],loadBalancerInfo[1],loadBalancerInfo[2],loadBalancerInfo[3]);
					lb.addServer(iterator.next().toString());
					lb.addServer(iterator.next().toString());
					//System.out.println(iterator.next().toString());
					//System.out.println(iterator.next().toString());
					System.out.println(lb.print());
					loadbalancers.add(lb);			
				}	
			}
		}
		count = 0;
		for(Iterator<String> iterator = fullinfo.iterator(); iterator.hasNext();)
		{
			String info = iterator.next().toString();
			// Load balancer
			//System.out.println(info);
			
			if(info.contains("Loadbalancer"))
			{	count++;
				
				if(count ==2)
				{
					loadBalancerInfo = info.split(" ");
					lb = new LoadBalancer(loadBalancerInfo[0],loadBalancerInfo[1],loadBalancerInfo[2],loadBalancerInfo[3]);
			
					iterator.next();
					lb.addServer(iterator.next().toString());
					lb.addServer(iterator.next().toString());
					lb.addServer(iterator.next().toString());
					System.out.println(lb.print());
					loadbalancers.add(lb);
				}	
			}
		}
		count = 0;
		for(Iterator<String> iterator = fullinfo.iterator(); iterator.hasNext();)
		{
			String info = iterator.next().toString();
			// Load balancer
			
			if(info.contains("Loadbalancer"))
			{	count++;
				
				if(count==3)
				{
					loadBalancerInfo = info.split(" ");
					lb = new LoadBalancer(loadBalancerInfo[0],loadBalancerInfo[1],loadBalancerInfo[2],loadBalancerInfo[3]);
					iterator.next();
					iterator.next();
					iterator.next();
					lb.addServer(iterator.next().toString());
					lb.addServer(iterator.next().toString());
					lb.addServer(iterator.next().toString());
					lb.addServer(iterator.next().toString());
					System.out.println(lb.print());
					loadbalancers.add(lb);	
				}	
			}		
		}		
	return false;
}
	  private void setUpgrades(int num) {
		this.numOfUpgrades = num;
		
	}
	public void run() {
		// Start Upgrade
		  //System.out.println("Inside run 7");
		  System.out.println("RUN"+getNumOfUpgrades());
			 /* for(Iterator<LoadBalancer> iterator = loadbalancers.iterator(); iterator.hasNext();)
				{
				  System.out.println("Inside run");
				  iterator.next().upgradeLB();*/
			
	       startUpgarde(); 
	      //  System.out.println("Hello from a thread!");
	    }
	public void startUpgarde() {
		
		///System.out.println("Inside Start upgrade...."+getNumOfUpgrades());
		//System.out.println("Inside Start upgrade...2."+numOfUpgrades);
		for(Iterator<LoadBalancer> iterator = loadbalancers.iterator(); iterator.hasNext();)
		{
			//System
			iterator.next().upgradeLB();
		 try {
				Thread.sleep(upgradeInterval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
