package security.security;

import io.quarkus.arc.Arc;
import io.quarkus.logging.Log;
import io.quarkus.security.identity.AuthenticationRequestContext;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.security.identity.SecurityIdentityAugmentor;
import io.quarkus.security.runtime.QuarkusSecurityIdentity;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import security.utils.TestService;
import security.utils.User;

import java.util.Set;

@ApplicationScoped
public class TestAugmentor implements SecurityIdentityAugmentor {

    private final TestService testService;

    @Inject
    public TestAugmentor(TestService testService) {
        this.testService = testService;
    }

    /** A) Below version of augment works WITH DynamoDb call and it throws ContextNotActiveException **/
    @Override
    public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context) {
        var vertxCtx = Vertx.currentContext();
        return testService.getUser()
            .emitOn(runnable -> vertxCtx.runOnContext(unused -> runnable.run()))
            .map(user -> {
                if (user == null) {
                    return identity;
                }
                QuarkusSecurityIdentity.Builder builder = QuarkusSecurityIdentity.builder(identity);
                builder.addRoles(user.getRoles());
                Log.info("///////////// in augmentor is active " + Arc.container().requestContext().isActive());
                return builder.build();
            });
    }

    /** B) Below version of augment works without DynamoDb call, and it allows to get access to GET /hello endpoint **/
//    @Override
//    public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context) {
//        return Uni.createFrom().item(() ->
//                QuarkusSecurityIdentity.builder(identity)
//                        .addRoles(Set.of("admin"))
//                        .build()
//        );
//    }

    /** C) Below version of augment works without DynamoDb call, and it does not allow to get access to GET /hello endpoint **/
//    @Override
//    public Uni<SecurityIdentity> augment(SecurityIdentity identity, AuthenticationRequestContext context) {
//        return Uni.createFrom().item(() ->
//            QuarkusSecurityIdentity.builder(identity)
//                .addRoles(Set.of("user"))
//                .build()
//        );
//    }
}
