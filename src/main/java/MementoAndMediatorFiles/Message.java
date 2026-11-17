package MementoAndMediatorFiles;

import java.time.LocalDateTime;
import java.util.List;

public class Message {

    private User sender;
    private List<User> recipients;
    private LocalDateTime timestamp;
    private String message;

    public Message(User sender, List<User> recipients, String message) {
        this.sender = sender;
        this.recipients = recipients;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public User getSender() {
        return sender;
    }

    public List<User> getRecipients() {
        return recipients;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + sender.getName()
                + ": " + message;
    }

}
