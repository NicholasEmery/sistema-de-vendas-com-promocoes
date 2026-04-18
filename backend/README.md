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
- Atualização em lote de produtos por tipo (ex.: desconto em todos os chocolates)
- Recursos protegidos por autenticação
- Estrutura em camadas: controllers, services, repositories, models, dtos e exceptions

## Estrutura de pacotes

```
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

As variáveis abaixo são opcionais, pois já possuem valores padrão no arquivo `application.yml`.

- `MONGO_URI` (padrão: `mongodb://localhost:27017/sistema_vendas_promocoes`)
- `JWT_SECRET` (padrão definido para ambiente local)
- `JWT_EXPIRATION_MS` (padrão: `86400000`)
- `BOOTSTRAP_ADMIN_NAME` (padrão: `Administrador`)
- `BOOTSTRAP_ADMIN_CPF` (padrão: `00000000000`)
- `BOOTSTRAP_ADMIN_USERNAME` (padrão: `admin`)
- `BOOTSTRAP_ADMIN_PASSWORD` (padrão: `admin123`)

Ao iniciar pela primeira vez com base vazia, o sistema cria automaticamente um usuário admin inicial.

## Como executar

1. Subir o MongoDB local (exemplo com Docker):

```bash
docker run -d --name mongo-vendas -p 27017:27017 mongo:7
```

2. Compilar:

```bash
mvn clean compile
```

3. Executar a aplicação:

```bash
mvn spring-boot:run
```

Aplicação sobe em `http://localhost:8080`.

## Autenticação

### Login

`POST /api/auth/login`

Body:

```json
{
	"username": "admin",
	"password": "admin123"
}
```

Resposta:

```json
{
	"token": "<jwt>",
	"tokenType": "Bearer",
	"expiresIn": 86400,
	"username": "admin",
	"roles": ["ROLE_ADMIN"]
}
```

Use o token nas demais rotas:

`Authorization: Bearer <jwt>`

## Endpoints

### Funcionários (ADMIN)

- `POST /api/employees`
- `GET /api/employees`
- `GET /api/employees/{id}`
- `PUT /api/employees/{id}`
- `DELETE /api/employees/{id}`

### Produtos (ADMIN/EMPLOYEE)

- `POST /api/products`
- `GET /api/products`
- `GET /api/products/{id}`
- `PUT /api/products/{id}`
- `DELETE /api/products/{id}`
- `PATCH /api/products/discount-by-type`

Body do desconto em lote:

```json
{
	"type": "CHOCOLATE",
	"discountPercent": 15
}
```

### Promoções (ADMIN/EMPLOYEE)

- `POST /api/promotions`
- `GET /api/promotions`
- `GET /api/promotions/{id}`
- `PUT /api/promotions/{id}`
- `DELETE /api/promotions/{id}`

### Clientes (ADMIN/EMPLOYEE)

- `POST /api/customers`
- `GET /api/customers`
- `GET /api/customers/{id}`
- `PUT /api/customers/{id}`
- `DELETE /api/customers/{id}`

### Compras (ADMIN/EMPLOYEE)

- `POST /api/purchases`
- `GET /api/purchases`
- `GET /api/purchases/{id}`
- `GET /api/purchases/customer/{customerId}`
- `PUT /api/purchases/{id}`
- `DELETE /api/purchases/{id}`

### Promoções personalizadas (ADMIN/EMPLOYEE)

- `GET /api/customers/{customerId}/personalized-promotions`

Regras de personalização:

- Considera tipos favoritos do cliente (`favoriteProductTypes`)
- Considera os tipos mais comprados pelo cliente (top 3)
- Filtra promoções ativas por período (`startDate`/`endDate`)
- Se a promoção tiver lista específica de CPFs, valida se o cliente está na lista

## Modelos mínimos suportados

- Funcionário: `id`, `name`, `cpf`, `username`, `password`, `roles`
- Produto: `name`, `currentPrice`, `promotionalPrice`, `type`, `description`, `expirationDate`
- Compra: `productName`, `price`, `purchaseDate` (e `productType` para personalização)
- Cliente: `name`, `cpf`, `age`

## Observações

- Todos os recursos, exceto `/api/auth/login`, exigem autenticação JWT.
- Recomendado alterar a senha padrão do admin inicial após a primeira execução.