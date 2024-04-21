package br.inatel.dm111promo.persistence.product;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface ProductRepository {

    Optional<Product> findById(String id) throws ExecutionException, InterruptedException;

}
