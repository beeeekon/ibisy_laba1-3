package org.example;

import java.io.IOException;

public interface TypeCipher {
    public String encryption(String word) throws IOException, InterruptedException;
    public String decryption(String word) throws IOException, InterruptedException;

}
