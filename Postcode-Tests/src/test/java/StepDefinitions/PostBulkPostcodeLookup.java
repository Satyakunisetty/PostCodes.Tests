package StepDefinitions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import POJO.GetPostcodeQueryModel;
import POJO.PostBulkPostcodesModel;
import POJO.PostcodeErrorModel;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import utils.ReusableMethods;

public class PostBulkPostcodeLookup {
	
	Properties prop;
	ObjectMapper mapper;
	PostBulkPostcodesModel postBulkPostcodeResponse;
	ReusableMethods reuseMethods;
	PostcodeErrorModel postcodeError;	
	HttpResponse<String> response;
	
	
	private String globalProperties = "src/test/java/resources/global.properties";
	private String baseURL, actualPostcode,expectedPostode, inputPostcodes,tablePostcode;
	
	
	public PostBulkPostcodeLookup()
	{
		prop = new Properties();	
		mapper = new ObjectMapper();
		reuseMethods = new ReusableMethods();
	}
	
	
	@Given("User configure the base URL of PostBulkPostcodeLookup API")
	public void configure_the_base_url_of_post_bulk_postcode_lookup_api() throws IOException, FileNotFoundException
	{
		FileInputStream fis = new FileInputStream(globalProperties);
		prop.load(fis);	   
		baseURL = prop.getProperty("baseURL");
	}

	@When("user calls PostBulkPostcodeLookup API passing {string}")
	public void user_calls_post_bulk_postcode_lookup_api(String postCodes) throws IOException, InterruptedException,JsonProcessingException
	{		
		inputPostcodes = postCodes;
		
		//CreatePayload from Input Postcodes
		String requestBody = reuseMethods.createPayload(postCodes);	
		
		//Call POST API Method
		response = reuseMethods.callPostAPI(baseURL, requestBody);
	
		postBulkPostcodeResponse = mapper.readValue(response.body(), PostBulkPostcodesModel.class);		
		Assert.assertNotNull(postBulkPostcodeResponse);						
	}
		
	@Then("Validate PostBulkPostcodeLookup API values")
	public void validate_post_bulk_postcode_lookup_api_(DataTable table) 
	{	
		String [] splitInputPostcodes = inputPostcodes.split(",");
		int postResponseResultCount = postBulkPostcodeResponse.result.size();
		List<List<String>> list = table.asLists(String.class);
		
		for (int i = 0; i<splitInputPostcodes.length; i++)
		{
			expectedPostode = splitInputPostcodes[i];
			for (int j=0; j<postResponseResultCount;j++)
			{
				actualPostcode = postBulkPostcodeResponse.result.get(j).getQuery();	
				
				if(expectedPostode.equals("") && actualPostcode.equals(""))
				{
					Assert.assertTrue(postBulkPostcodeResponse.result.get(j).getQuery().equals(""));
					Assert.assertTrue(postBulkPostcodeResponse.result.get(j).getResult()==null);
					break;					
				}				
				else if(expectedPostode.equals(actualPostcode))
				{
					for(int k = 0; k<list.size();k++)
					{
						tablePostcode = list.get(k).get(0);
						if(expectedPostode.equals(tablePostcode))
						{
							 var resultValues = postBulkPostcodeResponse.result.get(j).getResult();
							 Object[] expectedValues = {Integer.parseInt(list.get(k).get(1)),Integer.parseInt(list.get(k).get(2)),Integer.parseInt(list.get(k).get(3)),list.get(k).get(4),
									 					list.get(k).get(5),Double.parseDouble(list.get(k).get(6)),Double.parseDouble(list.get(k).get(7)),list.get(k).get(8),list.get(k).get(9),list.get(k).get(10)};
										 
							 Object [] actualValues = {resultValues.getQuality(),resultValues.getEastings(),resultValues.getNorthings(),resultValues.getCountry(),
									 resultValues.getNhs_ha(),resultValues.getLongitude(),resultValues.getLatitude(),resultValues.getEuropean_electoral_region(),
									 resultValues.getPrimary_care_trust(),resultValues.getRegion()};
								
							Assert.assertArrayEquals("validate Result Section values",expectedValues,actualValues);	
							break;
						}						
					}				
				}	 
			}
		}		
	}
		
	@Then("Validate PostBulkPostcodeLookup API result sections values")
	public void validate_post_bulk_postcode_lookup_api_result_section_values(DataTable table)
	{		
		String [] splitInputPostcodes = inputPostcodes.split(",");
		int postResponseResultCount = postBulkPostcodeResponse.result.size();
		List<List<String>> list = table.asLists(String.class);
		
		for (int i = 0; i<splitInputPostcodes.length; i++)
		{
			expectedPostode = splitInputPostcodes[i];
			for (int j=0; j<postResponseResultCount;j++)
			{
				actualPostcode = postBulkPostcodeResponse.result.get(j).getQuery();	
				
				if(expectedPostode.equals("") && actualPostcode.equals(""))
				{
					Assert.assertTrue(postBulkPostcodeResponse.result.get(j).getQuery().equals(""));
					Assert.assertTrue(postBulkPostcodeResponse.result.get(j).getResult()==null);
					break;					
				}				
				else if(expectedPostode.equals(actualPostcode))
				{					
					for(int k = 0; k<list.size();k++)
					{
						tablePostcode = list.get(k).get(0);
						if(expectedPostode.equals(tablePostcode))
						{
							 var resultValues = postBulkPostcodeResponse.result.get(j).getResult();
				
							 boolean admincounty = (list.get(k).get(8)==null) ? (resultValues.getAdmin_county()==null ? true:false) : (resultValues.getAdmin_county().equals(list.get(k).get(8))? true:false);
							 boolean cced = list.get(k).get(10)==null ? (resultValues.getCed()==null ? true:false) : (resultValues.getCed().equals(list.get(k).get(10))?true:false);
								
							 
							 Object[] expectedValues = {list.get(k).get(1),list.get(k).get(2),list.get(k).get(3),list.get(k).get(4),list.get(k).get(5),list.get(k).get(6),
										list.get(k).get(7),admincounty,list.get(k).get(9),cced,list.get(k).get(11),list.get(k).get(12)};
										 
							 Object [] actualValues = {resultValues.getLsoa(), resultValues.getMsoa(), resultValues.getIncode(), resultValues.getOutcode(),
									   				   resultValues.getParliamentary_constituency(), resultValues.getAdmin_district(),
									   				   resultValues.getParish(),admincounty,resultValues.getAdmin_ward(),cced, resultValues.getCcg(), resultValues.getNuts()};
								
							Assert.assertArrayEquals("validate Result Section values",expectedValues,actualValues);	
							break;
						}						
					}				
				}	 
			}
		}	
	}

	@Then("Validate PostBulkPostcodeLookup API codes sections values")
	public void validate_post_bulk_postcode_lookup_api_codes_section_values(DataTable table) throws IndexOutOfBoundsException
	{	
		String [] splitInputPostcodes = inputPostcodes.split(",");
		int postResponseResultCount = postBulkPostcodeResponse.result.size();
		List<List<String>> list = table.asLists(String.class);		
		
		for (int i = 0; i<splitInputPostcodes.length; i++)
		{
			expectedPostode = splitInputPostcodes[i];
			for (int j=0; j<postResponseResultCount;j++)
			{
				actualPostcode = postBulkPostcodeResponse.result.get(j).getQuery();	
				
				if(expectedPostode.equals("") && actualPostcode.equals(""))
				{					
					Assert.assertTrue(postBulkPostcodeResponse.result.get(j).getQuery().equals(""));
					Assert.assertTrue(postBulkPostcodeResponse.result.get(j).getResult()==null);
					break;
				}				
				else if(expectedPostode.equals(actualPostcode))
				{					
					for(int k = 0; k<list.size();k++)
					{
						tablePostcode = list.get(k).get(0);
						if(expectedPostode.equals(tablePostcode))
						{
							var resultValues = postBulkPostcodeResponse.result.get(j).getResult().getCodes();
											 
							 Object[] expectedValues = {list.get(k).get(1),list.get(k).get(2),list.get(k).get(3),list.get(k).get(4),list.get(k).get(5),list.get(k).get(6),
										list.get(k).get(7),list.get(k).get(8),list.get(k).get(9)};
										 
							 Object [] actualValues = {resultValues.getAdmin_district(),resultValues.getAdmin_county(),resultValues.getAdmin_ward(),resultValues.getParish(),
									 				   resultValues.getParliamentary_constituency(),resultValues.getCcg(),resultValues.getCcg_id(),resultValues.getCed(),resultValues.getNuts()};
								
							Assert.assertArrayEquals("validate Result Section values",expectedValues,actualValues);	
							break;
						}						
					}				
				}	 
			}
		}			
	}

	@When("user calls PostBulkPostcodeLookup API to check invalid scenarios based on {string}")
	public void call_post_bulk_postcode_lookup_api_to_check_invalid_scenarios(String redPathScenario) throws IOException, InterruptedException 
	{
		String requestBody= null;			
		FileInputStream fis = new FileInputStream(globalProperties);
		prop.load(fis);	
		
		if(redPathScenario.equals("InvalidJson"))
		{
			requestBody = "{ \"postcodes\" : \r\n" + 
								"}";
			response = reuseMethods.callPostAPI(baseURL, requestBody);	
		}			
		else if(redPathScenario.equals("Morethan100PostcodesSubmitted"))
		{		
			String postCodes = prop.getProperty("toManyPostcodes");			
			requestBody = reuseMethods.createPayload(postCodes);
			response = reuseMethods.callPostAPI(baseURL, requestBody);
		}
		else if(redPathScenario.equals("InvalidResource"))
		{
			requestBody = "{\r\n" + 
			"  \"postcodes\" : [\"CR0 2WR\"]\r\n" + 
			"}";
			
			baseURL = prop.getProperty("invalidBaseURL");
			response = reuseMethods.callPostAPI(baseURL, requestBody);
		}
		
		postcodeError = mapper.readValue(response.body(), PostcodeErrorModel.class);
		Assert.assertNotNull(postcodeError);	
	}

	@Then("user validate PostBulkPostcodeLookup API {int} and {string}")
	public void user_validate_post_bulk_postcode_lookup_api_and(Integer statusCode, String errorMessage) 
	{		
		Assert.assertTrue("Statuscodes check", statusCode==postcodeError.getStatus());
		Assert.assertEquals(errorMessage, postcodeError.getError());
		
	}
	
	
	
}
