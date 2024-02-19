# Project Details
Project name: Domingo and Zach's API server

Team members: Domingo Viesca (Dviesca) and Zach Stellato (Zstellat)
Total Estimated time on project: 25-30 hours

Git repo link: https://github.com/cs0320-s24/server-zstellat-dviesca.git


This project builds on the first sprint where we made a CSV parser that took in a csv and parsed its rows into 
whatever form a class outlined. In this server it was used to parse CSV's that were already on the back end, as well
as parsing that is queried from the US census website. For the Census: we query the census API, deserialize the JSON data,
extract what we need with java handlers (state codes, county codes, broadband statistics...), and serialise it to then
show on our own API that our users see. This project allows Users to access our server directly to more easily query
census information, or for developers our project allows them to easily mix and match or modify our class implementations
to suit their own needs.

# Design Choices

A high level view of what goes on in our classes is the following: The server main method is run to setup a spark server
which then pass in instances of the load, search, view, and broadband handlers. These handlers take in the queries of
server users and fill in data which gets manipulated to fulfil its respective purpose. Load stages/parses a CSV, view
displays the staged CSV as JSON, and search displays only the relevant row that contained a searched for object in the 
staged CSV. Broadband works slightly different as it also takes in user queryparameters, but on the back end it also has
to itself query the US census API to first identify the state number, then county number, to then make a final query
that includes the broadband information that will be parsed and presented to the front facing user. To prevent the 
equivalent of a DDOS attack on the government API we made a cache wrapper class that stores the data from these queries
for a short period in the case many users/tests use it.


**Security**:
A possible weak point of security for an API is that a user might maliciously try to access private
files that are stored on the backend through directory traversal. To prevent this we firstly append the input for 
file requests on load to a set filepath that starts at an allowed directory. This may be bypassed by use of '../' to
access parent directories. So secondly we take the full filepath and then take the canonic filepath of the file
that it points to, confirming that the allowed directory is included and thus only its contents accessed through 
subsequent parts of the code.


We also relied on the strengths of our CSV parser which instead of directly parsing to a list<list<x>> instead
returns a parsed datapacket object which can be shared through different classes/strategy objects to share states
as well as store important information such as if a csv has a header or not. For the same reason we also optionally allow
the user to specify if the CSV being loaded has headers or not through optional query parameters.

# Errors/Bugs
We had a tough time implementing the proxyClass for the cache mostly because of our unfamiliarity with the guava 
library, so it is very possible that there are bugs or errors that we oversaw out of not fully understanding its 
points of failure.

# Tests
ChatGPT was used to formulate ideas of test cases.
For Integration Tests - Make sure Maven is set up in your project, Run mvn test -Dtest=*IntegrationTest in 
the main folder. These tests check if the server correctly handles different web requests. Essentially, it's the same as
trying a bunch of requests in your browser.

For Unit Tests - Run mvn test -Dtest=*UnitTest from the main project folder. The Unit tests check new code parts like 
message handling or caching, without needing the whole server. They are helpful to make sure that when developers use
our code, it works, even in a different java app.

Mocking - We use mocking for tests to avoid too many real API calls. We can use dependency injection


# How to...

**To run the parser CSV section of the server:**
The user must firstly access the server API  by first having the server.java main run and copying the url that 
gets return to the terminal into a web browser 
The user should send queries to either Load (with the relative filepath of the file to be parsed on the backed + optionally
'true' or 'false' for the hasHeader parameter.
as a parameter), view (no parameters but load must have been done beforehand), or search (by entering the item searched in string form as a parameter, and optionally a column identifier as a string or integer).

**To run the census access section of the server**: is the same as the CSV section up to the server being put online. 
Then the user must query the API by adding /broadband to the url and then including the  sate and county names that are being assessed for broadband as parameters.

**The syntax for parameters is the following:** 
?{name of parameter}={input} *& {optionally '&' followed same format for subsequent parameters}