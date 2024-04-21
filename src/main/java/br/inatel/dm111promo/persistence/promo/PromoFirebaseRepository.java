package br.inatel.dm111promo.persistence.promo;

import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class PromoFirebaseRepository implements PromoRepository {

    private static final String COLLECTION_NAME = "promo";

    private final Firestore firestore;

    public PromoFirebaseRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public void save(Promo promo) {
        firestore.collection(COLLECTION_NAME)
                .document(promo.getId())
                .set(promo);
    }

    @Override
    public List<Promo> findAll() throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .parallelStream()
                .map(promo -> promo.toObject(Promo.class))                
                .toList();
    }

    @Override
    public Optional<Promo> findById(String id) throws ExecutionException, InterruptedException {
        var promo = firestore.collection(COLLECTION_NAME)
                .document(id)
                .get()
                .get()
                .toObject(Promo.class);

        return Optional.ofNullable(promo);
    }  
    
    @Override
    public List<Promo> findAllByUserId(String userId) throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .parallelStream()
                .map(promo -> promo.toObject(Promo.class))
//                .filter(promo -> promo.getUserId().equals(userId))
                .toList();
    }

    @Override
    public void delete(String id) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }

    @Override
    public void update(Promo promo) {
        save(promo);
    }
}
