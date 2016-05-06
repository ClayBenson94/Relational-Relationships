package helpers;

import objects.User;
import objects.RelationshipController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.sql.Date;
import java.sql.Connection;

/**
 * UserGen objects represent a randomly generated user.
 * <p>
 * Created by Josh Horning
 */
public class UserGen {


    private static final String[] ALPHABET =
            {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
                    "s", "t", "u", "v", "w", "x", "y", "z"};

    private static final String[] MALEFIRSTNAMES =
            {"Alan", "Andrew", "Andy", "Bert", "Billy", "Bob", "Cal", "Charley", "Chris", "Clay",
                    "Dan", "Dave", "Drew", "Dylan", "Eddie", "Frank", "Greg", "Jarryd", "Jimmy", "John",
                    "Josh", "Pablo", "Preston", "Raul", "Robert", "Samuel", "Scott", "Timmy", "Vince",
                    "Walter", "Wilbur", "Willie", "Zack"};

    private static final String[] FEMALEFIRSTNAMES =
            {"Alice", "Allison", "Amelia", "Becky", "Beth", "Brittany", "Cathy", "Cindy",
                    "Debra", "Elizabeth", "Emma", "Faye", "Flo", "Greta", "Helen", "Isabella",
                    "Janet", "Janice", "Jennifer", "Judy", "Julia", "Kate", "Rebecca", "Triss",
                    "Valerie"};

    private static final String[] LASTNAMES =
            {"Smith", "Johnson", "Williams", "Brown", "Jones", "Miller", "Davis", "Garcia", "Rodriguez", "Wilson",
                    "Martinez", "Anderson", "Taylor", "Thomas", "Hernandez", "Moore", "Martin", "Jackson", "Thompson",
                    "White", "Lopez", "Lee", "Gonzalez", "Harris", "Clark", "Lewis", "Robinson", "Walker", "Perez",
                    "Hall", "Young", "Allen", "Sanchez", "Wright", "King", "Scott", "Green", "Baker", "Adams",
                    "Nelson", "Hill", "Ramirez", "Campbell", "Mitchell", "Roberts", "Carter", "Phillips", "Evans",
                    "Turner", "Torres", "Parker", "Collins", "Edwards", "Stewart", "Flores", "Morris", "Nguyen",
                    "Murphy", "Rivera", "Cook", "Rogers", "Morgan", "Peterson", "Cooper", "Reed", "Bailey", "Bell",
                    "Gomez", "Kelly", "Howard", "Ward", "Cox", "Diaz", "Richardson", "Wood", "Watson", "Brooks",
                    "Bennett", "Gray", "James", "Reyes", "Cruz", "Hughes", "Price", "Myers", "Long", "Foster",
                    "Sanders", "Ross", "Morales", "Powell", "Sullivan", "Russell", "Ortiz", "Jenkins", "Gutierrez",
                    "Perry", "Butler", "Barnes", "Fisher", "Henderson", "Coleman", "Simmons", "Patterson", "Jordan",
                    "Reynolds", "Hamilton", "Graham", "Kim", "Gonzales", "Alexander", "Ramos", "Wallace", "Griffin",
                    "West", "Cole", "Hayes", "Chavez", "Gibson", "Bryant", "Ellis", "Stevens", "Murray", "Ford",
                    "Marshall", "Owens", "Mcdonald", "Harrison", "Ruiz", "Kennedy", "Wells", "Alvarez", "Woods",
                    "Mendoza", "Castillo", "Olson", "Webb", "Washington", "Tucker", "Freeman", "Burns", "Henry",
                    "Vasquez", "Snyder", "Simpson", "Crawford", "Jimenez", "Porter", "Mason", "Shaw", "Gordon",
                    "Wagner", "Hunter", "Romero", "Hicks", "Dixon", "Hunt", "Palmer", "Robertson", "Black", "Holmes",
                    "Stone", "Meyer", "Boyd", "Mills", "Warren", "Fox", "Rose", "Rice", "Moreno", "Schmidt", "Patel",
                    "Ferguson", "Nichols", "Herrera", "Medina", "Ryan", "Fernandez", "Weaver", "Daniels", "Stephens",
                    "Gardner", "Payne", "Kelley", "Dunn", "Pierce", "Arnold", "Tran", "Spencer", "Peters", "Hawkins",
                    "Grant", "Hansen", "Castro", "Hoffman", "Hart", "Elliott", "Cunningham", "Knight", "Bradley",
                    "Carroll", "Hudson", "Duncan", "Armstrong", "Berry", "Andrews", "Johnston", "Ray", "Lane", "Riley",
                    "Carpenter", "Perkins", "Aguilar", "Silva", "Richards", "Willis", "Matthews", "Chapman", "Lawrence",
                    "Garza", "Vargas", "Watkins", "Wheeler", "Larson", "Carlson", "Harper", "George", "Greene", "Burke",
                    "Guzman", "Morrison", "Munoz", "Jacobs", "Obrien", "Lawson", "Franklin", "Lynch", "Bishop", "Carr",
                    "Salazar", "Austin", "Mendez", "Gilbert", "Jensen", "Williamson", "Montgomery", "Harvey", "Oliver",
                    "Howell", "Dean", "Hanson", "Weber", "Garrett", "Sims", "Burton", "Fuller", "Soto", "Mccoy",
                    "Welch", "Chen", "Schultz", "Walters", "Reid", "Fields", "Walsh", "Little", "Fowler", "Bowman",
                    "Davidson", "May", "Day", "Schneider", "Newman", "Brewer", "Lucas", "Holland", "Wong", "Banks",
                    "Santos", "Curtis", "Pearson", "Delgado", "Valdez", "Pena", "Rios", "Douglas", "Sandoval",
                    "Barrett", "Hopkins", "Keller", "Guerrero", "Stanley", "Bates", "Alvarado", "Beck", "Ortega",
                    "Wade", "Estrada", "Contreras", "Barnett", "Caldwell", "Santiago", "Lambert", "Powers",
                    "Chambers", "Nunez", "Craig", "Leonard", "Lowe", "Rhodes", "Byrd", "Gregory", "Shelton",
                    "Frazier", "Becker", "Maldonado", "Fleming", "Vega", "Sutton", "Cohen", "Jennings", "Parks",
                    "Mcdaniel", "Watts", "Barker", "Norris", "Vaughn", "Vazquez", "Holt", "Schwartz", "Steele",
                    "Benson", "Neal", "Dominguez", "Horton", "Terry", "Wolfe", "Hale", "Lyons", "Graves", "Haynes",
                    "Miles", "Park", "Warner", "Padilla", "Bush", "Thornton", "Mccarthy", "Mann", "Zimmerman",
                    "Erickson", "Fletcher", "Mckinney", "Page", "Dawson", "Joseph", "Marquez", "Reeves", "Klein",
                    "Espinoza", "Baldwin", "Moran", "Love", "Robbins", "Higgins", "Ball", "Cortez", "Le", "Griffith",
                    "Bowen", "Sharp", "Cummings", "Ramsey", "Hardy", "Swanson", "Barber", "Acosta", "Luna", "Chandler",
                    "Daniel", "Blair", "Cross", "Simon", "Dennis", "Oconnor", "Quinn", "Gross", "Navarro", "Moss",
                    "Fitzgerald", "Doyle", "Mclaughlin", "Rojas", "Rodgers", "Stevenson", "Singh", "Yang",
                    "Figueroa", "Harmon", "Newton", "Paul", "Manning", "Garner", "Mcgee", "Reese", "Francis",
                    "Burgess", "Adkins", "Goodman", "Curry", "Brady", "Christensen", "Potter", "Walton",
                    "Goodwin", "Mullins", "Molina", "Webster", "Fischer", "Campos", "Avila", "Sherman", "Todd",
                    "Chang", "Blake", "Malone", "Wolf", "Hodges", "Juarez", "Gill", "Farmer", "Hines", "Gallagher",
                    "Duran", "Hubbard", "Cannon", "Miranda", "Wang", "Saunders", "Tate", "Mack", "Hammond", "Carrillo",
                    "Townsend", "Wise", "Ingram", "Barton", "Mejia", "Ayala", "Schroeder", "Hampton", "Rowe",
                    "Parsons", "Frank", "Waters", "Strickland", "Osborne", "Maxwell", "Chan", "Deleon", "Norman",
                    "Harrington", "Casey", "Patton", "Logan", "Bowers", "Mueller", "Glover", "Floyd", "Hartman",
                    "Buchanan", "Cobb", "French", "Kramer", "Mccormick", "Clarke", "Tyler", "Gibbs", "Moody",
                    "Conner", "Sparks", "Mcguire", "Leon", "Bauer", "Norton", "Pope", "Flynn", "Hogan", "Robles",
                    "Salinas", "Yates", "Lindsey", "Lloyd", "Marsh", "Mcbride", "Owen", "Solis", "Pham", "Lang",
                    "Pratt", "Lara", "Brock", "Ballard", "Trujillo", "Shaffer", "Drake", "Roman", "Aguirre", "Morton",
                    "Stokes", "Lamb", "Pacheco", "Patrick", "Cochran", "Shepherd", "Cain", "Burnett", "Hess", "Li",
                    "Cervantes", "Olsen", "Briggs", "Ochoa", "Cabrera", "Velasquez", "Montoya", "Roth", "Meyers",
                    "Cardenas", "Fuentes", "Weiss", "Wilkins", "Hoover", "Nicholson", "Underwood", "Short", "Carson",
                    "Morrow", "Colon", "Holloway", "Summers", "Bryan", "Petersen", "Mckenzie", "Serrano", "Wilcox",
                    "Carey", "Clayton", "Poole", "Calderon", "Gallegos", "Greer", "Rivas", "Guerra", "Decker",
                    "Collier", "Wall", "Whitaker", "Bass", "Flowers", "Davenport", "Conley", "Houston", "Huff",
                    "Copeland", "Hood", "Monroe", "Massey", "Roberson", "Combs", "Franco", "Larsen", "Pittman",
                    "Randall", "Skinner", "Wilkinson", "Kirby", "Cameron", "Bridges", "Anthony", "Richard", "Kirk",
                    "Bruce", "Singleton", "Mathis", "Bradford", "Boone", "Abbott", "Charles", "Allison", "Sweeney",
                    "Atkinson", "Horn", "Jefferson", "Rosales", "York", "Christian", "Phelps", "Farrell", "Castaneda",
                    "Nash", "Dickerson", "Bond", "Wyatt", "Foley", "Chase", "Gates", "Vincent", "Mathews", "Hodge",
                    "Garrison", "Trevino", "Villarreal", "Heath", "Dalton", "Valencia", "Callahan", "Hensley",
                    "Atkins", "Huffman", "Roy", "Boyer", "Shields", "Lin", "Hancock", "Grimes", "Glenn", "Cline",
                    "Delacruz", "Camacho", "Dillon", "Parrish", "Oneill", "Melton", "Booth", "Kane", "Berg", "Harrell",
                    "Pitts", "Savage", "Wiggins", "Brennan", "Salas", "Marks", "Russo", "Sawyer", "Baxter", "Golden",
                    "Hutchinson", "Liu", "Walter", "Mcdowell", "Wiley", "Rich", "Humphrey", "Johns", "Koch", "Suarez",
                    "Hobbs", "Beard", "Gilmore", "Ibarra", "Keith", "Macias", "Khan", "Andrade", "Ware", "Stephenson",
                    "Henson", "Wilkerson", "Dyer", "Mcclure", "Blackwell", "Mercado", "Tanner", "Eaton", "Clay",
                    "Barron", "Beasley", "Oneal", "Small", "Preston", "Wu", "Zamora", "Macdonald", "Vance", "Snow",
                    "Mcclain", "Stafford", "Orozco", "Barry", "English", "Shannon", "Kline", "Jacobson", "Woodard",
                    "Huang", "Kemp", "Mosley", "Prince", "Merritt", "Hurst", "Villanueva", "Roach", "Nolan", "Lam",
                    "Yoder", "Mccullough", "Lester", "Santana", "Valenzuela", "Winters", "Barrera", "Orr", "Leach",
                    "Berger", "Mckee", "Strong", "Conway", "Stein", "Whitehead", "Bullock", "Escobar", "Knox",
                    "Meadows", "Solomon", "Velez", "Odonnell", "Kerr", "Stout", "Blankenship", "Browning", "Kent",
                    "Lozano", "Bartlett", "Pruitt", "Buck", "Barr", "Gaines", "Durham", "Gentry", "Mcintyre", "Sloan",
                    "Rocha", "Melendez", "Herman", "Sexton", "Moon", "Hendricks", "Rangel", "Stark", "Lowery", "Hardin",
                    "Hull", "Sellers", "Ellison", "Calhoun", "Gillespie", "Mora", "Knapp", "Mccall", "Morse", "Dorsey",
                    "Weeks", "Nielsen", "Livingston", "Leblanc", "Mclean", "Bradshaw", "Glass", "Middleton", "Buckley",
                    "Schaefer", "Frost", "Howe", "House", "Mcintosh", "Ho", "Pennington", "Reilly", "Hebert",
                    "Mcfarland", "Hickman", "Noble", "Spears", "Conrad", "Arias", "Galvan", "Velazquez", "Huynh",
                    "Frederick", "Randolph", "Cantu", "Fitzpatrick", "Mahoney", "Peck", "Villa", "Michael",
                    "Donovan", "Mcconnell", "Walls", "Boyle", "Mayer", "Zuniga", "Giles", "Pineda", "Pace", "Hurley",
                    "Mays", "Mcmillan", "Crosby", "Ayers", "Case", "Bentley", "Shepard", "Everett", "Pugh", "David",
                    "Mcmahon", "Dunlap", "Bender", "Hahn", "Harding", "Acevedo", "Raymond", "Blackburn", "Duffy",
                    "Landry", "Dougherty", "Bautista", "Shah", "Potts", "Arroyo", "Valentine", "Meza", "Gould",
                    "Vaughan", "Fry", "Rush", "Avery", "Herring", "Dodson", "Clements", "Sampson", "Tapia", "Bean",
                    "Lynn", "Crane", "Farley", "Cisneros", "Benton", "Ashley", "Mckay", "Finley", "Best", "Blevins",
                    "Friedman", "Moses", "Sosa", "Blanchard", "Huber", "Frye", "Krueger", "Bernard", "Rosario", "Rubio",
                    "Mullen", "Benjamin", "Haley", "Chung", "Moyer", "Choi", "Horne", "Yu", "Woodward", "Ali", "Nixon",
                    "Hayden", "Rivers", "Estes", "Mccarty", "Richmond", "Stuart", "Maynard", "Brandt", "Oconnell",
                    "Hanna", "Sanford", "Sheppard", "Church", "Burch", "Levy", "Rasmussen", "Coffey", "Ponce",
                    "Faulkner", "Donaldson", "Schmitt", "Novak", "Costa", "Montes", "Booker", "Cordova", "Waller",
                    "Arellano", "Maddox", "Mata", "Bonilla", "Stanton", "Compton", "Kaufman", "Dudley", "Mcpherson",
                    "Beltran", "Dickson", "Mccann", "Villegas", "Proctor", "Hester", "Cantrell", "Daugherty", "Cherry",
                    "Bray", "Davila", "Rowland", "Madden", "Levine", "Spence", "Good", "Irwin", "Werner", "Krause",
                    "Petty", "Whitney", "Baird", "Hooper", "Pollard", "Zavala", "Jarvis", "Holden", "Hendrix", "Haas",
                    "Mcgrath", "Bird", "Lucero", "Terrell", "Riggs", "Joyce", "Rollins", "Mercer", "Galloway", "Duke",
                    "Odom", "Andersen", "Downs", "Hatfield", "Benitez", "Archer", "Huerta", "Travis", "Mcneil",
                    "Hinton", "Zhang", "Hays", "Mayo", "Fritz", "Branch", "Mooney", "Ewing", "Ritter", "Esparza",
                    "Frey", "Braun", "Gay", "Riddle", "Haney", "Kaiser", "Holder", "Chaney", "Mcknight", "Gamble",
                    "Vang", "Cooley", "Carney", "Cowan", "Forbes", "Ferrell", "Davies", "Barajas", "Shea", "Osborn",
                    "Bright", "Cuevas", "Bolton", "Murillo", "Lutz", "Duarte", "Kidd", "Key", "Cooke"};

    private ArrayList<User> userGens;
    private ArrayList<Integer> locations;

    public UserGen() {
        userGens = new ArrayList<>();
        locations = new ArrayList<>();

        Connection conn = RelationshipController.getConnection();

        String locationQuery = "SELECT zip_code FROM location";
        ResultSet resultSet = SQLHelper.executeQuery(conn, locationQuery);
        try {
            while (resultSet.next()) {
                locations.add(resultSet.getInt("zip_code"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> generateUsers(int numUsers) {
        String username;
        String password;
        String firstName;
        String lastName;
        String bio;
        String email;
        Date dob;
        RelationshipController.Gender gender;
        RelationshipController.Sexuality sexuality;
        int location;
        int preferredAgeMin;
        int preferredAgeMax;
        RelationshipController.Sexuality preferredSexuality;

        Random rand = new Random();

        for (int i = 0; i < numUsers; i++) {
            if (rand.nextInt(2) == 1) {
                gender = RelationshipController.Gender.Male;
                firstName = MALEFIRSTNAMES[rand.nextInt(MALEFIRSTNAMES.length)];
            } else {
                gender = RelationshipController.Gender.Female;
                firstName = FEMALEFIRSTNAMES[rand.nextInt(FEMALEFIRSTNAMES.length)];
            }

            lastName = LASTNAMES[rand.nextInt(LASTNAMES.length)];

            username = firstName.toLowerCase() + ALPHABET[rand.nextInt(ALPHABET.length)] +
                    ALPHABET[rand.nextInt(ALPHABET.length)] + ALPHABET[rand.nextInt(ALPHABET.length)] +
                    ALPHABET[rand.nextInt(ALPHABET.length)] + ALPHABET[rand.nextInt(ALPHABET.length)] +
                    rand.nextInt(10000);

            password = "";
            for (int x = 0; x < 10; x++) {
                password += rand.nextInt(10);
            }

            // 60% of users will live in Rochester
            if (RandomNumberHelper.randBetween(0,100) > 60) {
                location = locations.get(rand.nextInt(locations.size()));
            }
            else{
                location = 14620;
            }

            bio = "I am from location " + location + ". I really want to meet someone special";

            email = username + "@relationalrelationships.com";

            if (rand.nextInt(2) == 1) {
                sexuality = RelationshipController.Sexuality.Heterosexual;
            } else {
                sexuality = RelationshipController.Sexuality.Homosexual;
            }

            if (rand.nextInt(2) == 1) {
                preferredSexuality = RelationshipController.Sexuality.Heterosexual;
            } else {
                preferredSexuality = RelationshipController.Sexuality.Homosexual;
            }

            // 18 - 101
            preferredAgeMin = RandomNumberHelper.randBetween(18,101);

            // preferredAgeMin - 101
            preferredAgeMax = RandomNumberHelper.randBetween(preferredAgeMin,101);

            // Generate DOB between 1950 and 2008
            GregorianCalendar gc = new GregorianCalendar();
            int year = RandomNumberHelper.randBetween(1950, 1998);
            gc.set(Calendar.YEAR, year);
            int dayOfYear = RandomNumberHelper.randBetween(1, gc.getActualMaximum(Calendar.DAY_OF_YEAR));
            gc.set(Calendar.DAY_OF_YEAR, dayOfYear);
            dob = new Date(gc.getTime().getTime());

            userGens.add(new User(username, password, firstName + " " + lastName, bio, email, dob, gender, sexuality, location,
                    preferredAgeMin, preferredAgeMax, preferredSexuality, false));
        }

        insertIntoUserDB();
        return userGens;
    }

    private void insertIntoUserDB() {
        Connection conn = RelationshipController.getConnection();

        StringBuilder sb = new StringBuilder();

        sb.append("INSERT INTO user (username, password, name, bio, email, dob, gender, sexuality," +
                " location, preferred_age_min, preferred_age_max, preferred_sexuality) VALUES");

        for (int i = 0; i < userGens.size(); i++) {
            User user = userGens.get(i);
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
                    user.getUserPreferences().getPreferredAgeMin(),
                    user.getUserPreferences().getPreferredAgeMax(),
                    user.getUserPreferences().getPreferredSexuality()));
            if (i != userGens.size() - 1) {
                sb.append(",");
            } else {
                sb.append(";");
            }
        }
        if (userGens.size() > 0) {
            SQLHelper.execute(conn, sb.toString());
        }
    }
}
