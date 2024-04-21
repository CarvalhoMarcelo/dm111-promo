package br.inatel.dm111promo.persistence.supermarketlist;

import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class SuperMarketListFirebaseRepository implements SuperMarketListRepository{

    private static final String COLLECTION_NAME = "supermarket_list";

    private final Firestore firestore;

    public SuperMarketListFirebaseRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public List<SuperMarketList> findAllByUserId(String userId) throws ExecutionException, InterruptedException {
        return firestore.collection(COLLECTION_NAME)
                .get()
                .get()
                .getDocuments()
                .parallelStream()
                .map(spl -> spl.toObject(SuperMarketList.class))
                .filter(spl -> spl.getUserId().equals(userId))
                .toList();
    }

}
