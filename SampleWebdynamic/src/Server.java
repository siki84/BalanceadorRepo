import java.io.File;


public class Server {
	
	private String serverId = null;
	
	// Default version is -1 , once the data is loaded version of old application is 1
	// New upgrade application version is 2
	private int version = 0;
	// Weight of the load balancer would be the number of servers under it
	// for the top level load balancer the servers would be the load balancers under it
	private int weight = 0;
	private boolean isLoadBalancer = true;
	private String configTag = null;
	private String ipAddress = null;
	
	
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
	
	public int getVersion() {
		return version;
	}
	
	
	public int getWeight() {
		return weight;
	}


	public Server(String serverId,String configTag,String ipAddress, String weight, String version,String parent, boolean isLB)
	{
		// Server1 server1 server1:8080  1 1 Loadbalancer1
		this.serverId = serverId;
		this.weight = Integer.parseInt(weight);
		this.version = Integer.parseInt(version);
		this.isLoadBalancer = isLB;
		this.parent = parent;
		this.configTag = configTag;
		this.ipAddress =ipAddress;
	}
	
	public void setVersion(int i) {
		this.version= i;
		
	}


}
