package br.com.casamagalhaes.workshop.desafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.casamagalhaes.workshop.desafio.model.Item;

@Repository
public interface ItemPedidoRepository  extends JpaRepository<Item, Long> {

}
