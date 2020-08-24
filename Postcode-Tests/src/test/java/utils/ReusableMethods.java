package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import POJO.PostBulkPostcodeLookupPayload;

public class ReusableMethods {
	
	
	ObjectMapper mapper;
	Properties prop;
	HttpResponse<String> response;
	HttpClient client;
	HttpRequest request;
	PostBulkPostcodeLookupPayload payload;
	
	private String globalProperties = "src/test/java/resources/global.properties";
	int isPostCodeInvalid;
	
	
	public ReusableMethods() 
	{
		mapper = new ObjectMapper();
		prop = new Properties();		
		payload = new PostBulkPostcodeLookupPayload();
	}
	
	//Configure Base URL for Get API call using Postcode value	
	public String confiureBaseURL(String paramType, String postcode) throws IOException, FileNotFoundException
	{
		String baseURL = null;
		FileInputStream fis = new FileInputStream(globalProperties);
		prop.load(fis);	    
		
		if(paramType.equalsIgnoreCase("query"))
		{			
			if(postcode.equals(""))
		    	baseURL = prop.getProperty("baseURL");
		    else
		    	baseURL = prop.getProperty("baseURL") + "?q=" + URLEncoder.encode(postcode, StandardCharsets.UTF_8);			
		}
		else if(paramType.equalsIgnoreCase("path"))
		{
			if(postcode.equals(""))
		    	baseURL = prop.getProperty("baseURL");
		    else		    	
		    	baseURL = prop.getProperty("baseURL") + postcode.replaceAll(" ","%20");
		}		
		System.out.println(baseURL);
		return baseURL;
	}
	
	
	// Call the URL using Get Method	
	public HttpResponse<String> callGetAPI(String baseURL) throws IOException, InterruptedException
	{
		client = HttpClient.newHttpClient();	       
		request = HttpRequest.newBuilder()
				  .uri(URI.create(baseURL))
				  .build();
	    response =  client.send(request, HttpResponse.BodyHandlers.ofString());				
		return response;		
	}
	
	//Method to check the Postcode value is valid or not	
	String invalid_Codes [] = new String[]  {"CR0 2WR 123", "Al0 976 TR", "VBG TDAE", "OPYU TRX", "FDE THF" };
	public int checkPostCode(String postcode)
	{
		int postCodeFound = 0;
		for (int i = 0; i<invalid_Codes.length;i++ )
		{
			if(invalid_Codes[i].equalsIgnoreCase(postcode))
				postCodeFound = 1;		
		}		
		return postCodeFound;		
	}
		
	//Create Payload using postcodes for POST API 
	public String createPayload(String postCodes) throws JsonProcessingException
	{	
		List<String> values = new ArrayList<String>();
		
		var splitPostCodes = postCodes.split(",");
		
		
		for(int i = 0; i<splitPostCodes.length;i++)
		{
			//System.out.println(splitPostCodes[i]);
			if(splitPostCodes[i].equals(""))
				values.add("");
			else
				values.add(splitPostCodes[i]);			
		}
		//PostBulkPostcodeLookupPOJO p1 = new PostBulkPostcodeLookupPOJO();
		payload.setPostcodes(values);
			
		String requestBody = mapper.writeValueAsString(payload);	
		
		return requestBody;
		
	}
	
	//POST Method API call using Payload/Body 
	public HttpResponse<String> callPostAPI(String baseURL, String requestBody) throws IOException, InterruptedException,JsonProcessingException
	{
		HttpClient client = HttpClient.newHttpClient();
		
		HttpRequest request = HttpRequest.newBuilder()
							  .uri(URI.create(baseURL))
							  .header("Content-Type", "application/json")
							  .POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
		
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		
		
		return response;
		
	}
	
	
	
	
	

}
