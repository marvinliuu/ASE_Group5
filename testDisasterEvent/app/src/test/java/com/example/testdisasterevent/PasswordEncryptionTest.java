package com.example.testdisasterevent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.example.testdisasterevent.algorithms.PasswordEncryption;

import org.junit.Test;

public class PasswordEncryptionTest {

    @Test
    public void testEncryptPasswordValid() {
        String password = "MyPassword123";
        String expectedHash = "248193a983a515054f9ec036b550b14283beaeb81e65a44afc32ebd9c9dd871b";

        String actualHash = PasswordEncryption.encryptPassword(password);

        assertEquals(expectedHash, actualHash);
    }

    @Test
    public void testEncryptPasswordinValid() {
        String password = "MyPassword123";
        String expectedHash = "248193a983a515054f9ec036b550b14283beaeb8484bd9c9dd871b";

        String actualHash = PasswordEncryption.encryptPassword(password);

        assertNotEquals(expectedHash, actualHash);
    }
}
