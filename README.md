# This service provides functionalities for product management

## Initialization

The service starts on port 8084 (configuration that ca be changed from application.yml file)

It uses an in-memory H2 database for storing the products. All the data is saved into the PRODUCT table which has the following structure

| Column Name | Data type | Data size | Key |
|-------------|-----------|-----------|-----|
| ID          | NUMBER    | 10        | PK  |
| CODE        | VARCHAR   | 250       |     |
| NAME        | VARCHAR   | 250       |     |
| PRICE       | VARCHAR   | 10,2      |     |
| DESCRIPTION | VARCHAR   | 250       |     |

## Security

The service is using basic authentication and role-based authorization. 
There are 2 users defined:
* user - which has rights to access to search functionalities 
* admin - which has rights to search and also alter data 

For both of these users the password is "password"

## Functionalities

This service provides the following functionalities:
* add product\
Sample request\
  curl --location 'http://localhost:8084/product' \
  --header 'Content-Type: application/json' \
  --header 'Authorization: ••••••' \
  --data '{
  "code": "prod22",
  "name": "Product 12",
  "description": "desc",
  "price" : 20.5
  }'\
Response :\
200 OK
```json
  {
    "productId":3,
    "price":20.5,
    "name":"Product 12",
    "code":"prod22",
    "description":"desc"
  }
```
* delete product based on unique identifier\
Sample request:\
  curl --location --request DELETE 'http://localhost:8084/product/1' \
  --header 'Authorization: Basic YWRtaW46cGFzc3dvcmQ=' \
Response: \
200 OK
* search product based on unique identifier\
  Sample request:\
curl --location 'http://localhost:8084/product/4' \
  --header 'Authorization: Basic YWRtaW46cGFzc3dvcmQ=' \
Response: \
200 OK
```json
{
  "productId":4,
  "price":20.50,
  "name":"Product 12",
  "code":"prod22",
  "description":"desc"
}
```
* search products based on code or name (paginated). 
If the code and name are not provided, all records will be returned. 
If both are present then the records matching both filters will be returned \
Sample request: \
  curl --location 'http://localhost:8084/product?code=pro' \
  --header 'Authorization: Basic YWRtaW46cGFzc3dvcmQ=' \
  --header 'Cookie: JSESSIONID=F7C859A6908FFAD87EB400AEC00C23D9' \
Response: \
200 OK 
```json
  {
    "content": 
    [
        {
          "productId": 2,
          "price": 20.50,
          "name": "Product 12",
          "code": "prod22",
          "description": "desc"
        },
        {
          "productId": 3,
          "price": 20.50,
          "name": "Product 12",
          "code": "prod22",
          "description": "desc"
        },
        {
          "productId": 4,
          "price": 20.50,
          "name": "Product 12",
          "code": "prod22",
          "description": "desc"
        }
    ],
    "page": 
      {
          "size": 10,
          "number": 0,
          "totalElements": 3,
          "totalPages": 1
      }
  } 
```
* update product (partially) based on an unique identifier \
  curl --location --request PATCH 'http://localhost:8084/product/1' \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Basic YWRtaW46cGFzc3dvcmQ=' \
  --header 'Cookie: JSESSIONID=F7C859A6908FFAD87EB400AEC00C23D9' \
  --data '{
  "description": "desc7",
  "price" : 68
  }' \
Response: \
200 OK
```json
  {
    "productId":1,
    "price":68,
    "name":"Product 12",
    "code":"prod22",
    "description":"desc7"
  }
```



