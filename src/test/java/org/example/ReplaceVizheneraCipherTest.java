package org.example;

//import org.junit.Test;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReplaceVizheneraCipherTest {
    @Test
    public void encryptionEngTest()throws IOException, InterruptedException{
        String word= "programming";
        String key="MAGIC";

        Cipher obj= new Cipher(new ReplaceVizheneraCipher(key), word);
        String result=obj.encryption();
        assertEquals(result,"bruotmmsqps");
    }
    @Test
    public void encryptionRusTest()throws IOException, InterruptedException{//из-за юникода фокус не прокатит
        String word= "программирование";
        String key="магия";
        int x='а';
        Cipher obj= new Cipher(new ReplaceVizheneraCipher(key), word);
        String result=obj.encryption();
        assertEquals(result,"ьрслпммпспывгцзс");
    }
    @Test
    public void decryptionTextTest()throws IOException, InterruptedException{
        String word= "bruotmmsqps";
        String key="MAGIC";

        Cipher obj= new Cipher(new ReplaceVizheneraCipher(key), word);
        String result=obj.decryption();
        assertEquals(result,"programming");
    }

}