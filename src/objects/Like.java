package objects;


public class Like {

    // The user that liked
    private String sender;
    private Long timestamp;

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
