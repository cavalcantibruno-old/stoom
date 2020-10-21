# STOOM | Teste de Qualificação Backend.

#### Support:
~~~
GET, POST, PUT, DELETE: 

http://localhost:8080/endereco
~~~

O teste consiste em criar no padrão REST, um CRUD (Create, Read, Update, Delete) de uma entidade endereço com os seguintes atributos:

* -id*
* -streetName*
* -number*
* -complement
* -neighbourhood*
* -city*
* -state*
* -country*
* -zipcode*
* -latitude
* -longitude
~~~
Obs.: Os atributos marcados com * devem ser obrigatórios
~~~

### Obrigatório

- [x] 1- Deve-se utilizar Java para criação desse CRUD. O framework pode ser o que se sentir mais à vontade.

- [x] 2- Deve-se criar um repositório público no github para compartilhar o teste e este ser enviado ao examinador na conclusão

### Diferenciais

- [x] 1- Quando latitude e longitude não forem informados, o sistema precisa buscar essa informação utilizando a Geocoding API do Google (https://developers.google.com/maps/documentation/geocoding/start)
- [x] Use a chave temporária enviada por e-mail

- [x] 2- Criar um Dockerfile funcional com o projeto
Obs.: Não precisa se preocupar com banco de dados no Dockerfile, ele será executado usando em nosso ambiente que já irá considerar isso

- [x] 3- Criar ao menos um teste unitário básico para cada ação (Create, Read, Update, Delete) :warning: 
