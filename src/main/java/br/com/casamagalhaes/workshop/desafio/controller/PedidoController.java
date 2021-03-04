package br.com.casamagalhaes.workshop.desafio.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.casamagalhaes.workshop.desafio.exception.Exception;
import br.com.casamagalhaes.workshop.desafio.model.Item;
import br.com.casamagalhaes.workshop.desafio.model.Pedido;
import br.com.casamagalhaes.workshop.desafio.service.ItemService;
import br.com.casamagalhaes.workshop.desafio.service.PedidoService;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Pedido Venda")
@RestController
@RequestMapping(path = "/api/v1/pedidos")
public class PedidoController extends Exception {

    @Autowired
    private PedidoService service;

    @Autowired
    private ItemService itemService;

    @GetMapping({ "/", "" })
    public Page<Pedido> findAll(@RequestParam Integer pagina, @RequestParam Integer tamanho) {
        return service.findAll(pagina, tamanho);
    }

    @GetMapping({ "/{id}" })
    public Pedido findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping({ "/", "" })
    @ResponseStatus(code = HttpStatus.CREATED)
    public Pedido save(@Valid @RequestBody Pedido pedido) {
        Pedido pedidoNovo = service.save(pedido);

        for (Item element : pedidoNovo.getItens()) {
            element.setPedido(pedidoNovo);
            itemService.update(element.getId(), element);
        }
        
        return pedidoNovo;
    }

    @PostMapping({ "/{id}/status", "{id}/status" })
    @ResponseStatus(code = HttpStatus.OK)
    public Pedido saveStatus(@PathVariable Long id, @RequestBody Pedido pedido) {
        return service.saveStatus(id, pedido);
    }

    @PutMapping({ "/{id}" })
    @ResponseStatus(code = HttpStatus.OK)
    public Pedido update(@PathVariable Long id, @RequestBody Pedido pedido) {
        return service.update(id, pedido);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
