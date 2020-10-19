
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This program will read two files(shadow and common-passwords.txt) and print
 * user names and passwords based on the passwords present in the
 * common-passwords.txt . This program assumes that both files exist in the same
 * directory.
 * 
 * 1.shadow : It is shadow password file containing users list in
 * username:shash:……(several other fields) format 1. common-passwords.txt : This
 * is a list of common passwords
 * 
 * @Project - CS 645 - Project 1: Problem 1(Part 2)
 * @author 1. Ida Jebakirubai Stephen Joseph 2. Susmita Biswas
 * 
 * 
 */
public class Cracker {

	public Cracker() {

	}
	/**
	 * This method is used to return users list from shadow file
	 * 
	 * @param fileName
	 * @return userList- List of users in shadow file
	 * @throws IOException
	 */
	
	static List<User> getUsersFromShadowFile(String fileName) throws IOException {
		List<User> userList = new ArrayList<User>();
		Path path = Paths.get(fileName);
		/*
		 * Read all contents from shadow file
		 */
		List<String> shadowSimplePassword = Files.readAllLines(path);
		/*
		 * Loop through each line and populate user with username,salt and hash
		 */
		for (String lines : shadowSimplePassword) {
			/*
			 * Split lines based on ":"
			 * As mentioned, each line will be in format username:$1$salt$hash:...
			 */
			String[] parts = lines.split(":");
			/*
			 * Splitting the parts based on ":", 
			 * username = parts[0]
			 * saltedHash = parts[1] = $1$salt$hash 
			 */
			String userName = parts[0];
			/*
			 * Splitting the part again to get salt and hash based on "$",
			 * salt = saltHash[2]
			 * hash = saltHash[3]
			 */
			String[] saltHash = parts[1].split("\\$");
			String salt = saltHash[2];
			String hash = saltHash[3];
			userList.add(new User(userName, salt, hash));
		}
		return userList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String shadowFileName = "./shadow";
		String passwordFileName = "./common-passwords.txt";
		List<User> userList = new ArrayList<User>();
		String result = "";

		try {

			MD5Shadow shadow = new MD5Shadow();
			/*
			 * Read shadow file into memory and get users from the file
			 */
			userList = getUsersFromShadowFile(shadowFileName);
			/*
			 * Read dictionary file into memory
			 */
			Path path = Paths.get(passwordFileName);
			List<String> dictionary = Files.readAllLines(path);
			/*
			 * Check if both users and dictionary list have one data
			 */
			if(userList.size() > 0 && dictionary.size() > 0) {
				
				for(User user : userList) {
					for(String password : dictionary) {
						try {
							/*
							 * Call the crypt method to generate salted hash 
							 */
							result = shadow.crypt(password, user.salt);
							/*
							 * Check if generated hash matches the hash present in the file
							 * Then print the user and password
							 */
							if (result.compareTo(user.hash) == 0) { 
								System.out.println(user.userName+ ":" + password); 
							} 
						}catch(Exception ex) {
							System.out.println("Error decrypting the password :"+password); 
						}
						
					}
				}
				
			}else {
				System.out.println("Please check your shadow file or password file"); 
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.exit(0);
	}
}

class User {
	public String userName;
	public String salt;
	public String hash;

	User(String userName, String salt, String hash) {
		this.userName = userName;
		this.salt = salt;
		this.hash = hash;
	}
	
}
