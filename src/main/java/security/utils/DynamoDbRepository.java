package security.utils;

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
        return Uni.createFrom().completionStage(
                () -> table.getItem(r -> r.key(k -> k.partitionValue("jakub@gmail.com")))
        );
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

