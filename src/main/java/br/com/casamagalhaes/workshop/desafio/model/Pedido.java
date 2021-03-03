package br.com.casamagalhaes.workshop.desafio.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import br.com.casamagalhaes.workshop.desafio.enums.StatusPedido;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_pedido")
    private Long pedido;

    @NotEmpty(message = "nome do cliente é obrigatório")
    private String nomeCliente;

    @NotEmpty(message = "endereço do cliente é obrigatório")
    private String endereco;

    @NotEmpty(message = "telefone do cliente é obrigatório")
    @Size(min = 7, max = 11, message = "número de telefone celular brasileiro deve ter no mínimo 7 e no máximo 11 caracteres")
    private String telefone;

    private Double valorTotalProdutos;

    private Double taxa;

    private Double valorTotal;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<Item> itens;
}
