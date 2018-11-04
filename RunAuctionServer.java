import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.Naming;
/**
 * Creates and runs an instance of the Centeral Auction Server
 */
public class RunAuctionServer
{

    public static void main(String args[])
    {
        int attempt = 1;

        /* Loop binding code to allow for retry upon exception throw*/
        while(attempt < 4) {
            try {
                /* Bind the remote object in the RMI Registry  */
                CentralAuctioningServer server = new CentralAuctioningServerImp();
                CentralAuctioningServer stub = (CentralAuctioningServer) UnicastRemoteObject.exportObject(server, 0);
                Naming.rebind("rmi://localhost/AuctionServer", stub);

                System.out.println("=====================================================");
                System.out.println("\t\t Auction Server Active");
                System.out.println("=====================================================");
                break;

            } catch (Exception e) {
                System.out.println("\nFailed to start server\nAttempt Number:" + attempt + "\n");
                System.out.println(e);
                attempt++;
            }
        }
    }

}