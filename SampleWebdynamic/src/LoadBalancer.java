import java.util.ArrayList;
import java.util.Iterator;
public class LoadBalancer {

	final String connection = "ssh -i /Users/sdhawan/Downloads/nicta.pem  ubuntu@";
	final String connection2 = " -o StrictHostKeyChecking=no ";
	final String disableServer1 = " disable server VersionA/";
	final String disableServer2= "\" | socat stdio TCP-CONNECT:localhost:10010";
	final String enableServer1 = " disable server VersionB/";
	final String enableServer2= "\" | socat stdio TCP-CONNECT:localhost:10010";
	private ArrayList<Server> servers= new ArrayList<Server>();
	private int numberChildren = 0; // also weight
	private String loadbalancerID = null;
	private String ipAddress = null;
	private int version = 0;
	
	public LoadBalancer(String lbID, String ipAddr, String numberChildren, String version ) {
		this.loadbalancerID = lbID;
		this.ipAddress = ipAddr;
		this.numberChildren = Integer.parseInt(numberChildren);
	}
	
	public LoadBalancer() {
		// TODO Auto-generated constructor stub
	}

	public String print() {
		
				StringBuilder result = new StringBuilder();
			    result.append("Load Balancer children: "+getnumberLoadbalancerChildren()+" ");
			    result.append("Load Balancer Id: "+getloadbalancerID());
			    result.append("Load Balancer Ip address: "+getipAddress());
			    result.append("Load Balancer Version: "+getVersion());
			    return result.toString();
	}
	private String getipAddress() {
		// TODO Auto-generated method stub
		return ipAddress;
	}
	public String getloadbalancerID() {
		// TODO Auto-generated method stub
		return loadbalancerID;
	}
	public int getnumberLoadbalancerChildren() {
		// TODO Auto-generated method stub
		return numberChildren;
	}
	public int getVersion() {
		// TODO Auto-generated method stub
		return version;
	}
	public void setChildern(int lbCount) {
		this.numberChildren = lbCount;
		
	}
	public void addServer(String line) {
		
		boolean isLB = false;
		String [] serverInfo = line.split(" ");
		if(line.contains("Loadbalancer")) isLB = true;
		Server s = new Server(serverInfo[0],serverInfo[1],serverInfo[2],serverInfo[3],isLB);
		s.setParent(this.loadbalancerID);
		servers.add(s);
		
		//System.out.println(line);
		
	}
	public Server getServer(){
		Iterator<Server> iterator = servers.iterator();
		return iterator.next();
	}

	public ArrayList<Server> getServerList(){
	
		return this.servers;
	}

	public void setVersion(int i) {
		//System.out.println("Upgrade LB"+this.loadbalancerID);
		
			System.out.println(this.loadbalancerID+"upgraded");
			//After upgrading set the field
			this.version = i;
		
	}

	public Server getFirstServer() {
		Iterator<Server> iterator = servers.iterator();
		return iterator.next();
		
	}
	public boolean upgradeLB()
	{
		System.out.println("Upgrade LB"+this.loadbalancerID);
		return true;
	}

	public void disableBackend(Server serverToUpgrade) {
		String connect = connection+this.getipAddress()+connection2;
		String command = disableServer1+serverToUpgrade.getServerId()+disableServer2;
		RemoteConnection rc = new RemoteConnection();
		rc.runCommand(connect, command);
		
	}

	public void EnableBackend(Server serverToUpgrade) {
		String connect = connection+this.getipAddress()+connection2;
		String command = enableServer1+serverToUpgrade.getServerId()+enableServer2;
		RemoteConnection rc = new RemoteConnection();
		rc.runCommand(connect, command);
		
	}
	
}
