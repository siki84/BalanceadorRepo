import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;
public class LoadBalancer {
	final String disableServer = "disable server ";
	final String enableServer = "enable server ";
	final String setWeight=  "set weight ";	
	// TODO: Need to update the reset command
	final String reset = "reset"; 
	private ArrayList<Server> servers= new ArrayList<Server>();
	private int numberChildren = 0; // also weight
	private String loadbalancerID = null;
	private String ipAddress = null;
	private int version = 0;
	public String getParent() {
		return this.parent;
	}
	private String parent = "None";
	private String configTag = null;
	public LoadBalancer(String lbID, String configTag, String ipAddr, String numberChildren, String version,String parent) {
		this.loadbalancerID = lbID;
		this.ipAddress = ipAddr;
		this.numberChildren = Integer.parseInt(numberChildren);
		this.configTag = configTag;
		this.parent = parent;
		this.version = Integer.parseInt(version);
		
	}

	public LoadBalancer() {
		// TODO Auto-generated constructor stub
	}
	
	private String getipAddress() {
		// TODO Auto-generated method stub
		return this.ipAddress;
	}
	public String getloadbalancerID() {
		// TODO Auto-generated method stub
		return this.loadbalancerID;
	}
	public int getnumberLoadbalancerChildren() {
		// TODO Auto-generated method stub
		return this.numberChildren;
	}
	public int getVersion() {
		// TODO Auto-generated method stub
		return this.version;
	}
	public void setChildern(int lbCount) {
		this.numberChildren = lbCount;
		
	}
	public void addServer(String line) {
		
		boolean isLB = false;
		String [] serverInfo = line.split(" ");
		if(line.contains("Loadbalancer")) isLB = true;
		Server s = new Server(serverInfo[1],serverInfo[2],serverInfo[3],serverInfo[4],serverInfo[5],serverInfo[6],isLB);
		s.setParent(this.loadbalancerID);
		servers.add(s);
		
	}
	public Server getServer(){
		Iterator<Server> iterator = servers.iterator();
		return iterator.next();
	}

	public ArrayList<Server> getServerList(){
	
		return this.servers;
	}

	public void setVersion(int i) {
		System.out.println(this.loadbalancerID+"upgraded");
		//After upgrading set the field
		this.version = i;
		
	}


	public boolean disableBackend(Server serverToUpgrade, String backend) {
		String connect = this.getipAddress();
		String command = disableServer+ backend +"/"+serverToUpgrade.getServerId();
		HaProxySocket rc = new HaProxySocket();
		return rc.runCommand(connect, command);
		
	}

	public boolean EnableBackend(Server serverToUpgrade, String backend) {
		String connect = this.getipAddress();
		String command = enableServer+backend +"/"+"new"+serverToUpgrade.getServerId();
		HaProxySocket rc = new HaProxySocket();
		return rc.runCommand(connect, command);
		
	}

	public boolean resetConfiguration() {
		// Resets the configuration file of the old load balancer
		// Restart the load balancer
		// Need to update the command
		
		// Send restart command to haproxy
		// Assuming that there is a socket command
		
		String connect = this.getipAddress();
		
		String command = reset;
		HaProxySocket rc = new HaProxySocket();
		System.out.println(this.getloadbalancerID()+"Resetting Configuration");
		return rc.runCommand(connect, command);
		
	}

	public boolean decreaseWeight(LoadBalancer lb, String backend) {
		String connect = this.getipAddress();
		Integer w = this.getnumberLoadbalancerChildren();
		w--;
		// noVersion/web01 1
		String command = setWeight+backend+"/"+lb.getloadbalancerID()+" "+w.toString();
		HaProxySocket rc = new HaProxySocket();
		return rc.runCommand(connect, command);
		
	}

	public boolean increaseWeight(LoadBalancer lb, String backend) {
		String connect = this.getipAddress();
		Integer w = this.getnumberLoadbalancerChildren();
		w++;
		String command = setWeight+backend+"/"+lb.getloadbalancerID()+" "+w.toString();
		HaProxySocket rc = new HaProxySocket();
		return rc.runCommand(connect, command);
		
	}

	public boolean EnableBackend( String lbId, String backend) {
		String connect = this.getipAddress();
		String command =enableServer+backend +"/"+lbId;
		HaProxySocket rc = new HaProxySocket();
		return rc.runCommand(connect, command);
		
	}

	public boolean disableBackend(String backend) {
		String connect = this.getipAddress();
		String command = disableServer+ backend +"/"+getloadbalancerID();
		HaProxySocket rc = new HaProxySocket();
		return rc.runCommand(connect, command);
		
		
	}
	
}
