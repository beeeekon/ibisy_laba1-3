package org.example;

//import org.junit.Test;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class RSACipherTest {
    @Test
    public void encryptionEngTest() throws IOException, InterruptedException {


        Cipher obj= new Cipher(new RSACipher(3,19), "programming");
        String result=obj.encryption();
        System.out.println(result);
        //e=35, d=35, n=57
        assertEquals(result,"31,$1\"..*-$");

    }

    @Test
    public void decryptionEngTest() throws IOException, InterruptedException {


        Cipher obj= new Cipher(new RSACipher(35,57), "31,$1\"..*-$");
        String result=obj.decryption();
        System.out.println(result);
        //e=35, d=35, n=57
        assertEquals(result,"programming");
    }
}