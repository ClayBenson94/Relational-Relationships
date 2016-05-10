package objects;


/**
 * A class to represent a Like
 */
public class Like {

    // The user that liked
    private String sender;
    private Long timestamp;

    /**
     * Construct a Like object
     * @param username - The username of the user to be created
     * @param timestamp - The timestamp marking when the user was created
     */
    public Like(String username, Long timestamp) {
        this.sender = username;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return sender;
    }

    public Long getTimestamp() {
        return timestamp;
    }

}
