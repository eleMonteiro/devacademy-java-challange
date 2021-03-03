package br.com.casamagalhaes.workshop.desafio.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.casamagalhaes.workshop.desafio.model.Item;
import br.com.casamagalhaes.workshop.desafio.repository.ItemPedidoRepository;

@Service
public class ItemService {

    @Autowired
    private ItemPedidoRepository repository;

    public Item save(Long id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Item save(Item item) {
        return repository.saveAndFlush(item);
    }

    public Item update(Long id, Item item) {
        if (repository.existsById(id)) {
            return repository.saveAndFlush(item);
        } else
            throw new EntityNotFoundException("Pedido id: " + item.getPedido());
    }
}
