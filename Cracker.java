import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * @author Susmita
 *
 */
public class Cracker {
	/**
	 * This method is used to read a file
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public Cracker(){
		
	}
	
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
		

		String[] usersInput = new String[9];
		String[] saltsInput = new String[9];
		String[] hashesInput = new String[9];
		String[] shadowSimplePassword;
		Class[] paramString = new Class[2];
		paramString[0] = String.class;
		paramString[1] = String.class;
		try {
			
			Class cls = Class.forName("MD5Shadow");
			Object obj = cls.newInstance();
			
			shadowSimplePassword = readFile("shadow");
			
			for (int i=0; i< shadowSimplePassword.length -1; i++) {
				
				String[] parts = shadowSimplePassword[i].split(":");
				usersInput[i] = parts[0];
				saltsInput[i] = parts[1].substring(3, 11);
				hashesInput[i] = parts[1].substring(12);
			}
		
			/*
			 * Read dictionary file into memory
			 */
			String[] dictionary = readFile("common-passwords.txt");
			for (int j = 0; j < hashesInput.length-1; j++) {
				for (int i=0; i < dictionary.length; i++) {
					//call the crypt method of MDShadow class with passing the parameter the dictionary password and salt
					Method method = cls.getDeclaredMethod("crypt", paramString);
					String result = (String)method.invoke(obj, dictionary[i], saltsInput[j]);
					/*
					 * Print if we have a match
					 */
					if (result.compareTo(hashesInput[j]) == 0) {
						System.out.println(usersInput[j] + ":" + dictionary[i]);
					}
				}
			}
			
		} catch (IOException ioException) {
			// TODO Auto-generated catch block
			ioException.printStackTrace();
		}
		catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		System.exit(0);
		}
}


