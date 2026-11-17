import MementoAndMediatorFiles.*;

import java.util.Arrays;
import java.util.Iterator;

public class MementoAndMediatorDriver {

    public static void main(String[] args) {

        ChatServer server = new ChatServer();

        User joe = new User("Joe");
        User ado = new User("Ado");
        User doomguy = new User("Doomguy");

        server.registerUser(joe);
        server.registerUser(ado);
        server.registerUser(doomguy);

        System.out.println("\nTask 1: Sending Messages");

        joe.sendMessage(ado, "hello twin");
        ado.sendMessage(joe, "ohayo twin");
        doomguy.sendMessage(
                Arrays.asList(joe, ado),
                "doomguy"
        );

        System.out.println("\nTask 2: Undo Last Message");

        joe.undoLastMessage();

        System.out.println("\nTask 3: Block Users");

        server.blockUser(ado, joe);

        joe.sendMessage(ado, "come back twin");
        joe.sendMessage(doomguy, "fortnite pirate shop access twin");

        System.out.println("\nTask 4: Chat History");

        joe.displayChatHistory();
        ado.displayChatHistory();
        doomguy.displayChatHistory();

        System.out.println("\nTask 5: Unregister a User");

        server.unregisterUser(doomguy);

        // iterator
        System.out.println("\nTask 6: Search Messages by User");

        Iterator<Message> joeIterator = joe.iterator(ado);

        while (joeIterator.hasNext()) {
            Message message = joeIterator.next();
            System.out.println(message);
        }

        Iterator<Message> doomguyIterator = doomguy.iterator(joe);

        while (doomguyIterator.hasNext()) {
            Message message = doomguyIterator.next();
            System.out.println(message);
        }

    }

}
