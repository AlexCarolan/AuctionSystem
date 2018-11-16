import java.util.Scanner;
import java.text.DecimalFormat;
import java.math.RoundingMode;

/**
 * Provides a text based user interface used to create and end auctions
 */
public class CreateDriver
{
	
	private final int creatorID;
	private final Scanner reader;
	private final CentralAuctioningServer server;

    /**
     * Constructor for CreateDriver class, requires no inputs
     */
	public CreateDriver(){
        //Lookup remote auction server object - retry 3 times if failed
        server = new AuctionConnector().connect();
        //Get a new creator ID
        creatorID = obtainCreatorID();
        //Create an input scanner
        reader = new Scanner(System.in);
	}
	
    /**
     * Starts the auction creator interface
     */
	public void start()
	{
		//Print active auctions
        showActiveAuctions();

		//Allow for user input and respond to commands
		String inputCommand = "";

		while (!inputCommand.equals("exit") && !inputCommand.equals("EXIT") && !inputCommand.equals("Exit")) {

			//Display commands and take user input
			System.out.println("Commands - list (displays active auctions), create(create a new auction), end(end an active auction), exit (ends program)");
			System.out.print("Enter a command: ");
			inputCommand = reader.nextLine();

			//Check input against list of commands
			if(inputCommand.equals("list") || inputCommand.equals("LIST") || inputCommand.equals("List"))
			{
			    //Shows active auctions
                showActiveAuctions();
            }
			else if(inputCommand.equals("create") || inputCommand.equals("CREATE") || inputCommand.equals("Create"))
			{
                //Attempts to create a new auction
                addNewAuction();
			}
            else if(inputCommand.equals("end") || inputCommand.equals("END") || inputCommand.equals("End"))
            {
                //Attempts to end an active auction
                endActiveAuction();
            }
            else
            {
                System.out.println("!!! Command not valid !!!");
            }

		}

        //close input scanner before program termination
        reader.close();
        System.out.println("=====================================================");
        System.out.println("\t\tPROGRAM TERMINATED");
        System.out.println("=====================================================");

        return;
	}

    /**
     * Prints out active auctions created by the user
     */
	private void showActiveAuctions()
    {
        System.out.println("=====================================================");
        System.out.println("\t\tActive Auctions");
        System.out.println("=====================================================");
        System.out.println("ID#\tPRICE(GBP)\tRESERVE(GBP)\tDESCRIPTION");

        try{
            System.out.println(server.getAuctionText(creatorID));
        } catch(Exception e) {
            System.out.println("\nFailed to obtain active listings");
            System.out.println(e);
        }

        System.out.println("=====================================================");
    }

    /**
     * gets a new creator ID from the auction server
     */
    private int obtainCreatorID()
    {
        //attempt to get a new creator ID
        int attempt = 1;
        while (attempt < 4) {
            try {
                return server.getNewCreatorID();
            } catch (Exception e) {
                System.out.println("Failed to obtain a new creator ID\n" + e);
            }
        }

        //Terminate program on repeated failure
        System.out.println("\nCould not obtain creator ID - Terminating program");
        System.exit(0);
        return 1;
    }

	/**
	* Attempt to end an active auction 
	*/
    private void endActiveAuction()
    {
        System.out.println("=====================================================");
        System.out.println("\t\tEnd Auction");
        System.out.println("=====================================================");

        int auctionID;

        try {
            System.out.print("Auction ID: ");
            auctionID = reader.nextInt();
        } catch (Exception e) {
            System.out.println("\n!!! Input was not a valid ID !!!\n");
            reader.nextLine();
            return;
        }

        //Attempt to end the auction on the server
        try {
            System.out.println(server.endAuction(auctionID, creatorID));
            reader.nextLine();
        } catch (Exception e) {
            System.out.println("\nFailed to contact remote auction server object");
            System.out.println(e);
        }

    }


    /**
     * Collects user input and attempts to create a new auction using the provided data
     */
    private void addNewAuction()
    {
        System.out.println("=====================================================");
        System.out.println("\t\tCreate New Auction");
        System.out.println("=====================================================");

        double startPrice;
        double reservePrice;
        String description;

        //Define decimal format
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);

        //Get the start price and check for validation
        try {
            System.out.print("Starting price (GBP): ");
            startPrice = reader.nextDouble();
        } catch (Exception e) {
            System.out.println("\n!!! Input was not a valid number !!!\n");
            reader.nextLine();
            return;
        }

        //Truncate doubles to two decimal places for use as currency
        startPrice =  Double.parseDouble(df.format(startPrice));

        //Check that starting price is above 0
        if (startPrice <=0) {
            System.out.println("\n!!! Start price must be 0.01(GBP) or above !!!\n");
            reader.nextLine();
            return;
        }

        //Get reserve price and check for validation
        try {
            System.out.print("Reserve price (GBP): ");
            reservePrice = reader.nextDouble();
        } catch (Exception e) {
            System.out.println("\n!!! Input was not a valid number !!!\n");
            reader.nextLine();
            return;
        }

        //Truncate doubles to two decimal places for use as currency
        reservePrice =  Double.parseDouble(df.format(reservePrice));

        //Check that the reserve price is greater than the starting price
        if (reservePrice <= startPrice)
        {
            System.out.println("\n!!! Reserve price must be higher than starting price !!!\n");
            reader.nextLine();
            return;
        }

        //Get the auctions description
        System.out.print("Enter auction description: ");
        reader.nextLine();
        description = reader.nextLine();


        //Attempt to create the auction on the server
        try {
            System.out.println(server.addAuction(startPrice, description, reservePrice, creatorID));
        } catch (Exception e) {
            System.out.println("\nFailed to contact remote auction server object");
            System.out.println(e);
        }
    }


}

