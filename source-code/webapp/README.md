# Runing

Example JAX-RS (Java Api for RESTful Web Services) Web Application. Application uses standart JAX-RS annotions like @Path, @GET, @Produces.

Yo may run application:

```sh
mvn jetty:run
```

After running you can type in your browser's address bar:

```
http://localhost:8080/example-jaxrs-webapp/hello
```

or

```
http://localhost:8080/example-jaxrs-webapp/hello/FIRAT
```

or

```
http://localhost:8080/example-jaxrs-webapp/multiply/6/12
```

You can run tests using following commands

```
mvn test
```

# Version History

- 1.1: Servlet Spec 3.0 Annotation Based Version
- 1.0: Servlet Spec 2.5



