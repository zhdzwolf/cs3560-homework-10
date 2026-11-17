package MementoAndMediatorFiles;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String name;
    private ChatServer server;
    private ChatHistory chatHistory;

    private MessageMemento lastMessageMemento;
    private Message lastSentMessage;

    public User(String name) {
        this.name = name;
        this.chatHistory = new ChatHistory();
    }

    public String getName() {
        return name;
    }

    public void setServer(ChatServer server) {
        this.server = server;
    }

    public void sendMessage(User recipient, String message) {
        List<User> recipients = new ArrayList<>();
        recipients.add(recipient);

        sendMessage(recipients, message);
    }

    public void sendMessage(List<User> recipients, String content) {
        if (server == null) {
            System.out.println(name + " is not connected to the server.");
            return;
        }

        Message message = new Message(this, recipients, content);

        lastMessageMemento = new MessageMemento(
                message.getMessage(),
                message.getTimestamp()
        );

        lastSentMessage = message;

        server.sendMessage(message);
    }

    public void receiveMessage(Message message) {
        chatHistory.addMessage(message);

        System.out.println(name + " received a message from "
                + message.getSender().getName() + ": "
                + message.getMessage());
    }

    public void undoLastMessage() {
        if (lastMessageMemento == null || lastSentMessage == null) {
            System.out.println(name + " has no last message to undo.");
            return;
        }

        System.out.println(name + " has undid the last message: "
                + lastMessageMemento.getMessage());

        server.undoMessage(lastSentMessage);

        lastMessageMemento = null;
        lastSentMessage = null;
    }

    public void removeReceivedMessage(Message message) {
        chatHistory.removeMessage(message);
    }

    public void displayChatHistory() {
        System.out.println("\nChat History for " + name + ":");
        chatHistory.displayHistory();
    }

}
