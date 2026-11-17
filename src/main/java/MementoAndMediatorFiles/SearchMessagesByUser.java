package MementoAndMediatorFiles;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class SearchMessagesByUser implements Iterator<Message> {

    private List<Message> messages;
    private User userToSearchWith;
    private int currentIndex;
    private Message nextMessage;

    public SearchMessagesByUser(
            List<Message> messages,
            User userToSearchWith
    ) {
        this.messages = messages;
        this.userToSearchWith = userToSearchWith;
        this.currentIndex = 0;

        findNextMessage();
    }

    private void findNextMessage() {
        nextMessage = null;

        while (currentIndex < messages.size()) {
            Message message = messages.get(currentIndex);

            currentIndex++;

            if (message.getSender().equals(userToSearchWith)) {
                nextMessage = message;
                return;
            }

            if (message.getRecipients().contains(userToSearchWith)) {
                nextMessage = message;
                return;
            }
        }
    }

    @Override
    public boolean hasNext() {
        return nextMessage != null;
    }

    @Override
    public Message next() {
        if (!hasNext()) {
            throw new NoSuchElementException(
                    "No more messages involving this user."
            );
        }

        Message result = nextMessage;
        findNextMessage();
        return result;
    }

}
