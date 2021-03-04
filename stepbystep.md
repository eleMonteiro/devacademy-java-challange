## Premissas:
- Java 11 deve ser ou estar instalado;
  -  Para  verificar a versão no windows abra o prompt de comando e digite
    - **java -version**
  -  Para verificar a versão no  linux abra o prompt de comando e digite
    - Para o JDK: **javac -version**
    - Para o JRE: **java -version**
- De preferência  utilizar o Visual Studio Code

## Etapa 1 - Instalação
- Crie uma pasta no local que desejar
  -  Acesse-a pelo prompt de comando e digite
    -    **git init**
    -    **git clone https://github.com/eleMonteiro/devacademy-java-challange**
-  Caso não utilize o git basta apenas acessar o link **https://github.com/eleMonteiro/devacademy-java-challange** e baixar o zip do projeto

## Etapa 2 - Inicialização
- Abra o Visual Studio Code procure pela opção open folder ou abrir pasta e busque o local da pasta do projeto.
- Abra o terminal do próprio Visual Studio Code e digite
  - No windows e linux **./gradlew bootRun**

## Etapa 3 - Testando a Aplicação
- Depois de realizado a Etapa 2
- Acesse o arquivo **PedidoController.java** e execute a opção de Run
- Em seguida se deseja realizar teste sem necessariamente precisar digitar dados
- Acesse o arquivo **PedidoControllerTest.java** e execute a opção de Run, ele irá apresentar uma aba com os resultados de alguns teste realizados pelo desenvolvedor do projeto
EXEMPLO DE UM TESTE
```java
@Test
       public void deveriaReceberMensagemOK() {
                given().spec(requisicao).param("pagina", 0).param("tamanho", 5).expect().statusCode(HttpStatus.SC_OK)
                                .when().get();
        }

```

