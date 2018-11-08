import java.util.Scanner;
import java.text.DecimalFormat;
import java.math.RoundingMode;
/**
 * A class that allows users to browse and bid on auctions
 */
public class BrowserDriver
{

    /**
     * starts the auction browser
     */
    public void start(){

        //Lookup remote auction server object - retry 3 times if failed
        CentralAuctioningServer server = new AuctionConnector().connect();

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

        return;
    }


    /**
     * Prints out currently active listings
     * @param server - The server RMI object holding listings
     */
    private void showListings(CentralAuctioningServer server) {

        System.out.println("=====================================================");
        System.out.println("\t\t Auction Browser");
        System.out.println("=====================================================");
        System.out.println("ID#\tPRICE(GBP)\tDESCRIPTION");

        try {
            System.out.println(server.getAuctionText());
        } catch (Exception e) {
            System.out.println("\nFailed to obtain auction listings");
            System.out.println(e);
        }

        System.out.println("=====================================================");
    }

    /**
     * Allows for user to make a bid, validates inputs
     * @param server - the remote auction server object
     * @param reader - the input scanner object
     */
    private void addBid(CentralAuctioningServer server, Scanner reader)
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

