/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.After;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import javax.swing.JOptionPane;

public class QuickChatTest {
    
    private QuickChat quickChat;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    
    // Mock for JOptionPane
    private static class MockJOptionPane {
        static String lastMessage;
        static String input;
        
        static void showMessageDialog(Object parent, String message) {
            lastMessage = message;
        }
        
        static String showInputDialog(Object parent, String message) {
            return input;
        }
        
        static int showOptionDialog(Object parent, String message, String title, 
                                  int optionType, int messageType, 
                                  Object icon, Object[] options, Object initialValue) {
            return 0; // return first option by default
        }
    }
    
    @Before
    public void setUp() {
        quickChat = new QuickChat();
        System.setOut(new PrintStream(outContent));
        
        // Replace JOptionPane with our mock
        try {
            java.lang.reflect.Field field = JOptionPane.class.getDeclaredField("debug");
            field.setAccessible(true);
            field.set(null, true);
        } catch (Exception e) {
            // Ignore if we can't set debug mode
        }
    }
    
    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }
    
    @Test
    public void testStartWithZeroMessages() {
        MockJOptionPane.input = "3"; // Quit immediately
        quickChat.start();
        assertTrue(MockJOptionPane.lastMessage.contains("Welcome to QuickChat"));
    }
    
    @Test
    public void testSendMessageFlow() {
        // Set up mock responses
        MockJOptionPane.input = "1\n+27123456789\nTest message\n3";
        
        quickChat.start();
        
        // Verify the message was processed
        assertTrue(MockJOptionPane.lastMessage.contains("Message successfully sent"));
        assertEquals(1, Messages.returnTotalMessages());
    }
    
    @Test
    public void testInvalidRecipientNumber() {
        // Set up mock responses
        MockJOptionPane.input = "1\ninvalid\n3";
        
        quickChat.start();
        
        // Verify error message was shown
        assertTrue(MockJOptionPane.lastMessage.contains("Phone number not formatted correctly"));
    }
    
    @Test
    public void testMessageLengthValidation() {
        // Create a 251-character message
        String longMessage = new String(new char[251]).replace('\0', 'a');
        MockJOptionPane.input = "1\n+27123456789\n" + longMessage + "\n3";
        
        quickChat.start();
        
        // Verify error message was shown
        assertTrue(MockJOptionPane.lastMessage.contains("Message contains more than 250 characters"));
    }
    
    @Test
    public void testMessageLimitEnforcement() {
        // Set up mock to try sending 2 messages when limit is 1
        MockJOptionPane.input = "1\n+27123456789\nFirst message\n1\n+27123456789\nSecond message\n3";
        
        // Set message limit to 1
        ByteArrayInputStream in = new ByteArrayInputStream("1".getBytes());
        System.setIn(in);
        
        quickChat.start();
        
        // Verify limit enforcement
        assertTrue(MockJOptionPane.lastMessage.contains("Cannot send more than 1 messages"));
    }
}