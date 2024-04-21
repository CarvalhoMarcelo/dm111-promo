package br.inatel.dm111promo;

import br.inatel.dm111promo.api.core.ApiException;
import br.inatel.dm111promo.api.promo.controller.PromoController;
import br.inatel.dm111promo.api.promo.service.PromoService;
import br.inatel.dm111promo.persistence.promo.Promo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PromoControllerTest {

	@Mock
	private PromoService service;

	@InjectMocks
	private PromoController controller;

	@Test
	void getAllPromoShouldReturnAListOfAllPromo() throws ApiException {
		// given
		var expected = buildPromoList();
		BDDMockito.given(service.searchAll()).willReturn(expected);

		// when
		var result = controller.getAll();

		// then
		Assertions.assertEquals(expected, result.getBody());
	}

	@Test
	void getPromoThrowsApiExceptionDueToSameError() throws ApiException {
		// given
		BDDMockito.given(service.searchAll()).willThrow(ApiException.class);

		// when
		assertThrows(ApiException.class, () -> controller.getAll());
	}

	private List<Promo> buildPromoList() {
		return List.of(buildPromo());
	}

	private Promo buildPromo() {
		return new Promo("id", "name","starting","expiration",new ArrayList<>());
	}




}
