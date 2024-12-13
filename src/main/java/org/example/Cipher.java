package org.example;

import java.io.IOException;

public class Cipher {
    private TypeCipher type;
    private String word;
    public Cipher(){
        type=null;
        word="";
    }
    public Cipher(TypeCipher nameClass, String stroka){
        type=nameClass;
        word=stroka;
    }
    public String encryption() throws IOException, InterruptedException{
        String result="";
        result=type.encryption(word);
        return result;
    }
    public String decryption() throws IOException, InterruptedException{
        String result="";
        result=type.decryption(word);
        return result;
    }

}
