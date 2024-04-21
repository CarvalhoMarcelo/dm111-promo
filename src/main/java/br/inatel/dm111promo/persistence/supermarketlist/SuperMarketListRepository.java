package br.inatel.dm111promo.persistence.supermarketlist;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface SuperMarketListRepository {

    List<SuperMarketList> findAllByUserId(String userId) throws ExecutionException, InterruptedException;
}
