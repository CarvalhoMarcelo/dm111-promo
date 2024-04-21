package br.inatel.dm111promo.api.promo.service;

import br.inatel.dm111promo.api.core.ApiException;
import br.inatel.dm111promo.api.core.AppErrorCode;
import br.inatel.dm111promo.api.promo.PromoRequest;
import br.inatel.dm111promo.persistence.product.Product;
import br.inatel.dm111promo.persistence.product.ProductRepository;
import br.inatel.dm111promo.persistence.promo.Promo;
import br.inatel.dm111promo.persistence.promo.PromoProduct;
import br.inatel.dm111promo.persistence.promo.PromoRepository;
import br.inatel.dm111promo.persistence.supermarketlist.SuperMarketList;
import br.inatel.dm111promo.persistence.supermarketlist.SuperMarketListFirebaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class PromoService {

    private static final Logger log = LoggerFactory.getLogger(PromoService.class);

    private final PromoRepository promoRepository;
    private final ProductRepository productRepository;

    private final SuperMarketListFirebaseRepository splRepository;

    public PromoService(PromoRepository promoRepository, ProductRepository productRepository, SuperMarketListFirebaseRepository splRepository) {
        this.promoRepository = promoRepository;
        this.productRepository = productRepository;
        this.splRepository = splRepository;
    }

    public List<Promo> searchAll() throws ApiException {
        try {
            return promoRepository.findAll();
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new ApiException(AppErrorCode.PROMO_QUERY_ERROR);
        }
    }

    public List<Promo> searchAllByUser(String userId) throws ApiException {
        // build a list of all supermarket lists of the user
        List<String> userSplProducts = getUserSplProducts(userId);;

        // build the promo list with the relevant products for the user
        return getUserPromoList(userSplProducts, searchAll());
    }

    public Promo searchById(String id) throws ApiException {
        return retrievePromo(id);
    }

    public Promo createList(PromoRequest request) throws ApiException {
        validateRequestDates(request);

        var list = buildPromo(request);
        var allProductsAvailable = true;

        for (PromoProduct promoProduct: list.getProducts()) {
            try {
                if (productRepository.findById(promoProduct.getId()).isEmpty()) {
                    allProductsAvailable = false;
                    break;
                }
            } catch (ExecutionException | InterruptedException e) {
                throw new ApiException(AppErrorCode.PRODUCTS_QUERY_ERROR);
            }
        }

        if (allProductsAvailable) {
            promoRepository.save(list);
            return list;
        } else {
            throw new ApiException(AppErrorCode.PRODUCTS_NOT_FOUND);
        }
    }

    public Promo updateList(String id, PromoRequest request) throws ApiException {
        validateRequestDates(request);

        var list = retrievePromo(id);

        list.setName(request.name());
        list.setStarting(request.starting());
        list.setExpiration(request.expiration());
        list.setProducts(request.products());

        var allProductsAvailable = true;
        for (PromoProduct promoProduct: list.getProducts()) {
            try {
                if (productRepository.findById(promoProduct.getId()).isEmpty()) {
                    allProductsAvailable = false;
                    break;
                }
            } catch (ExecutionException | InterruptedException e) {
                throw new ApiException(AppErrorCode.PRODUCTS_QUERY_ERROR);
            }
        }

        if (allProductsAvailable) {
            promoRepository.update(list);
            return list;
        } else {
            throw new ApiException(AppErrorCode.PRODUCTS_NOT_FOUND);
        }

    }

    public void removeList(String id) throws ApiException {
        try {
            var promo = retrievePromo(id);
            if (Objects.nonNull(promo)) {
                promoRepository.delete(promo.getId());
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.PROMO_QUERY_ERROR);
        }
    }

    private Promo buildPromo(PromoRequest request) {
        var id = UUID.randomUUID().toString();
        return new Promo(id, request.name(), request.starting(), request.expiration(), request.products());
    }

    private Promo retrievePromo(String id) throws ApiException {
        try {
            return promoRepository.findById(id)
                    .orElseThrow(() -> new ApiException(AppErrorCode.PROMO_NOT_FOUND));
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.PROMO_QUERY_ERROR);
        }
    }

    // build a list of all supermarket lists of the user
    private List<String> getUserSplProducts(String userId) throws ApiException {
        List<String> userSplProducts = new ArrayList<>();
        try{
            List<SuperMarketList> superMarketList = splRepository.findAllByUserId(userId);
            if(!superMarketList.isEmpty()) {
                superMarketList.stream()
                        .flatMap(spl -> spl.getProducts().stream())
                        .forEach(userSplProducts::add);
            }
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new ApiException(AppErrorCode.PROMO_QUERY_ERROR);
        }

        return userSplProducts;
    }

    // retrieve only promo list from a valid date interval
    public List<Promo> getUserPromoList(List<String> userSplProducts, List<Promo> promoList) throws ApiException {
        List<Promo> userPromoList = new ArrayList<>();

        if(!promoList.isEmpty()) {
            Date actual_date = getDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            Date startDate = null;
            Date expDate = null;
            for(Promo promo : promoList) {
                startDate = getDate(promo.getStarting());
                expDate = getDate(promo.getExpiration());
                // filter only valid promo
                if(actual_date.compareTo(startDate) >= 0 && actual_date.compareTo(expDate) <= 0) {
                    List<PromoProduct> promoProductList = buildPromoProductList(promo,userSplProducts);
                    if(!promoProductList.isEmpty()){
                        userPromoList.add(buildPromo(promo, promoProductList));
                    }
                }
            }
        }
        return userPromoList;
    }

    private Promo buildPromo(Promo promo, List<PromoProduct> promoProductList) {
        return new Promo(
                promo.getId(),
                promo.getName(),
                promo.getStarting(),
                promo.getExpiration(),
                promoProductList);
    }

    // build the promo list for the user containing only the relevant products.
    private List<PromoProduct> buildPromoProductList(Promo promo, List<String> userSplProducts) throws ApiException {
        List<PromoProduct> promoProductList = new ArrayList<>();
        for(PromoProduct promoProduct : promo.getProducts()) {
            if(checkListVersusPromotion(promoProduct, userSplProducts)){
                promoProductList.add(new PromoProduct(promoProduct.getId(), promoProduct.getDiscount()));
            }
        }
        return promoProductList;
    }


    private Date getDate(String date) throws ApiException {
        try{
            return new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            throw new ApiException(AppErrorCode.FORMAT_DATE_INCORRECT);
        }
    }

    private void validateRequestDates(PromoRequest request) throws ApiException {
        getDate(request.starting());
        getDate(request.expiration());
    }

    private boolean checkListVersusPromotion(PromoProduct promoProduct, List<String> userSplProducts) throws ApiException {
        if(checkSplContainsPromoProductId(promoProduct, userSplProducts)){
            return true;
        } else {
            if(checkSplContainsPromoBrandName(promoProduct, userSplProducts)){
                return true;
            } else {
                return checkSplContainsPromoProductName(promoProduct, userSplProducts);
            }
        }
    }

    public boolean checkSplContainsPromoProductId(PromoProduct promoProduct, List<String> userSplProducts){
        return userSplProducts.contains(promoProduct.getId());
    }

    public boolean checkSplContainsPromoBrandName(PromoProduct promoProduct, List<String> userSplProducts) throws ApiException {
        try{
            String promoProductBrand = productRepository.findById(promoProduct.getId()).orElse(new Product()).getBrand();
            for(String splId : userSplProducts){
                String splProductBrand = productRepository.findById(splId).orElse(new Product()).getBrand();
                if(!splProductBrand.isEmpty() && !promoProductBrand.isEmpty() && splProductBrand.contains(promoProductBrand)) {
                    return true;
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.PRODUCTS_QUERY_ERROR);
        }
        return false;
    }

    public boolean checkSplContainsPromoProductName(PromoProduct promoProduct, List<String> userSplProducts) throws ApiException {
        try{
            String promoProductName = productRepository.findById(promoProduct.getId()).orElse(new Product()).getName();
            for(String splId : userSplProducts){
                String splProductName = productRepository.findById(splId).orElse(new Product()).getName();
                if(!splProductName.isEmpty() && !promoProductName.isEmpty() && splProductName.contains(promoProductName)) {
                    return true;
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.PRODUCTS_QUERY_ERROR);
        }
        return false;
    }



}
