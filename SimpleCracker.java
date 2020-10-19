
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
       
    /**
	 * This method is used to return users list from shadow file
	 * 
	 * @param fileName
	 * @return userList- List of users in shadow file
	 * @throws IOException
	 */
	
	static List<SimpleUser> getUsersFromShadowFile(String fileName) throws IOException {
		List<SimpleUser> userList = new ArrayList<SimpleUser>();
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
			 * As mentioned, each line will be in format username:salt:hash
			 */
			String[] parts = lines.split(":");
			/*
			 * Splitting the parts based on ":", 
			 * username = parts[0]
			 * salt = parts[1]
			 * hash = parts[2]
			 */
			String userName = parts[0];
			String salt = parts[1];
			String hash = parts[2];
			userList.add(new SimpleUser(userName, salt, hash));
		}
		return userList;
	}


    //converts a byte array into a String that contains the hexadecimal representation of the byte array

    public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }



    public static void main(String[] args) {


        String shadowFileName = "./shadow-simple";
		String passwordFileName = "./common-passwords.txt";
        List<SimpleUser> userList = new ArrayList<SimpleUser>();

        MessageDigest hashGenerator;

        try {
            hashGenerator = MessageDigest.getInstance("MD5");
            /*
			 * Read shadow file into memory and get users from the file
			 */
            userList = getUsersFromShadowFile(shadowFileName);
            /*
			 * Read dictionary file into memory
			 */
			Path path = Paths.get(passwordFileName);
			List<String> dictionaryPasswords = Files.readAllLines(path);
			/*
			 * Check if both users and dictionary list have one data
			 */
			if(userList.size() > 0 && dictionaryPasswords.size() > 0) {
				
				for(SimpleUser user : userList) {
					for(String password : dictionaryPasswords) {
							// Concatenate each dictionary item with each salt
		                    byte[] saltPasswordInBytes = ((String)(user.salt+  password)).getBytes();
	
		                    //Hash the salt password in bytes and converting it to hex using toHex function
		                    String saltedHashForPassword = toHex(hashGenerator.digest(saltPasswordInBytes));
	
	
							if (saltedHashForPassword.compareTo(user.hash) == 0) { 
								System.out.println(user.userName+ ":" + password); 
							} 
							
						}
					}
				
				}else {
					System.out.println("Please check your shadow file or password file"); 
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

class SimpleUser {
	public String userName;
	public String salt;
	public String hash;

	SimpleUser(String userName, String salt, String hash) {
		this.userName = userName;
		this.salt = salt;
		this.hash = hash;
	}
}


