package StepDefinitions;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import POJO.GetPostcodeQueryModel;
import POJO.PostcodeErrorModel;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import utils.ReusableMethods;

public class GetPostcodeQuery {
	
	HttpClient client;
	HttpRequest request;
	HttpResponse<String> response;
	ObjectMapper mapper;
	ReusableMethods reuseMethods;
	GetPostcodeQueryModel postcodeQuery;
	PostcodeErrorModel postcodeError;
	Properties prop;
	
	private String parameterType = "Query";
	private String baseURL;
	private int isPostCodeInvalid;
	private String globalProperties = "src/test/java/resources/global.properties";
	
	public GetPostcodeQuery()
	{
		reuseMethods = new ReusableMethods();
		prop = new Properties();	
		mapper = new ObjectMapper();
	}
	
	@Given("User configure the base URL of GetPostcodeQuery API using {string}")
	public void user_configure_the_base_url_of_get_postcode_query_api(String postcode) throws FileNotFoundException, IOException 
	{
		//Get baseURL
		baseURL = reuseMethods.confiureBaseURL(parameterType, postcode);		    
	}

	@When("user calls GetPostcodeQueryAPI based on {string} value")
	public void user_calls_get_postcode_query_api_based_on_value(String postcode) throws IOException, InterruptedException 
	{
		//Get API call and retrieve response.
		response = reuseMethods.callGetAPI(baseURL);
		
		if(postcode.equals(""))
		{
			postcodeError = mapper.readValue(response.body(), PostcodeErrorModel.class);
			Assert.assertNotNull(postcodeError);			
		}
		else
		{
			postcodeQuery = mapper.readValue(response.body(), GetPostcodeQueryModel.class);
			Assert.assertNotNull(postcodeQuery);			
		}		
	}
	
	//Validate First 10 values from result section from Get Postcode Query API using Query Parameter
	@Then("user validates GetPostcodeQuery API response values like {int},{int},{int},{string},{string},{double},{double},{string},{string} and {string}")
	public void validate_get_postcode_query_result(int quality, int eastings, int northings, String country, String nhs_ha,double longitude, 
												   double latitude,String european_electoral_region, String primary_care_trust, String region) 
	{
	    var resultValues = postcodeQuery.result;
	    
	    Object[] expectedValues = {quality,eastings,northings,country,nhs_ha,longitude,latitude,european_electoral_region,primary_care_trust,region};
				 
		Object [] actualValues = {resultValues.get(0).getQuality(),resultValues.get(0).getEastings(),resultValues.get(0).getNorthings(),resultValues.get(0).getCountry(),
										resultValues.get(0).getNhs_ha(), resultValues.get(0).getLongitude(),resultValues.get(0).getLatitude(),
										resultValues.get(0).getEuropean_electoral_region(),resultValues.get(0).getPrimary_care_trust(),resultValues.get(0).getRegion()};
		
		Assert.assertTrue(1==postcodeQuery.result.size()); 
		Assert.assertArrayEquals("validate Result Section values",expectedValues,actualValues);			 
	}
	
	//Validate remaining values from result section from Get Postcode Query API using Query Parameter
	@Then("user validates GetPostcodeQuery API result section values like {string},{string},{string},{string},{string},{string},{string},{string},{string},{string},{string},{string}")
	public void validate_get_postcode_query_api_result_section(String lsoa, String msoa, String incode, String outcode, String parliamentary_constituency, String admin_district, 
			  String parish, String admin_county, String admin_ward, String ced, String ccg, String nuts) 
	{		
		var resultValues = postcodeQuery.result;
				
		boolean admincounty = (admin_county.equals("")) ? (((postcodeQuery.result.get(0).getAdmin_county()==null) && (admin_county.equals(""))) ? true:false) : (postcodeQuery.result.get(0).getAdmin_county().equals(admin_county)? true:false);
		boolean cced = ced.equals("") ? (((postcodeQuery.result.get(0).getCed()==null) && (ced.equals(""))) ? true:false) : (postcodeQuery.result.get(0).getCed().equals(ced)?true:false);
		  
		Object[] expectedValues = { lsoa,  msoa,  incode,  outcode,  parliamentary_constituency,  admin_district, 
                parish,  admincounty,  admin_ward,  cced,  ccg,  nuts};

		Object [] actualValues = {resultValues.get(0).getLsoa(), resultValues.get(0).getMsoa(),
					   resultValues.get(0).getIncode(), resultValues.get(0).getOutcode(),
					   resultValues.get(0).getParliamentary_constituency(), resultValues.get(0).getAdmin_district(),
					   resultValues.get(0).getParish(),admincounty,resultValues.get(0).getAdmin_ward(),cced
					   , resultValues.get(0).getCcg(), resultValues.get(0).getNuts()};
		
		Assert.assertTrue(1==postcodeQuery.result.size()); 
		Assert.assertArrayEquals("validate Result Section values",expectedValues,actualValues);  
	}

	//Validate Codes section under Result section 
	@Then("user validates GetPostcodeQuery API codes section values like {string},{string},{string},{string},{string},{string},{string},{string},{string}")
	public void validate_get_postcode_query_api_codes_section_values(String admin_district, String admin_county, String admin_ward, String parish,
			  														 String parliamentary_constituency, String ccg, String ccg_id, String ced, String nuts) 
	{	
		var objCodes = postcodeQuery.result.get(0).getCodes(); 	
		
		Assert.assertTrue(1==postcodeQuery.result.size()); 
		Assert.assertEquals("Validate admin_district value",objCodes.getAdmin_district(), admin_district);
		Assert.assertEquals("Validate admin_county value",objCodes.getAdmin_county(), admin_county);
		Assert.assertEquals("Validate admin_ward value",objCodes.getAdmin_ward(), admin_ward);
		Assert.assertEquals("Validate parish value",objCodes.getParish(), parish);
		Assert.assertEquals("Validate parliamentary_constituency value",objCodes.getParliamentary_constituency(), parliamentary_constituency);
		Assert.assertEquals("Validate ccg value",objCodes.getCcg(), ccg);
		Assert.assertEquals("Validate ccg_id value",objCodes.getCcg_id(), ccg_id);
		Assert.assertEquals("Validate ced value",objCodes.getCed(), ced);
		Assert.assertEquals("Validate nuts value",objCodes.getNuts(), nuts);		
	}
	
	@Then("user validates GetPostcodeQuery API response values based on Partial postcode")
	public void user_validates_get_postcode_query_api_response_values_based_on_partial_postcode(DataTable table) {
	   
		List<List<String>> list = table.asLists(String.class);		
		String expectedPostcode, actualPostcode;
		
		Assert.assertTrue(10==postcodeQuery.result.size()); 
		
		for (int i=1; i<list.size();i++)
		{
			expectedPostcode = list.get(i).get(0);
			
			for(int j =0; j<postcodeQuery.result.size();j++)
			{
				actualPostcode = postcodeQuery.result.get(j).getPostcode();
				if(expectedPostcode.equals(actualPostcode))
				{
					var resultValues = postcodeQuery.result;
				    
				    Object[] expectedValues = {Integer.parseInt(list.get(i).get(1)),Integer.parseInt(list.get(i).get(2)),Integer.parseInt(list.get(i).get(3)),
				    						   list.get(i).get(4),list.get(i).get(5),Double.parseDouble(list.get(i).get(6)),Double.parseDouble(list.get(i).get(7)),list.get(i).get(8),list.get(i).get(9),list.get(i).get(10)};
							 
					Object [] actualValues = {resultValues.get(j).getQuality(),resultValues.get(j).getEastings(),resultValues.get(j).getNorthings(),resultValues.get(j).getCountry(),
											  resultValues.get(j).getNhs_ha(), resultValues.get(j).getLongitude(),resultValues.get(j).getLatitude(),
											  resultValues.get(j).getEuropean_electoral_region(),resultValues.get(j).getPrimary_care_trust(),resultValues.get(j).getRegion()};
					
					
					Assert.assertArrayEquals("validate Result Section values",expectedValues,actualValues);	
					
				}
			}
		}		
	}	
	
	@Then("user validate GetPostcodeQuery API response result section values based on Partial postcode")
	public void validate_get_postcode_query_api_response_result_section_partial_postcode(DataTable table) {
		
		List<List<String>> list = table.asLists(String.class);		
		String expectedPostcode, actualPostcode;
		
		Assert.assertTrue(10==postcodeQuery.result.size()); 
		
		for (int i=1; i<list.size();i++)
		{
			expectedPostcode = list.get(i).get(0);
			
			for(int j =0; j<postcodeQuery.result.size();j++)
			{
				actualPostcode = postcodeQuery.result.get(j).getPostcode();
				if(expectedPostcode.equals(actualPostcode))
				{
					var resultValues = postcodeQuery.result;
				    
					boolean admincounty = (list.get(i).get(8).equals("")) ? (((postcodeQuery.result.get(0).getAdmin_county()==null) && (list.get(i).get(8).equals(""))) ? true:false) : (postcodeQuery.result.get(0).getAdmin_county().equals(list.get(i).get(8))? true:false);
					boolean cced = list.get(i).get(10).equals("") ? (((postcodeQuery.result.get(0).getCed()==null) && ( list.get(i).get(10).equals(""))) ? true:false) : (postcodeQuery.result.get(0).getCed().equals( list.get(i).get(10))?true:false);
					  
					Object[] expectedValues = {list.get(i).get(1),list.get(i).get(2),list.get(i).get(3),list.get(i).get(4),list.get(i).get(5),list.get(i).get(6),
							list.get(i).get(7),admincounty,list.get(i).get(9),cced,list.get(i).get(11),list.get(i).get(12)};

					Object [] actualValues = {resultValues.get(j).getLsoa(), resultValues.get(j).getMsoa(),
							   resultValues.get(j).getIncode(), resultValues.get(j).getOutcode(),
							   resultValues.get(j).getParliamentary_constituency(), resultValues.get(j).getAdmin_district(),
							   resultValues.get(j).getParish(),admincounty,resultValues.get(j).getAdmin_ward(),cced
							   , resultValues.get(j).getCcg(), resultValues.get(j).getNuts()};
					 
					Assert.assertArrayEquals("validate Result Section values",expectedValues,actualValues);	
					
				}
			}
		}
	   
	}

	@Then("user validates GetPostcodeQuery API response codes section values based on Partial postcode")
	public void validates_get_postcode_query_api_response_codes_section_values(DataTable table) {
		List<List<String>> list = table.asLists(String.class);		
		String expectedPostcode, actualPostcode;
		
		Assert.assertTrue(10==postcodeQuery.result.size()); 
		
		for (int i=1; i<list.size();i++)
		{
			expectedPostcode = list.get(i).get(0);
			
			for(int j =0; j<postcodeQuery.result.size();j++)
			{
				actualPostcode = postcodeQuery.result.get(j).getPostcode();
				if(expectedPostcode.equals(actualPostcode))
				{
					var resultValues = postcodeQuery.result.get(j).getCodes();				    
					
					Object[] expectedValues = {list.get(i).get(1),list.get(i).get(2),list.get(i).get(3),list.get(i).get(4),list.get(i).get(5),list.get(i).get(6),
							                   list.get(i).get(7),list.get(i).get(8),list.get(i).get(9)};

					Object [] actualValues = {resultValues.getAdmin_district(),resultValues.getAdmin_county(),resultValues.getAdmin_ward(),resultValues.getParish(),
							                  resultValues.getParliamentary_constituency(),resultValues.getCcg(),resultValues.getCcg_id(),resultValues.getCed(),resultValues.getNuts()};
					 
					Assert.assertArrayEquals("validate Result Section values",expectedValues,actualValues);	
					
				}
			}
		}
	}
		
	@Then("user validate GetPostcodeQuery API {int} and {string} based on {string}")
	public void validate_get_postcode_query_api_errors(int statusCode, String errorMessage, String postcode) 
	{		
		isPostCodeInvalid = reuseMethods.checkPostCode(postcode);
		
		if(postcode.equals(""))
			{
				 Assert.assertEquals(statusCode, postcodeError.getStatus());	
				 Assert.assertEquals(errorMessage, postcodeError.getError());
						
			}
		else if (isPostCodeInvalid == 1)
			{
				 Assert.assertEquals(statusCode, postcodeQuery.status);					
				 Assert.assertEquals(null, postcodeQuery.result);
			}			
	}
	
	@Given("User configure the base URL of GetPostcodeQuery API using {string} and {string}")
	public void user_configure_the_base_url_of_get_postcode_query_api_using_and(String postcode, String redPathScenario) throws IOException,FileNotFoundException, InterruptedException
	{
		FileInputStream fis = new FileInputStream(globalProperties);
		prop.load(fis);	
		if(redPathScenario.equals("InvalidResource"))
		{
			baseURL = prop.getProperty("invalidBaseURL") + "?q=" +  URLEncoder.encode(postcode, StandardCharsets.UTF_8);			
			response = reuseMethods.callGetAPI(baseURL);
			postcodeError = mapper.readValue(response.body(), PostcodeErrorModel.class);			
		}
	}

	@Then("user validate API response using {string}, {int} and {string}")
	public void user_validate_get_postcode_query_api_based_on_and(String string, Integer statusCode, String errorMessage) 
	{
		Assert.assertTrue("Statuscodes check", statusCode==postcodeError.getStatus());
		Assert.assertEquals(errorMessage, postcodeError.getError());
	   
	}
	
}



