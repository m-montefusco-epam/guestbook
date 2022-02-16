Guest Book
=================

This is a demo guest book project written with Java and Spring.

This is a basic but functional demo. Some improvements can be made to make it a finished product.

### Scope ###
Create a guestbook application for a hotel.
* In homepage everybody can see approved posts.
* To write a post you have to login as USER profile.
* To approve, delete and modify entries posted by all the users you have to login as ADMIN profile.
* To logout move to Home page and click Logout

### HOW TO USE ###
This application need a database to work. In the example we are using MySQL.
Please configure your db in src/main/resources/application.properties

Create required tables using the sql commands in the file src/main/resources/create-schema.sql .

Create your own or use default users by sing the sql commands in the file src/main/resources/create-data.sql .
##### NOTE: #####
To create your own user password use bcrypt online tool :https://bcrypt.online/

Default users are
* gesposito / password (USER and ADMIN)
* jdoe / 12345 (USER)
* pgomez / 12345 (USER)

The application can be compiled with Maven and started as Spring Boot jar.

### Main Technologies ###
* Java
* Spring
* Spring Boot 2
* Spring Security
* Spring Web
* Thymeleaf
* Bootstrap
