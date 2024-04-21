package br.inatel.dm111promo.persistence.promo;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface PromoRepository {

    void save(Promo promo);

    List<Promo> findAll() throws ExecutionException, InterruptedException;

    Optional<Promo> findById(String id) throws ExecutionException, InterruptedException;

    List<Promo> findAllByUserId(String userId) throws ExecutionException, InterruptedException;

    void delete(String id) throws ExecutionException, InterruptedException;

    void update(Promo promo);
}
