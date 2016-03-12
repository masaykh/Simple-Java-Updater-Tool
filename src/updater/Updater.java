package updater;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.net.URLDecoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Updater{

/*
 private static File externalJar;
 private static boolean updaterUpdated = false; 
 private static String MainClass;
*/
    public static void downloadUrl(String urlString) throws MalformedURLException,  FileNotFoundException,  IOException {
    URL	website = new URL(urlString);
    String filename = website.getFile().substring(1);
    ReadableByteChannel rbc = Channels.newChannel(website.openStream());
    FileOutputStream fos = new FileOutputStream(filename);
    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	fos.close();
    }

    public static void extractFolder(String zipFile,String extractFolder) 
    {
        try
        {
            int BUFFER = 2048;
            File file = new File(zipFile);

            ZipFile zip = new ZipFile(file);
            String newPath = extractFolder;

            new File(newPath).mkdir();
            Enumeration<? extends ZipEntry> zipFileEntries = zip.entries();

            // Process each entry
            while (zipFileEntries.hasMoreElements())
            {
                // grab a zip file entry
                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                String currentEntry = entry.getName();

                File destFile = new File(newPath, currentEntry);
                //destFile = new File(newPath, destFile.getName());
                File destinationParent = destFile.getParentFile();

                // create the parent directory structure if needed
                destinationParent.mkdirs();

                if (!entry.isDirectory())
                {
                    BufferedInputStream is = new BufferedInputStream(zip
                    .getInputStream(entry));
                    int currentByte;
                    // establish buffer for writing file
                    byte data[] = new byte[BUFFER];

                    // write the current file to disk
                    FileOutputStream fos = new FileOutputStream(destFile);
                    BufferedOutputStream dest = new BufferedOutputStream(fos,
                    BUFFER);

                    // read and write until last byte is encountered
                    while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, currentByte);
                    }
                    dest.flush();
                    dest.close();
                    is.close();
                    zip.close();
                }
            }
        }
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
        }
        System.out.println("Zip file unpacked");
    }
    
    
    private static String byteArray2Hex(byte[] hash) { 
    	  Formatter formatter = new Formatter(); 
    	  for (byte b : hash) { 
    	   formatter.format("%02x", b); 
    	  }
    	  formatter.close();
    	  return formatter.toString(); 
    	 } 
    
    public static String calculateHash(String fileName) throws Exception {

		MessageDigest algorithm = MessageDigest.getInstance("MD5");
		FileInputStream fis = new FileInputStream(fileName);
		BufferedInputStream bis = new BufferedInputStream(fis);
		DigestInputStream dis = new DigestInputStream(bis, algorithm);

		while (dis.read() != -1);

		byte[] hash = algorithm.digest();
		dis.close();
		return byteArray2Hex(hash);
	}

    public static void main(String[] args) {
    	JSONParser parser = new JSONParser();
    	try {
    		
    		JSONObject config = (JSONObject) parser.parse(new FileReader("config.json"));
    		long version = (long) config.get("version");
    		String server = URLDecoder.decode( (String) config.get("server"),"ISO-8859-1");
    		downloadUrl(server);
    		JSONObject newconfig = (JSONObject) parser.parse(new FileReader("newconfig.json"));
    		if (version != (long)newconfig.get("version")){
				ArrayList<?> urls = (JSONArray) newconfig.get("urls");
        		urls.forEach((string) -> {
						try {
							downloadUrl((URLDecoder.decode((String) string,"ISO-8859-1")));
						} catch (Exception e) { e.printStackTrace(); }
        		});

        		String md5 = (String) newconfig.get("md5");
        		
        		if (!md5.equals("off")){
        			System.out.println("Checking MD5 Checksum");
        			String checksum =calculateHash("update.zip");
        			if (md5.equals(checksum)) extractFolder("update.zip",".");
        			System.out.println("MD5 checksum test passed - files integrity trusted");
        			System.out.println("MD5 checksum: "+ checksum);
        			System.out.println("Unpacking of update");
        		}

        		
    		}
    		else { System.out.println("No new version available"); }
    		
    		
    	/*	externalJar = new File ((String) newconfig.get("externalJar"));
            updaterUpdated = (boolean) newconfig.get("updaterUpdated");
    		MainClass = (String) newconfig.get("MainClass");
    	*/
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	} catch (MalformedURLException e) {
    		e.printStackTrace();
    	}catch (IOException e) {
    		e.printStackTrace();
    	} catch (ParseException e) {
    		e.printStackTrace();
    	} catch (Exception e) {
			e.printStackTrace();
		} 	

     }
}