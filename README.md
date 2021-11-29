# REST APIs to synchronize temperature data

Create an API, to be used by a client connected to a temperature sensor. The sensor sends to the
client a continuous stream of temperature data that must be sent to the API. It's possible for the
client not to have an internet connection in which case, the data is stored locally and synchronized,
in bulk, to the API as soon as the network connection is established. The client displays this
information in a chart where data can be seen per hour or daily, however since the client has
resource limitations, it relies on the API to aggregate data.


## How to build
`mvn clean install`

## How to run
`java -Dspring.config.location=application.properties -jar temperatureapis-0.0.1-SNAPSHOT.jar`

### Run Profiles
There are four profiles defined in two categories as below:
* Group 1 - App Profile:
    - `prod`: turn of both debug mode and log in to the console
    - `dev`: turn on both debug mode and log in to the console

* Group 2 - Repository Profile:
    - `h2Repo`: use h2 in-memory database as repository to mimic RDBMS 
    - `inMemoryRepo`: use In-memory structure as repository

_Note: **only one profile from each category** must be chosen **at the same time**_

These profiles can be specified in the line 4 of application.properties file.   
**Possible choices:**
```
1. spring.profiles.active=prod,h2Repo
2. spring.profiles.active=prod,inMemoryRepo
3. spring.profiles.active=dev,h2Repo
4. spring.profiles.active=dev,inMemoryRepo
```

## Useful Links
### Health check
[http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

### H2 console
* [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
* database url: `jdbc:h2:mem:testdb`
* database username: `sa`

_**Note: Only with `h2Repo` profile can be accessed**_

### API Documentation
[http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/)

### Sample Requests
Sample [postman](https://www.postman.com/downloads/) collection can be found here:
[temperature apis.postman_collection](temperature apis.postman_collection)

### Tools/Technologies
* Java 11
* Spring
* H2
* JUnit 5
* Swagger

### Github Repository Link
[https://github.com/ghasemel/temperatureApis](https://github.com/ghasemel/temperatureApis)