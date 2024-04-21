package br.inatel.dm111promo;

import br.inatel.dm111promo.api.core.ApiException;
import br.inatel.dm111promo.api.promo.service.PromoService;
import br.inatel.dm111promo.persistence.product.Product;
import br.inatel.dm111promo.persistence.product.ProductRepository;
import br.inatel.dm111promo.persistence.promo.Promo;
import br.inatel.dm111promo.persistence.promo.PromoProduct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
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
        PromoProduct promoProduct = buildPromoProduct();
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
        PromoProduct promoProduct = buildPromoProduct();
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
        var result = service.checkSplContainsPromoProductName(buildPromoProduct(), buildSplProducId());

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
        var result = service.checkSplContainsPromoProductName(buildPromoProduct(), buildSplProducId());

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
        var result = service.checkSplContainsPromoBrandName(buildPromoProduct(), buildSplProducId());

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
        var result = service.checkSplContainsPromoBrandName(buildPromoProduct(), buildSplProducId());

        // then
        Assertions.assertFalse(result);
    }

    @Test
    void checkPromoListHasValidPromoListDateStartingThenReturnValidList() throws ApiException {
        // given
        Instant today = Instant.now();
        Instant tomorrow = Instant.now().plus(Duration.ofDays(1));
        Date dToday = Date.from(today);
        Date dTomorrow = Date.from(tomorrow);
        String starting = new SimpleDateFormat("dd/MM/yyyy").format(dToday); // start today
        String expiration = new SimpleDateFormat("dd/MM/yyyy").format(dTomorrow); // expiration tomorrow
        List<String> userSplProducts = List.of("productId");
        List<Promo> promoList = buildPromoList("productId", "name", starting, expiration);

        // when
        var result = service.getUserPromoList(userSplProducts, promoList);

        // then
        Assertions.assertEquals(promoList.size(), result.size());

    }

    @Test
    void checkPromoListNotHasValidPromoListDateStartingThenReturnEmptyList() throws ApiException {
        // given
        Instant tomorrow = Instant.now().plus(Duration.ofDays(1));
        Date dTomorrow = Date.from(tomorrow);
        String starting = new SimpleDateFormat("dd/MM/yyyy").format(dTomorrow); // start tomorrow
        String expiration = new SimpleDateFormat("dd/MM/yyyy").format(dTomorrow); // expiration tomorrow
        List<String> userSplProducts = List.of("productId");
        List<Promo> promoList = buildPromoList("productId", "name", starting, expiration);

        // when
        var result = service.getUserPromoList(userSplProducts, promoList);

        // then
        Assertions.assertEquals(0, result.size());

    }

    @Test
    void checkPromoListNotHasValidPromoListDateExpirationThenReturnEmptyist() throws ApiException {
        // given
        Instant yesterday = Instant.now().minus(Duration.ofDays(1));
        Date dYesterday = Date.from(yesterday);
        String starting = new SimpleDateFormat("dd/MM/yyyy").format(dYesterday); // start yesterday
        String expiration = new SimpleDateFormat("dd/MM/yyyy").format(dYesterday); // expiration yesterday
        List<String> userSplProducts = List.of("productId");
        List<Promo> promoList = buildPromoList("productId", "name", starting, expiration);

        // when
        var result = service.getUserPromoList(userSplProducts, promoList);

        // then
        Assertions.assertEquals(0, result.size());

    }

    private Promo buildPromo(String id, String name, String starting, String expiration) {
        return new Promo(id, name, starting, expiration, buildPromoProductList());
    }

    private List<Promo> buildPromoList(String id, String name, String starting, String expiration) {
        return List.of(buildPromo(id, name, starting, expiration));
    }

    private PromoProduct buildPromoProduct() {
        return new PromoProduct("productId", 99);
    }

    private List<PromoProduct> buildPromoProductList() {
        return List.of(buildPromoProduct());
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
