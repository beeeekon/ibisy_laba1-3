package org.example;

import org.testng.annotations.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DoublePermutationCipherTest {
    /*@Test
    public void removeDuplicateLettersTest(){
        String s="edccbaac";
        DoublePermutationCipher obj=new DoublePermutationCipher("a","a");
        String result=obj.removeDuplicateLetters(s);
        assertEquals(result,"edcba");
    }

    @Test
    public void massivPermutationTestLetters(){
        String s="edcba";
        DoublePermutationCipher obj=new DoublePermutationCipher("a","a");
        String result=obj.massivPermutation(s);
        assertEquals(result,"54321");
    }

    @Test
    public void massivPermutationTestNumbers(){
        String s="783495";
        DoublePermutationCipher obj=new DoublePermutationCipher("a","a");
        String result=obj.massivPermutation(s);
        assertEquals(result,"451263");
    }*/
    @Test
    public void encryptionNumberTest()throws IOException, InterruptedException{
        String word= "SHIFROVANIE_PERECTANOVKOY";
        String keyFirst="13524";
        String keySecond="24153";

        Cipher obj= new Cipher(new DoublePermutationCipher(keyFirst,keySecond), word);
        String result=obj.encryption();
        assertEquals(result,"HFSRI_EERPVOOYKVNOIACAENT");
    }
    @Test
    public void encryptionTextTest()throws IOException, InterruptedException{
        String word= "SHIFROVANIE_PERECTANOVKA";//24=6*4
        String keyFirst="orange";
        String keySecond="bear";

        Cipher obj= new Cipher(new DoublePermutationCipher(keyFirst,keySecond), word);
        String result=obj.encryption();
        assertEquals(result,"TACNVKOAHISFERPEIEN_OVRA");
    }
    @Test
    public void decryptionNumberTest()throws IOException, InterruptedException{
        String word= "HFSRI_EERPVOOYKVNOIACAENT";
        String keyFirst="13524";
        String keySecond="24153";

        Cipher obj= new Cipher(new DoublePermutationCipher(keyFirst,keySecond), word);
        String result=obj.decryption();
        assertEquals(result,"SHIFROVANIE_PERECTANOVKOY");
    }
    @Test
    public void decryptionTextTest()throws IOException, InterruptedException{
        String word= "TACNVKOAHISFERPEIEN_OVRA";
        String keyFirst="orange";
        String keySecond="bear";

        Cipher obj= new Cipher(new DoublePermutationCipher(keyFirst,keySecond), word);
        String result=obj.decryption();
        assertEquals(result,"SHIFROVANIE_PERECTANOVKA");
    }



}