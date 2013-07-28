/* This is the main class that provides the interface to the webservice
 * 
 */
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

public class UpgradeController implements Runnable, Iterable<Server> {
	// Class variables
	static int upgradeInterval = 400;
	// true represents upgrade one by once from 1st server
	// false represents upgrade server from one load balancer and then from the
	// other
	// Default is true
	static boolean sequentialSequence = true;

	static ArrayList<LoadBalancer> loadbalancers = new ArrayList<LoadBalancer>();
	static int numOfUpgrades = 0;
	static int upgradedServers = 0;
	static int totolUpgradedServers = 0;

	// Object pass
	// static variables
	// UpgradeLogger log = new UpgradeLogger();
	// Logger log1 = log.log();

	public int getNumberOfUpgradedServers() {
		return totolUpgradedServers;

	}

	public UpgradeController(int upgradeInvervalInput) {
		this.upgradeInterval = upgradeInvervalInput;
		this.sequentialSequence = true;

	}

	public int getNumOfUpgrades() {
		return this.numOfUpgrades;

	}

	public int getNumberOfNonUpgradedServers() {
		Iterator<Server> it = this.iterator();
		int count = 0;
		while(it.hasNext())
		{
			if (it.next().getVersion() == 1)
				count++;
		}
		return count;
	}

	public ArrayList<LoadBalancer> getloadBalancerList() {
		return this.loadbalancers;
	}

	public Server getServerOther() {
		Iterator<LoadBalancer> iterator = loadbalancers.iterator();
		iterator.next();
		return iterator.next().getServer();
	}

	public UpgradeController() {
		// TODO Auto-generated constructor stub
	}

	public boolean loadData(String fileName) {
		/*
		 * Return true if the data is loaded successfully else return false
		 */

		ArrayList<String> fullinfo = new ArrayList<String>();
		Scanner scanner = null;
		LoadBalancer lb, lb1 = null;
		String[] loadBalancerInfo = null;
		try {
			scanner = new Scanner(new File(fileName));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				// log.info(line);
				if (line.contains("/*") || line.equals("")) {
					// Ignore the line beginning with /* - comments
					continue;
				} else {
					// Save the remaining content in an array
					fullinfo.add(line);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Saved into the proper file
		// System.out.println("Size is"+ fullinfo.size());
		int num = fullinfo.size() - 1;
		System.out.println("Upgrades" + numOfUpgrades);
		setUpgrades(num);

		// Iterate through the list and fill the classes
		int count = 0;
		for (Iterator<String> iterator = fullinfo.iterator(); iterator
				.hasNext();) {
			String info = iterator.next().toString();
			// Load balancer found add Servers to the LB

			if (info.contains("Loadbalancer")) {
				count++;

				loadBalancerInfo = info.split(" ");
				// Loadbalancer1 TopLoadBalancer noTag
				// ec2-54-226-78-37.compute-1.amazonaws.com 2 1 none
				// String lbID, String configTag, String ipAddr, String
				// numberChildren, String version. String parent
				lb = new LoadBalancer(loadBalancerInfo[1], loadBalancerInfo[2],
						loadBalancerInfo[3], loadBalancerInfo[4],
						loadBalancerInfo[5], loadBalancerInfo[6]);
				// Iterator<String> iteratorTemp = fullinfo.iterator();

				for (Iterator<String> iteratorTemp = fullinfo.iterator(); iteratorTemp
						.hasNext();) {
					String line = iteratorTemp.next().toString();
					String[] loadBalancerTemp = line.split(" ");
					if (loadBalancerTemp[6]
							.equalsIgnoreCase(loadBalancerInfo[1])) {

						lb.addServer(line);

					}
					// lb.addServer(iteratorTemp.next().toString());
				}
				loadbalancers.add(lb);

			}
		}

		System.out.println("Leaving load data");
		return false;
	}

	private void setUpgrades(int num) {
		this.numOfUpgrades = num;

	}
	
	public void setInterval(int interval) {
		upgradeInterval = interval;
	}

	public void run() {
		startUpgarde();
	}

	public void startUpgarde() {
		if (this.sequentialSequence == true) {

			Iterator<Server> itServer = new NormalSequence();
			while (itServer.hasNext()) {
				// Gives the Server

				Server serverToUpgrade = itServer.next();
				System.out.println("Upgrade Server"
						+ serverToUpgrade.getServerId());
				upgradeServer(serverToUpgrade);
				totolUpgradedServers++;
				System.out.println("UpgradeComplete, total servers upgraded"
						+ totolUpgradedServers);// getNumberOfUpgradedServers());

				try {
					Thread.sleep(upgradeInterval);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			Iterator<Server> itServer = new OtherSequence();
			while (itServer.hasNext()) {
				// Gives the Server

				Server serverToUpgrade = itServer.next();
				System.out.println("Upgrade Server"
						+ serverToUpgrade.getServerId());
				upgradeServer(serverToUpgrade);
				totolUpgradedServers++;
				System.out.println("UpgradeComplete, total servers upgraded"
						+ totolUpgradedServers);// getNumberOfUpgradedServers());

				try {
					Thread.sleep(upgradeInterval);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	private void upgradeServer(Server serverToUpgrade) {
		System.out
				.println("Upgrading Server :" + serverToUpgrade.getServerId());
		/*
		 * For each server /* 1. Find parent 2. Go to the parent LB and 3. Go to
		 * the Parent Load balancer's config file backend old versio - disable
		 * server backedn new version - enable newserver backend noversion -
		 * disable server, enable newserver In the top level LB (parent's
		 * parent) backend old version - decrease weight for the parent backend
		 * new version - inc weight for the parent
		 * 
		 * When the first lb's server is upgraded in the top level LB(parent's
		 * parent) new Version backend enable parent
		 * 
		 * When the last lb's server is upgraded in the olVersion backend
		 * disable parent
		 */

		// find parent
		LoadBalancer lb = returnParent(serverToUpgrade.getParent());
		lb.disableBackend(serverToUpgrade, "oldversion");
		lb.EnableBackend(serverToUpgrade, "newversion");
		lb.disableBackend(serverToUpgrade, "noversion");
		lb.EnableBackend(serverToUpgrade, "noversion");

		LoadBalancer lbparent = returnParent(lb.getParent());
		lbparent.decreaseWeight(lb, "oldversion");
		lbparent.increaseWeight(lb, "newversion");

		// Server is upgraded, now upgrade the Parent
		upgradeParent(serverToUpgrade.getParent());
		// after upgrading the Server and the parent, update the information in
		// the classes
		serverToUpgrade.setVersion(2);

		// Not required since if the weight is 0 it wont be used,
		// Will be required if we dont used weighted policy
		// May be, so the code is there to be sure
		if (lb.getnumberLoadbalancerChildren() == 0)
			lbparent.disableBackend("oldversion");

	}

	private void upgradeParent(String parent) {
		// Find the Loadbalancer with this name and
		// Call Upgrade on that
		// System.out.println("upgrade Parent");
		ArrayList<LoadBalancer> loadbalancersTemp = new ArrayList<LoadBalancer>();
		loadbalancersTemp = getloadBalancerList();
		for (Iterator<LoadBalancer> iterator = loadbalancersTemp.iterator(); iterator
				.hasNext();) {
			// System.out.println("For aloop");
			LoadBalancer lbTemp = new LoadBalancer();
			lbTemp = iterator.next();
			if (lbTemp.getloadbalancerID().equalsIgnoreCase(parent)) {
				if (lbTemp.getVersion() == 1) {
					System.out.println("Upgrading Load balancer"
							+ lbTemp.getloadbalancerID());
					LoadBalancer lb = returnParent(lbTemp.getParent());
					lb.EnableBackend(lbTemp.getloadbalancerID(), "newversion");// this
																				// is
																				// parent
																				// and
																				// enable

					// lbTemp.upgradeLB();
					lbTemp.setVersion(2);
				}
			}
		}
	}

	private LoadBalancer returnParent(String parent) {
		// Find the LoadBalancer with this name and
		// Call Upgrade on that
		// System.out.println("upgrade Parent");
		ArrayList<LoadBalancer> loadbalancersTemp = new ArrayList<LoadBalancer>();
		loadbalancersTemp = getloadBalancerList();
		for (Iterator<LoadBalancer> iterator = loadbalancersTemp.iterator(); iterator
				.hasNext();) {
			// System.out.println("For aloop");
			LoadBalancer lbTemp = new LoadBalancer();
			lbTemp = iterator.next();
			if (lbTemp.getloadbalancerID().equalsIgnoreCase(parent)) {

				return lbTemp;
			}
		}
		return null;
	}

	@Override
	public Iterator<Server> iterator() {
		if (this.sequentialSequence == true)
			return new NormalSequence();
		else
			return new OtherSequence();
	}

	public class NormalSequence implements Iterator<Server> {

		private Queue<Server> tempStack;

		public NormalSequence() {
			// First find the load balancer
			// Finish all server then go to the next LB
			ArrayList<LoadBalancer> loadbalancersTemp = new ArrayList<LoadBalancer>();
			loadbalancersTemp = getloadBalancerList();
			tempStack = new LinkedList<Server>();

			for (Iterator<LoadBalancer> iterator = loadbalancersTemp.iterator(); iterator
					.hasNext();) {
				ArrayList<Server> ServerListTemp = new ArrayList<Server>();

				ServerListTemp = iterator.next().getServerList();
				for (Iterator<Server> iteratorServer = ServerListTemp
						.iterator(); iteratorServer.hasNext();) {
					Server serverTemp = iteratorServer.next();

					if (serverTemp.getServerId().contains("server")) {
						tempStack.add(serverTemp);
					}
				}

			}
		}

		@Override
		public boolean hasNext() {

			if (tempStack.isEmpty())
				return false;
			else
				return true;
		}

		@Override
		public Server next() {
			// Iterator<Server> itStack = tempStack.iterator();
			return tempStack.remove();
		}

		@Override
		public void remove() {
			// Not required
		}

	}// Class end

	public class OtherSequence implements Iterator<Server> {

		private Queue<Server> tempStack = new LinkedList<Server>();

		public OtherSequence() {
			// First find the load balancer

			ArrayList<LoadBalancer> loadbalancersTemp = new ArrayList<LoadBalancer>();
			loadbalancersTemp = getloadBalancerList();
			ArrayList<Server> ServerList = new ArrayList<Server>();
			ArrayList<Server> ServerList2 = new ArrayList<Server>();

			ServerList = loadbalancersTemp.get(1).getServerList();
			ServerList2 = loadbalancersTemp.get(2).getServerList();

			int index_a = 0, index_b = 0;

			// traverse and add proper values to the merged array
			while (index_a < ServerList.size() && index_b < ServerList2.size()) {
				// find smaller value and add it to the merged array
				if (ServerList.get(index_a).getServerId().contains("server")) {
					tempStack.add(ServerList.get(index_a));
				}
				index_a++;
				if (ServerList2.get(index_b).getServerId().contains("server")) {
					tempStack.add(ServerList2.get(index_b));
				}
				index_b++;
			}
			// need to handle cases that there are still items to be copied
			if (index_a < ServerList.size()) { // items left in array a
				for (int i = index_a; i < ServerList.size(); i++)
					tempStack.add(ServerList.get(index_a));
			} else { // items left in array b
				for (int i = index_b; i < ServerList2.size(); i++)
					tempStack.add(ServerList2.get(index_b));
			}

		}

		@Override
		public boolean hasNext() {
			if (tempStack.isEmpty())
				return false;
			else
				return true;
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

	public void reset() {
		// Reset the Configuration in the load balancers
		// For all load balancers call the reset method
		//for (Iterator<LoadBalancer> iterator = loadbalancers.iterator(); iterator
		//		.hasNext();) {
		//	iterator.next().resetConfiguration();
		//}
		upgradedServers = 0;
		System.out.println("reset the server");
		Iterator<Server> it = this.iterator();
		while(it.hasNext())
		{
			it.next().setVersion(1);
		}
		

	}

}