package br.inatel.dm111promo.api.promo.controller;

import br.inatel.dm111promo.api.core.ApiException;
import br.inatel.dm111promo.api.promo.PromoRequest;
import br.inatel.dm111promo.api.promo.PromoResponseByUser;
import br.inatel.dm111promo.api.promo.service.PromoService;
import br.inatel.dm111promo.persistence.promo.Promo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//http://localhost:8081/dm111/promo
@RestController
@RequestMapping("/dm111")
public class PromoController {

    private final PromoService service;

    public PromoController(PromoService service) {
        this.service = service;
    }

    @GetMapping("/promo")
    public ResponseEntity<List<Promo>> getAll() throws ApiException {
        var list = service.searchAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/promo/users/{userId}")
    public ResponseEntity<List<PromoResponseByUser>> getAllByUser(@PathVariable("userId") String userId) throws ApiException {
        var list = service.searchAllByUser(userId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/promo/{id}")
    public ResponseEntity<Promo> getById(@PathVariable("id") String id)
            throws ApiException {
        var list = service.searchById(id);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/promo")
    public ResponseEntity<Promo> postPromo(@RequestBody PromoRequest request) throws ApiException {
        var list = service.createList(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(list);
    }

    @PutMapping("/promo/{id}")
    public ResponseEntity<Promo> putPromo(@PathVariable("id") String id,
                                          @RequestBody PromoRequest request) throws ApiException {
        var list = service.updateList(id, request);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/promo/{id}")
    public ResponseEntity<?> deletePromo(@PathVariable("id") String id) throws ApiException {
        service.removeList(id);
        return ResponseEntity.noContent().build();
    }
}
