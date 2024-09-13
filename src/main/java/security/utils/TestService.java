package security.utils;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;

import java.util.Set;

@ApplicationScoped
public class TestService {

    private final DynamoDbRepository repository;

    @Inject
    public TestService(DynamoDbRepository repository) {
        this.repository = repository;
    }

//    @ActivateRequestContext
    public Uni<User> getUser() {
        return repository.getUser();
    }

    public Uni<Void> saveUser() {
        return repository.saveUser();
    }
}
