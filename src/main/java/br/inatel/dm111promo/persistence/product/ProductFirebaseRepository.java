package br.inatel.dm111promo.persistence.product;

import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class ProductFirebaseRepository implements ProductRepository{

    private static final String COLLECTION_NAME = "products";

    private final Firestore firestore;

    public ProductFirebaseRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public Optional<Product> findById(String id) throws ExecutionException, InterruptedException {
        var product = firestore.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .get()
                .toObject(Product.class);
        return Optional.ofNullable(product);
    }

}
