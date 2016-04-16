package helpers;

import helpers.SQLHelper;
import java.util.ArrayList;
import java.util.Random;
import java.sql.Date;
import java.sql.Connection;

/**
 * UserGen objects represent a randomly generated user.
 *
 * Created by Josh Horning
 */
public class UserGen{

    private static final String[] ALPHABET = 
    {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r",
     "s","t","u","v","w","x","y","z"};

    private static final String[] MALEFIRSTNAMES = 
	 {"Alan","Andrew","Andy","Bert","Billy","Bob","Cal","Charley","Chris","Clay",
	 "Dan","Dave","Drew","Dylan","Eddie","Frank","Greg","Jarryd","Jimmy","John",
	 "Josh","Pablo","Preston","Raul","Robert","Samuel","Scott","Timmy","Vince",
	 "Walter","Wilbur","Willie","Zack"};

    private static final String[] FEMALEFIRSTNAMES = 
	 {"Alice","Allison","Amelia","Becky","Beth","Brittany","Cathy","Cindy",
	 "Debra","Elizabeth","Emma","Faye","Flo","Greta","Helen","Isabella",
	 "Janet","Janice","Jennifer","Judy","Julia","Kate","Rebecca","Triss",
	 "Valerie"};

    private String username;
    private String password;
    private String firstName;
    private String bio;
    private String email;
    private Date dob;
    private String gender;
	private String sexuality;
	private int location;
	private ArrayList<Integer> locations;
	private int preferredAgeMin;
	private int preferredAgeMax;
	private String preferredSexuality;
  

    public UserGen(){
        Random rand = new Random();
		if(rand.nextInt(2) == 1){
		    gender = "Male";
		    firstName = MALEFIRSTNAMES[rand.nextInt(MALEFIRSTNAMES.length)];
	    }
	  	else{
	        gender = "Female";
			firstName = FEMALEFIRSTNAMES[rand.nextInt(FEMALEFIRSTNAMES.length)];
		}
		
		username = firstName.toLowerCase()+alphabet[rand.nextInt(ALPHABET.length)]+
		           alphabet[rand.nextInt(ALPHABET.length)]+alphabet[rand.nextInt(ALPHABET.length)]+
		           alphabet[rand.nextInt(ALPHABET.length)]+alphabet[rand.nextInt(ALPHABET.length)]+
		           rand.nextInt(10000);
		           
		password = "";
		for(int x = 0; x < 10; x++){
		    password += rand.nextInt(10);
		}
		
		Connection conn = RelationshipController.getConnection();
		try{
		    String locationQuery = "SELECT zip_code FROM location";
		    ResultSet resultSet = SQLHelper.executeQuery(conn, locationQuery)
		    while(resultSet.next()){
		        locations.add(resultSet.getInt("zip_code"));	
		    }
		}
		catch(SQLException e){
		    e.printStackTrace();    
		}
		
		location = locations.get(rand.nextInt(locations.size()));
		
		bio = "I am from location " + location;
		
	    email = username + "@relationalrelationships.com"; 
		
		if(rand.nextInt(2) == 1){
		    sexuality = "Heterosexual";
		}
		else{
		    sexuality = "Homosexual";
		}
		
		if(rand.nextInt(2) == 1){
		    preferredSexuality = "Heterosexual";
		}
		else{
		    preferredSexuality = "Homosexual";
		}
		
	  	preferredAgeMin = rand.nextInt(101);
		preferredAgeMax = 0;
		while(preferredAgeMax <= preferredAgeMin){
		    preferredAgeMax = rand.nextInt(101);
		}
		
		long num = 0;
		long lower = 20000;
		long current = System.currentTimeMillis();
		while(num < lower || num > current){
		    num = lower + (long)(rand.nextDouble()*(current-lower));
		}
		dob = new Date(num);
		
    }
  
    public void insertIntoUserDB(){
        try{
            String userInsertQuery = "INSERT INTO user "
                + "VALUES (\'" + this.getUsername()
                + "\',\'" + this.getPassword()
                + "\',\'" + this.getName()
                + "\',\'" + this.getBio()
                + "\',\'" + this.getEmail()
                + "\',\'" + this.getDOB()
                + "\',\'" + this.getGender()
                + "\',\'" + this.getSexuality()
                + "\'," + this.getLocation()
                + "," + this.getPreferredAgeMin()
                + "," + this.getPreferredAgeMax()
                + ",\'" + this.getPreferredSexuality() + "\');";
            SQLHelper.executeQuery(conn, userInsertQuery);
        }
        catch(SQLException e){
            e.printStackTrace();    
        }
    }
  
    public String getUsername(){
	     return username;
	 }
	 
	 public String getPassword(){
	     return password;
	 }
	 
	 public String getName(){
	     return firstName;
	 }
	 
	 public String getBio(){
	     return bio;
	 }
	 
	 public String getEmail(){
	     return email;
	 }
	 
	 public Date getDOB(){
	     return dob;
	 }
	 
	 public String getGender(){
	     return gender;
	 }
	 
	 public String getSexuality(){
	     return sexuality;
	 }
	 
	 public int getLocation(){
	     return location;
	 }
	 
	 public int getPreferredAgeMin(){
	     return preferredAgeMin;
	 }
	 
	 public int getPreferredAgeMax(){
	     return preferredAgeMax;
	 }
	 
	 public String getPreferredSexuality(){
	     return preferredSexuality;
	 }
  
  

    public static void main(String [] args){
  
        UserGen usr;
  
        for(int i = 0; i < 10; i++){
		      usr = new UserGen();
				System.out.print("{"+
				    usr.getUsername()+", "+ 
					 usr.getPassword()+", "+ 
					 usr.getName()+", "+
					 usr.getBio()+", "+
					 usr.getEmail()+", "+
					 usr.getDOB()+", "+
					 usr.getGender()+", "+
					 usr.getSexuality()+", "+
					 usr.getLocation()+", "+
					 usr.getPreferredAgeMin()+", "+
					 usr.getPreferredAgeMax()+", "+
					 usr.getPreferredSexuality()+
					 "}\n");  
		  }

    }

} 
