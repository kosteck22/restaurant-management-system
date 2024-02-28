# Spring Boot Restaurant management system API using microservices architecture
## Overview

This app is designed to help me manage the finances of my restaurant. Process information about the company's expenses, sales, check inventory and also calculate prices of the dishes served based on their recipes and products used. The application is in the development process. New microservices are added on a regular basis

## Technologies Used

- **Spring Boot**
- **Spring Cloud** (discovery server, config server, open feign, api gateway)
- **Spring Data MongoDB**
- **Docker**
- **AWS S3, AWS Textract**

## Getting Started

### Prerequisites

- Java Development Kit (JDK17)
- Apache Maven
- Your favorite IDE (e.g., IntelliJ, Eclipse)

2. The project includes configured docker-compose file.

3. You have to make sure that environment variables for AWS connection, MongoDB connection and Spring Config Server connection are set 


## Microservices

### Company service

This microservice is used to store company information, getting company information from external api, getting company information by nip etc.

| Method | Url | Decription | Valid Request Body | 
| ------ | --- | ---------- | --------------------------- |
| GET   | /api/v1/companies/{id} | Get company by id | |
| GET   | /api/v1/companies/nip/{nip} | Get company by nip | |
| GET   | /api/v1/companies/search/{name} | Get companies ids for given name | |
| POST   | /api/v1/companies | Add company | [JSON](#companyrequest) |

### Invoice extractor service

This microservice is used to extract invoices for given companies from image using AWS Textract

| Method | Url | Description | Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| POST    | /extract/invoice | Extract invoice from | |

### Invoice management service

This microservice is used to make CRUD and aggregation oppreations on invoices.

| Method | Url | Description | Valid Request Body |
| ------ | --- | ----------- | ------------------------- |
| GET    | /api/v1/invoices | Get all invoices | |
| GET    | /api/v1/invoices/{id} | Get invoice by id | |
| DELETE | /api/v1/invoices/{id} | Delete invoice by id | |
| POST   | /api/v1/invoices | Add invoice | [JSON](#invoicerequest) |
| PUT    | /api/v1/invoices/{id} | Update invoice | [JSON](#invoicerequest) |
| GET | /api/v1/invoices/summary | Get summary information for invoices | |


## Valid JSON Requests

##### <a id="companyrequest">Save Company -> /api/v1/companies</a>
```json
{
	"name": "Lorem Ipsum Sp. z o.o.",
	"street": "Street Number 4",
	"city": "New York",
	"postalCode": "01-001",
	"nip": "123-456-78-90",
	"regon": "12345678"
}
```

##### <a id="invoicerequest">Login -> /api/v1/invoices</a>
```json
{
	"number": "1/123/2024",
	"createdAt": "2024-01-12",
  "seller": {
  	"name": "Lorem Ipsum Sp. z o.o.",
  	"street": "Street Number 4",
  	"city": "New York",
  	"postalCode": "01-001",
  	"nip": "123-456-78-90",
  	"regon": "12345678"
  },
  "buyer": {
  	"name": "Lorem Ipsum Sp. z o.o.",
  	"street": "Street Number 4",
  	"city": "New York",
  	"postalCode": "01-001",
  	"nip": "123-456-78-90",
  	"regon": "12345678"
  },
  "order": {
    "orderDetails": [
      {
        "positionNumber": 1,
        "quantity": 2,
        "discount": 0,
        "product": {
          "name": "Bread",
          "unitOfMeasure": "szt.",
          "netPrice": 4.99,
          "vat": 8
        }
      },
      {
        "positionNumber": 2,
        "quantity": 1,
        "discount": 0,
        "product": {
          "name": "Water 0,33ml",
          "unitOfMeasure": "szt.",
          "netPrice": 2.99,
          "vat": 23
        }
      }
  ]}
}
```
