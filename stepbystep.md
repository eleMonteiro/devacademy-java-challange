## Premissas:
- Java 11 deve ser ou estar instalado;
  -  Para  verificar a versão no windows abra o prompt de comando e digite
    - `java -version`
  -  Para verificar a versão no  linux abra o prompt de comando e digite
    - Para o JDK: `javac -version`
    - Para o JRE: `java -version`
- De preferência  utilizar o [Visual Studio Code](https://code.visualstudio.com/)

## Etapa 1 - Instalação
- Crie uma pasta no local que desejar
  -  Acesse-a pelo prompt de comando e digite
    -    `git init`
    -    `git clone https://github.com/eleMonteiro/devacademy-java-challange`
-  Caso não utilize o git basta apenas acessar o link `https://github.com/eleMonteiro/devacademy-java-challange` e baixar o zip do projeto

## Etapa 2 - Inicialização
- Abra o Visual Studio Code procure pela opção open folder ou abrir pasta e busque o local da pasta do projeto.
- Abra o terminal do próprio Visual Studio Code e digite
  - No windows e linux `./gradlew bootRun`

## Etapa 3 - Testando a Aplicação com os Testes Automáticos
1. Depois de realizado a [Etapa 2](./Etapa 2 - Inicialização)
2. Acesse o arquivo `PedidoController.java` e execute a opção de Run
3. Acesse o arquivo `PedidoControllerTest.java` e execute a opção de Run, ele irá apresentar uma aba com os resultados de alguns teste realizados pelo desenvolvedor do projeto
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

## Etapa 3 - Vizualizar Documentção da API
1. Depois de realizado a [Etapa 2](./Etapa 2 - Inicialização)
2. Acesse o arquivo `PedidoController.java` e execute a opção de Run
3. Acesse o link [Documentação API](http://localhost:8080/swagger-ui/index.html?configUrl=/api-docs/swagger-config) para vizualizar a documentação da  API

## Etapa 4 - Testando a Aplicação na Mão
3. Acesse o link [Etapa 2](./Etapa 2 - Inicialização) para vizualizar a documentação da  API
1. Depois de realizado a 
2. Acesse o arquivo `PedidoController.java` e execute a opção de Run
3. Acesse o Postman

- Para testar o `POST` para criar um Pedido crie um requisição para a seguinte rota `localhost:8080/api/v1/pedidos/`
  - Adicione o seguinte JSON ao Body ou  Corpo da Requisição
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

- Para testar o `GET` para listar todos os Pedidos crie um requisição para a seguinte rota `localhost:8080/api/v1/pedidos/`
  - Adicione os seguintes dados ao `Query Param` **key=pagina value=0** **key=tamanho value=5**

- Para testar o `GET` para listar um Pedido crie um requisição para a seguinte rota `localhost:8080/api/v1/pedidos/1`
  - Onde o 1 representa o Pedido que deseja buscar

- Para testar o `PUT` para atualizar um Pedido crie um requisição para a seguinte rota `localhost:8080/api/v1/pedidos/1`
  - Onde o 1 representa o Pedido que deseja atualizar
  - Adicione o seguinte JSON ao Body ou  Corpo da Requisição

```json
{
    "id": 1,
    "pedido": "123456",
    "nomeCliente": "JOSE FRANCISCO",
    "endereco": "Rua 6, 500",
    "telefone": "8532795578",
    "valorTotalProdutos": 13.5,
    "taxa": 2.5,
    "valorTotal": 16.0,
}
```
- Para testar o `DELETE` para listar um Pedido crie um requisição para a seguinte rota `localhost:8080/api/v1/pedidos/1`
  - Onde o 1 representa o Pedido que deseja deletar
  
- Para testar o `POST` para alterar o status do Pedido crie um requisição para a seguinte rota `localhost:8080/api/v1/pedidos/1/status`
  - Onde o 1 representa o Pedido que deseja atualizar o status
  - Adicione o seguinte JSON ao Body ou  Corpo da Requisição
```json
{
  "status": "EM_ROTA"
}
```
