package security.utils;

import io.quarkus.arc.Arc;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

import java.util.Set;

@ApplicationScoped
public class DynamoDbRepository {

    private final DynamoDbAsyncTable<User> table;

    public DynamoDbRepository(DynamoDbEnhancedAsyncClient enhancedDynamoClient) {
        TableSchema<User> tableSchema = TableSchema.fromBean(User.class);
        this.table = enhancedDynamoClient.table("Table", tableSchema);
    }

    public Uni<User> getUser() {
        Log.info("get user ////////////////////");
        return Uni.createFrom().completionStage(
                () -> {
                    Log.info("/////////// get item");
                    return table.getItem(r -> {
                        Log.info("////// get key");
                        r.key(k -> {
                            Log.info("/////// get email");
                            k.partitionValue("jakub@gmail.com");
                        });
                    });
                }
        ).invoke(r -> Log.info("//////// after " + Arc.container().requestContext().isActive() + " user " + r));
//        User user = new User();
//        user.setEmail("jakub@gmail.com");
//        user.setRoles(Set.of("admin"));
//        return Uni.createFrom().item(user);
    }

    public Uni<Void> saveUser() {
        User user = new User();
        user.setEmail("jakub@gmail.com");
        user.setRoles(Set.of("admin"));

        return Uni.createFrom().completionStage(() ->
                table.putItem(PutItemEnhancedRequest.builder(User.class).item(user).build())
        ).replaceWithVoid();
    }
}

