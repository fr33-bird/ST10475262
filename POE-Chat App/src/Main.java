import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        Register r = new Register();
        
        // allow user to enter username
        boolean validUsername = false;
        
            String inputUsername = JOptionPane.showInputDialog("Enter username: ");
            validUsername = r.checkUserName(inputUsername);
            if (!validUsername) {
                JOptionPane.showMessageDialog(null,"Username is not correctly formatted, please ensure that your username contains an "
                        + "underscore and is no more than five characters in length.");
            } else {
                r.setUsername(inputUsername);
                 JOptionPane.showMessageDialog(null,"Username successfully captured.");
            }
        
        
        // allow user to enter password
        boolean validPassword = false;
        
            String inputPassword = JOptionPane.showInputDialog("Enter password: ");
            validPassword = r.checkPasswordComplexity(inputPassword);
            if (!validPassword) {
                JOptionPane.showMessageDialog(null,"Password is not correctly formatted; please ensure that the password contains at"
                        + " least eight characters, a capital letter, a number, and a special character.");
            } else {
                r.setPassword(inputPassword);
                 JOptionPane.showMessageDialog(null,"Password successfully captured.");
            }
        
        
        // allow user to enter south african cell phone number
        boolean validCellPhone = false;
        
            String inputCellPhone = JOptionPane.showInputDialog("Enter cell phone number with international code +27:   ");
            validCellPhone = r.checkCellPhoneNumber(inputCellPhone);
            if (!validCellPhone) {
                JOptionPane.showMessageDialog(null,"Cell phone number incorrectly formatted or does not contain international code.");
            } else {
                r.setCellPhone(inputCellPhone);
                JOptionPane.showMessageDialog(null,"Cellphone number successfully captured.");
            }
        
        boolean login= false;
        //verify if username and password matches
            String loginName = JOptionPane.showInputDialog("Enter username:");
            String loginPass = JOptionPane.showInputDialog("Enter password:");
            
            if(loginName.equals(inputUsername) && loginPass.equals(inputPassword)){
                JOptionPane.showMessageDialog(null,"You have successfully logged in");
                
                try{
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

