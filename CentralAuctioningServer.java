import java.util.HashMap;
import java.rmi.*;

/**
 * Interface to the remote auction server class
 */
public interface CentralAuctioningServer extends Remote
{
	
	public static int currentID = 1; //Stores the current highest auction ID
	public static int creatorID = 1; //Stores the current highest creator ID
    public static final HashMap<Integer, Auction> auctions = new HashMap<Integer, Auction>(); //Holds all auctions

    /**
     * Adds an auction to the system
     * @param SP - starting price of auction
     * @param desc - auction description
     * @param RP - reserve price of the auction
     */
	public String addAuction(double SP, String desc, double RP, int CID) throws RemoteException;

    /**
     * Allocates and provides a new creator ID
     * @return the new creator ID
     * @throws RemoteException
     */
    public int getNewCreatorID() throws RemoteException;

    /**
     * Returns a specific auction using its ID
     * @param ID - the desired auctions ID
     * @return the auction instance requested by ID
     */
	public Auction getAuction(int ID) throws RemoteException;

	/**
	 * Attempts to end an active auction
	 * @param AID - the ID of the auction to end
	 * @param CID - the ID of the creator making the request
	 * @return a message explaing the result of the attempt
	 * @throws RemoteExpection
	 */
	public String endAuction(int AID, int CID) throws RemoteException;
	
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

    /**
     * Prvoides auction listings in text form - limited to acutons with matching creatorIDs
     * @param CID
     * @return A string containing auction listings belonging to the creator
     * @throws RemoteException
     */
    public String getAuctionText(int CID) throws RemoteException;
	
}

