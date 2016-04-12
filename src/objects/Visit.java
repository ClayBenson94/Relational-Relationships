package objects;

/**
 * This class is intended to store a user who visited another user and when they visited
 **/
public class Visit {

    // The user that visited
    private String username;
    private Long timestamp;

    public Visit(String username, Long timestamp) {
        this.username = username;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}


