package br.com.casamagalhaes.workshop.desafio.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "itens")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_item")
    @JsonIgnore
    private Long id;

    @NotEmpty(message = "descrição do produto é obrigatória")
    private String descricao;

    @NotNull(message = "preço do produto é obrigatória")
    private Double precoUnitario;

    @NotNull(message = "quantidade do produto é obrigatória")
    @Min(value = 1, message = "quntidade do produto deve ser pelo menos 1")
    private Long quantidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Pedido pedido;

}
