import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.HashMap;
import java.rmi.*;

/**
 * Stores and provides access to running auctions
 */
public class CentralAuctioningServerImp implements CentralAuctioningServer
{
	//Stores the current highest auction ID
	public static int currentID = 1;
	
	//Setup of the hash table for auctions
    public static final HashMap<Integer, Auction> auctions = new HashMap<Integer, Auction>();

    /**
     * Add an auction to the system
     * @param SP - starting price of auction
     * @param desc - auction description
     * @param RP - reserve price of the auction
     */
	public void addAuction(double SP, String desc, double RP) throws RemoteException
    {
		auctions.put(currentID, new Auction(currentID,SP,desc,RP));
        currentID++;
	}

    /**
     * provides access to a specific auction using its ID
     * @param ID - the desired auctions ID
     * @return the auction instance requested by ID
     */
	public Auction getAuction(int ID) throws RemoteException
    {
		return auctions.get(ID);
	}

	/**
	 * Adds a new highest bid to the specified auction
	 * @param ID - the ID of the auction
	 * @param P - the price of the bid
	 * @param B - the bidder who made the bid
	 * @return a message of success or explaining failure of the attempt
	 */
	public String addBid(int ID, double P, Bidder B) throws RemoteException
    {
        Auction biddingAuction = (Auction) auctions.get(ID);
        //Check that auction is active
        if(biddingAuction != null)
        {
            //Check that bid is higher than current highest
            if(P <= biddingAuction.getHighestBid())
            {
                return "\nBidding pice was too low - bid must be above " + biddingAuction.getHighestBid() +"\n";
            }

            //Add new bid if info is valid
            biddingAuction.setHighestBid(P);
            biddingAuction.setHighestBidder(B);
            return "\nYour bid was successfuly made\n";
        } else {
            return "\nBid failed - no auction with an ID of " + ID + " was found\n";
        }
	}

	/**
	 * Provides the current auction ID
	 * @return the ID of the current auction
	 */
	public int getCurrentID() throws RemoteException
	{
		return currentID;
	}


    /**
     * Prvoides auction listings in text form
     * @return A string containing auction listings
     */
	public String getAuctionText() throws RemoteException
	{
		String auctionText = "";
		Set set = auctions.entrySet();
		Iterator iterator = set.iterator();

		if (auctions.size() == 0) {
            return "\nThere are no active auctions\n";
        }

		while(iterator.hasNext()) {
			Map.Entry mentry = (Map.Entry)iterator.next();
			Auction auctionListing = (Auction) mentry.getValue();

            auctionText = auctionText
					+ (auctionListing.getID() + "\t"
					+ String.format( "%.2f", auctionListing.getHighestBid()) + "\t\t"
					+ auctionListing.getDescription() + "\n");
		}

		return auctionText;
	}
}

