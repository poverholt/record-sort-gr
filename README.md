# record-sort-gr

A system to parse and sort a set of records.

- [Requirements](https://github.com/poverholt/record-sort-gr/blob/main/README.md##Requirements)
- [API](https://github.com/poverholt/record-sort-gr/blob/main/README.md##API)
- [Installation](https://github.com/poverholt/record-sort-gr/blob/main/README.md##Installation)
- [Usage](https://github.com/poverholt/record-sort-gr/blob/main/README.md##Usage)
- [Testing](https://github.com/poverholt/record-sort-gr/blob/main/README.md##Testing)

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
* POST to /records will be rejected if...
  * Gender is not some non-blank substring of "female" or "male", case-insensitive.
    * m, M, male, MALE, f, F, FEM, female are all valid.
  * Birthdate does not meet required MM/DD/YYYY.
  * LastName, FirstName or FavoriteColor are missing or white-space only.
* The following endpoints were added to help with testing...
  * DELETE /records - reset the records list to empty
  * GET / - returns a test string
  * ANY /request - returns the request for debugging. The response body is  HTML.
* Duplicate records are accepted by the POST to /records.

### Possible Improvements

* Avoid genuine duplicates by including a unique ID field or adding a UUID during record creation,
although the latter still does not avoid the same person enter their details twice.
* Make response to POST /records more consistent.
* Use Swagger to document the API.

## Installation

The server and its test client server are already installed on Heroku...
* https://record-sort-gr.herokuapp.com/
* https://record-sort-gr-client.herokuapp.com/

The server and test client projects are available on GitHub for download...
* https://github.com/poverholt/record-sort-gr/
* https://github.com/poverholt/record-sort-gr-client/

## Usage

Visit the Heroku sites mentioned above to use the existing installations.

To use the downloaded project...
* lein run [args?]
* lein repl, (-main [args?])
* $java $JVM_OPTS -cp target/uberjar/record-sort-gr.jar clojure.main -m record-sort-gr.core [args?]
* $java $JVM_OPTS -cp <path-to-jar> clojure.main -m record-sort-gr.core [args?] => if you moved the jar
  * See [Testing](##Testing) for ways to use the server

### Options

Without arguments, the server defaults to port 8000.
You can override the default server port in two ways...
* PORT environment variable
* As an argument, for example, *lein run 5000*

It is recommended not to override defaults. This will work best both locally and on Heroku.

A port of -1 is a special case that, instead of running the server, will print assignment step 1
output to \*out\*.

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
One environment targets http://localhost:8000 and one targets the Heroku record server installation.
To use these, you must import them into a Postman installation. Note that the localhost
environment can only be used with the desktop installation of Postman.

### Test Client

A test client is available as a GitHub project and as a Heroku installation...
* https://github.com/poverholt/record-sort-gr-client/
* https://record-sort-gr-client.herokuapp.com/

### Possible Improvements

Test for performance with large data files.
Test with slow or broken networks or simulations thereof.