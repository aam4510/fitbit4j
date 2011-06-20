This repository hosts the public Fitbit Java Client API and Examples project.

The Fitbit API Java Client library (Fitbit4J) is a complete solution to access all Fitbit API endpoints from your Java application. We have detailed walkthrough and best practice of using it on the Fitbit API wiki. http://wiki.fitbit.com

Source code for the Fitbit4J library available under the LGPL license, along with the complete source code for the Fitbit Example Java Application. You are welcome to pull the code, fork it and customize it for your own projects, and if you think you have changes applicable to the wider Fitbit API developer community, let us know, and we will look at pulling those changes back into the main repository. Our hope is to be able to respond to developer requests for bug fixes and feature enhancements more quickly.

The Fitbit Java client uses a significant amount of code from Twitter4J, the Java client for Twitter. If you have already used Twitter4J or any other client of an API service using OAuth authentication, your authentication code and flow using the Fitbit client will be similar.


# Fitbit4J Required Open-Source Libraries #

In addition to the standard Java runtime library, the Fitbit API Java Client library depends on the following third-party libraries:

Apache Commons Logging 1.1.1
Joda Time Library 1.5
JSON Java Library


* Note.* If you're using maven or Ivy, you can check out the gh-pages branch of the repository, where we are hosting a maven repository for the project.

# Fitbit Example Java Application #

You can find example of config file in /fitbit4j-example-client/src/main/resources/config.properties.example.
You should rename it to config.properties and change properties if needed.



# See also #

* http://wiki.fitbit.com/display/API/Fitbit-API-Java-Client
* http://wiki.fitbit.com/display/API/API-Client-Reference-App