import java.rmi.Naming;

/**
 * This class allows for connection to the remote auction server object
 */
public class AuctionConnector
{

    /**
     * provides a connection to the auction, ends program if lookup fails
     * @return the active auction server
     */
    public CentralAuctioningServer connect()
    {
        CentralAuctioningServer server = null;
        int attempt = 1;

        //attempt to find the RMI object
		while (attempt < 4) {
            try {
                // Get a reference to the remote object through the rmiregistry
                server = (CentralAuctioningServer) Naming.lookup("rmi://localhost/AuctionServer");
                break;
            } catch (Exception e) {
                System.out.println("\nFailed to find the RMI object\nAttempt Number:" + attempt + "\n");
                System.out.println(e);
                attempt++;
            }
        }

        //End program if lookup fails
        if (attempt >= 4) {
            System.out.println("\nFailed object lookup - Terminating program");
            System.exit(0);
        }
        return server;
    }
}