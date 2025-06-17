import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        Register r = new Register();
        
        // allow user to enter username
        boolean validUsername = false;
        
            String inputUsername = JOptionPane.showInputDialog("Enter username (must contain _ and be no more than 5 characters): ");
            validUsername = r.checkUserName(inputUsername);
            while (!validUsername) {
              
              JOptionPane.showMessageDialog(null,"Username is not correctly formatted, please ensure that your username contains an "
                        + "underscore and is no more than five characters in length.");
                inputUsername = JOptionPane.showInputDialog("Enter username (must contain _ and be no more than 5 characters): ");
            } 
            if(validUsername=true) {
                r.setUsername(inputUsername);
                 JOptionPane.showMessageDialog(null,"Username successfully captured.");
            }
        
        
        // allow user to enter password
        boolean validPassword = false;
        
            String inputPassword = JOptionPane.showInputDialog("Enter password (must contain 8 characters, a capital letter, number and special character): ");
            validPassword = r.checkPasswordComplexity(inputPassword);
            
            while(!validPassword) {
                JOptionPane.showMessageDialog(null,"Password is not correctly formatted; please ensure that the password contains at"
                        + " least eight characters, a capital letter, a number, and a special character.");
                inputPassword = JOptionPane.showInputDialog("Enter password (must contain 8 characters, a capital letter, number and special character): ");
            } 
            
            if(validPassword = true){
                r.setPassword(inputPassword);
                 JOptionPane.showMessageDialog(null,"Password successfully captured.");
            }
        
        
        // allow user to enter south african cell phone number
        boolean validCellPhone = false;
        
            String inputCellPhone = JOptionPane.showInputDialog("Enter cell phone number with international code +27:   ");
            validCellPhone = r.checkCellPhoneNumber(inputCellPhone);
            
            while (!validCellPhone) {
                JOptionPane.showMessageDialog(null,"Cell phone number incorrectly formatted or does not contain international code.");
                inputCellPhone = JOptionPane.showInputDialog("Enter cell phone number with international code +27:   ");
            }
            
            if(validCellPhone = true) {
                r.setCellPhone(inputCellPhone);
                JOptionPane.showMessageDialog(null,"Cellphone number successfully captured.");
            }
        
        boolean login= false;
        
            JOptionPane.showMessageDialog(null, "Log in to account");
            String loginName = JOptionPane.showInputDialog("Enter username:");
            String loginPass = JOptionPane.showInputDialog("Enter password:");
            
            if(loginName.equals(inputUsername) && loginPass.equals(inputPassword)){//verify if username and password matches
                
                try{
                    JOptionPane.showMessageDialog(null,"You have successfully logged in. Welcome "+loginName);
                    QuickChat chat = new QuickChat();
                    chat.start();
                }catch(Exception e){
                    JOptionPane.showMessageDialog(null,"Couldn't start app");
                }
            }
            else{
                JOptionPane.showMessageDialog(null,"Username or password incorrect.");
            }
            
    }
}

