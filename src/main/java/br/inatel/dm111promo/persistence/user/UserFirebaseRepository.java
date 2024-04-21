package br.inatel.dm111promo.persistence.user;

import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class UserFirebaseRepository {

    private static final String COLLECTION_NAME = "users";

    private final Firestore firestore;

    public UserFirebaseRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public Optional<User> findById(String id) throws ExecutionException, InterruptedException {
        var user = firestore.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .get()
                .toObject(User.class);

        return Optional.ofNullable(user);
    }

    public Optional<User> findByEmail(String email) throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .stream()
                .map(user -> user.toObject(User.class))
                .filter(user -> user.getEmail().toLowerCase().equals(email.toLowerCase()))
                .findFirst();
    }
}
