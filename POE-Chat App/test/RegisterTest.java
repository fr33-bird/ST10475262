/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RegisterTest {

    private Register register;

    @Before
    public void setUp() {
        register = new Register();
        register.setUsername("user_");
        register.setPassword("Pass@123");
        register.setCellPhone("+1234567890");
    }

    @Test
    public void testCheckUserName() {
        assertTrue(register.checkUserName("abc_"));
        assertFalse(register.checkUserName("abcdef"));
        assertFalse(register.checkUserName("abc"));
    }

    @Test
    public void testCheckPasswordComplexity() {
        assertTrue(register.checkPasswordComplexity("Abc@1234"));
        assertFalse(register.checkPasswordComplexity("abc123")); // no caps or special char
    }

    @Test
    public void testCheckCellPhoneNumber() {
        assertTrue(register.checkCellPhoneNumber("+27831234567"));
        assertFalse(register.checkCellPhoneNumber("0831234567"));
    }

    @Test
    public void testLoginUser() {
        assertTrue(register.loginUser("user_", "Pass@123"));
        assertFalse(register.loginUser("wrong", "Pass@123"));
    }

    @Test
    public void testReturnLoginStatus() {
        assertEquals("Welcome user_, it is great to see you again.",
                register.returnLoginStatus(true));
        assertEquals("Username or password incorrect, please try again.",
                register.returnLoginStatus(false));
    }

    /**
     * Test of setUsername method, of class Register.
     */
    @Test
    public void testSetUsername() {
    }

    /**
     * Test of setPassword method, of class Register.
     */
    @Test
    public void testSetPassword() {
    }

    /**
     * Test of setCellPhone method, of class Register.
     */
    @Test
    public void testSetCellPhone() {
    }

    /**
     * Test of registerUser method, of class Register.
     */
    @Test
    public void testRegisterUser() {
    }
}
