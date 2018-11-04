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







}