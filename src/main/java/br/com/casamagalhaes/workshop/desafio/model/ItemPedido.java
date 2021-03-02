package br.com.casamagalhaes.workshop.desafio.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item")
    private Long id;

    @NotEmpty(message = "descrição do item é obrigatória")
    private String descricao;

    private String precoUnitario;

    private Long quantidade;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pedido")
    private Pedido pedido;
}
