import javax.swing.*;
import java.util.regex.Pattern;

public class Register {
    private String username;
    private String password;
    private String cellPhone;
    
    
    // Mutators for all user data
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }
    

    public boolean checkUserName(String username) {
        return username != null && username.contains("_") && username.length() <= 5 ;//checks if username contains underscore and if it has no more than 5 characters
    }

    public boolean checkPasswordComplexity(String password) {
        
        if (password == null || password.length() < 8) {// checks if password empty or if it as less than 8 characters
            return false;
        }
        
        boolean hasCaps = false;
        boolean hasNum = false;
        boolean hasSpecialChar = false;
        
        for (char c : password.toCharArray()) {//checks if the password contains a capital letter
            if (Character.isUpperCase(c)) {
                hasCaps = true;
            } else if (Character.isDigit(c)) {//checks if the password contains a number
                hasNum = true;
            } else if (!Character.isLetterOrDigit(c)) {//checks if the password contains a special character
                hasSpecialChar = true;
            }
            
            if (hasCaps && hasNum && hasSpecialChar) {//if all coditions are met return true
                return true;
            }
        }
        
        return false;
    }

    public boolean checkCellPhoneNumber(String cellPhone) {
        // Regular expression to validate cell phone number with international code
        // Pattern: optional '+' followed by digits (country code), then up to 10 digits
        // This regex was created with the assistance of : High-Flyer.(2025).Deepseek(Mar 24 version)
        String regex = "^\\+\\d{1,3}\\d{1,10}$";
        return Pattern.matches(regex, cellPhone);
    }

}

