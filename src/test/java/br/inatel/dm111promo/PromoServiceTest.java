package br.inatel.dm111promo;

import br.inatel.dm111promo.api.core.ApiException;
import br.inatel.dm111promo.api.promo.service.PromoService;
import br.inatel.dm111promo.persistence.product.Product;
import br.inatel.dm111promo.persistence.product.ProductRepository;
import br.inatel.dm111promo.persistence.promo.PromoProduct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PromoServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private PromoService service;

    @Test
    void checkSplContainsPromoProductIdThenReturnTrueIfYes() throws ExecutionException, InterruptedException, ApiException {
        // given
        PromoProduct promoProduct = buildPromo();
        promoProduct.setId("splProductId");
        List<String> list = buildSplProducId();

        // when
        var result = service.checkSplContainsPromoProductId(promoProduct, list);

        // then
        Assertions.assertTrue(result);
    }

    @Test
    void checkSplContainsPromoProductIdThenReturnFalseIfNo() throws ExecutionException, InterruptedException, ApiException {
        // given
        PromoProduct promoProduct = buildPromo();
        List<String> list = buildSplProducId();

        // when
        var result = service.checkSplContainsPromoProductId(promoProduct, list);

        // then
        Assertions.assertFalse(result);
    }

    @Test
    void checkSplContainsPromoProductNameThenReturnTrueIfYes() throws ExecutionException, InterruptedException, ApiException {
        // given
        var expected = buildProduct1();
        var expectedSpl = buildSplProduct();
        var productId = "productId";
        var splProductId = "splProductId";

        when(repository.findById(productId)).thenReturn(Optional.of(expected));
        when(repository.findById(splProductId)).thenReturn(Optional.of(expectedSpl));

        // when
        var result = service.checkSplContainsPromoProductName(buildPromo(), buildSplProducId());

        // then
        Assertions.assertTrue(result);
    }


    @Test
    void checkSplContainsPromoProductNameThenReturnFalseIfNot() throws ExecutionException, InterruptedException, ApiException {
        // given
        var expected = buildProduct2();
        var expectedSpl = buildSplProduct();
        var productId = "productId";
        var splProductId = "splProductId";

        when(repository.findById(productId)).thenReturn(Optional.of(expected));
        when(repository.findById(splProductId)).thenReturn(Optional.of(expectedSpl));

        // when
        var result = service.checkSplContainsPromoProductName(buildPromo(), buildSplProducId());

        // then
        Assertions.assertFalse(result);
    }

    @Test
    void checkSplContainsPromoBrandNameThenReturnTrueIfYes() throws ApiException, ExecutionException, InterruptedException {
        // given
        var expected = buildProduct1();
        var expectedSpl = buildSplProduct();
        var productId = "productId";
        var splProductId = "splProductId";

        when(repository.findById(productId)).thenReturn(Optional.of(expected));
        when(repository.findById(splProductId)).thenReturn(Optional.of(expectedSpl));

        // when
        var result = service.checkSplContainsPromoBrandName(buildPromo(), buildSplProducId());

        // then
        Assertions.assertTrue(result);
    }

    @Test
    void checkSplContainsPromoBrandNameThenReturnFalseIfNo() throws ApiException, ExecutionException, InterruptedException {
        // given
        var expected = buildProduct2();
        var expectedSpl = buildSplProduct();
        var productId = "productId";
        var splProductId = "splProductId";

        when(repository.findById(productId)).thenReturn(Optional.of(expected));
        when(repository.findById(splProductId)).thenReturn(Optional.of(expectedSpl));

        // when
        var result = service.checkSplContainsPromoBrandName(buildPromo(), buildSplProducId());

        // then
        Assertions.assertFalse(result);
    }


    private PromoProduct buildPromo() {
        return new PromoProduct("productId", 99);
    }

    private List<String> buildSplProducId() {
        List<String> list = new ArrayList<>();
        list.add("splProductId");
        return list;
    }

    private Product buildProduct1() {
        return new Product("id", "name", "brand", "", 99, 99);
    }

    private Product buildProduct2() {
        return new Product("other", "other", "other", "", 99, 99);
    }

    private Product buildSplProduct() {
        return new Product("id", "nameSpl", "brand", "", 99, 99);
    }


}
