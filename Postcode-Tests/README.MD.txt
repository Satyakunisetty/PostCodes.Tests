
Postcodes.io Tests

Created a Maven Project with name Postcode-Tests and 30 Tests are included as part of the suite for the following three endpoints

1. POST api.postcodes.io/postcodes
2. GET api.postcodes.io/postcodes?q=:postcode
3. GET api.postcodes.io/postcodes/:postcode

Import the project through Eclipse by navigating to File - Import - Maven - Existing Maven Project - Click Next - Select the projct using Browse option
Once sucessfully imported, Refresh the project to ensure the all the Maven dependencies are loaded sucessfully.

Please find below for the individual sections along with their descritpion from the project structure.

a. Postcode-Tests\src\test\java\cucumber\Options - This consists of the TestRunner file. This helps to execute the tests present as part of the project.
	By Default I have configured this file to execute all tests from three endpoints. We can customise using features and tags options
	Tags - Are used to help to execute specific test case rather than entire suite. Currently this is configured as NULL to execute all tests
		i. @GetAPIPostcodeQueryParameter - Used to test all scenarios defined in the feature file to call GET method using Query parameter 
	   ii. @GetAPIPostcodePathParameter - Used to test all scenarios defined in the feature file to call GET method using Path parameter 
	  iii. @BulkPostAPIPostcodesLookup - Used to test all scenarios defined in the feature file to call POST method using Bulk Postcodes.

b. Postcode-Tests\target\cucumber-html-reports - This consists of detailed reports created as part of the execution.

		i. overview-features.html - This consists of number of testcases, steps, duration seggregated as per feature file.
	   ii. overview-tags.html - This consists of in detailed description of scenarios, steps executed as per tag defined in feature file.
	  iii. overview-failures.html - This consists of number of tests failed as part of the execution	  
	  
c. Postcode-Tests\src\main\java\POJO - Package POJO is created. This consists of all the POJO classes required for the three endpoints.

	i.POST api.postcodes.io/postcodes - 
		PostBulkPostcodesModel - This is used to store the response after the POST request is been hit.
		PostBulkPostcodeLookupPayload - This is used to create the Body/Payload for the POST request based on Postcodes provided

	ii. GET api.postcodes.io/postcodes?q=:postcode
		GetPostcodeQueryModel - This is used to store the response after the GET request with query parameter is called.

	iii. GET api.postcodes.io/postcodes/:postcode
		GetPostcodePathModel - This is used to store the response after the GET request with path parameter is called.
		
	iv. For all endpoints, to store the error response PostcodeErrorModel is created.

d. Postcode-Tests\src\test\java\Features - Contains a feature file for every endpoint

	i. PostBulkPostcodeLookup.feature - This feature file consists of all tests related to POST API call using Bulk Postcodes - POST api.postcodes.io/postcodes  

	ii. GetPostcodeQuery.feature - This feature file consists of all tests related to GET API call using Postcode as Query parameter - GET api.postcodes.io/postcodes?q=:postcode

	iii. GetPostcodePath.feature - This feature file consists of all tests related to GET API call using Postcode as Path parameter - GET api.postcodes.io/postcodes/:postcode

e. Postcode-Tests\src\test\java\StepDefinitions - Contains respective Step definition file for every endpoint. 

	i. PostBulkPostcodeLookup.java - This file consists of all step implementations for the steps defined in PostBulkPostcodeLookup.feature as part of POST endpoint- POST api.postcodes.io/postcodes  

	ii. GetPostcodeQuery.java - This file consists of all step implementations for the steps defined in GetPostcodeQuery.feature as part of GET endpoint with Query parameter - GET api.postcodes.io/postcodes?q=:postcode

	iii. GetPostcodePath.java - This file consists of all step implementations for the steps defined in GetPostcodePath.feature as part of GET endpoint with Path parameter - GET api.postcodes.io/postcodes/:postcode

f. Postcode-Tests\src\test\java\resources - This section is created to pass input required for the project

	i. global.properties - This cosnists of all the static information required for the execution.
	
g. Postcode-Tests\src\test\java\utils - This section is used to define all the generic/re-usable methods.

	i. ReusableMethods - This consists of all the generic/re-usable methods required by all three endpoints.







