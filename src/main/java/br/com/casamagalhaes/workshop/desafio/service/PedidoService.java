package br.com.casamagalhaes.workshop.desafio.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.casamagalhaes.workshop.desafio.enums.StatusPedido;
import br.com.casamagalhaes.workshop.desafio.model.Item;
import br.com.casamagalhaes.workshop.desafio.model.Pedido;
import br.com.casamagalhaes.workshop.desafio.repository.PedidoRepository;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    public Page<Pedido> findAll(Integer pagina, Integer tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return repository.findAll(pageable);
    }

    public Pedido findById(Long id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Pedido save(Pedido pedido) {
        if (pedido.getItens().isEmpty()) {
            throw new NullPointerException("pedido de venda sem produtos");
        }

        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setValorTotalProdutos(valorTotalDosProdutos(pedido.getItens()));
        pedido.setValorTotal(pedido.getValorTotalProdutos() + pedido.getTaxa());

        return repository.saveAndFlush(pedido);
    }

    public Pedido update(Long id, Pedido pedido) {
        if (repository.existsById(id)) {
            Pedido pedidoAntigo = findById(id);

            if (pedido.getStatus() != pedidoAntigo.getStatus()) {
                throw new UnsupportedOperationException("STATUS do pedido informado diferente do atual.");
            }

            if (id != pedido.getId()) {
                throw new UnsupportedOperationException("ID informado diferente do Pedido.");
            }
            
            if (pedido.getItens().isEmpty()) {
                throw new NullPointerException("pedido de venda sem produtos");
            }
            
            pedido.setValorTotalProdutos(valorTotalDosProdutos(pedido.getItens()));
            pedido.setValorTotal(pedido.getValorTotalProdutos() + pedido.getTaxa());

            return repository.saveAndFlush(pedido);
        } else
            throw new EntityNotFoundException("Pedido id: " + pedido.getId());

    }

    public void delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else
            throw new EntityNotFoundException("Pedido id: " + id);
    }

    public Pedido saveStatus(Long id, Pedido pedido) {
        if (repository.existsById(id)) {
            Pedido pedidoAtual = findById(id);

            if (pedido.getStatus().equals(StatusPedido.CANCELADO)
                    && !pedidoAtual.getStatus().equals(StatusPedido.EM_ROTA)
                    && !pedidoAtual.getStatus().equals(StatusPedido.ENTREGUE)
                    && !pedidoAtual.getStatus().equals(StatusPedido.CANCELADO))
                throw new UnsupportedOperationException(
                        "STATUS não pode ser alterado pois seu status atual não coresponde a EM ROTA, ENTREGUE ou CANCELADO.");

            if (pedido.getStatus().equals(StatusPedido.EM_ROTA) && pedidoAtual.getStatus().equals(StatusPedido.PRONTO))
                throw new UnsupportedOperationException(
                        "STATUS não pode ser alterado pois seu status atual coresponde a PRONTO.");

            if (pedido.getStatus().equals(StatusPedido.ENTREGUE)
                    && pedidoAtual.getStatus().equals(StatusPedido.EM_ROTA))
                throw new UnsupportedOperationException(
                        "STATUS não pode ser alterado pois seu status atual coresponde a EM ROTA.");

            pedidoAtual.setStatus(pedido.getStatus());
            return repository.saveAndFlush(pedidoAtual);
        } else
            throw new EntityNotFoundException("Pedido não existe.");

    }

    private Double valorTotalDosProdutos(List<Item> itens) {
        Double valor = 0.0;
        for (Item itemPedido : itens) {
            valor = valor + (itemPedido.getPrecoUnitario() * itemPedido.getQuantidade());
        }
        return valor;
    }

}
