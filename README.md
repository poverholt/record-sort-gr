# record-sort-gr

A system to parse and sort a set of records.

- [Requirements](##Requirements)
- [API](##API)
- [Installation](##Installation)
- [Usage](##Usage)
- [Testing](##Testing)

## Requirements

### Step 1 - Build a system to parse and sort a set of records

Create a command line app that takes as input a file with a set of records in one of three formats
described below, and outputs (to the screen) the set of records sorted in one of three ways.

#### Input

A record consists of the following 5 fields: last name, first name, gender, date of birth and favorite
color. The input is 3 files, each containing records stored in a different format. You may generate
these files yourself, and you can make certain assumptions if it makes solving your problem easier.

* The pipe-delimited file lists each record as follows: 
  > LastName | FirstName | Gender | FavoriteColor | DateOfBirth
* The comma-delimited file looks like this: 
  > LastName, FirstName, Gender, FavoriteColor, DateOfBirth
* The space-delimited file looks like this: 
  > LastName FirstName Gender FavoriteColor DateOfBirth

You may assume that the delimiters (commas, pipes and spaces) do not appear anywhere in the
data values themselves. Write a program in a language of your choice to read in records from these
files and combine them into a single set of records.

#### Output

Create and display 3 different views of the data you read in:

* Output 1 – sorted by gender (females before males) then by last name ascending.
* Output 2 – sorted by birth date, ascending.
* Output 3 – sorted by last name, descending.

Display dates in the format M/D/YYYY.

### Step 2 - Build a REST API to access your system

Tests for this section are required as well.

**Within the same code base,** build a standalone REST API with the following endpoints:

* POST /records - Post a single data line in any of the 3 formats supported by your existing code
* GET /records/gender - returns records sorted by gender
* GET /records/birthdate - returns records sorted by birthdate
* GET /records/name - returns records sorted by name

It's your choice how you render the output from these endpoints as long as it well structured data.
These endpoints should return JSON.

To keep it simple, don't worry about using a persistent datastore.

## API

Here are some notes on the final API, in addition to those specified in the requirements above.

* The returned JSON-format records have the following keys: "lname", "fname", "gender", "color", "bdate".
* POST to /records will respond with the created record in JSON format if possible, along with field
"success" or "error", for successful and failed attempts respectively. The success value is a boolean
The error value can be a string or a vector of strings, when multiple errors are reported.
* The following endpoints were added to help with testing...
  * DELETE /records - reset the records list to empty
  * GET / - returns a test string
  * ANY /request - returns the request for debugging. The response body is  HTML.
* Duplicate records are accepted by the POST to /records.

### Possible Improvements

* Avoid genuine duplicates by including a unique ID field or adding a UUID during record creation,
although the latter still does not avoid the same person enter there details twice.
* Make response to POST /records more consistent.
* Use Swagger to document the API.

## Installation

The server and its test client server are already installed on Heroku...
* TODO with port #. API
* TODO with port #. client

The server and test client projects are available on GitHub for download...
* https://github.com/poverholt/record-sort-gr
* https://github.com/poverholt/record-sort-gr-client

## Usage

Visit the Herokku sites mentioned above to use the existing installations.

To use the download project...
* lein run - will print step 1 output to *out*
* lein run 8000 - will start the Record Sort GR server at http://localhost:8000
  * lein run port - any port can be used, but the test client will target port 8000
  * See [Testing](##Testing) for ways to use the server
* TODO: $ java -jar blah-0.1.0-standalone.jar [args]

## Testing

### Clojure.test

For automated Clojure testing, run with "lein test", each source file has a corresponding test file.
As much as possible, the testing and corner case testing is done in parse-test and sort-test.
This limits the testing needed elsewhere. Testing in other Clojure test files and in the Postman
API test files are as sparse as is reasonable.

### Postman API Testing

REST API testing with Postman is basic. It checks each endpoint can be reached and suitable data is
returned. It checks that an invalid endpoint and invalid record creation data gets a proper error.
It checks that the record count is always correct through a sequence of resetting the record list,
attempting to add invalid records, adding valid records, adding a duplicate record, then finally
resetting the record list again.

The Postman test collection and test environments files can be found at /test of the project.
One environment is for http://localhost:8000 and one is for heroku location. TODO.
To use these, you must import them into a Postman installation. Note that the localhost
environment can only be used with the desktop installation of Postman.

### Test Client

A test client is available as a GitHub project and as a heroku installation...
* https://github.com/poverholt/record-sort-gr-client
* TODO: heroku
