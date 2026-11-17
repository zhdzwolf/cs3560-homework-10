package MementoAndMediatorFiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class ChatHistory implements IterableByUser {

    private List<Message> messages = new ArrayList<>();

    public void addMessage(Message message){
        messages.add(message);
    }

    public Message getLastMessage(){
        if (messages.isEmpty()){
            return null;
        }

        return messages.get(messages.size() - 1);
    }

    public void removeLastMessage() {
        if (!messages.isEmpty()) {
            messages.remove(messages.size() - 1);
        }
    }

    public void removeMessage(Message message) {
        messages.remove(message);
    }

    public void displayHistory() {
        if (messages.isEmpty()){
            System.out.println("No messages left in chat history.");
            return;
        }

        for (Message message : messages) {
            System.out.println(message);
        }
    }

    // iterator
    @Override
    public Iterator<Message> iterator(User userToSearchWith) {
        return new SearchMessagesByUser(
                messages,
                userToSearchWith
        );
    }

}
