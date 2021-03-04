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
                requisicao = new RequestSpecBuilder().setBasePath("/api/v1/pedidos").setPort(porta)
                                .setAccept(ContentType.JSON).setContentType(ContentType.JSON).build();
        }

        @Test
        public void deveriaReceberMensagemOK() {
                given().spec(requisicao).param("pagina", 0).param("tamanho", 5).expect().statusCode(HttpStatus.SC_OK)
                                .when().get();
        }

        @Test
        public void deveriaReceberMensagemBadRequest() {
                given().spec(requisicao).expect().statusCode(HttpStatus.SC_BAD_REQUEST).when().get();
        }

        @Test
        public void deveriaCriarUmPedido() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoComTodosOsDados();
                pedido.setPedido("123456");
                given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when().post().then()
                                .statusCode(HttpStatus.SC_CREATED);
        }

        @Test
        public void deveriaRetornarUmBadRequestAoCriarUmPedidoSemItems() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoFaltandoOsItems();
                pedido.setPedido("123457");
                given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when().post().then()
                                .statusCode(HttpStatus.SC_BAD_REQUEST);
        }

        @Test
        public void deveriaRetornarUmBadRequestAoCriarUmPedidoComItemsSemQuantidade() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoFaltandoAQuantidadeDosItens();
                pedido.setPedido("123458");
                given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when().post().then()
                                .statusCode(HttpStatus.SC_BAD_REQUEST);
        }

        @Test
        public void deveriaRetornarUmPedido() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoComTodosOsDados();
                pedido.setPedido("123459");

                Pedido pedidoSalvo = given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when()
                                .post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

                assertNotNull(pedidoSalvo);

                Pedido pedidoPesquisado = given().spec(requisicao).pathParam("id", pedidoSalvo.getId()).when()
                                .get("/{id}").then().extract().as(Pedido.class);

                assertNotNull(pedidoPesquisado);
        }

        @Test
        public void deveriaNaoRetornarOPedidoPesquisado() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoComTodosOsDados();
                pedido.setPedido("123460");

                Pedido pedidoSalvo = given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when()
                                .post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

                assertNotNull(pedidoSalvo);

                given().spec(requisicao).pathParam("id", pedidoSalvo.getId() + 1).when().get("/{id}").then()
                                .statusCode(HttpStatus.SC_NOT_FOUND);
        }

        @Test
        public void deveriaRetornarUmErrorPorReceberUmaStringParaPesquisarPedido() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoComTodosOsDados();
                pedido.setPedido("123461");

                Pedido pedidoSalvo = given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when()
                                .post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

                assertNotNull(pedidoSalvo);

                given().spec(requisicao).pathParam("id", "F").when().get("/{id}").then()
                                .statusCode(HttpStatus.SC_BAD_REQUEST);

        }

        @Test
        public void deveriaAtualizarUmPedido() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoComTodosOsDados();
                pedido.setPedido("123462");

                Pedido pedidoSalvo = given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when()
                                .post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

                assertNotNull(pedidoSalvo);

                pedidoSalvo.setEndereco("Rua A, 168.");

                given().spec(requisicao).body(objectMapper.writeValueAsString(pedidoSalvo))
                                .pathParam("id", pedidoSalvo.getId()).when().put("/{id}").then()
                                .statusCode(HttpStatus.SC_OK);
        }

        @Test
        public void deveriaNaoAtualizarUmPedidoComStatusDiferenteDoAtual() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoComTodosOsDados();
                pedido.setPedido("123463");

                Pedido pedidoSalvo = given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when()
                                .post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

                assertNotNull(pedidoSalvo);

                pedidoSalvo.setStatus(StatusPedido.CANCELADO);

                given().spec(requisicao).body(objectMapper.writeValueAsString(pedidoSalvo))
                                .pathParam("id", pedidoSalvo.getId()).when().put("/{id}").then()
                                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);

        }

        @Test
        public void deveriaNaoAtualizarUmPedidoComIdDiferenteDoIdDoPedido() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoComTodosOsDados();
                pedido.setPedido("123464");

                Pedido pedidoSalvo = given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when()
                                .post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

                assertNotNull(pedidoSalvo);

                Long id = pedidoSalvo.getId();
                pedidoSalvo.setId(pedidoSalvo.getId() + 1000);

                given().spec(requisicao).body(objectMapper.writeValueAsString(pedidoSalvo)).pathParam("id", id).when()
                                .put("/{id}").then().statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);

        }

        @Test
        public void deveriaNaoAtualizarUmPedidoComIdInexistente() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoComTodosOsDados();
                pedido.setPedido("123465");

                Pedido pedidoSalvo = given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when()
                                .post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

                assertNotNull(pedidoSalvo);

                pedidoSalvo.setId(pedidoSalvo.getId() + 1000);

                given().spec(requisicao).body(objectMapper.writeValueAsString(pedidoSalvo))
                                .pathParam("id", pedidoSalvo.getId()).when().put("/{id}").then()
                                .statusCode(HttpStatus.SC_NOT_FOUND);

        }

        @Test
        public void deveriaRetornarUmErrorPorReceberUmaStringParaAtualizarUmPedido() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoComTodosOsDados();
                pedido.setPedido("123466");

                Pedido pedidoSalvo = given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when()
                                .post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

                assertNotNull(pedidoSalvo);

                given().spec(requisicao).body(objectMapper.writeValueAsString(pedidoSalvo)).pathParam("id", "F").when()
                                .put("/{id}").then().statusCode(HttpStatus.SC_BAD_REQUEST);

        }

        @Test
        public void deveriaRemoverUmPedido() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoComTodosOsDados();
                pedido.setPedido("123467");

                Pedido pedidoSalvo = given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when()
                                .post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

                assertNotNull(pedidoSalvo);

                given().spec(requisicao).body(objectMapper.writeValueAsString(pedidoSalvo))
                                .pathParam("id", pedidoSalvo.getId()).when().delete("/{id}").then()
                                .statusCode(HttpStatus.SC_NO_CONTENT);

        }

        @Test
        public void deveriaNaoRemoverUmPedido() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoComTodosOsDados();
                pedido.setPedido("123468");

                Pedido pedidoSalvo = given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when()
                                .post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

                assertNotNull(pedidoSalvo);

                given().spec(requisicao).body(objectMapper.writeValueAsString(pedidoSalvo))
                                .pathParam("id", pedidoSalvo.getId() + 1).when().delete("/{id}").then()
                                .statusCode(HttpStatus.SC_NOT_FOUND);

        }

        @Test
        public void deveriaRetornarUmErrorPorReceberUmaStringParaRemoverUmPedido() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoComTodosOsDados();
                pedido.setPedido("123469");

                Pedido pedidoSalvo = given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when()
                                .post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

                assertNotNull(pedidoSalvo);

                given().spec(requisicao).body(objectMapper.writeValueAsString(pedidoSalvo)).pathParam("id", "F").when()
                                .delete("/{id}").then().statusCode(HttpStatus.SC_BAD_REQUEST);

        }

        @Test
        public void deveriaAtualizarOStatusDoPedidoParaPreparando() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoComTodosOsDados();
                pedido.setPedido("123470");

                Pedido pedidoSalvo = given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when()
                                .post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

                assertNotNull(pedidoSalvo);

                Pedido pedidoRequest = new Pedido();
                pedidoRequest.setStatus(StatusPedido.PREPARANDO);

                Pedido pedidoAtualizado = given().spec(requisicao).pathParam("id", pedidoSalvo.getId())
                                .body(objectMapper.writeValueAsString(pedidoRequest)).when().post("/{id}/status").then()
                                .statusCode(HttpStatus.SC_OK).extract().as(Pedido.class);

                assertNotNull(pedidoAtualizado);

        }

        @Test
        public void deveriaAtualizarOStatusDoPedidoParaCanceladoQuandoOAtualForEmRota() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoComTodosOsDados();
                pedido.setPedido("123471");

                Pedido pedidoSalvo = given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when()
                                .post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

                assertNotNull(pedidoSalvo);

                Pedido pedidoRequest = new Pedido();
                pedidoRequest.setStatus(StatusPedido.EM_ROTA);

                given().spec(requisicao).pathParam("id", pedidoSalvo.getId())
                                .body(objectMapper.writeValueAsString(pedidoRequest)).when().post("/{id}/status").then()
                                .statusCode(HttpStatus.SC_OK);

                pedidoRequest.setStatus(StatusPedido.CANCELADO);

                given().spec(requisicao).pathParam("id", pedidoSalvo.getId())
                                .body(objectMapper.writeValueAsString(pedidoRequest)).when().post("/{id}/status").then()
                                .statusCode(HttpStatus.SC_OK);

        }


        @Test
        public void deveriaAtualizarOStatusDoPedidoParaCanceladoQuandoOAtualForEntregue() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoComTodosOsDados();
                pedido.setPedido("123472");

                Pedido pedidoSalvo = given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when()
                                .post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

                assertNotNull(pedidoSalvo);

                Pedido pedidoRequest = new Pedido();
                pedidoRequest.setStatus(StatusPedido.ENTREGUE);

                given().spec(requisicao).pathParam("id", pedidoSalvo.getId())
                                .body(objectMapper.writeValueAsString(pedidoRequest)).when().post("/{id}/status").then()
                                .statusCode(HttpStatus.SC_OK);

                pedidoRequest.setStatus(StatusPedido.CANCELADO);

                given().spec(requisicao).pathParam("id", pedidoSalvo.getId())
                                .body(objectMapper.writeValueAsString(pedidoRequest)).when().post("/{id}/status").then()
                                .statusCode(HttpStatus.SC_OK);

        }

        @Test
        public void deveriaAtualizarOStatusDoPedidoParaCanceladoQuandoOAtualForCancelado() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoComTodosOsDados();
                pedido.setPedido("123473");

                Pedido pedidoSalvo = given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when()
                                .post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

                assertNotNull(pedidoSalvo);

                Pedido pedidoRequest = new Pedido();
                pedidoRequest.setStatus(StatusPedido.EM_ROTA);

                given().spec(requisicao).pathParam("id", pedidoSalvo.getId())
                                .body(objectMapper.writeValueAsString(pedidoRequest)).when().post("/{id}/status").then()
                                .statusCode(HttpStatus.SC_OK);
        
                pedidoRequest.setStatus(StatusPedido.CANCELADO);

                given().spec(requisicao).pathParam("id", pedidoSalvo.getId())
                                .body(objectMapper.writeValueAsString(pedidoRequest)).when().post("/{id}/status").then()
                                .statusCode(HttpStatus.SC_OK);

                pedidoRequest.setStatus(StatusPedido.CANCELADO);

                given().spec(requisicao).pathParam("id", pedidoSalvo.getId())
                                .body(objectMapper.writeValueAsString(pedidoRequest)).when().post("/{id}/status").then()
                                .statusCode(HttpStatus.SC_OK);
        }

        @Test
        public void deveriaNaoAtualizarOStatusDoPedidoParaCanceladoQuandoOAtualForPendente() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoComTodosOsDados();
                pedido.setPedido("123474");

                Pedido pedidoSalvo = given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when()
                                .post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

                assertNotNull(pedidoSalvo);

                Pedido pedidoRequest = new Pedido();
                pedidoRequest.setStatus(StatusPedido.CANCELADO);

                given().spec(requisicao).pathParam("id", pedidoSalvo.getId())
                                .body(objectMapper.writeValueAsString(pedidoRequest)).when().post("/{id}/status").then()
                                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
        }

        @Test
        public void deveriaNaoAtualizarOStatusDoPedidoParaEmRotaQuandoOAtualForPronto() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoComTodosOsDados();
                pedido.setPedido("123475");

                Pedido pedidoSalvo = given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when()
                                .post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

                assertNotNull(pedidoSalvo);

                Pedido pedidoRequest = new Pedido();
                pedidoRequest.setStatus(StatusPedido.PRONTO);

                given().spec(requisicao).pathParam("id", pedidoSalvo.getId())
                                .body(objectMapper.writeValueAsString(pedidoRequest)).when().post("/{id}/status").then()
                                .statusCode(HttpStatus.SC_OK);

                pedidoRequest.setStatus(StatusPedido.EM_ROTA);

                given().spec(requisicao).pathParam("id", pedidoSalvo.getId())
                                .body(objectMapper.writeValueAsString(pedidoRequest)).when().post("/{id}/status").then()
                                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
        }

        @Test
        public void deveriaAtualizarOStatusDoPedidoParaEmRotaQuandoOAtualNaoForPronto() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoComTodosOsDados();
                pedido.setPedido("123476");

                Pedido pedidoSalvo = given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when()
                                .post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

                assertNotNull(pedidoSalvo);

                Pedido pedidoRequest = new Pedido();
                pedidoRequest.setStatus(StatusPedido.EM_ROTA);

                given().spec(requisicao).pathParam("id", pedidoSalvo.getId())
                                .body(objectMapper.writeValueAsString(pedidoRequest)).when().post("/{id}/status").then()
                                .statusCode(HttpStatus.SC_OK);
        }

        @Test
        public void deveriaAtualizarOStatusDoPedidoParaEntregueQuandoOAtualNaoForEmRota() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoComTodosOsDados();
                pedido.setPedido("123477");

                Pedido pedidoSalvo = given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when()
                                .post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

                assertNotNull(pedidoSalvo);

                Pedido pedidoRequest = new Pedido();
                pedidoRequest.setStatus(StatusPedido.ENTREGUE);

                given().spec(requisicao).pathParam("id", pedidoSalvo.getId())
                                .body(objectMapper.writeValueAsString(pedidoRequest)).when().post("/{id}/status").then()
                                .statusCode(HttpStatus.SC_OK);
        }

        @Test
        public void deveriaNaoAtualizarOStatusDoPedidoParaEntregueQuandoOAtualForEmRota() throws JsonProcessingException {
                Pedido pedido = dadoUmPedidoComTodosOsDados();
                pedido.setPedido("123478");

                Pedido pedidoSalvo = given().spec(requisicao).body(objectMapper.writeValueAsString(pedido)).when()
                                .post().then().statusCode(HttpStatus.SC_CREATED).extract().as(Pedido.class);

                assertNotNull(pedidoSalvo);

                Pedido pedidoRequest = new Pedido();
                pedidoRequest.setStatus(StatusPedido.EM_ROTA);

                given().spec(requisicao).pathParam("id", pedidoSalvo.getId())
                                .body(objectMapper.writeValueAsString(pedidoRequest)).when().post("/{id}/status").then()
                                .statusCode(HttpStatus.SC_OK);

                pedidoRequest.setStatus(StatusPedido.ENTREGUE);

                given().spec(requisicao).pathParam("id", pedidoSalvo.getId())
                                .body(objectMapper.writeValueAsString(pedidoRequest)).when().post("/{id}/status").then()
                                .statusCode(HttpStatus.SC_UNPROCESSABLE_ENTITY);
        }

        private Pedido dadoUmPedidoComTodosOsDados() {
                Pedido pedido = new Pedido();
                pedido.setNomeCliente("JOSE FRANCISCO");
                pedido.setEndereco("Rua A, 168.");
                pedido.setTelefone("8532795578");
                pedido.setValorTotalProdutos(5.50);
                pedido.setTaxa(2.5);
                pedido.setValorTotal(8.0);

                List<Item> items = new ArrayList<>();

                Item item = new Item();
                item.setDescricao("Refri");
                item.setPrecoUnitario(5.5);
                item.setQuantidade(1L);
                item.setPedido(pedido);
                items.add(item);

                pedido.setItens(items);

                return pedido;
        }

        private Pedido dadoUmPedidoFaltandoAQuantidadeDosItens() {
                Pedido pedido = new Pedido();
                pedido.setNomeCliente("JOSE FRANCISCO");
                pedido.setEndereco("Rua A, 168.");
                pedido.setTelefone("8532795578");
                pedido.setValorTotalProdutos(5.50);
                pedido.setTaxa(2.5);
                pedido.setValorTotal(8.0);

                List<Item> items = new ArrayList<>();

                Item item = new Item();
                item.setDescricao("Refri");
                item.setPrecoUnitario(5.5);
                item.setPedido(pedido);
                items.add(item);

                pedido.setItens(items);

                return pedido;
        }

        private Pedido dadoUmPedidoFaltandoOsItems() {
                Pedido pedido = new Pedido();
                pedido.setNomeCliente("JOSE FRANCISCO");
                pedido.setEndereco("Rua A, 168.");
                pedido.setTelefone("8532795578");
                pedido.setValorTotalProdutos(5.50);
                pedido.setTaxa(2.5);
                pedido.setValorTotal(8.0);

                return pedido;
        }
}
