package com.spyder.webapp;


import java.io.IOException;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;







public class Main {

  private static final Log LOG = LogFactory.getLog(Main.class);
  static {
      BasicConfigurator.configure();
  }

  private static URI getBaseURI() {
    return UriBuilder.fromUri("http://localhost/").port(8080).build();
  }

  static final URI BASE_URI = getBaseURI();

  static HttpServer startServer() {
    ResourceConfig rc = ResourceConfig.forApplication(new ExampleApp());
    return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
  }

  public static void main(String[] args) throws IOException {
    System.out.println("Starting grizzly...");
    HttpServer httpServer = startServer();
    System.out.printf("Jersey app started with WADL available at %sapplication.wadl%n", BASE_URI);
    System.out.println("Hit enter to stop it...");
    System.in.read();
    httpServer.shutdownNow();
  }
}