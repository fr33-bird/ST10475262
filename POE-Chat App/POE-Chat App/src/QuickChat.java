import javax.swing.*;
public class QuickChat{
    public  void start(){
        
        
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat");
        
        int numberOfMessages = Integer.parseInt(JOptionPane.showInputDialog("How many messages would you like to send?"));
        int numberOfMessagesSent = 0 ;
        
        while (true){
            String menu = JOptionPane.showInputDialog("Select an option: \n "
                                                  + "1)Send messages \n "
                                                  + "2)Show recently sent messages \n "
                                                  + "3)More Options\n "
                                                  + "4)Quit");
            if (menu == null) break;
            int menuOption = Integer.parseInt(menu);
            switch (menuOption){
                case 1:
                    if(numberOfMessagesSent > numberOfMessages){
                        JOptionPane.showMessageDialog(null, "Cannot send more than "+numberOfMessages+" messages");
                        break;
                    }
                    
                    String recipientNumber = JOptionPane.showInputDialog("Enter recipients' phone number");
                    String message = JOptionPane.showInputDialog("Enter message(no more than 250 characters)");
                    
                    if(message.length()>250){
                         JOptionPane.showMessageDialog(null, "Message contains more than 250 characters. Please remove "+(message.length()-250)+" characters");
                        break;
                    }
                    
                    Messages msgs = new Messages(recipientNumber, message);
                    
                    if(!msgs.checkRecipientNumber()){
                        JOptionPane.showMessageDialog(null, "Phone number not formatted correctly. "
                                + "Please ensure it contains the international code (+27) and is no more than 10 characters.");
                        break;
                    }
                    
                    String[] choices = {"Send Messages","Discard Message","Store Messsage"};
                    int choice =  JOptionPane.showOptionDialog(null, "Select an option: ", "Send Message",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, choices, choices[0]);
                    
                    String outcome ="";
                    switch (choice){
                        case 0:
                            outcome = msgs.sentMessages("send");
                            numberOfMessagesSent++;
                            break;
                        case 1:
                            outcome = msgs.sentMessages("discard");
                            break;
                        case 2:
                            outcome = msgs.sentMessages("store");
                            break;
                        default:
                            outcome = "No oprion selected.";
                    }
                    
                        JOptionPane.showMessageDialog(null, msgs.printMessages() +"\n\nChoice: "+outcome);
                        break;
                        
                case 2:
                    JOptionPane.showMessageDialog(null, "Coming Soon.");
                    break;
                    
                case 3:
                    String option = JOptionPane.showInputDialog("a) Display all recipients\n" +
                                                                "b) Display longest message\n" +
                                                                "c) Search by message ID\n" +
                                                                "d) Search by recipient\n" +
                                                                "e) Delete by message hash\n" +
                                                                "f) Full message report\n" +
                                                                "x) Back to main menu");    
            
        
    
            if (option == null || option.equalsIgnoreCase("x")) 
                break;
    
                    switch (option.toLowerCase()) {
                        case "a":
                            JOptionPane.showMessageDialog(null, Messages.readAllReceivers());
                            break;

                        case "b":
                            JOptionPane.showMessageDialog(null, Messages.findLongestMessage());
                            break;

                        case "c":
                            String searchId = JOptionPane.showInputDialog("Enter message ID to search:");

                            if (searchId != null) {
                                JOptionPane.showMessageDialog(null, Messages.searchByMessageId(searchId));
                            }
                            break;

                        case "d":
                            String searchRecipient = JOptionPane.showInputDialog("Enter recipient phone number:");

                            if (searchRecipient != null) {
                                JOptionPane.showMessageDialog(null, Messages.searchByRecipient(searchRecipient));
                            }
                            break;

                        case "e":
                            String deleteHash = JOptionPane.showInputDialog("Enter message hash to delete:");

                            if (deleteHash != null) {
                                JOptionPane.showMessageDialog(null, Messages.deleteByHash(deleteHash));
                            }
                            break;

                        case "f":
                            JOptionPane.showMessageDialog(null, Messages.generateFullReport());
                            break;

                        default:
                            JOptionPane.showMessageDialog(null, "Invalid option!");
                    }
                    break;
    
                case 4:
                    
                    JOptionPane.showMessageDialog(null, "Total Messages Sent: "+Messages.returnTotalMessages());
                    System.exit(0);
                    break;
                    
                default:
                    JOptionPane.showMessageDialog(null, "Invalid Process. Please select an option between 1, 2 and 3.");
                    
            }
            
        }
    }
    
    
}