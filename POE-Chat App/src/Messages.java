import java.util.*;
import java.io.*;
import org.json.JSONObject;

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
        return UniqueMessageId;
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
        return UniqueMessageId.length() == 10;
    }
    
    public boolean checkRecipientNumber(){
        return recipient.matches("^\\+\\d{1,3}\\d{1,10}$");
    }
    
    public String createMessageHash(){
        String[] text = textMessage.trim().split("\\s+");
        String firstText = text[0].toUpperCase();
        String lastText = text[text.length - 1].toUpperCase();
        return textMessage.substring(0,2)+":"+messageNum +":"+firstText+lastText;
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
        return "MessageID: "+UniqueMessageId +"\n"+
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
            obj.put("MessageID", UniqueMessageId);
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
    
    
}

