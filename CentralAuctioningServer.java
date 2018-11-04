import java.util.HashMap;
import java.rmi.*;

/**
 * Interface to the remote auction server class
 */
public interface CentralAuctioningServer extends Remote
{
	
	public static int currentID = 1; //Stores the current highest auction ID
    public static final HashMap<Integer, Auction> auctions = new HashMap<Integer, Auction>(); //Holds all auctions

    /**
     * Adds an auction to the system
     * @param SP - starting price of auction
     * @param desc - auction description
     * @param RP - reserve price of the auction
     */
	public void addAuction(double SP, String desc, double RP) throws RemoteException;

    /**
     * Returns a specific auction using its ID
     * @param ID - the desired auctions ID
     * @return the auction instance requested by ID
     */
	public Auction getAuction(int ID) throws RemoteException;
	
	/**
	 * Adds a new highest bid and bidder to the specified auction
	 * @param ID - the ID of the auction
	 * @param P - the price of the bid
	 * @param B - the bidder who made the bid
     * @return a message of success or explaining failure of the attempt
	 */
    public String addBid(int ID, double P, Bidder B) throws RemoteException;

	/**
	 * Returns the current auction ID
	 * @return the ID of the current auction
	 */
	public int getCurrentID() throws RemoteException;

	/**
	 * Prvoides auction listings in text form
	 * @return A string containing auction listings
	 */
	public String getAuctionText()throws RemoteException;
	
}

