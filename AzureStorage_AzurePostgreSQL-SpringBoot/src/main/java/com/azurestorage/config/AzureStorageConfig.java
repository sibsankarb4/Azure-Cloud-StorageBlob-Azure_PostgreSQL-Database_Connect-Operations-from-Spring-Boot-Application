package com.azurestorage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//@Configuration
//@ComponentScan
@Component
public class AzureStorageConfig {
	
	//@Bean
	public static HashMap<String, String> getDatabaseProperties(String storageConnectionString,Map<String, Object> configurationProperties)  {
		
		File sourceFile = null, downloadedFile = null;
		System.out.println("Azure Blob storage quick start sample");

		CloudStorageAccount storageAccount=null;
		CloudBlobClient blobClient = null;
		CloudBlobContainer container=null;
		FileReader fileReader =null;
		HashMap<String, String> property_maps = new HashMap<>();
		
	try {	
		storageAccount = CloudStorageAccount.parse(storageConnectionString);
		blobClient = storageAccount.createCloudBlobClient();
		container = blobClient.getContainerReference((configurationProperties.get("CONTAINER_NAME")).toString());
		System.out.println("Connecting to container: " + container.getName());
		CloudBlockBlob blob = container.getBlockBlobReference((configurationProperties.get("BLOB_NAME")).toString());
		System.out.println("Blob URI :: ++++  "+blob.getUri());
		sourceFile = File.createTempFile("sampleFile", ".json");
		downloadedFile = new File(sourceFile.getParentFile(), "whiskey-dev-db-details_temp.json");
		blob.downloadToFile(downloadedFile.getAbsolutePath());
		System.out.println("downloadedFile.getAbsolutePath() :: ++++  "+downloadedFile.getAbsolutePath());
		} 
		/*catch (StorageException ex)
		{
		System.out.println(String.format("Error returned from the service. Http code: %d and error code: %s", ex.getHttpStatusCode(), ex.getErrorCode()));
		}*/
		catch (Exception ex) 
		{
		System.out.println(ex.getMessage());
		}
	
	try {
		JSONParser parser = new JSONParser();
		 fileReader = new FileReader(downloadedFile.getAbsolutePath().toString());
		Object obj = parser.parse(fileReader);
		JSONObject jsonObject = (JSONObject) obj;
		property_maps.put("spring.datasource.driver-class-name", jsonObject.get("driverName").toString());
        property_maps.put("spring.datasource.url", jsonObject.get("jdbcURL").toString());
        property_maps.put("spring.datasource.username", jsonObject.get("jdbcUserName").toString());
        property_maps.put("spring.datasource.password", jsonObject.get("jdbcPwd").toString());
		} 	
		catch(Exception ex) {
			ex.printStackTrace();
			} 
		
	finally{
		System.out.println("Deleting the source, and downloaded files");	
		 if (fileReader != null) {
		        try { 	
		        	fileReader.close();
		        } catch (IOException e) {
		             e.printStackTrace();
		        }
		    }		 

	    if(sourceFile != null)
			sourceFile.delete();
		if(downloadedFile != null)
			downloadedFile.deleteOnExit();	
			//downloadedFile.delete();	
		}
	
	
	return property_maps;
	}

}
