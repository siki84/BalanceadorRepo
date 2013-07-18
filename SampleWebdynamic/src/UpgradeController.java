/* This is the main class that provides the interface to the webservice
 * 
 */
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class UpgradeController  implements Runnable, Iterable<Server> {
	// Class variables
	static int upgradeInterval=400;
	// true represents upgrade one by once from 1st server
	// false represents upgrade server from one load balancer and then from the other
	// Default is true
	static boolean sequentialSequence=false;
	
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
		return this.numOfUpgrades;
		
	}
	public  Server getServer()
	{
		Iterator<LoadBalancer> iterator = loadbalancers.iterator();
		return  iterator.next().getServer();		
	}
	public ArrayList<LoadBalancer> getloadBalancerList()
	{
		return this.loadbalancers;
	}
	public  Server getServerOther()
	{
		Iterator<LoadBalancer> iterator = loadbalancers.iterator();
		iterator.next();
		return iterator.next().getServer();	
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
		String[] loadBalancerInfo = null;
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
		
			 /* for(Iterator<LoadBalancer> iterator = loadbalancers.iterator(); iterator.hasNext();)
				{
				  System.out.println("Inside run");
				  iterator.next().upgradeLB();*/
			
	       startUpgarde(); 
	    }
	
	public void startUpgarde() {	
		if(this.sequentialSequence==true)
		{
			System.out.println("Inside Normal Sequence");
				Iterator<Server> itServer = new NormalSequence(); 
				while(itServer.hasNext())
				{
					// Gives the Server
					// Call Upgrade on the Server
					Server serverToUpgrade = itServer.next();
					//System.out.println("Calling Upgrade on :"+serverToUpgrade.getServerId());
					if(serverToUpgrade.upgradeServer())
					{
						// Now change the Parameters in the LB
						LoadBalancer lb = returnParent(serverToUpgrade.getParent());
						lb.disableBackend(serverToUpgrade);
						lb.EnableBackend(serverToUpgrade);
						//Server is upgraded, now upgrade the Parent
						upgradeParent(serverToUpgrade.getParent());
						// after upgrading the Server and the parent, update the information in the classes
						serverToUpgrade.setVersion(1);
						
					}
					
						try {
							Thread.sleep(upgradeInterval);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
		}	
	}
	
	
	private void upgradeParent(String parent) {
		// Find the Loadbalancer with this name and 
		// Call Upgrade on that
	//	System.out.println("upgrade Parent");
		ArrayList<LoadBalancer> loadbalancersTemp = new ArrayList<LoadBalancer>();
		loadbalancersTemp  = getloadBalancerList(); 
		for(Iterator<LoadBalancer> iterator = loadbalancersTemp.iterator(); iterator.hasNext();)
		{
			//System.out.println("For aloop");
			LoadBalancer lbTemp = new LoadBalancer();
			lbTemp = iterator.next();
			if(lbTemp.getloadbalancerID().equalsIgnoreCase(parent))
			{
				if(lbTemp.getVersion()==0)
				{
				System.out.println("Upgrading Load balancer"+lbTemp.getloadbalancerID());
				lbTemp.upgradeLB();
				lbTemp.setVersion(1);
				}
			}
		}
	}
	private LoadBalancer returnParent(String parent) {
		// Find the Loadbalancer with this name and 
		// Call Upgrade on that
	//	System.out.println("upgrade Parent");
		ArrayList<LoadBalancer> loadbalancersTemp = new ArrayList<LoadBalancer>();
		loadbalancersTemp  = getloadBalancerList(); 
		for(Iterator<LoadBalancer> iterator = loadbalancersTemp.iterator(); iterator.hasNext();)
		{
			//System.out.println("For aloop");
			LoadBalancer lbTemp = new LoadBalancer();
			lbTemp = iterator.next();
			if(lbTemp.getloadbalancerID().equalsIgnoreCase(parent))
			{
				
				return lbTemp;
			}
		}
		return null;
	}
	@Override
	public Iterator<Server> iterator() {
		if(this.sequentialSequence==true)
			return new NormalSequence();
		else
			return new OtherSequence();
	}
	public class NormalSequence implements Iterator<Server> {
	
		private Queue<Server> tempStack = new LinkedList<Server>();
		
		public NormalSequence(){
			// First find the load balancer
			// Finish all server then go to the next LB 
			ArrayList<LoadBalancer> loadbalancersTemp = new ArrayList<LoadBalancer>();
			loadbalancersTemp  = getloadBalancerList();
		
			for(Iterator<LoadBalancer> iterator = loadbalancersTemp.iterator(); iterator.hasNext();)
			{
				ArrayList<Server> ServerListTemp = new ArrayList<Server>();
				
				ServerListTemp = iterator.next().getServerList();
				for(Iterator<Server> iteratorServer = ServerListTemp.iterator(); iteratorServer.hasNext();)
				{
					Server serverTemp = iteratorServer.next();
					
					if(serverTemp.getServerId().contains("Server"))
					{
						tempStack.add(serverTemp);
					}		
				}
		
			}
		}
		
		@Override
		public boolean hasNext() {
		
			if(tempStack.isEmpty()) return false;
			else return true;
		}

		@Override
		public Server next() {
			//Iterator<Server> itStack = tempStack.iterator();
			return tempStack.remove();		
		}

		@Override
		public void remove() {
			// Not required	
		}
			
	}// Class end

	public class OtherSequence implements Iterator<Server> {

		private Queue<Server> tempStack = new LinkedList<Server>();
		
		public OtherSequence(){
			/*System.out.println("Other SEq");
			// First find the load balancer
			// Finish all server then go to the next LB 
			ArrayList<LoadBalancer> loadbalancersTemp = new ArrayList<LoadBalancer>();
			loadbalancersTemp  = getloadBalancerList();
		
			for(Iterator<LoadBalancer> iterator = loadbalancersTemp.iterator(); iterator.hasNext();)
			{
				// Loop until all load balancers
				ArrayList<Server> ServerListTemp = new ArrayList<Server>();
				ServerListTemp = iterator.next().getServerList();
				// Server List
				Iterator<Server> iteratorServer = ServerListTemp.iterator(); 
				while(iteratorServer.hasNext())
				{
					Server serverTemp = iteratorServer.next();
					// Server
					if(serverTemp.getServerId().contains("Server"))
					{
						tempStack.add(serverTemp);	
					}
				}
				
					
			}*/
			
		}

		
		@Override
		public boolean hasNext() {
			if(tempStack.isEmpty()) return false;
			else return true;
		}

		@Override
		public Server next() {
			return tempStack.remove();
		}

		@Override
		public void remove() {
			// Not required		
		}
	
	}// Class end
		
}