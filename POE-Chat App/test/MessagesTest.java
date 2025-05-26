/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MessagesTest {
    private Messages message;
    private static final String TEST_RECIPIENT = "+1234567890";
    private static final String TEST_MESSAGE = "This is a test message";
    private static final String JSON_FILE = "savedMessages.json";

    @Before
    public void setUp() {
        // Reset the static counter before each test
        resetMessageCounter();
        message = new Messages(TEST_RECIPIENT, TEST_MESSAGE);
    }

    // Helper method to reset the static counter using reflection
    private void resetMessageCounter() {
        try {
            java.lang.reflect.Field field = Messages.class.getDeclaredField("numMessagesSent");
            field.setAccessible(true);
            field.set(null, 0);
        } catch (Exception e) {
            fail("Failed to reset message counter: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorInitialization() {
        assertEquals(TEST_RECIPIENT, message.getRecipient());
        assertEquals(TEST_MESSAGE, message.getTextMessage());
        assertEquals(1, message.getMessageNum());
    }

    @Test
    public void testCheckMessageID() {
        assertTrue(message.checkMessageID());
    }

    @Test
    public void testCheckRecipientNumber() {
        assertTrue(message.checkRecipientNumber());
        
        Messages invalidMessage1 = new Messages("1234567890", "Invalid - no plus");
        assertFalse(invalidMessage1.checkRecipientNumber());
        
        Messages invalidMessage2 = new Messages("+abc", "Invalid - letters");
        assertFalse(invalidMessage2.checkRecipientNumber());
    }

    @Test
    public void testCreateMessageHash() {
        String hash = message.createMessageHash();
        assertNotNull(hash);
        assertTrue(hash.startsWith("Th:1:THISmessage"));
        
        Messages singleWordMessage = new Messages(TEST_RECIPIENT, "Single");
        String singleWordHash = singleWordMessage.createMessageHash();
        assertTrue(singleWordHash.contains("SINGLESINGLE"));
    }

    @Test
    public void testSentMessages() {
        assertEquals("Message successfully sent.", message.sentMessages("send"));
        assertEquals("Press 0 to delete the message", message.sentMessages("discard"));
        assertEquals("Message successfully stored.", message.sentMessages("store"));
        assertEquals("Invalid action.", message.sentMessages("invalid"));
    }

    @Test
    public void testPrintMessages() {
        String printed = message.printMessages();
        assertTrue(printed.contains("MessageID: " + message.getUniqueMessageId()));
        assertTrue(printed.contains("MessageHash: " + message.createMessageHash()));
        assertTrue(printed.contains("Recipient: " + TEST_RECIPIENT));
        assertTrue(printed.contains("Text Message: " + TEST_MESSAGE));
    }

    @Test
    public void testReturnTotalMessages() {
        assertEquals(1, Messages.returnTotalMessages());
        new Messages(TEST_RECIPIENT, "Another message");
        assertEquals(2, Messages.returnTotalMessages());
    }

    @Test
    public void testSaveMessageToJSON() throws Exception {
        // Clean up any existing test file
        Files.deleteIfExists(Paths.get(JSON_FILE));
        
        message.saveMessageToJSON();
        
        assertTrue(Files.exists(Paths.get(JSON_FILE)));
        String content = new String(Files.readAllBytes(Paths.get(JSON_FILE)));
        assertTrue(content.contains(TEST_RECIPIENT));
        assertTrue(content.contains(TEST_MESSAGE));
        
        // Clean up
        Files.deleteIfExists(Paths.get(JSON_FILE));
    }

    @Test
    public void testMessageNumberIncrementation() {
        assertEquals(1, message.getMessageNum());
        
        Messages message2 = new Messages(TEST_RECIPIENT, "Second message");
        assertEquals(2, message2.getMessageNum());
        assertEquals(2, Messages.returnTotalMessages());
    }
}