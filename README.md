
# project-management-java-rest-api

Based on a Microservice Architecture, this project has been designed and developed as a solution for "Problem Statement #1: Project Mgmt Micro App" of "Weekly Status Reporting Project". This is a simple REST API based application developed with JAVA, Spring Boot, and Spring Framework to manage Project related CRUD operations.


## Features

This API provides HTTP endpoints for the following operations:

- Create and manage company/organization level Projects.
- Find individual Project by id and name.
- Find Projects in a sorted and paginated form.
- Create and manage mailing list for individual Project.

## Requirements

- Java 1.8+
- Maven 3.0+
- Docker Engine
- Latest MySQL

## Tech Stack

- Java 1.8+
- Maven 3.0+
- Spring Boot 2.7.0+
- JUnit 5
- Docker Engine
- Latest MySQL 8.0+

## Run Locally

Clone the project

```bash
  git clone https://github.com/VipulJadhav12/project-management-java-rest-api
```

Go to the project directory

```bash
  cd project-management-java-rest-api
```

Open and edit the src/main/resources/application.properties file

```bash
  server.port=<PORT_NO>
  server.error.include-message=always

  spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
  spring.datasource.url=jdbc:mysql://localhost:3306/<DATABASE_NAME>?autoReconnect=true&useSSL=false&createDatabaseIfNotExist=true
  spring.datasource.username=<USERNAME>
  spring.datasource.password=<PASSWORD>
  spring.jpa.generate-ddl=true
  spring.jpa.show-sql=true
  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

Pull the public MySQL docker image and run it

```bash
  docker pull mysql

  docker run --name some-mysql -e MYSQL_ROOT_PASSWORD=my-secret-pw -d mysql:tag
```

Run the above source code through mvn command line

```bash
  mvn spring-boot:run
```

Compile and Package the above source code as a JAR

```bash
  mvn clean package

  or

  mvn clean package -Dmaven.test.skip=true
```

Run the above packaged source code through java command line. For that, go to the target directory

```bash
  java -jar project-management-rest-api-0.0.1-SNAPSHOT.jar
```

By default, the API will be available at

```bash
  http://localhost:<PORT_NO>/api/v1/projects

  and

  http://localhost:<PORT_NO>/api/v1/emails
```

## API Reference

### List of Project level APIs:

#### Get default health check

```http
  GET /api/v1/projects
```
#### Get project by ID

```http
  GET /api/v1/projects/getBy=ID/project/{projectId}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `projectId`      | `long` | **Required**. Id of project to fetch, must not be negative. |

#### Get project by NAME

```http
  GET /api/v1/projects/getBy=NAME/project/{projectName}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `projectName`      | `long` | **Required**. Name of project to fetch. |

#### Get all projects

```http
  GET /api/v1/projects/getAllBy=NONE
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `NA` | `NA` |  |

#### Get all projects with pagination and sorted by given FIELD in ASCENDING order

```http
  GET /api/v1/projects/getAllBy=NONE/pagination=TRUE/offset/{offset}/pageSize/{pageSize}/sort=ASC/sortBy/{field}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `offset` | `int` | **Required**. Zero-based page index, must not be negative. |
| `pageSize` | `int` | **Required**. Size of the page to be returned, must be greater than 0. |
| `field` | `string` | **Required**. Field name of the project entity. |

#### Get all projects with pagination and sorted by given FIELD in DESCENDING order

```http
  GET /api/v1/projects/getAllBy=NONE/pagination=TRUE/offset/{offset}/pageSize/{pageSize}/sort=DESC/sortBy/{field}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `offset` | `int` | **Required**. Zero-based page index, must not be negative. |
| `pageSize` | `int` | **Required**. Size of the page to be returned, must be greater than 0. |
| `field` | `string` | **Required**. Field name of the project entity. |

#### Add a project

```http
  POST /api/v1/projects/add
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `NA` | `NA` |  |

| Data Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `{"name": "Project #1", "startDate": "yyyy-MM-dd", "endDate": "yyyy-MM-dd", "managerName": "Thomas Hardy", "managerEmail": "thardy@myorg.com"` | `JSON` |  |

#### Update a project

```http
  PUT /api/v1/projects/updateBy=ID/project/{projectId}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `projectId` | `long` | **Required**. Id of project to fetch and update, must not be negative. |

| Data Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `{"name": "Project #1", "startDate": "yyyy-MM-dd", "endDate": "yyyy-MM-dd", "managerName": "Thomas Hardy", "managerEmail": "thardy@myorg.com"` | `JSON` |  |

#### Assign an email to a project

```http
  PUT /api/v1/projects/assignEmail/project/{projectId}/email/{mailId}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `projectId` | `long` | **Required**. Id of project to fetch, must not be negative. |
| `mailId` | `long` | **Required**. Id of an email to assign, must not be negative. |

#### Remove an email from a project

```http
  PUT /api/v1/projects/removeEmail/project/{projectId}/email/{mailId}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `projectId` | `long` | **Required**. Id of project to fetch, must not be negative. |
| `mailId` | `long` | **Required**. Id of an email to remove, must not be negative. |

#### Delete a project by ID

```http
  DELETE /api/v1/projects/deleteBy=ID/project/{projectId}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `projectId` | `long` | **Required**. Id of project to delete, must not be negative. |

#### Delete all projects

```http
  DELETE /api/v1/projects/deleteAllBy=NONE
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `NA` | `NA` |  |

### List of Mailing-List level APIs:

#### Get default health check

```http
  GET /api/v1/emails
```
#### Get email by ID

```http
  GET /api/v1/emails/getBy=ID/email/{mailId}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `mailId`  | `long`   | **Required**. Id of email to fetch, must not be negative. |

#### Get email by RECIPIENT_NAME

```http
  GET /api/v1/emails/getBy=RECIPIENT_NAME/recipient_name/{recipientName}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `recipientName`  | `string`   | **Required**. Recipient name of email to fetch. |

#### Get all emails

```http
  GET /api/v1/emails/getAllBy=NONE
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `NA`  | `NA`   |  |

#### Get all un-assigned emails by PROJECT_ID

```http
  GET /api/v1/emails/getAllUnAssignedBy=PROJECT_ID/project/{projectId}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `projectId`  | `long`   | **Required**. Id of project for which un-assigned emails to be fetch, must not be negative. |

#### Add an email

```http
  POST /api/v1/emails/add
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `NA`  | `NA`   |  |

| Data Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `{ "recipientName": "John Smith", "email": "foo.bar@myorg.com" }` | `JSON` |  |

#### Update an email

```http
  PUT /api/v1/emails/updateBy=ID/email/{mailId}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `mailId`  | `long`   | **Required**. Id of email to fetch and update, must not be negative. |

| Data Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `{ "recipientName": "John Smith", "email": "foo.bar@myorg.com" }` | `JSON` |  |

#### Delete an email by ID

```http
  DELETE /api/v1/emails/deleteBy=ID/email/{mailId}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `mailId`  | `long`   | **Required**. Id of email to delete, must not be negative. |

#### Delete an email by ID assigned to a project

```http
  DELETE /api/v1/emails/deleteBy=ID/project/{projectId}/email/{mailId}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `projectId`  | `long`   | **Required**. Id of project to fetch, must not be negative. |
| `mailId`  | `long`   | **Required**. Id of email to delete, must not be negative. |

#### Delete all emails

```http
  DELETE /api/v1/emails/deleteAllBy=NONE
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `NA`  | `NA`   |  |

