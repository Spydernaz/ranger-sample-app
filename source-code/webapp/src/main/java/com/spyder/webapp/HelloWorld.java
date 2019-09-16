
package com.spyder.webapp;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;







@Path("/")
public final class HelloWorld {

    private static final Log LOG = LogFactory.getLog(Main.class);
    private RangerWebAuthorizer authorizer = new RangerWebAuthorizer();
    static {
        BasicConfigurator.configure();
    }

    {
        authorizer.init();
    }
    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    @GET
    @Path("/multiply/{a}/{b}")
    @Produces(MediaType.TEXT_PLAIN)
    public int mulitiply(@PathParam("a") final int a,
            @PathParam("b") final int b) {

        return a * b;
    }

    //~ ----------------------------------------------------------------------------------------------------------------

    @GET
    @Path("/hello/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHello(@PathParam("name") final String name) {

        return "Hello " + name + "!";
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHelloWorld() {
        Set<String> userGroups = new HashSet<String>();
        if (authorizer.authorize("/hello", "get", "admin", userGroups)){
            return "Hello World!";
        }
        else {
            return "Unauthorized: 403!";
        }
    }
}
