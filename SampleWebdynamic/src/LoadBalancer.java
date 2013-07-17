import java.util.ArrayList;
import java.util.Iterator;
public class LoadBalancer {

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
	private String getloadbalancerID() {
		// TODO Auto-generated method stub
		return loadbalancerID;
	}
	public int getnumberLoadbalancerChildren() {
		// TODO Auto-generated method stub
		return numberChildren;
	}
	private int getVersion() {
		// TODO Auto-generated method stub
		return version;
	}
	public void setChildern(int lbCount) {
		this.numberChildren = lbCount;
		
	}
	public void addServer(String line) {
		
		String [] serverInfo = line.split(" ");
		Server s = new Server(serverInfo[0],serverInfo[1],serverInfo[2],serverInfo[3]);
		servers.add(s);
		System.out.println(line);
		
	}

	public void upgradeLB() {
		System.out.println("Upgrade LB"+this.loadbalancerID);
		for(Iterator<Server> iterator = servers.iterator(); iterator.hasNext();)
		{
			iterator.next().upgradeServer();
		}
	}
}
