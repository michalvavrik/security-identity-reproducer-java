package security.controllers;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import security.utils.TestService;
import security.utils.User;

@Path("/hello")
public class TestController {

    private final TestService testService;

    @Inject
    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GET
    @RolesAllowed("admin")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello1() {
        return "Hello";
    }

    @GET
    @Path("/test-repo")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<User> testRepository() {
        return testService.getUser();
    }

    @POST
    @Path("/save")
    public Uni<Void> saveUser() {
        return testService.saveUser();
    }
}
