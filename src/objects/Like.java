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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Like like = (Like) o;

        return sender.equals(like.sender) && timestamp.equals(like.timestamp);
    }

    @Override
    public int hashCode() {
        int result = sender.hashCode();
        result = 31 * result + timestamp.hashCode();
        return result;
    }
}
