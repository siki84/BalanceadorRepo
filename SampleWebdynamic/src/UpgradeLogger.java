import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class UpgradeLogger {
	private Logger logger = Logger.getLogger("MyLog");  
	private FileHandler fh; 
	public Logger log()
	{
		

 

        // This block configure the logger with handler and formatter  
        try {
			fh = new FileHandler("/Users/sdhawan/Documents/Sakshi/Summer/Studio/UpgradeControllerLogs/Upgrade.log");
			  // Creating a logger for tracking the upgrades in case there are some issues
	        // for logging call logger.info("Message")
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  

	        // the following statement is used to log any messages  
	        logger.info("My first log");  
	        logger.setUseParentHandlers(false);
			return logger;
        } catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;  

	}
	public void close()
	{
		fh.close();
	}
}