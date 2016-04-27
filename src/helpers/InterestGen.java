package helpers;


import objects.RelationshipController;
import objects.User;
import tables.UserInterestsTable;

import java.util.ArrayList;

public class InterestGen {

    public void InterestGen(){
        "3D printing", "Amateur radio", "Acting", "Baton twirling", "Board games", "Book restoration", "Cabaret", "Calligraphy", "Candle making", "Computer programming", "Home roasting coffee", "Cooking", "Coloring book", "Cosplaying", "Couponing", "Creative writing", "Crocheting", "Crossword puzzles", "Cryptography", "Dance", "Digital art", "Drama", "Drawing", "Do it yourself", "Electronics", "Embroidery", "Fashion", "Flower arranging", "Second-language acquisition", "Gambling", "Genealogy", "Glassblowing", "Gunsmithing", "Homebrewing", "Ice skating", "Jewelry making", "Jigsaw puzzles", "Juggling", "Knapping", "Knitting", "Kabaddi", "Knife making", "Kombucha", "Lace", "Lapidary club", "Leather crafting", "Lego", "Lockpicking", "Machining", "Macrame", "Metalworking", "Magic", "Model building", "Music", "Origami", "Painting", "Music", "Pets", "Performance art", "Pottery", "Puzzle", "Quilting", "Reading (process)", "Scrapbooking", "Sculpting", "Sewing", "Singing", "Sudoku", "Sketching ", "Drawing", "Soapmaking", "Stand-up comedy", "Table tennis", "Video gaming", "Movies", "Web surfing", "Whittling", "Wood carving", "Woodworking", "Worldbuilding", "Writing", "Yoga", "Yo-yoing"
    }

    public void generateUserInterests(ArrayList<User> users){
        for (User u: users){
            UserInterestsTable.addInterestToUser(RelationshipController.getConnection(),u.getUsername(), null);
        }
    }
}
