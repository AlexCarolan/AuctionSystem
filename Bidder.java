import java.io.*;
/**
 * A class that represents a single Bidder
 */
public class Bidder implements Serializable
{
	//Bidders details
	private String name;
	private String email;

    /**
     * Constructor for bidder class
     * @param N - bidder name
     * @param E - bidders email
     */
	public Bidder(String N, String E){
	    name = N;
	    email = E;
    }

    /**
     * Sets the name to input value
     * @param N bidders name
     */
	public void setName(String N){
	    name = N;
    }

    /**
     *Sets the email value to input value
     * @param E bidders email address
     */
    public void setEmail(String E){
        email = E;
    }

    /**
     * Provides the bidders name
     * @return - the name of the bidder
     */
    public String getName()
    {
        return name;
    }

    /**
     * Provides the bidders email
     * @return - the email of the bidder
     */
    public String getEmail() {
        return email;
    }







}