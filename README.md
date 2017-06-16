# Logistics Microservice

[![Build Status](https://travis-ci.org/kaiocb/logistics.svg?branch=master)](https://travis-ci.org/kaiocb/logistics)

O projeto consiste em construir um micro serviço capaz de suprir a lógica necessária para o funcionamento de um sistema 
terceiro auxiliando na logística de carga.

[Definição do Projeto](PROJECT.md)

#### Plano para execução:
* Fundamentação do micro serviço utilizando Spring Boot 1.5.4.
* Criação do repositório Git.
* Planejar lógica e estruturar testes antecipados (API e lógica da aplicação).
    * DeliveryControllerTest 
        * Test POST /delivery
            * empty request: 415 - Unsupported Media Type
            * missing data: 500 - Internal Server Error
            * invalid data: 422 - Unprocessable entity
            * duplicate delivery id: 409 - conflict (TODO)
            * valid data: 201 - Created
            
        * Test GET /delivery/{deliveryId}/step
            * empty path parameter: 400 - Bad Request
            * invalid path parameter (out of bounds): 400 - Bad Request
            * valid path parameter: 200 - OK (valid list of steps)
            
* Criar entidades necessárias e mapea-las para persistência e manipulação.
* Configurar datasource em: application.yml utilizando variáveis de embiente.
* Criar profiles para prod, test e dev.
* Adicionar h2 para persistência em testes.
* Implementar métodos necessários na camada de controle.
* Configurar Git com Travis CI.
* Gerar documentação JavaDocs e liberar em /docs
* Gerar documentação da API /delivery - JSONDoc
* Adicionar Heroku no projeto como cloud platform e configura-lo.

#### REQUISITOS
Java 8 ou superior
Database - aplicação configurada para PostgreSQL.


#### INSTRUÇõES

`git clone https://github.com/kaiocb/logistics.git`

* Teste `./gradlew test --info`

* Build `./gradlew build && `

* Run `./gradlew build && java -Dspring.profiles.active=dev -Dserver.port=8080 -jar build/libs/logistics-0.0.1-SNAPSHOT.jar `

#### DOCS

[Documentção - JavaDoc](https://kaiocb.github.io/logistics)

##### API DOC

* Run `./gradlew build && java -Dspring.profiles.active=dev -Dserver.port=8080 -jar build/libs/logistics-0.0.1-SNAPSHOT.jar `
* Entrar em: http://localhost:8080/jsondoc-ui.html#
* Obter Documentação da URL: http://localhost:8080/jsondoc 

Heroku Build:
* 
* Entrar em: https://kaiocb-logistics.herokuapp.com/jsondoc-ui.html#
* Obter Documentação da URL: https://kaiocb-logistics.herokuapp.com/jsondoc 

 
#####PostgreSQL local database setup
* logistics
* logistics_dev (create-drop strategy)

`run: createdb logistics | logistics_dev`

##### VARIÁVEIS DE AMBIENTE

Inserir em: `/etc/environment`

`DATABASE_URL` e.q: jdbc:user:pwd@postgresql://localhost:5432/logistics_dev

# ON HEROKU:

Utilizar: 
https://kaiocb-logistics.herokuapp.com/jsondoc-ui.html#
URL: https://kaiocb-logistics.herokuapp.com/jsondoc

JSONDoc API:
Utilizar para adicionar entregas (deliveries) e solicitar passos (steps), ou qualquer REST client.