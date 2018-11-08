import java.util.Scanner;
import java.text.DecimalFormat;
import java.math.RoundingMode;

/**
 * Provides a text based user interface used to create and end auctions
 */
public class CreateDriver
{
    /**
     * Starts the auction creator interface
     */
	public void start()
	{

		//Lookup remote auction server object - retry 3 times if failed
		CentralAuctioningServer server = new AuctionConnector().connect();

		//Get a new creator ID
        int creatorID = obtainCreatorID(server);

		//Print active auctions
        showActiveAuctions(creatorID, server);

		//Allow for uesr input and respond to commands
		String inputCommand = "";
		Scanner reader = new Scanner(System.in);

		while (!inputCommand.equals("exit") && !inputCommand.equals("EXIT") && !inputCommand.equals("Exit")) {

			//Display commands and take user input
			System.out.println("Commands - list (displays active auctions), create(create a new auction), end(end an active auction), exit (ends program)");
			System.out.print("Enter a command: ");
			inputCommand = reader.nextLine();

			//Check input against list of commands
			if(inputCommand.equals("list") || inputCommand.equals("LIST") || inputCommand.equals("List"))
			{
			    //Shows active auctions
                showActiveAuctions(creatorID, server);
            }
			else if(inputCommand.equals("create") || inputCommand.equals("CREATE") || inputCommand.equals("Create"))
			{
                //Attempts to create a new auction
                addNewAuction(creatorID, server, reader);
			}
            else if(inputCommand.equals("end") || inputCommand.equals("END") || inputCommand.equals("End"))
            {
                //Attempts to end an active auction
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
     * @param creatorID - ID of the creator
     * @param server - the auction server
     */
	private void showActiveAuctions(int creatorID, CentralAuctioningServer server)
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
     * @param server - the auction serevr
     * @return the new creator ID
     */
    private int obtainCreatorID(CentralAuctioningServer server)
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
     * Collects user input and attempts to create a new auction using the provided data
     * @param creatorID - The ID of the auction creator
     * @param server - The RMI server AuctionServer object
     * @param reader - The scanner used to take user input
     */
    private void addNewAuction(int creatorID, CentralAuctioningServer server, Scanner reader)
    {
        System.out.println("=====================================================");
        System.out.println("\t\tCreate New Auction");
        System.out.println("=====================================================");

        double startPrice;
        double reservePrice;
        String description;

        //Get the start and reserve price and check for validation
        try {
            System.out.print("Starting price (GBP): ");
            startPrice = reader.nextDouble();
            System.out.print("Reserve price (GBP): ");
            reservePrice = reader.nextDouble();
        } catch (Exception e) {
            System.out.println("\n!!! Input was not a valid number !!!\n");
            reader.nextLine();
            return;
        }

        //Truncate doubles to two decimal places for use as currency
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        startPrice =  Double.parseDouble(df.format(startPrice));
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

