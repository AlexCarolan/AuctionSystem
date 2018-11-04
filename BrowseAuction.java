import java.rmi.Naming;
import java.util.Scanner;
import java.text.DecimalFormat;
import java.math.RoundingMode;
/**
 * A class that allows users to browse and bid on auctions
 */
public class BrowseAuction
{

	//TO DO:
	//- Display current auctions (Check if still active)
	//- Allow bids on aucitons

    public static void main(String args[]) {

        //Lookup remote auction server object - retry 3 times if failed
        CentralAuctioningServer server = null;
        int attempt = 1;
        while (attempt < 4) {
            try {
                // Get a reference to the remote object through the rmiregistry
                server = (CentralAuctioningServer) Naming.lookup("rmi://localhost/AuctionServer");
                server.addAuction(20.00, "New hat", 40);
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

        //Print out currently active auctions
        showListings(server);

        //Allow for uesr input and respond to commands
        String inputCommand = "";
        Scanner reader = new Scanner(System.in);

        while (!inputCommand.equals("exit") && !inputCommand.equals("EXIT") && !inputCommand.equals("Exit")) {

            //Display commands and take user input
            System.out.println("Commands - list (displays active auctions), bid (bid on an item), exit (ends program)");
            System.out.print("Enter a command: ");
            inputCommand = reader.nextLine();

            //Check input against list of commands
            if(inputCommand.equals("list") || inputCommand.equals("LIST") || inputCommand.equals("List"))
            {
                showListings(server);
            }
            else if(inputCommand.equals("bid") || inputCommand.equals("BID") || inputCommand.equals("Bid"))
            {
                addBid(server, reader);
            }

        }


        //close input scanner before program termination
        reader.close();
        System.out.println("=====================================================");
        System.out.println("\t\tPROGRAM TERMINATED");
        System.out.println("=====================================================");

    }


    /**
     * Prints out currently active listings
     * @param server - The server RMI object holding listings
     */
    public static void showListings(CentralAuctioningServer server) {
        try {
            System.out.println("=====================================================");
            System.out.println("\t\t Auction Browser");
            System.out.println("=====================================================");
            System.out.println("ID#\tPRICE\tDESCRIPTION");
            System.out.println(server.getAuctionText());
            System.out.println("=====================================================");
        } catch (Exception e) {
            System.out.println("\nFailed to obtain auction listings");
            System.out.println(e);
        }
    }

    /**
     * Allows for user to make a bid, validates inputs
     * @param server - the remote auction server object
     * @param reader - the input scanner object
     */
    public static void addBid(CentralAuctioningServer server, Scanner reader)
    {

        System.out.println("=====================================================");
        System.out.println("\t\tBid Submission");
        System.out.println("=====================================================");

        int auctionID;
        double bidAmmount;
        Bidder bidder = new Bidder();

        //Get the ID number and bid ammount check input valid
        try {
            System.out.print("Select an auction (ID#): ");
            auctionID = reader.nextInt();
            System.out.print("Enter bid ammount (GBP): ");
            bidAmmount = reader.nextDouble();
        } catch (Exception e) {
            System.out.println("\n!!! Input was not a valid number !!!\n");
            reader.nextLine();
            return;
        }

        //Check bid is valid format
        if (bidAmmount <= 0) {
            System.out.println("\n!!! Input bid was not valid, must be larger than zero !!!\n");
            return;
        }

        //Truncate bid value to two decimal places for use as currency
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        bidAmmount =  Double.parseDouble(df.format(bidAmmount));

        //Get the users name and email
        System.out.print("Enter your name: ");
        reader.nextLine();
        bidder.setName(reader.nextLine());

        System.out.print("Enter your email adress: ");
        bidder.setEmail(reader.nextLine());

        //Attempt contact auction server and add bid
        try {
            System.out.println(server.addBid(auctionID, bidAmmount, bidder));
        } catch (Exception e) {
            System.out.println("\nFailed to contact remote auction server object");
            System.out.println(e);
        }
    }


}

