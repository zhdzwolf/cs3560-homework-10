import MementoAndMediatorFiles.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.time.LocalDateTime;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

public class ChatAppTests {

    private ChatHistory getHistory(User user) {
        try {
            Field field = User.class.getDeclaredField("chatHistory");
            field.setAccessible(true);
            return (ChatHistory) field.get(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // MESSAGE TESTS

    @Test
    public void testGetSender() {
        User alice = new User("Alice");
        User bob = new User("Bob");

        Message message = new Message(
                alice, Arrays.asList(bob), "Hello!"
        );

        assertEquals(alice, message.getSender());
    }

    @Test
    public void testGetRecipients() {
        User alice = new User("Alice");
        User bob = new User("Bob");
        User charlie = new User("Charlie");

        Message message = new Message(
                alice, Arrays.asList(bob, charlie), "Hello!"
        );

        assertEquals(2, message.getRecipients().size());
        assertTrue(message.getRecipients().contains(bob));
        assertTrue(message.getRecipients().contains(charlie));
    }

    @Test
    public void testGetTimestamp() {
        User alice = new User("Alice");
        User bob = new User("Bob");

        LocalDateTime before = LocalDateTime.now();

        Message message = new Message(
                alice, Arrays.asList(bob), "Hello!"
        );

        LocalDateTime after = LocalDateTime.now();

        assertNotNull(message.getTimestamp());
        assertFalse(message.getTimestamp().isBefore(before));
        assertFalse(message.getTimestamp().isAfter(after));
    }

    @Test
    public void testGetContent() {
        User alice = new User("Alice");
        User bob = new User("Bob");

        Message message = new Message(
                alice, Arrays.asList(bob), "Hello Bob!"
        );

        assertEquals("Hello Bob!", message.getMessage());
    }

    @Test
    public void testToString() {
        User alice = new User("Alice");
        User bob = new User("Bob");

        Message message = new Message(
                alice, Arrays.asList(bob), "Hello Bob!"
        );

        assertTrue(message.toString().contains("Alice"));
        assertTrue(message.toString().contains("Hello Bob!"));
    }

    // MESSAGE MEMENTO TESTS

    @Test
    public void testMementoGetContent() {
        LocalDateTime time = LocalDateTime.now();

        MessageMemento memento = new MessageMemento(
                "Hello Bob!", time
        );

        assertEquals("Hello Bob!", memento.getMessage());
    }

    @Test
    public void testMementoGetTimestamp() {
        LocalDateTime time = LocalDateTime.now();

        MessageMemento memento = new MessageMemento(
                "Hello Bob!", time
        );

        assertEquals(time, memento.getTimestamp());
    }

    // CHAT HISTORY TESTS

    @Test
    public void testAddMessage() {
        ChatHistory history = new ChatHistory();

        User alice = new User("Alice");
        User bob = new User("Bob");

        Message message = new Message(
                alice, Arrays.asList(bob), "Hello!"
        );

        history.addMessage(message);

        assertEquals(message, history.getLastMessage());
    }

    @Test
    public void testGetLastMessage() {
        ChatHistory history = new ChatHistory();

        User alice = new User("Alice");
        User bob = new User("Bob");

        Message first = new Message(
                alice, Arrays.asList(bob), "First message"
        );

        Message second = new Message(
                alice, Arrays.asList(bob), "Second message"
        );

        history.addMessage(first);
        history.addMessage(second);

        assertEquals(second, history.getLastMessage());
    }

    @Test
    public void testGetLastMessageWhenEmpty() {
        ChatHistory history = new ChatHistory();

        assertNull(history.getLastMessage());
    }

    @Test
    public void testRemoveLastMessage() {
        ChatHistory history = new ChatHistory();

        User alice = new User("Alice");
        User bob = new User("Bob");

        Message first = new Message(
                alice, Arrays.asList(bob), "First message"
        );

        Message second = new Message(
                alice, Arrays.asList(bob), "Second message"
        );

        history.addMessage(first);
        history.addMessage(second);

        history.removeLastMessage();

        assertEquals(first, history.getLastMessage());
    }

    @Test
    public void testRemoveMessage() {
        ChatHistory history = new ChatHistory();

        User alice = new User("Alice");
        User bob = new User("Bob");

        Message message = new Message(
                alice, Arrays.asList(bob), "Hello!"
        );

        history.addMessage(message);
        history.removeMessage(message);

        assertNull(history.getLastMessage());
    }

    @Test
    public void testDisplayHistory() {
        ChatHistory history = new ChatHistory();

        User alice = new User("Alice");
        User bob = new User("Bob");

        Message message = new Message(
                alice, Arrays.asList(bob), "Hello!"
        );

        history.addMessage(message);

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        PrintStream original = System.out;

        System.setOut(new PrintStream(output));

        try {
            history.displayHistory();

            assertTrue(output.toString().contains("Hello!"));
        } finally {
            System.setOut(original);
        }
    }

    // USER TESTS

    @Test
    public void testGetName() {
        User alice = new User("Alice");

        assertEquals("Alice", alice.getName());
    }

    @Test
    public void testSetServer() {
        User alice = new User("Alice");
        User bob = new User("Bob");

        ChatServer server = new ChatServer();

        server.registerUser(alice);
        server.registerUser(bob);

        assertDoesNotThrow(() ->
                alice.sendMessage(bob, "Hello!")
        );
    }

    @Test
    public void testSendMessageToOneUser() {
        ChatServer server = new ChatServer();

        User alice = new User("Alice");
        User bob = new User("Bob");

        server.registerUser(alice);
        server.registerUser(bob);

        alice.sendMessage(bob, "Hello Bob!");

        assertEquals(
                "Hello Bob!",
                getHistory(bob).getLastMessage().getMessage()
        );
    }

    @Test
    public void testSendMessageToMultipleUsers() {
        ChatServer server = new ChatServer();

        User alice = new User("Alice");
        User bob = new User("Bob");
        User charlie = new User("Charlie");

        server.registerUser(alice);
        server.registerUser(bob);
        server.registerUser(charlie);

        alice.sendMessage(
                Arrays.asList(bob, charlie),
                "Hello everyone!"
        );

        assertEquals(
                "Hello everyone!",
                getHistory(bob).getLastMessage().getMessage()
        );

        assertEquals(
                "Hello everyone!",
                getHistory(charlie).getLastMessage().getMessage()
        );
    }

    @Test
    public void testReceiveMessage() {
        User alice = new User("Alice");
        User bob = new User("Bob");

        Message message = new Message(
                alice, Arrays.asList(bob), "Hello!"
        );

        bob.receiveMessage(message);

        assertEquals(
                message,
                getHistory(bob).getLastMessage()
        );
    }

    @Test
    public void testUndoLastMessage() {
        ChatServer server = new ChatServer();

        User alice = new User("Alice");
        User bob = new User("Bob");

        server.registerUser(alice);
        server.registerUser(bob);

        alice.sendMessage(bob, "Undo this message!");

        assertNotNull(getHistory(bob).getLastMessage());

        alice.undoLastMessage();

        assertNull(getHistory(bob).getLastMessage());
    }

    @Test
    public void testUndoWithoutPreviousMessage() {
        User alice = new User("Alice");

        assertDoesNotThrow(() ->
                alice.undoLastMessage()
        );
    }

    @Test
    public void testDisplayChatHistory() {
        ChatServer server = new ChatServer();

        User alice = new User("Alice");
        User bob = new User("Bob");

        server.registerUser(alice);
        server.registerUser(bob);

        alice.sendMessage(bob, "Testing history!");

        ByteArrayOutputStream output =
                new ByteArrayOutputStream();

        PrintStream original = System.out;

        System.setOut(new PrintStream(output));

        try {
            bob.displayChatHistory();

            assertTrue(
                    output.toString().contains("Testing history!")
            );
        } finally {
            System.setOut(original);
        }
    }

    // CHAT SERVER TESTS

    @Test
    public void testRegisterUser() {
        ChatServer server = new ChatServer();

        User alice = new User("Alice");
        User bob = new User("Bob");

        server.registerUser(alice);
        server.registerUser(bob);

        assertDoesNotThrow(() ->
                alice.sendMessage(bob, "Hello!")
        );
    }

    @Test
    public void testUnregisterUser() {
        ChatServer server = new ChatServer();

        User alice = new User("Alice");
        User bob = new User("Bob");

        server.registerUser(alice);
        server.registerUser(bob);

        server.unregisterUser(bob);

        alice.sendMessage(bob, "Hello!");

        assertNull(getHistory(bob).getLastMessage());
    }

    @Test
    public void testSendMessage() {
        ChatServer server = new ChatServer();

        User alice = new User("Alice");
        User bob = new User("Bob");

        server.registerUser(alice);
        server.registerUser(bob);

        Message message = new Message(
                alice, Arrays.asList(bob), "Hello Bob!"
        );

        server.sendMessage(message);

        assertEquals(
                message,
                getHistory(bob).getLastMessage()
        );
    }

    @Test
    public void testBlockUser() {
        ChatServer server = new ChatServer();

        User alice = new User("Alice");
        User bob = new User("Bob");

        server.registerUser(alice);
        server.registerUser(bob);

        server.blockUser(bob, alice);

        alice.sendMessage(bob, "You should not receive this!");

        assertNull(getHistory(bob).getLastMessage());
    }

    @Test
    public void testUndoMessage() {
        ChatServer server = new ChatServer();

        User alice = new User("Alice");
        User bob = new User("Bob");

        server.registerUser(alice);
        server.registerUser(bob);

        Message message = new Message(
                alice, Arrays.asList(bob), "Undo this!"
        );

        server.sendMessage(message);

        assertNotNull(getHistory(bob).getLastMessage());

        server.undoMessage(message);

        assertNull(getHistory(bob).getLastMessage());
    }

}
