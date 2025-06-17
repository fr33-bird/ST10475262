/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessagesTest {
    private Messages message;
    private static final String TEST_RECIPIENT = "+1234567890";
    private static final String TEST_MESSAGE = "This is a test message";
    private static final String JSON_FILE = "savedMessages.json";

    @Before
    public void setUp() {
        resetMessageCounter();
        message = new Messages(TEST_RECIPIENT, TEST_MESSAGE);
    }

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
        Files.deleteIfExists(Paths.get(JSON_FILE));
        
        message.saveMessageToJSON();
        
        assertTrue(Files.exists(Paths.get(JSON_FILE)));
        String content = new String(Files.readAllBytes(Paths.get(JSON_FILE)));
        assertTrue(content.contains(TEST_RECIPIENT));
        assertTrue(content.contains(TEST_MESSAGE));
        
        Files.deleteIfExists(Paths.get(JSON_FILE));
    }

    @Test
    public void testMessageNumberIncrementation() {
        assertEquals(1, message.getMessageNum());
        
        Messages message2 = new Messages(TEST_RECIPIENT, "Second message");
        assertEquals(2, message2.getMessageNum());
        assertEquals(2, Messages.returnTotalMessages());
    }

    @Test
    public void testReadAllReceivers() throws IOException {
        // Setup test file
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            writer.write("{\"Reciever\":\"+1234567890\",\"TextMessage\":\"Test 1\"}\n");
            writer.write("{\"Reciever\":\"+0987654321\",\"TextMessage\":\"Test 2\"}\n");
        }
        
        String result = message.readAllReceivers();
        assertTrue(result.contains("All Receivers:"));
        assertTrue(result.contains("👉 +1234567890"));
        assertTrue(result.contains("👉 +0987654321"));
        
        Files.deleteIfExists(Paths.get(JSON_FILE));
    }

    @Test
    public void testFindLongestMessage() throws IOException {
        // Setup test file
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            writer.write("{\"TextMessage\":\"Short\"}\n");
            writer.write("{\"TextMessage\":\"This is the longest message\"}\n");
            writer.write("{\"TextMessage\":\"Medium\"}\n");
        }
        
        String result = message.findLongestMessage();
        assertTrue(result.contains("Longest message (25 characters)"));
        assertTrue(result.contains("This is the longest message"));
        
        Files.deleteIfExists(Paths.get(JSON_FILE));
    }

    @Test
    public void testSearchByMessageId() throws IOException {
        // Setup test file
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            writer.write("{\"MessageID\":\"ID1\",\"Reciever\":\"+123\",\"TextMessage\":\"Msg1\"}\n");
            writer.write("{\"MessageID\":\"ID2\",\"Reciever\":\"+456\",\"TextMessage\":\"Msg2\"}\n");
        }
        
        String result = Messages.searchByMessageId("ID1");
        assertTrue(result.contains("Found message to +123"));
        assertTrue(result.contains("\"Msg1\""));
        
        assertEquals("Message ID not found.", Messages.searchByMessageId("ID99"));
        
        Files.deleteIfExists(Paths.get(JSON_FILE));
    }

    @Test
    public void testSearchByRecipient() throws IOException {
        // Setup test file
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            writer.write("{\"Reciever\":\"+123\",\"TextMessage\":\"Msg1\"}\n");
            writer.write("{\"Reciever\":\"+456\",\"TextMessage\":\"Msg2\"}\n");
            writer.write("{\"Reciever\":\"+123\",\"TextMessage\":\"Msg3\"}\n");
        }
        
        String result = Messages.searchByRecipient("+123");
        assertTrue(result.contains("To: +123"));
        assertTrue(result.contains("Message: \"Msg1\""));
        assertTrue(result.contains("Message: \"Msg3\""));
        assertFalse(result.contains("Message: \"Msg2\""));
        
        assertEquals("No messages to +999", Messages.searchByRecipient("+999"));
        
        Files.deleteIfExists(Paths.get(JSON_FILE));
    }

    @Test
    public void testDeleteByHash() throws IOException {
        // Setup test file
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            writer.write("{\"MessageHash\":\"HASH1\",\"TextMessage\":\"Msg1\"}\n");
            writer.write("{\"MessageHash\":\"HASH2\",\"TextMessage\":\"Msg2\"}\n");
        }
        
        String result = Messages.deleteByHash("HASH1");
        assertEquals("Message deleted!", result);
        
        // Verify deletion
        String content = new String(Files.readAllBytes(Paths.get(JSON_FILE)));
        assertFalse(content.contains("HASH1"));
        assertTrue(content.contains("HASH2"));
        
        assertEquals("Message hash not found.", Messages.deleteByHash("HASH99"));
        
        Files.deleteIfExists(Paths.get(JSON_FILE));
    }

    @Test
    public void testGenerateFullReport() throws IOException {
        // Setup test file
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            writer.write("{\"MessageID\":\"ID1\",\"Reciever\":\"+123\",\"TextMessage\":\"Msg1\",\"MessageHash\":\"HASH1\"}\n");
            writer.write("{\"MessageID\":\"ID2\",\"Reciever\":\"+456\",\"TextMessage\":\"Msg2\",\"MessageHash\":\"HASH2\"}\n");
        }
        
        String result = Messages.generateFullReport();
        assertTrue(result.contains("=== ALL MESSAGES ==="));
        assertTrue(result.contains("Message ID: ID1"));
        assertTrue(result.contains("To: +123"));
        assertTrue(result.contains("Message: \"Msg1\""));
        assertTrue(result.contains("Hash: HASH1"));
        assertTrue(result.contains("Message ID: ID2"));
        assertTrue(result.contains("To: +456"));
        assertTrue(result.contains("Message: \"Msg2\""));
        assertTrue(result.contains("Hash: HASH2"));
        
        Files.deleteIfExists(Paths.get(JSON_FILE));
        assertEquals("No messages found.", Messages.generateFullReport());
    }
}