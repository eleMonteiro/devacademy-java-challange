## Premissas:
- Java 11 deve ser ou estar instalado;
  -  Para  verificar a versão no windows abra o prompt de comando e digite
    - `**java -version**`
  -  Para verificar a versão no  linux abra o prompt de comando e digite
    - Para o JDK: `**javac -version**`
    - Para o JRE: `**java -version**`
- De preferência  utilizar o Visual Studio Code

## Etapa 1 - Instalação
- Crie uma pasta no local que desejar
  -  Acesse-a pelo prompt de comando e digite
    -    `**git init**`
    -    `**git clone https://github.com/eleMonteiro/devacademy-java-challange**`
-  Caso não utilize o git basta apenas acessar o link `**https://github.com/eleMonteiro/devacademy-java-challange**` e baixar o zip do projeto

## Etapa 2 - Inicialização
- Abra o Visual Studio Code procure pela opção open folder ou abrir pasta e busque o local da pasta do projeto.
- Abra o terminal do próprio Visual Studio Code e digite
  - No windows e linux `**./gradlew bootRun**`

## Etapa 3 - Testando a Aplicação com os Testes Automáticos
1. Depois de realizado a Etapa 2
2. Acesse o arquivo `**PedidoController.java**` e execute a opção de Run
3. Acesse o arquivo `**PedidoControllerTest.java**` e execute a opção de Run, ele irá apresentar uma aba com os resultados de alguns teste realizados pelo desenvolvedor do projeto
- Exemplo de um teste que lista os dados páginados da página 0 e com quuantidade de arquivos por páginna 5
```java
@Test
public void deveriaReceberMensagemOK() {
  given()
  .spec(requisicao)
    .param("pagina", 0)
    .param("tamanho", 5)
  .expect()
    .statusCode(HttpStatus.SC_OK)
  .when()
    .get();
}
```
## Etapa 3 - Testando a Aplicação na Mão
1. Depois de realizado a Etapa 2
2. Acesse o arquivo `**PedidoController.java**` e execute a opção de Run
3. Acesse o Postman

- Para testar o `POST` criei umm requissição para a seguinte rota `localhost:8080/api/v1/pedidos/`
- Addicione o seguinte JSON ao Body ou  Corpo da Requisição
```json
{
  "pedido":"123456",
  "nomeCliente":"JOSE FRANCISCO",
  "endereco":"Rua A, 500",
  "telefone":"8532795578",
  "valorTotalProdutos":13.50,
  "taxa":2.50,
  "valorTotal":16.00,
  "itens": 
      [{
          "descricao": "Refri",
          "precoUnitario": 5.5,
          "quantidade": 1
     },
     {
          "descricao": "Coxinha",
          "precoUnitario": 3.00,
          "quantidade": 1
     },
     {
          "descricao": "Batatinha",
          "precoUnitario": 5.00,
          "quantidade": 1
     }]
 }
```
