import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This program will read two files(shadow and common-passwords.txt) and 
 * print user names and passwords based on the passwords present in the common-passwords.txt .
 * This program assumes that both files exist in the same directory.
 * 
 * 1.shadow : It is shadow password file containing users list in username:shash:……(several other fields) format
 * 2. common-passwords.txt : This is a list of common passwords
 * 
 * @Project - CS 645 - Project 1: Problem 1(Part 2) 
 * @author 
 * 1. Ida Jebakirubai Stephen Joseph
 * 2. Susmita Biswas
 * 
 * 
 */
public class Cracker {
	
	public Cracker(){
		
	}
	/**
	 * This method is used to read a file
	 * @param file
	 * @return
	 * @throws IOException
	 */
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


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String shadowFileName = "./shadow";
		String passwordFileName = "./common-passwords.txt";
		String[] usersInput = new String[10];
		String[] saltsInput = new String[10];
		String[] hashesInput = new String[10];
		String[] shadowSimplePassword;
		String result="" ;

		try {
			
			MD5Shadow shadow = new MD5Shadow();
			
			shadowSimplePassword = readFile(shadowFileName);
			
			for (int i=0; i< shadowSimplePassword.length; i++) {
				
				String[] parts = shadowSimplePassword[i].split(":");
				usersInput[i] = parts[0];
				String[] saltHash = parts[1].split("\\$");
				saltsInput[i] = saltHash[2];
				hashesInput[i] = saltHash[3];
			}
			/*
			 * Read dictionary file into memory
			 */
			String[] dictionary = readFile(passwordFileName);
			for (int j = 0; j < hashesInput.length; j++) {
				for (int i=0; i < dictionary.length; i++) {
					try {
						
						//call the crypt method of MDShadow class with passing the parameter the dictionary password and salt
						result = shadow.crypt(dictionary[i], saltsInput[j]);
						
						}catch(Exception ex) {
							System.out.println("Error decrypting the password :"+dictionary[i]);
						}
					
					/*
					 * Print if we have a match
					 */
					if (result.compareTo(hashesInput[j]) == 0) {
						System.out.println(usersInput[j] + ":" + dictionary[i]);
					}
				}
			}
			
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		System.exit(0);
	}
}


