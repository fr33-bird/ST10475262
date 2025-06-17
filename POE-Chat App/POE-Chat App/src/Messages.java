import java.util.*;
import javax.swing.*;
import java.io.*;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Messages {
    private String UniqueMessageId;
    private static int numMessagesSent = 0;
    private int messageNum;
    private String recipient;
    private String textMessage;
    private String messageHash;

    public Messages(String recipient, String textMessage) {
        this.UniqueMessageId = UniqueMessageId;
        this.recipient = recipient;
        this.textMessage = textMessage;
        this.messageNum = ++numMessagesSent;
        this.messageHash = createMessageHash();
    }

    public String getUniqueMessageId() {
        return UniqueMessageId=generateRandomMessageID();
    }

    public static int getNumMessagesSent() {
        return numMessagesSent;
    }

    public int getMessageNum() {
        return messageNum;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public String getMessageHash() {
        return messageHash;
    }
    
    
    
    
    public String generateRandomMessageID(){
        long randNum = (long) (Math.random() * 1_000_000_000L);
        return String.format("%010d", randNum);
    }
    
    public boolean checkMessageID(){
        UniqueMessageId = generateRandomMessageID();
        return UniqueMessageId.length() == 10;
    }
    
    public boolean checkRecipientNumber(){
        return recipient.matches("^\\+\\d{1,3}\\d{1,10}$");
    }
    
    public String createMessageHash(){
        String[] text = textMessage.trim().split("\\s+");
        String firstText = text[0].toUpperCase();
        String lastText = text[text.length - 1].toUpperCase();
        messageHash = textMessage.substring(0,2)+":"+messageNum +":"+firstText+lastText;
        return messageHash;
    }
    
    public String sentMessages(String task){
        switch (task.toLowerCase()){
            case "send":
                return "Message successfully sent.";
            case "discard":
                return "Press 0 to delete the message";
            case "store":
                saveMessageToJSON();
                return "Message successfully stored.";
            default:
                return "Invalid action.";
        }
    }
    
    public String printMessages(){
        return "MessageID: "+generateRandomMessageID() +"\n"+
               "MessageHash: "+messageHash + "\n"+
               "Recipient: "+recipient +"\n"+
               "Text Message: "+textMessage; 
    }
    
    public static int returnTotalMessages(){
        return numMessagesSent;
    }
    
    public void saveMessageToJSON(){
        try{
            JSONObject obj = new JSONObject();
            obj.put("MessageID", generateRandomMessageID());
            obj.put("MessageHash", messageHash);
            obj.put("Reciever", recipient);
            obj.put("TextMessage", textMessage);
            
            FileWriter write = new FileWriter("savedMessages.json",true);
            write.write(obj.toString() + System.lineSeparator());
            write.close();
        } catch (IOException e){
            System.out.println("There was an error while saving the message to JSON: "+e.getMessage());
        }
    }
    
    public static String readAllReceivers() {
    String filePath = "savedMessages.json";
    StringBuilder receivers = new StringBuilder("All Receivers:\n");
    
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            try {
                JSONObject jsonObject = new JSONObject(line);
                if (jsonObject.has("Reciever")) {
                    receivers.append("👉 ").append(jsonObject.getString("Reciever")).append("\n");
                }
            } catch (Exception e) {
                
            }
        }
    } catch (IOException e) {
        return "Error reading messages: " + e.getMessage();
    }
    
    return receivers.toString();
}
    
    public static String findLongestMessage() {
    String filePath = "savedMessages.json";
    String longestMessage = "";
    int maxLength = 0;
    
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            try {
                JSONObject json = new JSONObject(line);
                if (json.has("TextMessage")) {
                    String currentMessage = json.getString("TextMessage");
                    if (currentMessage.length() > maxLength) {
                        maxLength = currentMessage.length();
                        longestMessage = currentMessage;
                    }
                }
            } catch (Exception e) {
                
            }
        }
    } catch (IOException e) {
        return "Error reading messages: " + e.getMessage();
    }
    
    if (longestMessage.isEmpty()) {
        return "No messages found in the log.";
    } else {
        return String.format("Longest message (%d characters):\n\"%s\"", 
                            maxLength, longestMessage);
    }
}
    
    public static String searchByMessageId(String targetId) {
    String filePath = "savedMessages.json";
    
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            JSONObject json = new JSONObject(line);
            if (json.getString("MessageID").equals(targetId)) {
                return "Found message to " + json.getString("Reciever") + 
                       ":\n\"" + json.getString("TextMessage") + "\"";
            }
        }
    } catch (Exception e) {
        return "Error: " + e.getMessage();
    }
    return "Message ID not found.";
}
    
    public static String searchByRecipient(String targetRecipient) {
    String filePath = "savedMessages.json";
    StringBuilder results = new StringBuilder();
    
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            JSONObject json = new JSONObject(line);
            if (json.getString("Reciever").contains(targetRecipient)) {
                results.append("To: ").append(json.getString("Reciever"))
                      .append("\nMessage: \"").append(json.getString("TextMessage"))
                      .append("\"\n\n");
            }
        }
    } catch (Exception e) {
        return "Error: " + e.getMessage();
    }
    
    return results.length() == 0 ? 
           "No messages to " + targetRecipient : 
           results.toString();
}
    
    public static String deleteByHash(String targetHash) {
    String filePath = "savedMessages.json";
    File tempFile = new File("temp.json");
    boolean found = false;
    
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
         FileWriter writer = new FileWriter(tempFile)) {
        String line;
        while ((line = reader.readLine()) != null) {
            JSONObject json = new JSONObject(line);
            if (!json.getString("MessageHash").equals(targetHash)) {
                writer.write(line + System.lineSeparator());
            } else {
                found = true;
            }
        }
    } catch (Exception e) {
        return "Error: " + e.getMessage();
    }
    
    if (found) {
        
        new File(filePath).delete();
        tempFile.renameTo(new File(filePath));
        return "Message deleted!";
    }
    return "Message hash not found.";
}
    
    public static String generateFullReport() {
    String filePath = "savedMessages.json";
    StringBuilder allMessages = new StringBuilder("=== ALL MESSAGES ===\n\n");
    
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            try {
                JSONObject json = new JSONObject(line);
                allMessages.append("Message ID: ").append(json.getString("MessageID")).append("\n")
                           .append("To: ").append(json.getString("Reciever")).append("\n")
                           .append("Message: \"").append(json.getString("TextMessage")).append("\"\n")
                           .append("Hash: ").append(json.getString("MessageHash")).append("\n")
                           .append("----------------\n");
            } catch (Exception e) {
                allMessages.append("(Invalid message format)\n----------------\n");
            }
        }
    } catch (IOException e) {
        return "Error reading file: " + e.getMessage();
    }
    
    return allMessages.length() > 20 ? allMessages.toString() : "No messages found.";
}
    
}

