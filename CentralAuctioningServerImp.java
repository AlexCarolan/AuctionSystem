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
	public static int creatorID = 1;
	
	//Setup of the hash table for auctions
    public static final HashMap<Integer, Auction> auctions = new HashMap<Integer, Auction>();

    /**
     * Add an auction to the system
     * @param SP - starting price of auction
     * @param desc - auction description
     * @param RP - reserve price of the auction
     * @return string describing outcome of request
     */
	public synchronized String addAuction(double SP, String desc, double RP, int CID) throws RemoteException
    {
		auctions.put(currentID, new Auction(currentID, CID, SP, desc, RP));
        currentID++;
        return "\nYour auction was created successfully, ID: "
                + (currentID - 1) + "\n";
	}

    /**
     * Allocates and provides a new creator ID
     * @return the new creator ID
     * @throws RemoteException
     */
	public synchronized int getNewCreatorID() throws RemoteException
    {
        creatorID++;
        return creatorID;
    }


    /**
     * Attempts to end an active auction
     * @param AID - the ID of the auction to end
     * @param CID - the ID of the creator making the request
     * @return a message explaing the result of the attempt
     * @throws RemoteExpection
     */
	public String endAuction(int AID, int CID) throws RemoteException
    {
        Auction auction = (Auction) auctions.get(AID);

        //Check auction ID mathches active auction
        if(auction != null)
        {
            //Check that the request was made by the auctions creator
            if(auction.getCreatorID() == CID) {

                //Check that the auction has been bid on
                if(auction.getStartPrice() >= auction.getHighestBid()){
                    auctions.remove(AID);
                    return "\nAuction " + AID + " has ended, no bids were made\n";

                //Check if the reserve has been met
                } else if (auction.getHighestBid() < auction.getReservePrice()){
                    auctions.remove(AID);
                    return "\nAuction " + AID + " has ended, the higest bid was "
                            + String.format( "%.2f", auction.getHighestBid())
                            + " (GBP) the reserve price of "
                            + String.format( "%.2f", auction.getReservePrice())
                            + " (GBP) was not met\n";

                //If reserve has been met, display auction winner
                } else {
                    auctions.remove(AID);
                    Bidder winner = auction.getHighestBidder();

                    return "\nAuction " + AID + " has ended successfully!\n"
                            + "\nWinner Details:\n"
                            + "Name: " + winner.getName() + "\n"
                            + "Email: " + winner.getEmail() + "\n"
                            + "End Price: " + auction.getHighestBid() + "\n";
                }

            } else {
                return "\nThis auction does not belong to you\n";
            }

        } else {
            return "\nNo auction with an ID of " + AID +" could be found\n";
        }
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
            //!!! Not Efficent !!! Improve This !!! Possibly use concurrent hash map
            synchronized(this) {
                //Check that bid is higher than current highest
                if(P <= biddingAuction.getHighestBid())
                {
                    return "\nBidding pice was too low - bid must be above " + biddingAuction.getHighestBid() +"\n";
                }

                //Add new bid if info is valid
                biddingAuction.setHighestBid(P);
                biddingAuction.setHighestBidder(B);
            }
            return "\nYour bid was successfuly made\n";
        } else {
            return "\nBid failed - no auction with an ID of " + ID + " was found\n";
        }
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

    /**
     * Prvoides auction listings in text form - limited to acutons with matching creatorIDs
     * @param CID
     * @return A string containing auction listings belonging to the creator
     * @throws RemoteException
     */
    public String getAuctionText(int CID) throws RemoteException
    {
        String auctionText = "";
        Set set = auctions.entrySet();
        Iterator iterator = set.iterator();

        while(iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)iterator.next();
            Auction auctionListing = (Auction) mentry.getValue();

            //Check that the creator IDs match
            if (auctionListing.getCreatorID() == CID)
            {
                auctionText = auctionText
                        + (auctionListing.getID() + "\t"
                        + String.format("%.2f", auctionListing.getHighestBid()) + "\t\t"
                        + String.format("%.2f", auctionListing.getReservePrice()) + "\t\t"
                        + auctionListing.getDescription() + "\n");
            }
        }

        if (auctionText.equals(""))
        {
            return "\nYou have no active auctions\n";
        }

        return auctionText;
    }
}

