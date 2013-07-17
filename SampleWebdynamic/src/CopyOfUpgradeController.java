/* This is the main class that provides the interface to the webservice
 * 
 */
import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.Vector;

public class CopyOfUpgradeController {
	// Class variables
	private int upgradeInterval;
	// true represents upgrade one by once from 1st server
	// false represents upgrade server from one load balancer and then from the other
	// Default is true
	private boolean sequenctialSequence=true;
	
	private ArrayList<LoadBalancer> loadbalancers= new ArrayList<LoadBalancer>();

	
	public CopyOfUpgradeController(int upgradeInvervalInput)
	{
	
		this.upgradeInterval = upgradeInvervalInput;
		this.sequenctialSequence = true;
	
	}

	public boolean loadData(Logger log) {
	 /* Return true if the data is loaded successfully 
	  * else return false
	  * Check for the correct format
	  * empty file etc
	  * send appropriate message
	  */

		String[] fullinfo = null;
		String[] loadBalancerInfo = null;

		String[] serverInfo = null;
		Scanner scanner = null;
		int lbCount = 0;
		int serverCount = 0;
		int flagTopLevel = 0;
		LoadBalancer lbTopLevel = null;
		LoadBalancer lb2 = null;
		LoadBalancer lb3 = null;
		Server s  = null;
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
					// If a Load balancer
					if(line.contains("Loadbalancer"))
					{
						// If a top level load balancer
						if(flagTopLevel==0)
						{
							log.info("LoadBalancer found"+line);
							loadBalancerInfo = line.split(" ");
							//lbTopLevel = new LoadBalancer(loadBalancerInfo[0],loadBalancerInfo[1]);
							flagTopLevel =1;
						//	loadbalancers.add(lbTopLevel);
						}
						// If not a top level load balancer
						else if(flagTopLevel==1)
						{
	
							System.out.println("Not a top level "+ line);
							// Not a top level
							// Increment lbchildern count and add it to the number of children
							// Add children for top level load balancer
							if(line.contains("Loadbalancer"))
							{
								
								lbCount++;
							}
							// Update second level LB information

							//log.info("LoadBalancer found"+line);
							//loadBalancerInfo = line.split(" ");
							//lb2 = new LoadBalancer(loadBalancerInfo[0],loadBalancerInfo[1]);
							
						/*	line = scanner.nextLine();
							while(line.contains("Server"))
							{
								log.info("Server Found"+ line);
								lb2.addServer(line);
								line = scanner.nextLine();
								serverCount++;
							}
							lb2.setChildern(serverCount);
					*/
							
						} // Not a top level
						// Now we know the total number of children, so set the number 
						System.out.println("Lineis"+line);
						/*line = scanner.nextLine();
						while(line.contains("Server"))
						{
							log.info("Server Found"+ line);
							lb2.addServer(line);
							line = scanner.nextLine();
							serverCount++;
						}*/
						//lb2.setChildern(serverCount);
						
						lbTopLevel.setChildern(lbCount);
						
					}
					
				}
			
			}
			System.out.println(lbTopLevel.print());
			loadbalancers.add(lbTopLevel);
			
		
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	return false;
}

}
