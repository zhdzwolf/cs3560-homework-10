package MementoAndMediatorFiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChatServer {

    private List<User> users = new ArrayList<>();

    private Map<User, Set<User>> blockedUsers = new HashMap<>();

    public void registerUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
            blockedUsers.put(user, new HashSet<>());
            user.setServer(this);

            System.out.println(user.getName() + " registered with the chat server.");
        }
    }

    public void unregisterUser(User user) {
        users.remove(user);
        blockedUsers.remove(user);

        for (Set<User> blocked :  blockedUsers.values()) {
            blocked.remove(user);
        }

        user.setServer(null);

        System.out.println(user.getName() + " unregistered from the chat server.");
    }

    public void sendMessage(Message message) {
        User sender = message.getSender();

        if (!users.contains(sender)) {
            System.out.println("Sender is not registered.");
            return;
        }

        System.out.println("\nServer forwarding message from "
                + sender.getName() + "...");

        for (User recipient : message.getRecipients()) {
            if (!users.contains(recipient)) {
                System.out.println(recipient.getName() + " is not registered.");
                continue;
            }

            if (blockedUsers.get(recipient).contains(sender)) {
                System.out.println(recipient.getName() + " has blocked "
                        + sender.getName() + ". Message not delivered.");
            } else {
                recipient.receiveMessage(message);
            }
        }
    }

    public void blockUser(User blocker, User blocked) {
        if (!users.contains(blocker) || !users.contains(blocked)
        || blocker.equals(blocked)) {
            System.out.println("Invalid block request.");
            return;
        }

        blockedUsers.get(blocker).add(blocked);

        System.out.println(blocker.getName() + " has blocked "
                + blocked.getName() + ".");
    }

    public void undoMessage(Message message) {
        for (User recipient : message.getRecipients()) {
            if (users.contains(recipient)) {
                recipient.removeReceivedMessage(message);
            }
        }

        System.out.println("Message successfully undone and removed from recipient histories.");
    }

}
