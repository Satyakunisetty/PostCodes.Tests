@GetAPIPostcodePathParameter
Feature: Get API Method call using Postcode as a path parameter

	@GetAPIPathResultSection
  Scenario Outline: Validate Get API values by passing Postcode as path parameter
    Given User configure the base URL of GetPostcodePath API using "<Postcode>"
    When user calls GetPostcodePathAPI based on "<Postcode>" value
    Then user validates GetPostcodePath API response values like <quality>,<eastings>,<northings>,"<country>","<nhs_ha>",<longitude>,<latitude>,"<european_electoral_region>","<primary_care_trust>" and "<region>"
    Examples: 
      | Postcode | quality | eastings | northings | country | nhs_ha           | longitude | latitude  | european_electoral_region | primary_care_trust | region     |
      | RH10 1ED |       1 |   526993 |    136858 | England | South East Coast | -0.187002 | 51.116948 | South East                | West Sussex        | South East |
      | CR0 2WR  |       1 |   532440 |    166172 | England | London           | -0.098363 | 51.379158 | London                    | Croydon            | London     |

	@GetAPIPathResultSection
  Scenario Outline: Validate result section values from Get API values by using Postcode as path parameter
    Given User configure the base URL of GetPostcodePath API using "<Postcode>"
    When user calls GetPostcodePathAPI based on "<Postcode>" value
    Then user validates GetPostcodePath API result section values "<lsoa>","<msoa>","<incode>","<outcode>","<parliamentary_constituency>","<admin_district>","<parish>","<admin_county>","<admin_ward>","<ced>","<ccg>","<nuts>"
    Examples: 
      | Postcode | lsoa         | msoa        | incode | outcode | parliamentary_constituency | admin_district | parish                   | admin_county | admin_ward    | ced                    | ccg                   | nuts                     |
      | RH10 1ED | Crawley 004C | Crawley 004 | 1ED    | RH10    | Crawley                    | Crawley        | Crawley, unparished area | West Sussex  | Three Bridges | Northgate & West Green | NHS West Sussex       | West Sussex (North East) |
      | CR0 2WR  | Croydon 027C | Croydon 027 | 2WR    | CR0     | Croydon Central            | Croydon        | Croydon, unparished area |              | Fairfield     |                        | NHS South West London | Croydon                  |

	@GetAPIPathCodesSection
  Scenario Outline: Validate codes section values from Get API values by using Postcode as path parameter
    Given User configure the base URL of GetPostcodePath API using "<Postcode>"
    When user calls GetPostcodePathAPI based on "<Postcode>" value
    Then user validates GetPostcodePath API codes section values "<admin_district>","<admin_county>","<admin_ward>","<parish>","<parliamentary_constituency>","<ccg>","<ccg_id>","<ced>","<nuts>"
    Examples: 
      | Postcode | admin_district | admin_county | admin_ward | parish    | parliamentary_constituency | ccg       | ccg_id | ced       | nuts  |
      | RH10 1ED | E07000226      | E10000032    | E05012925  | E43000148 | E14000652                  | E38000248 | 70F    | E58001645 | UKJ28 |
      | CR0 2WR  | E09000008      | E99999999    | E05011468  | E43000198 | E14000654                  | E38000245 | 36L    | E99999999 | UKI62 |

	@GetAPIPathErroCodes
  Scenario Outline: Validate error codes from Get API by passing Postcode as path parameter
    Given User configure the base URL of GetPostcodePath API using "<Postcode>"
    When user calls GetPostcodePathAPI based on "<Postcode>" value
    Then user validates GetPostcodePath API <StatusCode> and "<ErrorMessage>" based on "<Postcode>"

    Examples: 
      | Postcode    | StatusCode | ErrorMessage                                                     |
      | CR0 2WR 123 |        404 | Invalid postcode                                                 |
      |             |        400 | No postcode query submitted. Remember to include query parameter |

	@GetAPIPathErroCodes
  Scenario Outline: Validate error codes from Get API method by passing Invalid Resource
    Given User configure the base URL of GetPostcodePath API using "<Postcode>" and "<Scenario>"
    Then user validate GetPostcodePath API response using "<Scenario>", <StatusCode> and "<ErrorMessage>"

    Examples: 
      | Scenario        | Postcode | StatusCode | ErrorMessage       |
      | InvalidResource | CR0 2WR  |        404 | Resource not found |