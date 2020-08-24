@BulkPostAPIPostcodesLookup
Feature: POST API call by passing multiple Postcodes as a payload

  Background: 
    Given User configure the base URL of PostBulkPostcodeLookup API

  @PostAPIResultSection
  Scenario Outline: Validate Post API values by passing single/multiple Postcodes as payload
    When user calls PostBulkPostcodeLookup API passing "<Postcodes>"
    Then Validate PostBulkPostcodeLookup API values
      | Postcode | quality | eastings | northings | country | nhs_ha     | longitude | latitude  | european_electoral_region | primary_care_trust        | region     |
      | PR3 0SG  |       1 |   351012 |    440302 | England | North West | -2.746251 | 53.856635 | North West                | North Lancashire Teaching | North West |
      | M45 6GN  |       1 |   380416 |    406002 | England | North West | -2.297052 |  53.55028 | North West                | Bury                      | North West |
      | EX165BL  |       1 |   294478 |    112252 | England | South West |  -3.50197 | 50.900064 | South West                | Devon                     | South West |
      | SW4 9HS  |       1 |   529397 |    174374 | England | London     | -0.139096 |  51.45357 | London                    | Lambeth                   | London     |
      | BA12 6PN |       1 |   377771 |    131822 | England | South West |  -2.31874 | 51.085279 | South West                | Wiltshire                 | South West |

    Examples: 
      | Postcodes               |
      | PR3 0SG,M45 6GN,EX165BL |
      | SW4 9HS,,BA12 6PN       |

  @PostAPIResultSection
  Scenario Outline: Validate Post API result section values by passing single/multiple Postcodes as payload
    When user calls PostBulkPostcodeLookup API passing "<Postcodes>"
    Then Validate PostBulkPostcodeLookup API result sections values
      | Postcode | lsoa           | msoa          | incode | outcode | parliamentary_constituency | admin_district | parish                    | admin_county | admin_ward           | ced             | ccg                                                     | nuts                          |
      | PR3 0SG  | Wyre 006A      | Wyre 006      | 0SG    | PR3     | Wyre and Preston North     | Wyre           | Myerscough and Bilsborrow | Lancashire   | Brock with Catterall | Wyre Rural East | NHS Fylde and Wyre                                      | Lancaster and Wyre            |
      | M45 6GN  | Bury 019C      | Bury 019      | 6GN    | M45     | Bury South                 | Bury           | Bury, unparished area     |              | Pilkington Park      |                 | NHS Bury                                                | Greater Manchester North East |
      | EX165BL  | Mid Devon 005C | Mid Devon 005 | 5BL    | EX16    | Tiverton and Honiton       | Mid Devon      | Tiverton                  | Devon        | Westexe              | Tiverton West   | NHS Devon                                               | Devon CC                      |
      | SW4 9HS  | Lambeth 019A   | Lambeth 019   | 9HS    | SW4     | Streatham                  | Lambeth        | Lambeth, unparished area  |              | Clapham Common       |                 | NHS South East London                                   | Lambeth                       |
      | BA12 6PN | Wiltshire 050C | Wiltshire 050 | 6PN    | BA12    | South West Wiltshire       | Wiltshire      | Zeals                     |              | Mere                 |                 | NHS Bath and North East Somerset, Swindon and Wiltshire | Wiltshire                     |

    Examples: 
      | Postcodes               |
      | PR3 0SG,M45 6GN,EX165BL |
      | SW4 9HS,,BA12 6PN       |

  @PostAPICodesSection
  Scenario Outline: Validate Post API codes section values by passing single/multiple Postcodes as payload
    When user calls PostBulkPostcodeLookup API passing "<Postcodes>"
    Then Validate PostBulkPostcodeLookup API codes sections values
      | Postcode | admin_district | admin_county | admin_ward | parish    | parliamentary_constituency | ccg       | ccg_id | ced       | nuts  |
      | PR3 0SG  | E07000128      | E10000017    | E05009934  | E04005340 | E14001057                  | E38000226 | 02M    | E58000832 | UKD44 |
      | M45 6GN  | E08000002      | E99999999    | E05000677  | E43000156 | E14000612                  | E38000024 | 00V    | E99999999 | UKD37 |
      | EX165BL  | E07000042      | E10000008    | E05003531  | E04003055 | E14000996                  | E38000230 | 15N    | E58000304 | UKK43 |
      | SW4 9HS  | E09000022      | E99999999    | E05000418  | E43000212 | E14000978                  | E38000244 | 72Q    | E99999999 | UKI45 |
      | BA12 6PN | E06000054      | E99999999    | E05008378  | E04011875 | E14000954                  | E38000231 | 92G    | E99999999 | UKK15 |

    Examples: 
      | Postcodes               |
      | PR3 0SG,M45 6GN,EX165BL |
      | SW4 9HS,,BA12 6PN       |

  @PostAPIErrorCodes
  Scenario Outline: Validate Error codes through POST API method
    When user calls PostBulkPostcodeLookup API to check invalid scenarios based on "<Scenario>"
    Then user validate PostBulkPostcodeLookup API <StatusCode> and "<ErrorMessage>"

    Examples: 
      | Scenario                      | StatusCode | ErrorMessage                                                                                                                                                             |
      | InvalidJson                   |        400 | Invalid JSON submitted.\nYou need to submit a JSON object with an array of postcodes or geolocation objects.\nAlso ensure that Content-Type is set to application/json\n |
      | Morethan100PostcodesSubmitted |        400 | Too many postcodes submitted. Up to 100 postcodes can be bulk requested at a time                                                                                        |
      | InvalidResource               |        404 | Resource not found                                                                                                                                                       |
      
      
