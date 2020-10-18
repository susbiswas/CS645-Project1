package com.com.simplecracker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class SimpleCracker {
    public SimpleCracker() {

    }
    /**
     * Subject CS645 Project1
     * @author Susmita Biswas
     * @author Ida Jebakirubai
     */
    //Read the input file

    static String[] readFile(String file) throws IOException {

        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<String>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        return lines.toArray(new String[lines.size()]);
    }


    //converts a byte array into a String that contains the hexadecimal representation of the byte array

    public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }



    public static void main(String[] args) {


        // Read shadow file provided , save the Usernames, salt, and hashes into memory

        String[] usersInput = new String[10];
        String[] saltsInput = new String[10];
        String[] hashesInput = new String[10];
        String[] shadowSimplePassword;

        MessageDigest hashGenerator;

        try {
            hashGenerator = MessageDigest.getInstance("MD5");

            shadowSimplePassword = readFile("shadow-simple");

            for (int i=0; i< shadowSimplePassword.length; i++) {
                usersInput[i] = shadowSimplePassword[i].substring(0, 5);
                saltsInput[i] = shadowSimplePassword[i].substring(6, 14);
                hashesInput[i] = shadowSimplePassword[i].substring(15, shadowSimplePassword[i].length());
            }


            // Read the dictionary file
            String[] dictionaryPasswords = readFile("common-passwords.txt");
            for (int i = 0; i < hashesInput.length; i++) {
                for (int j=0; j < dictionaryPasswords.length; j++) {

                    // Concatenate each dictionary item with each salt
                    byte[] saltPasswordInBytes = ((String)(saltsInput[i] +  dictionaryPasswords[j])).getBytes();

                    //Hash the salt password in bytes and converting it to hex using toHex function
                    String saltedHashForPassword = toHex(hashGenerator.digest(saltPasswordInBytes));


                    //Print the list of users whose passwords are compromised from the common password file
                    if (saltedHashForPassword.compareTo(hashesInput[i]) == 0) {
                        System.out.println(usersInput[i] + ":" + dictionaryPasswords[j]);
                    }
                }
            }

        } catch (IOException e) {
            // catch block
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e1) {
            // catch block
            e1.printStackTrace();
        }

        System.exit(0);
    }
}

