# Local serve

Please run `serve.sh`.

# Build

Please run `build.sh`.

# Before deploy

Please add `application.properties`.

src\main\resources\application.properties

```sh
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://{public ip}:3306/birds_eye
spring.datasource.username=**********
spring.datasource.password=**********
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```

Mysql require a database and a user. If you haven't prepare them, please create.

SQL Example

```sql
CREATE DATABASE IF NOT EXISTS birds_eye

CREATE USER IF NOT EXISTS 'spring'@'{ec2 public ip}' IDENTIFIED BY '**********';
GRANT ALL PRIVILEGES ON *.* TO 'spring'@'{ec2 public ip}' IDENTIFIED BY '**********';
FLUSH PRIVILEGES;
```

# Deploy

Please run `deploy.sh`.
