package security.security;

import io.netty.util.concurrent.CompleteFuture;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import security.utils.User;

import java.util.Set;

@ApplicationScoped
public class Other {

    @ActivateRequestContext
    public Uni<User> getUser() {
        User user = new User();
        user.setEmail("jakub@gmail.com");
        user.setRoles(Set.of("admin"));
        return Uni.createFrom().completionStage(() -> null);
    }

}
