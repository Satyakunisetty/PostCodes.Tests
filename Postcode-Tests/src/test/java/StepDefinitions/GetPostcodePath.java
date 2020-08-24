package StepDefinitions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.junit.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

import POJO.GetPostcodePathModel;
import POJO.PostBulkPostcodesModel.Codes;
import POJO.PostcodeErrorModel;

import io.cucumber.java.en.*;
import utils.ReusableMethods;

public class GetPostcodePath {
	
	
	HttpClient client;
	HttpRequest request;
	HttpResponse<String> response;
	ObjectMapper mapper;
	ReusableMethods reuseMethods;
	GetPostcodePathModel postcodePath;
	PostcodeErrorModel postcodeError;
	Properties prop;
	
	private String parameterType = "Path";
	private String baseURL;
	private int isPostCodeInvalid;
	private String globalProperties = "src/test/java/resources/global.properties";
	
	public GetPostcodePath()
	{
		reuseMethods = new ReusableMethods();
		mapper = new ObjectMapper();
		prop = new Properties();
	}

	
	@Given("User configure the base URL of GetPostcodePath API using {string}")
	public void user_configure_the_base_url_of_get_postcode_path_api(String postcode) throws FileNotFoundException, IOException 
	{
		//Configure baseURl
		baseURL = reuseMethods.confiureBaseURL(parameterType, postcode);			
	}
	
	@When("user calls GetPostcodePathAPI based on {string} value")
	public void user_calls_get_postcode_path_api_based_on_value(String postcode) throws IOException, InterruptedException 
	{
		//Get API call and retrieve response.
		response = reuseMethods.callGetAPI(baseURL);	    
	    
	    isPostCodeInvalid = reuseMethods.checkPostCode(postcode);
	    
	    if(postcode.equals("") || isPostCodeInvalid == 1)		
	    {
	    	postcodeError = mapper.readValue(response.body(), PostcodeErrorModel.class);
	    	Assert.assertNotNull(postcodeError);	
	    }
		else
		{
			postcodePath = mapper.readValue(response.body(), GetPostcodePathModel.class);
			Assert.assertNotNull(postcodePath);	
		}   
	}
	
	
	@Then("user validates GetPostcodePath API response values like {int},{int},{int},{string},{string},{double},{double},{string},{string} and {string}")
	public void user_validates_get_postcode_path_api_response_values(int quality, int eastings, int northings, String country, String nhs_ha,double longitude, 
			   double latitude,String european_electoral_region, String primary_care_trust, String region)
	{
		var resultValues = postcodePath.result;
			
		Object[] expectedValues = {quality,eastings,northings,country,nhs_ha,longitude,latitude,european_electoral_region,primary_care_trust,region};
			 
		Object [] actualValues = {resultValues.getQuality(),resultValues.getEastings(),resultValues.getNorthings(),resultValues.getCountry(),
									resultValues.getNhs_ha(), resultValues.getLongitude(),resultValues.getLatitude(),
									resultValues.getEuropean_electoral_region(),resultValues.getPrimary_care_trust(),resultValues.getRegion()};
		
		Assert.assertArrayEquals("validate Result Section values",expectedValues,actualValues);
	}

	@Then("user validates GetPostcodePath API result section values {string},{string},{string},{string},{string},{string},{string},{string},{string},{string},{string},{string}")
	public void validate_get_postcode_path_api_result_section_values(String lsoa, String msoa, String incode, String outcode, String parliamentary_constituency, String admin_district, 
			  String parish, String admin_county, String admin_ward, String ced, String ccg, String nuts) 
	{
		var resultValues = postcodePath.result;
				
		boolean admincounty = (admin_county.equals("")) ? (((postcodePath.result.getAdmin_county()==null) && (admin_county.equals(""))) ? true:false) : (postcodePath.result.getAdmin_county().equals(admin_county)? true:false);
		boolean cced = ced.equals("") ? (((postcodePath.result.getCed()==null) && (ced.equals(""))) ? true:false) : (postcodePath.result.getCed().equals(ced)?true:false);
		  
		Object[] expectedValues = { lsoa,  msoa,  incode,  outcode,  parliamentary_constituency,  admin_district, 
				                    parish,  admincounty,  admin_ward,  cced,  ccg,  nuts};
		 
		Object [] actualValues = {resultValues.getLsoa(), resultValues.getMsoa(),
								   resultValues.getIncode(), resultValues.getOutcode(),
								   resultValues.getParliamentary_constituency(), resultValues.getAdmin_district(),
								   resultValues.getParish(),admincounty,resultValues.getAdmin_ward(),cced
								   , resultValues.getCcg(), resultValues.getNuts()};
		
		Assert.assertArrayEquals("validate Result Section values",expectedValues,actualValues);   
	}
	
	@Then("user validates GetPostcodePath API codes section values {string},{string},{string},{string},{string},{string},{string},{string},{string}")
	public void validate_get_postcode_path_api_codes_section_values(String admin_district, String admin_county, String admin_ward, String parish,
				 String parliamentary_constituency, String ccg, String ccg_id, String ced, String nuts) 
	{
		var resultCodes = postcodePath.result.getCodes();	 
		
		Assert.assertEquals("Validate admin_district value",resultCodes.getAdmin_district(), admin_district);
		Assert.assertEquals("Validate admin_county value",resultCodes.getAdmin_county(), admin_county);
		Assert.assertEquals("Validate admin_ward value",resultCodes.getAdmin_ward(), admin_ward);
		Assert.assertEquals("Validate parish value",resultCodes.getParish(), parish);
		Assert.assertEquals("Validate parliamentary_constituency value",resultCodes.getParliamentary_constituency(), parliamentary_constituency);
		Assert.assertEquals("Validate ccg value",resultCodes.getCcg(), ccg);
		Assert.assertEquals("Validate ccg_id value",resultCodes.getCcg_id(), ccg_id);
		Assert.assertEquals("Validate ced value",resultCodes.getCed(), ced);
		Assert.assertEquals("Validate nuts value",resultCodes.getNuts(), nuts);		   		 
	}
	
	@Then("user validates GetPostcodePath API {int} and {string} based on {string}")
	public void user_validates_get_postcode_path_api_and_based_on(Integer statusCode, String errorMessage, String postcode)
	{
		Assert.assertTrue("Validate Stauscode",statusCode==postcodeError.getStatus());
		Assert.assertEquals("Validate ErrorMessage",errorMessage, postcodeError.getError());				
	}
	
	
	@Given("User configure the base URL of GetPostcodePath API using {string} and {string}")
	public void user_configure_the_base_url_of_get_postcode_path_api_using_and(String postcode, String redPathScenario) throws IOException,FileNotFoundException, InterruptedException 
	{
		FileInputStream fis = new FileInputStream(globalProperties);
		prop.load(fis);	
		if(redPathScenario.equals("InvalidResource"))
		{
			baseURL = prop.getProperty("invalidBaseURL") + URLEncoder.encode(postcode, StandardCharsets.UTF_8);			
			response = reuseMethods.callGetAPI(baseURL);
			postcodeError = mapper.readValue(response.body(), PostcodeErrorModel.class);			
		}
	   
	}

	@Then("user validate GetPostcodePath API response using {string}, {int} and {string}")
	public void user_validate_get_postcode_path_api_response_using_and(String string, Integer statusCode, String errorMessage) 
	{
		Assert.assertTrue("Statuscodes check", statusCode==postcodeError.getStatus());
		Assert.assertEquals(errorMessage, postcodeError.getError());
	}

	

}
