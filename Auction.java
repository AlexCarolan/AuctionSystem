/**
 *A class that represents an auction
 */
public class Auction
{
	//Data relating to auction
	private int auctionID;
	private int creatorID;
	private double startingPrice;
	private String description;
	private double reservePrice;
	private double highestBid;
	private Bidder highestBidder = null;
	
	//Auction constructor

    /**
     * Constructor for auction class
     * @param ID - ID for the auction
	 * @param CID - ID of the auctions creator
     * @param SP - Starting price for the auction
     * @param desc - Description of the auction
     * @param RP - Reserve price for the auction
     */
	public Auction (int ID, int CID, double SP, String desc, double RP)
	{
		auctionID = ID;
		creatorID = CID;
		startingPrice = SP;
		description = desc;
		reservePrice = RP;
		highestBid = SP;
	}
	
	// Accessor methods for auction data

    /**
     * Provides access to auction ID
     * @return ID of the auction
     */
	public int getID()
	{
		return auctionID;
	}

    /**
     * Provides access to the creatorID
     * @return ID of the auctions creator
     */
	public int getCreatorID() {
	    return creatorID;
    }

    /**
     * Provides access to start price
     * @return starting price of auction
     */
	public double getStartPrice()
	{
		return startingPrice;
	}

    /**
     * Provides access to auction description
     * @return description of the auction
     */
	public String getDescription()
	{
		return description;
	}

    /**
     * Provides access to auction reserve price
     * @return reserve price of auction
     */
	public double getReservePrice()
	{
		return reservePrice;
	}

    /**
     * Provides access to current highest bid
     * @return the current highest bid
     */
	public double getHighestBid()
	{
		return highestBid;
	}

    /**
     * Provides access to the higest bidder instance
     * @return highest bidder instance
     */
	public Bidder getHighestBidder()
	{
		return highestBidder;
	}
	
	//Set methods for auction data

    /**
     * changes the value of the higest bid
     * @param HB - new highest bid value
     */
	public void setHighestBid(double HB)
	{
		highestBid = HB;
	}

    /**
     *
     * @param B - Bidder instance of the new highest bidder
     */
	public void setHighestBidder(Bidder B)
	{
		highestBidder = B;
	}

}

