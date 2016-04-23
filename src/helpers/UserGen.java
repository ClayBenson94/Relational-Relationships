package helpers;

import objects.User;
import objects.RelationshipController;
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

    private ArrayList<User> userGens;
    private ArrayList<Integer> locations;
    

    public UserGen(){
        userGens = new ArrayList<UserGen>();
        locations = new ArrayList<Integer>();
        
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
    }
  
    public void generateUsers(int numUsers){
        String username;
        String password;
        String firstName;
        String bio;
        String email;
        Date dob;
        String gender;
	    String sexuality;
	    int location;
	    int preferredAgeMin;
	    int preferredAgeMax;
	    String preferredSexuality;  
	    
	    Random rand = new Random();
	    
	    for(int i = 0; i < numUsers; i++){
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
		
		    userGens.add(new User(username,password,firstName,bio,email,gender,sexuality,location,
		        preferredAgeMin,preferredAgeMax,preferredSexuality,false));
	    }
    }
    
    public void insertIntoUserDB(){
        Connection conn = RelationshipController.getConnection();
        try{
            StringBuilder sb = new StringBuilder();

            sb.append("INSERT INTO user (username, password, name, bio, email, dob, gender, sexuality," +
                  " location, preferred_age_min, preferred_age_max, preferred_sexuality) VALUES");

            for (int i = 0; i < userGens.size(); i++) {
                UserGen user = userGens.get(i);
                sb.append(String.format("('%s','%s','%s','%s','%s','%s','%s','%s',%d,%d,%d,'%s')",
                    user.getUsername(),
                    user.getPassword(),
                    user.getName(),
                    user.getBio(),
                    user.getEmail(),
                    user.getDob(),
                    user.getGender(),
                    user.getSexuality(),
                    user.getLocation(),
                    user.getUserPreferences.getPreferredAgeMin(),
                    user.getUserPreferences.getPreferredAgeMax(),
                    user.getUserPreferences.getPreferredSexuality()));
                if (i != usersGens.size() - 1) {
                    sb.append(",");
                } else {
                    sb.append(";");
                }
            }
            SQLHelper.execute(conn, sb.toString());
        }
        catch(SQLException e){
            e.printStackTrace();    
        }
    }
  
} 
