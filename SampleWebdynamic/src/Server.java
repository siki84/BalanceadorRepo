import java.io.File;


public class Server {
	
	final String tomCatRestart = "";
	final String tomCatStop = "";
	final String tomCatStart = "";
	final String connection = "ssh -i /Users/sdhawan/Downloads/nicta.pem  ubuntu@";
	final String connection2 = " -o StrictHostKeyChecking=no ";
	final String disableServer1 = " disable server VersionA/";
	final String disableServer2= "\" | socat stdio TCP-CONNECT:localhost:10010";
	private String serverId = null;
	
	private String ipAddress = null;
	// Default version is -1 , once the data is loaded version of old application is 1
	// New upgrade application version is 2
	private int version = 0;
	// Weight of the load balancer would be the number of servers under it
	// for the top level load balancer the servers would be the load balancers under it
	private int weight = 0;
	private boolean isLoadBalancer = true;
	public String getParent() {
		return this.parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	private String parent = null;
	public String getServerId() {
		return serverId;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	
	public int getVersion() {
		return version;
	}
	
	
	public int getWeight() {
		return weight;
	}


	public Server(String serverId,String ipAddress, String weight, String version,boolean isLB)
	{
		this.serverId = serverId;
		this.ipAddress = ipAddress;
		this.weight = Integer.parseInt(weight);
		this.version = Integer.parseInt(version);
		this.isLoadBalancer = isLB;
	}
	public String print() {
		
		StringBuilder result = new StringBuilder();
	    result.append("Server ID : "+getServerId());
	    result.append("Server IP : "+getIpAddress());
	    result.append("Server Weight: "+getWeight());
	    result.append("Server Version: "+getVersion());
	    return result.toString();
}

	public boolean upgradeServer() {
		
		System.out.println("Upgrade Server"+this.serverId);
		// Stop Apache Tomcat
		// Replace the new version of the application
		
		
		// ReStart Apache Tomcat
		RemoteConnection rc = new RemoteConnection();
		rc.newVersion();
		rc.runCommand(connection+this.ipAddress+connection2,tomCatRestart);
		// disable the server from backend and enable from frontend
		// Using the parent		
		return true;
		
	}

	public void numUpgradedServers() {
		
		
	}

	public void setVersion(int i) {
		this.version= i;
		
	}
}
