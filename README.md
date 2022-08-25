# NASA API assignment

## How to use
1. Make sure you have Java 17 installed.
2. Run on the terminal: `mvn spring-boot:run`
   1. Alternatively, you can just run the AsteroidApi class with the default configuration on your IDE.
3. Easy to test with Swagger OpenAPI exposed in: `http://localhost:9000/swagger-ui/index.html`
    1. Endpoints:
        1. "GET api/v1/asteroid/closest-asteroids" display the 10 closest asteroids between two dates.
        2. "GET api/v1/asteroid/largest-asteroid" display the largest asteroid on a given year.

## Task description
Imagine a customer has asked you to build a tool that can provide data on nearby asteroids using NASA Asteroids API. The tool is required to have the following features:

1. Show 10 asteroids that passed the closest to Earth between two user-provided dates.
2. Show characteristics of the largest asteroid (estimated diameter) passing Earth during a user-provided year.

Using Java, implement a RESTful API service to handle such queries. The service should employ some kind of caching to avoid extra external API calls.

Some general things to consider:

* Database modeling. Should data be persisted in the database?
* API design. How should it be presented in the API? Will it be easy to include more queries or extend the current ones to cater for new feature requests?
* Would it be easier for other developers to get up-and-running if you use docker?
* Testing approach. How would you approach it in your implementation?

Final result should consist of:

1. Source code with instructions on how to run it in a git repository we can access (Github, Bitbucket etc.)
2. A service deployed to a cloud provider of your choice using IaC approach. 
   1. This is optional — only do it if you would like to demonstrate your DevOps skills.
