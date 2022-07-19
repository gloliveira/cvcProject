# cvcProject

## Inserção de agendamento


## Parametros Consulta

- Endpoint: http://localhost:8080/cvc/v1/schedule?page=0&size=20&lang=BR
- page = Pagina atual. Default = 0
- size = Tamanho da página a ser buscada no banco. Default = 20
- lang = Default = US
```shell
curl --location --request GET 'http://localhost:8080/cvc/v1/schedule'
```
## Post (Salvar agendamento)

### Request
```shell
curl --location --request POST 'http://localhost:8080/cvc/v1/schedule' \
--header 'Content-Type: application/json' \
--data-raw '{
"amount": 30,
"transferDate": "2021-11-13",
"scheduleDate": "2021-11-13",
"accountOrigin": "3456789",
"accountDestination": "212345"
}'
```
### Response
```shell
{
"id": 1,
"amount": 30,
"taxAmount": 3.90,
"transferDate": "2021-11-13",
"scheduleDate": "2021-11-13",
"accountOrigin": "3456789",
"accountDestination": "212345",
"language": null
}
```

## Swagger

- http://localhost:8080/cvc/swagger

## Actuator (monitoramento )
 
- Actuator: http://localhost:8080/cvc/actuator
- Info: http://localhost:8080/cvc/actuator/info
- Health check: http://localhost:8080/cvc/actuator/health
- Metrics: http://localhost:8080/cvc/actuator/metrics

Requisitos:
 - Persistência de dados em memória => Banco H2
 - Código em última versão do Java => 16
 - Código com ferramenta de build => Maven
 - Sem arquivos de IDE => .gitignore atualizado
 - Código em inglês => Ok
 
Recomendações:
- Utilizado Spring Boot em sua última versão com arquitetura modular separada em camadas baseada em suas responsabilidades
- Para executar o projeto Executar a classe SelectApplication ou rodar o build e executar o jar.
- Regras de negócio descritas foram aplicadas.