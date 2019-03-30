package com.azurestorage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@ComponentScan
public class AppConfig {
		
	 	@Bean
	    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() throws Exception {
	      
		 MutablePropertySources propertySources = new MutablePropertySources();
		 String CONTAINER_NAME = System.getenv("CONTAINER_NAME");
		 String BLOB_NAME = System.getenv("BLOB_NAME");
		 String ACCOUNT_NAME = System.getenv("ACCOUNT_NAME");
	     String ACCOUNT_KEY = System.getenv("ACCOUNT_KEY");
	     String ENDPOINTS_PROTOCOL = System.getenv("ENDPOINTS_PROTOCOL");
	     String ENDPOINT_SUFFIX = System.getenv("ENDPOINT_SUFFIX");
		 
	     String storageConnectionString =  "DefaultEndpointsProtocol="+ENDPOINTS_PROTOCOL+";" +
					"AccountName="+ACCOUNT_NAME+";" +
					"AccountKey="+ACCOUNT_KEY+";" +
					"EndpointSuffix="+ENDPOINT_SUFFIX ;
	     
	     Map<String, Object> configurationProperties = new HashMap<>();
	     configurationProperties.put("CONTAINER_NAME", CONTAINER_NAME);
	     configurationProperties.put("BLOB_NAME", BLOB_NAME);
	     configurationProperties.put("ACCOUNT_NAME", ACCOUNT_NAME);
	     configurationProperties.put("ACCOUNT_KEY", ACCOUNT_KEY);
	     configurationProperties.put("ENDPOINTS_PROTOCOL", ENDPOINTS_PROTOCOL);
	     configurationProperties.put("ENDPOINT_SUFFIX", ENDPOINT_SUFFIX);
	     configurationProperties.put("CONNECTION_STRING", storageConnectionString);
	     HashMap<String, String> property_map= AzureStorageConfig.getDatabaseProperties(storageConnectionString, configurationProperties);
	  
	     for (Map.Entry<String,String> entry : property_map.entrySet())  
	     {	 System.out.println("Key = " + entry.getKey() + 
                     ", Value = " + entry.getValue()); 
	    	 configurationProperties.put(entry.getKey(),entry.getValue()); }

	     propertySources.addLast(new MapPropertySource(
	                "configuration",
	                Collections.unmodifiableMap(configurationProperties)
	        ));
	     	     
	        propertySources.addLast(new ResourcePropertySource(
	                "application",
	                "classpath:application.properties"
	        ));

	        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
	        configurer.setPropertySources(propertySources);
	        
	        return configurer;
	 }
	
	  @Bean
	    public CorsFilter corsFilter() {
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        CorsConfiguration config = new CorsConfiguration();
	        config.setAllowCredentials( true );
	        config.addAllowedOrigin( "*" );
	        config.addAllowedHeader( "*" );
	        config.addAllowedMethod( "*" );
	        source.registerCorsConfiguration( "/*/**" , config);
	        return new CorsFilter(source);
	    }

}
