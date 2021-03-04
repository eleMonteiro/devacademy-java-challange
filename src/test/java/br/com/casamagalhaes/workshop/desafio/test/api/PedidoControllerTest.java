package br.com.casamagalhaes.workshop.desafio.test.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.casamagalhaes.workshop.desafio.enums.StatusPedido;
import br.com.casamagalhaes.workshop.desafio.model.Item;
import br.com.casamagalhaes.workshop.desafio.model.Pedido;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class PedidoControllerTest {

    @Value("${server.port}")
    private int porta;

    private RequestSpecification requisicao;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    private void prepararRequisicao() {
        requisicao = new RequestSpecBuilder().setBasePath("/api/v1/pedidos").setPort(porta).setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON).build();
    }

    @Test
    public void deveriaReceberMensagemOK() {
        given().spec(requisicao).param("pagina", 0).param("tamanho", 5).expect().statusCode(HttpStatus.SC_OK).when()
                .get();
    }

    @Test
    public void deveriaReceberMensagemBadRequest() {
        given().spec(requisicao).expect().statusCode(HttpStatus.SC_BAD_REQUEST).when().get();
    }

    @Test
    public void deveriaCriarUmPedido() throws JsonProcessingException {
        given().spec(requisicao).body(objectMapper.writeValueAsString(dadoUmPedidoComTodosOsDados())).when().post()
                .then().statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void deveriaRetornarUmBadRequestAoCriarUmPedidoSemItems() throws JsonProcessingException {
        given().spec(requisicao).body(objectMapper.writeValueAsString(dadoUmPedidoFaltandoOsItems())).when().post()
                .then().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void deveriaRetornarUmPedido() throws JsonProcessingException {
        Pedido pedido = given().spec(requisicao).body(objectMapper.writeValueAsString(dadoUmPedidoComTodosOsDados()))
                .when().post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

        assertNotNull(pedido);

        Pedido pedidoPesquisado = given().spec(requisicao).pathParam("id", pedido.getPedido()).when().get("/{id}")
                .then().extract().as(Pedido.class);

        assertNotNull(pedidoPesquisado);
    }

    @Test
    public void deveriaNaoRetornarOPedidoPesquisado() throws JsonProcessingException {
        Pedido pedido = given().spec(requisicao).body(objectMapper.writeValueAsString(dadoUmPedidoComTodosOsDados()))
                .when().post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

        assertNotNull(pedido);

        given().spec(requisicao).pathParam("id", pedido.getPedido() + 1).when().get("/{id}").then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void deveriaRetornarUmErrorPorReceberUmaStringParaPesquisarPedido() throws JsonProcessingException {
        Pedido pedido = given().spec(requisicao).body(objectMapper.writeValueAsString(dadoUmPedidoComTodosOsDados()))
                .when().post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

        assertNotNull(pedido);

        given().spec(requisicao).pathParam("id", "F").when().get("/{id}").then().statusCode(HttpStatus.SC_BAD_REQUEST);

    }

    @Test
    public void deveriaAtualizarUmPedido() throws JsonProcessingException {
        Pedido pedido = given().spec(requisicao).body(objectMapper.writeValueAsString(dadoUmPedidoComTodosOsDados()))
                .when().post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

        assertNotNull(pedido);

        pedido.setEndereco("Rua A, 168.");

        given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).pathParam("id", pedido.getPedido())
                .when().put("/{id}").then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void deveriaNaoAtualizarUmPedidoComStatusDiferenteDoAtual() throws JsonProcessingException {
        Pedido pedido = given().spec(requisicao).body(objectMapper.writeValueAsString(dadoUmPedidoComTodosOsDados()))
                .when().post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

        assertNotNull(pedido);

        pedido.setStatus(StatusPedido.CANCELADO);

        given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).pathParam("id", pedido.getPedido())
                .when().put("/{id}").then().statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);

    }

    @Test
    public void deveriaNaoAtualizarUmPedidoComIdDiferenteDoIdDoPedido() throws JsonProcessingException {
        Pedido pedido = given().spec(requisicao).body(objectMapper.writeValueAsString(dadoUmPedidoComTodosOsDados()))
                .when().post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

        assertNotNull(pedido);

        Long id = pedido.getPedido();
        pedido.setPedido(pedido.getPedido() + 1000);

        given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).pathParam("id", id).when().put("/{id}")
                .then().statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);

    }

    @Test
    public void deveriaNaoAtualizarUmPedidoComIdInexistente() throws JsonProcessingException {
        Pedido pedido = given().spec(requisicao).body(objectMapper.writeValueAsString(dadoUmPedidoComTodosOsDados()))
                .when().post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

        assertNotNull(pedido);

        pedido.setPedido(pedido.getPedido() + 1000);

        given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).pathParam("id", pedido.getPedido())
                .when().put("/{id}").then().statusCode(HttpStatus.SC_NOT_FOUND);

    }

    @Test
    public void deveriaRetornarUmErrorPorReceberUmaStringParaAtualizarUmPedido() throws JsonProcessingException {
        Pedido pedido = given().spec(requisicao).body(objectMapper.writeValueAsString(dadoUmPedidoComTodosOsDados()))
                .when().post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

        assertNotNull(pedido);

        given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).pathParam("id", "F")
                .when().put("/{id}").then().statusCode(HttpStatus.SC_BAD_REQUEST);

    }

    @Test
    public void deveriaRemoverUmPedido() throws JsonProcessingException {
        Pedido pedido = given().spec(requisicao).body(objectMapper.writeValueAsString(dadoUmPedidoComTodosOsDados()))
                .when().post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

        assertNotNull(pedido);

        given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).pathParam("id", pedido.getPedido())
                .when().delete("/{id}").then().statusCode(HttpStatus.SC_NO_CONTENT);

    }

    @Test
    public void deveriaNaoRemoverUmPedido() throws JsonProcessingException {
        Pedido pedido = given().spec(requisicao).body(objectMapper.writeValueAsString(dadoUmPedidoComTodosOsDados()))
                .when().post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

        assertNotNull(pedido);

        given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).pathParam("id", pedido.getPedido() + 1)
                .when().delete("/{id}").then().statusCode(HttpStatus.SC_NOT_FOUND);

    }

    @Test
    public void deveriaRetornarUmErrorPorReceberUmaStringParaRemoverUmPedido() throws JsonProcessingException {
        Pedido pedido = given().spec(requisicao).body(objectMapper.writeValueAsString(dadoUmPedidoComTodosOsDados()))
                .when().post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

        assertNotNull(pedido);

        given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).pathParam("id", "F")
                .when().delete("/{id}").then().statusCode(HttpStatus.SC_BAD_REQUEST);

    }


    private Pedido dadoUmPedidoComTodosOsDados() {
        Pedido pedido = new Pedido();
        pedido.setPedido(1L);
        pedido.setNomeCliente("JOSE FRANCISCO");
        pedido.setEndereco("Rua A, 168.");
        pedido.setTelefone("8532795578");
        pedido.setValorTotalProdutos(5.50);
        pedido.setTaxa(2.5);
        pedido.setValorTotal(8.0);

        List<Item> items = new ArrayList<>();

        Item item = new Item();
        item.setId(1L);
        item.setDescricao("Refri");
        item.setPrecoUnitario(5.5);
        item.setQuantidade(1L);
        item.setPedido(pedido);
        items.add(item);

        pedido.setItens(items);

        return pedido;
    }

    private Pedido dadoUmPedidoFaltandoOsItems() {
        Pedido pedido = new Pedido();
        pedido.setPedido(1L);
        pedido.setNomeCliente("JOSE FRANCISCO");
        pedido.setEndereco("Rua A, 168.");
        pedido.setTelefone("8532795578");
        pedido.setValorTotalProdutos(5.50);
        pedido.setTaxa(2.5);
        pedido.setValorTotal(8.0);

        return pedido;
    }
}
