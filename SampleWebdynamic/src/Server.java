import java.io.File;


public class Server {
	
	private String serverId = null;
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
	
	private String ipAddress = null;
	// Default version is -1 , once the data is loaded version of old application is 1
	// New upgrade application version is 2
	private int version = 0;
	// Weight of the load balancer would be the number of servers under it
	// for the top level load balancer the servers would be the load balancers under it
	private int weight = 0;

	public Server(String serverId,String ipAddress, String weight, String version)
	{
		this.serverId = serverId;
		this.ipAddress = ipAddress;
		this.weight = Integer.parseInt(weight);
		this.version = Integer.parseInt(version);
	}
	public String print() {
		
		StringBuilder result = new StringBuilder();
	    result.append("Server ID : "+getServerId());
	    result.append("Server IP : "+getIpAddress());
	    result.append("Server Weight: "+getWeight());
	    result.append("Server Version: "+getVersion());
	    return result.toString();
}

	public void upgradeServer() {
		System.out.println("Upgrade Server"+this.serverId);
		 //File wd = new File("/Users/sdhawan/Documents/Sakshi/Summer/Studio/");
	      // System.out.println("Working Directory: " +wd);
		  try
          {
			  Process proc = Runtime.getRuntime().exec( "chmod 777 /Users/sdhawan/Documents/Sakshi/Summer/Studio/test.sh");
                   Runtime.getRuntime().exec("/Users/sdhawan/Documents/Sakshi/Summer/Studio/test.sh");
                  System.out.println("Print Test Line.");
          }
          catch (Exception e)
          {
                  System.out.println(e.getMessage());
                  e.printStackTrace();
          }
		
	}
}
