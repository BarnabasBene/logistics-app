# logistics-app

This is a Spring Boot application with data access, service, and REST layers. The logistics company manages transport plans that contain the sections and milestones through which transports will be delivered. The entities of the data model are:

## Entities

- **TransportPlan**: Represents a transport plan, encapsulating the expected income for the delivery and the contained sections.
- **Section**: Represents a section of the transport plan, with a start and end milestone. It contains a number as well, showing the order of the section in the transport plan (counting from 0).
- **Milestone**: Represents a milestone during the transport. It references an address and contains when the milestone should be reached (plannedTime).
- **Address**: Encapsulates the data of an address, including the 2-letter ISO code of the country, city, street, zip code, house number, latitude, and longitude.
  
Each entity has a generated long primary key.

## RESTful Endpoints

The app contains the following RESTful endpoints (the request and response objects are DTOs):

- **POST /api/addresses**: Creates a new address in the database. The request body is a JSON object with properties corresponding to the fields of the Address entity.
  
  - Response in case of success: 200 OK, the response body is a JSON object containing a property “id” with the generated id of the entity.
  - Response in case of invalid input: 400 Bad Request
    - If the body is empty
    - If the “id” property in the request is filled
    - If any of country, city, zip code, street, or house number is null or an empty string

- **GET /api/addresses**: Returns all addresses.
  
  - Response: 200 OK, the response body is a JSON array containing all addresses stored in the DB.

- **GET /api/addresses/{id}**: Returns the address with the given id.
  
  - Response if the address exists: 200 OK, with the properties of the address in the body.
  - Response for non-existing id: 404 Not Found.

- **DELETE /api/addresses/{id}**: Deletes the address with the given id.
  
  - Response: 200 OK with an empty body, even if there was no address with the given id, and no deletion happened for this reason.

- **PUT /api/addresses/{id}**: Modifies the address in the DB with the given id. The request body contains a JSON object with the updated properties of the address.
  
  - Success response: 200 OK, the body including the new, updated state of the entity.
  - Error response 400 Bad Request:
    - If the request body is empty
    - If the “id” property in the body is filled and differs from the value in the URL
    - If any of country, city, zip code, street, or house number is null or an empty string
  - Error response 404 Not Found: If the address with the given id does not exist in the DB.

- **POST /api/addresses/search**: An endpoint supporting the search of addresses by country, city, zip code, and street. The request body is a JSON object containing the filter values for the fields above, with the following details:

  - An empty or missing property means no filtering is needed by that field.
  - The non-empty filter values are combined with an AND relationship.
  - In the case of city and street, an exact match is not required; a prefix match is enough, in a case-insensitive manner.
  - In the case of country code and zip code, an exact match is required.

  - Success response: 200 OK, the body containing the array of found addresses, which is empty if no match was found.
  - 400 Bad Request, if the request body is empty.

  This searching endpoint accepts query parameters for paging and sorting:

  - `page`: the number of pages requested, counting from 0
  - `size`: size of the page
  - `sort`: the field by which the found addresses are sorted, with the optionally added direction (asc/desc) of the sort, after a comma. If the direction is not specified, it is ascending by default.
  - Example: /api/addresses/search?page=2&size=10&sort=city,desc

  Default values:

  - If the size parameter is missing, return all results
  - If the page is missing, return the 0. page
  - If sort is missing, sort by id, in ascending order by default
  - The response contains the addresses of the specified page, and a custom HTTP header called "X-Total-Count" telling the total number of the matching addresses (i.e., as if there were only filtering, no paging)

- **POST /api/transportPlans/{id}/delay**: Registers an expected delay given in minutes, to a milestone of the transport plan with the given id, according to the following rules:

  - The id of the milestone and the duration of the delay in minutes are specified in a JSON object in the request body.
  
  - Error responses: In case of a non-existing transport plan or milestone, a 404 Not Found response is returned. If the milestone with the specified id is not part of any sections of the given transport plan, a 400 Bad Request response is returned.
  
  - The response is 200 OK otherwise, and:
    - The delay is added to the planned time of the milestone.
    - If the milestone was a start milestone of a section, the delay is added to the planned time of the end milestone of the section as well.
    - If the milestone was an end milestone of a section, the delay is added to the planned time of the start milestone of the next section.
    - The expected income of the transport plan is decreased by a few percents. The concrete percentage value depends on the length of the delay, and it is defined via properties. The limits where the percentage grows are: 30 minutes, 60 minutes, 120 minutes.

## Testing

- Integration test has been written for the last endpoint, covering the possible cases.

## Authentication and Authorization

The app contains authentication and authorization with Spring Security:

- It has a REST endpoint for login (POST /api/login). In case of a correct username/password pair, it returns a JWT token that is valid for 10 minutes and contains the user name and the authorities of the user.

- The endpoints for creating and updating addresses should only be available with AddressManager authority.

- The endpoint for registering delays is only available with TransportManager authority.

- It contains some hard-wired, in-memory users.




