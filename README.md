# Sistema de Vendas com Promoções

Backend em Spring Boot + MongoDB para gestão de produtos, promoções e clientes, com autenticação JWT e recomendação de promoções personalizadas com base em preferências e histórico de compras.

## Tecnologias

- Java 17
- Spring Boot 3.3.5
- Spring Web
- Spring Security (JWT)
- Spring Data MongoDB
- Maven

## Requisitos atendidos

- CRUD completo de produtos
- CRUD completo de usuários do supermercado (funcionários)
- Login de usuário do supermercado
- CRUD completo de promoções
- Retorno de promoções personalizadas por cliente
- Atualização em lote de produtos por tipo
- Recursos protegidos por autenticação

## Estrutura de pacotes

```text
src/main/java/com/supermercado/vendas
├── config
├── controller
├── dto
├── exception
├── model
├── repository
├── security
└── service
```

## Configuração

As variáveis abaixo são opcionais, pois já possuem valores padrão no `application.yml`.

- `MONGO_URI` (padrão: `mongodb://localhost:27017/sistema_vendas_promocoes`)
- `JWT_SECRET` (padrão definido para ambiente local)
- `JWT_EXPIRATION_MS` (padrão: `86400000`)
- `BOOTSTRAP_ADMIN_NAME` (padrão: `Administrador`)
- `BOOTSTRAP_ADMIN_CPF` (padrão: `00000000000`)
- `BOOTSTRAP_ADMIN_USERNAME` (padrão: `admin`)
- `BOOTSTRAP_ADMIN_PASSWORD` (padrão: `admin123`)

Ao iniciar pela primeira vez com base vazia, o sistema cria automaticamente um usuário admin inicial.

## Como executar

1. Suba o MongoDB local (sem autenticação):

```bash
docker run -d --name mongo-vendas -p 27017:27017 mongo:7
```

2. Compile o backend:

```bash
mvn clean compile
```

3. Execute a aplicação:

```bash
mvn spring-boot:run
```

Aplicação disponível em `http://localhost:8080`.

## Convenções da API

- Base URL: `http://localhost:8080`
- Header padrão para JSON: `Content-Type: application/json`
- Header de autenticação (todas as rotas exceto login): `Authorization: Bearer <token>`
- IDs (`id`, `customerId`) são strings de ObjectId do MongoDB
- Formato `LocalDate`: `YYYY-MM-DD` (ex.: `2026-12-31`)
- Formato `LocalDateTime`: `YYYY-MM-DDTHH:mm:ss` (ex.: `2026-04-18T14:30:00`)

### Enums aceitos

#### ProductType

`MEAT`, `DAIRY`, `BAKERY`, `DRINKS`, `CLEANING`, `CHOCOLATE`, `GROCERY`, `FRUIT`, `VEGETABLE`, `FROZEN`, `PERSONAL_CARE`, `OTHER`

#### Role

`ROLE_ADMIN`, `ROLE_EMPLOYEE`

## Autenticação

### POST /api/auth/login

- Acesso: público
- Headers: `Content-Type: application/json`

Body:

```json
{
	"username": "admin",
	"password": "admin123"
}
```

Exemplo cURL:

```bash
curl -X POST "http://localhost:8080/api/auth/login" \
	-H "Content-Type: application/json" \
	-d '{
		"username": "admin",
		"password": "admin123"
	}'
```

Resposta (exemplo):

```json
{
	"token": "<jwt>",
	"tokenType": "Bearer",
	"expiresIn": 86400,
	"username": "admin",
	"roles": ["ROLE_ADMIN"]
}
```

Para facilitar os exemplos abaixo:

```bash
BASE_URL="http://localhost:8080"
TOKEN="<cole-o-jwt-aqui>"
```

## Rotas e como montar cada requisição

## Funcionários (apenas ROLE_ADMIN)

### POST /api/employees

- Headers: `Authorization: Bearer <token>`, `Content-Type: application/json`

Body:

```json
{
	"name": "Maria Silva",
	"cpf": "12345678901",
	"username": "maria.silva",
	"password": "senha123",
	"roles": ["ROLE_EMPLOYEE"]
}
```

Observações:

- `roles` não pode ser `null`.
- `roles` pode ser enviado vazio (`[]`), e o backend atribui `ROLE_EMPLOYEE`.
- `password` precisa ter ao menos 6 caracteres.

Exemplo cURL:

```bash
curl -X POST "$BASE_URL/api/employees" \
	-H "Authorization: Bearer $TOKEN" \
	-H "Content-Type: application/json" \
	-d '{
		"name": "Maria Silva",
		"cpf": "12345678901",
		"username": "maria.silva",
		"password": "senha123",
		"roles": ["ROLE_EMPLOYEE"]
	}'
```

### GET /api/employees

- Headers: `Authorization: Bearer <token>`
- Body: não enviar

Exemplo cURL:

```bash
curl -X GET "$BASE_URL/api/employees" \
	-H "Authorization: Bearer $TOKEN"
```

### GET /api/employees/{id}

- Headers: `Authorization: Bearer <token>`
- Path param: `id` (ObjectId)
- Body: não enviar

Exemplo cURL:

```bash
curl -X GET "$BASE_URL/api/employees/{id}" \
	-H "Authorization: Bearer $TOKEN"
```

### PUT /api/employees/{id}

- Headers: `Authorization: Bearer <token>`, `Content-Type: application/json`
- Path param: `id` (ObjectId)

Body (todos os campos são opcionais):

```json
{
	"name": "Maria Souza",
	"cpf": "12345678901",
	"username": "maria.souza",
	"password": "novaSenha123",
	"roles": ["ROLE_EMPLOYEE", "ROLE_ADMIN"]
}
```

Exemplo cURL:

```bash
curl -X PUT "$BASE_URL/api/employees/{id}" \
	-H "Authorization: Bearer $TOKEN" \
	-H "Content-Type: application/json" \
	-d '{
		"name": "Maria Souza",
		"username": "maria.souza"
	}'
```

### DELETE /api/employees/{id}

- Headers: `Authorization: Bearer <token>`
- Path param: `id` (ObjectId)
- Body: não enviar

Exemplo cURL:

```bash
curl -X DELETE "$BASE_URL/api/employees/{id}" \
	-H "Authorization: Bearer $TOKEN"
```

## Produtos (ROLE_ADMIN ou ROLE_EMPLOYEE)

### POST /api/products

- Headers: `Authorization: Bearer <token>`, `Content-Type: application/json`

Body:

```json
{
	"name": "Chocolate 70%",
	"currentPrice": 12.90,
	"promotionalPrice": 10.90,
	"type": "CHOCOLATE",
	"description": "Barra 100g",
	"expirationDate": "2026-12-31"
}
```

Observação:

- `promotionalPrice` pode ser `null`, mas não pode ser maior que `currentPrice`.

Exemplo cURL:

```bash
curl -X POST "$BASE_URL/api/products" \
	-H "Authorization: Bearer $TOKEN" \
	-H "Content-Type: application/json" \
	-d '{
		"name": "Chocolate 70%",
		"currentPrice": 12.90,
		"promotionalPrice": 10.90,
		"type": "CHOCOLATE",
		"description": "Barra 100g",
		"expirationDate": "2026-12-31"
	}'
```

### GET /api/products

- Headers: `Authorization: Bearer <token>`
- Body: não enviar

Exemplo cURL:

```bash
curl -X GET "$BASE_URL/api/products" \
	-H "Authorization: Bearer $TOKEN"
```

### GET /api/products/{id}

- Headers: `Authorization: Bearer <token>`
- Path param: `id` (ObjectId)
- Body: não enviar

Exemplo cURL:

```bash
curl -X GET "$BASE_URL/api/products/{id}" \
	-H "Authorization: Bearer $TOKEN"
```

### PUT /api/products/{id}

- Headers: `Authorization: Bearer <token>`, `Content-Type: application/json`
- Path param: `id` (ObjectId)

Body (mesmo contrato do POST):

```json
{
	"name": "Chocolate 70% Premium",
	"currentPrice": 14.90,
	"promotionalPrice": 12.90,
	"type": "CHOCOLATE",
	"description": "Barra 120g",
	"expirationDate": "2027-01-15"
}
```

Exemplo cURL:

```bash
curl -X PUT "$BASE_URL/api/products/{id}" \
	-H "Authorization: Bearer $TOKEN" \
	-H "Content-Type: application/json" \
	-d '{
		"name": "Chocolate 70% Premium",
		"currentPrice": 14.90,
		"promotionalPrice": 12.90,
		"type": "CHOCOLATE",
		"description": "Barra 120g",
		"expirationDate": "2027-01-15"
	}'
```

### DELETE /api/products/{id}

- Headers: `Authorization: Bearer <token>`
- Path param: `id` (ObjectId)
- Body: não enviar

Exemplo cURL:

```bash
curl -X DELETE "$BASE_URL/api/products/{id}" \
	-H "Authorization: Bearer $TOKEN"
```

### PATCH /api/products/discount-by-type

- Headers: `Authorization: Bearer <token>`, `Content-Type: application/json`

Body:

```json
{
	"type": "CHOCOLATE",
	"discountPercent": 15
}
```

Observação:

- `discountPercent` deve estar entre `0.01` e `100.00`.

Exemplo cURL:

```bash
curl -X PATCH "$BASE_URL/api/products/discount-by-type" \
	-H "Authorization: Bearer $TOKEN" \
	-H "Content-Type: application/json" \
	-d '{
		"type": "CHOCOLATE",
		"discountPercent": 15
	}'
```

## Promoções (ROLE_ADMIN ou ROLE_EMPLOYEE)

### POST /api/promotions

- Headers: `Authorization: Bearer <token>`, `Content-Type: application/json`

Body:

```json
{
	"name": "Promo Chocolate de Inverno",
	"description": "Desconto para chocolates e bebidas quentes",
	"targetProductTypes": ["CHOCOLATE", "DRINKS"],
	"discountPercent": 12.5,
	"startDate": "2026-06-01",
	"endDate": "2026-06-30",
	"customerCpfTargets": ["12345678901", "98765432100"]
}
```

Observações:

- `targetProductTypes` é obrigatório.
- `endDate` não pode ser menor que `startDate`.
- `customerCpfTargets` é opcional (se não enviar, o backend trata como vazio).

Exemplo cURL:

```bash
curl -X POST "$BASE_URL/api/promotions" \
	-H "Authorization: Bearer $TOKEN" \
	-H "Content-Type: application/json" \
	-d '{
		"name": "Promo Chocolate de Inverno",
		"description": "Desconto para chocolates e bebidas quentes",
		"targetProductTypes": ["CHOCOLATE", "DRINKS"],
		"discountPercent": 12.5,
		"startDate": "2026-06-01",
		"endDate": "2026-06-30",
		"customerCpfTargets": ["12345678901", "98765432100"]
	}'
```

### GET /api/promotions

- Headers: `Authorization: Bearer <token>`
- Body: não enviar

Exemplo cURL:

```bash
curl -X GET "$BASE_URL/api/promotions" \
	-H "Authorization: Bearer $TOKEN"
```

### GET /api/promotions/{id}

- Headers: `Authorization: Bearer <token>`
- Path param: `id` (ObjectId)
- Body: não enviar

Exemplo cURL:

```bash
curl -X GET "$BASE_URL/api/promotions/{id}" \
	-H "Authorization: Bearer $TOKEN"
```

### PUT /api/promotions/{id}

- Headers: `Authorization: Bearer <token>`, `Content-Type: application/json`
- Path param: `id` (ObjectId)

Body (mesmo contrato do POST):

```json
{
	"name": "Promo Chocolate Atualizada",
	"description": "Desconto estendido",
	"targetProductTypes": ["CHOCOLATE"],
	"discountPercent": 10,
	"startDate": "2026-06-01",
	"endDate": "2026-07-15",
	"customerCpfTargets": []
}
```

Exemplo cURL:

```bash
curl -X PUT "$BASE_URL/api/promotions/{id}" \
	-H "Authorization: Bearer $TOKEN" \
	-H "Content-Type: application/json" \
	-d '{
		"name": "Promo Chocolate Atualizada",
		"description": "Desconto estendido",
		"targetProductTypes": ["CHOCOLATE"],
		"discountPercent": 10,
		"startDate": "2026-06-01",
		"endDate": "2026-07-15",
		"customerCpfTargets": []
	}'
```

### DELETE /api/promotions/{id}

- Headers: `Authorization: Bearer <token>`
- Path param: `id` (ObjectId)
- Body: não enviar

Exemplo cURL:

```bash
curl -X DELETE "$BASE_URL/api/promotions/{id}" \
	-H "Authorization: Bearer $TOKEN"
```

## Clientes (ROLE_ADMIN ou ROLE_EMPLOYEE)

### POST /api/customers

- Headers: `Authorization: Bearer <token>`, `Content-Type: application/json`

Body:

```json
{
	"name": "Cliente Teste",
	"cpf": "12345678901",
	"age": 30,
	"favoriteProductTypes": ["CHOCOLATE", "DRINKS"]
}
```

Observação:

- `favoriteProductTypes` é opcional (se não enviar, o backend salva vazio).

Exemplo cURL:

```bash
curl -X POST "$BASE_URL/api/customers" \
	-H "Authorization: Bearer $TOKEN" \
	-H "Content-Type: application/json" \
	-d '{
		"name": "Cliente Teste",
		"cpf": "12345678901",
		"age": 30,
		"favoriteProductTypes": ["CHOCOLATE", "DRINKS"]
	}'
```

### GET /api/customers

- Headers: `Authorization: Bearer <token>`
- Body: não enviar

Exemplo cURL:

```bash
curl -X GET "$BASE_URL/api/customers" \
	-H "Authorization: Bearer $TOKEN"
```

### GET /api/customers/{id}

- Headers: `Authorization: Bearer <token>`
- Path param: `id` (ObjectId)
- Body: não enviar

Exemplo cURL:

```bash
curl -X GET "$BASE_URL/api/customers/{id}" \
	-H "Authorization: Bearer $TOKEN"
```

### PUT /api/customers/{id}

- Headers: `Authorization: Bearer <token>`, `Content-Type: application/json`
- Path param: `id` (ObjectId)

Body (mesmo contrato do POST):

```json
{
	"name": "Cliente Atualizado",
	"cpf": "12345678901",
	"age": 31,
	"favoriteProductTypes": ["MEAT", "CHOCOLATE"]
}
```

Exemplo cURL:

```bash
curl -X PUT "$BASE_URL/api/customers/{id}" \
	-H "Authorization: Bearer $TOKEN" \
	-H "Content-Type: application/json" \
	-d '{
		"name": "Cliente Atualizado",
		"cpf": "12345678901",
		"age": 31,
		"favoriteProductTypes": ["MEAT", "CHOCOLATE"]
	}'
```

### DELETE /api/customers/{id}

- Headers: `Authorization: Bearer <token>`
- Path param: `id` (ObjectId)
- Body: não enviar

Exemplo cURL:

```bash
curl -X DELETE "$BASE_URL/api/customers/{id}" \
	-H "Authorization: Bearer $TOKEN"
```

### GET /api/customers/{customerId}/personalized-promotions

- Headers: `Authorization: Bearer <token>`
- Path param: `customerId` (ObjectId)
- Body: não enviar

Exemplo cURL:

```bash
curl -X GET "$BASE_URL/api/customers/{customerId}/personalized-promotions" \
	-H "Authorization: Bearer $TOKEN"
```

Regras aplicadas pelo backend:

- Considera tipos favoritos do cliente (`favoriteProductTypes`).
- Considera tipos mais comprados pelo cliente.
- Filtra promoções ativas por período (`startDate`/`endDate`).
- Se a promoção tiver CPF alvo, valida elegibilidade do cliente.

## Compras (ROLE_ADMIN ou ROLE_EMPLOYEE)

### POST /api/purchases

- Headers: `Authorization: Bearer <token>`, `Content-Type: application/json`

Body:

```json
{
	"customerId": "{customerId}",
	"productName": "Chocolate 70%",
	"price": 10.9,
	"purchaseDate": "2026-04-18T14:30:00",
	"productType": "CHOCOLATE"
}
```

Observação:

- `customerId` precisa existir no cadastro de clientes.

Exemplo cURL:

```bash
curl -X POST "$BASE_URL/api/purchases" \
	-H "Authorization: Bearer $TOKEN" \
	-H "Content-Type: application/json" \
	-d '{
		"customerId": "{customerId}",
		"productName": "Chocolate 70%",
		"price": 10.9,
		"purchaseDate": "2026-04-18T14:30:00",
		"productType": "CHOCOLATE"
	}'
```

### GET /api/purchases

- Headers: `Authorization: Bearer <token>`
- Body: não enviar

Exemplo cURL:

```bash
curl -X GET "$BASE_URL/api/purchases" \
	-H "Authorization: Bearer $TOKEN"
```

### GET /api/purchases/{id}

- Headers: `Authorization: Bearer <token>`
- Path param: `id` (ObjectId)
- Body: não enviar

Exemplo cURL:

```bash
curl -X GET "$BASE_URL/api/purchases/{id}" \
	-H "Authorization: Bearer $TOKEN"
```

### GET /api/purchases/customer/{customerId}

- Headers: `Authorization: Bearer <token>`
- Path param: `customerId` (ObjectId)
- Body: não enviar

Exemplo cURL:

```bash
curl -X GET "$BASE_URL/api/purchases/customer/{customerId}" \
	-H "Authorization: Bearer $TOKEN"
```

### PUT /api/purchases/{id}

- Headers: `Authorization: Bearer <token>`, `Content-Type: application/json`
- Path param: `id` (ObjectId)

Body (mesmo contrato do POST):

```json
{
	"customerId": "{customerId}",
	"productName": "Chocolate 85%",
	"price": 12.9,
	"purchaseDate": "2026-04-19T09:15:00",
	"productType": "CHOCOLATE"
}
```

Exemplo cURL:

```bash
curl -X PUT "$BASE_URL/api/purchases/{id}" \
	-H "Authorization: Bearer $TOKEN" \
	-H "Content-Type: application/json" \
	-d '{
		"customerId": "{customerId}",
		"productName": "Chocolate 85%",
		"price": 12.9,
		"purchaseDate": "2026-04-19T09:15:00",
		"productType": "CHOCOLATE"
	}'
```

### DELETE /api/purchases/{id}

- Headers: `Authorization: Bearer <token>`
- Path param: `id` (ObjectId)
- Body: não enviar

Exemplo cURL:

```bash
curl -X DELETE "$BASE_URL/api/purchases/{id}" \
	-H "Authorization: Bearer $TOKEN"
```

## Resumo rápido de permissões

- Público: `POST /api/auth/login`
- Apenas `ROLE_ADMIN`: todas as rotas de `/api/employees`
- `ROLE_ADMIN` ou `ROLE_EMPLOYEE`: todas as demais rotas

## Observações finais

- Todos os recursos, exceto `/api/auth/login`, exigem autenticação JWT.
- Recomenda-se alterar a senha do admin padrão após a primeira execução.