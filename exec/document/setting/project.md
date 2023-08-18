# 프로젝트 구조 및 셋팅

## 프로젝트 구조

```bash
.
├── Back-End
│   ├── fashionity
│   │   ├── build
│   │   │   ├── classes
│   │   │   │   └── java
│   │   │   ├── generated
│   │   │   │   └── sources
│   │   │   ├── libs
│   │   │   ├── resources
│   │   │   │   └── main
│   │   │   └── tmp
│   │   │       ├── bootJar
│   │   │       └── compileJava
│   │   ├── gradle
│   │   │   └── wrapper
│   │   └── src
│   │       ├── main
│   │       │   ├── java
│   │       │   └── resources
│   │       └── test
│   │           ├── java
│   │           └── resources
│   └── socketserver
│       ├── build
│       │   ├── classes
│       │   │   └── java
│       │   ├── generated
│       │   │   └── sources
│       │   ├── libs
│       │   ├── resources
│       │   │   └── main
│       │   └── tmp
│       │       ├── bootJar
│       │       └── compileJava
│       ├── gradle
│       │   └── wrapper
│       └── src
│           ├── main
│           │   ├── java
│           │   └── resources
│           └── test
│               └── java
├── Front-End
│   └── fashionity
│       ├── public
│       └── src
│           ├── api
│           ├── assets
│           │   └── img
│           ├── components
│           │   ├── layout
│           │   └── pages
│           ├── plugins
│           ├── router
│           ├── store
│           │   └── modules
│           └── views
└── img
```

## Front 환경변수 설정

### Front-End/fashionity 경로에 아래 파일을 추가한다

**.env**

```bash
VUE_APP_API_URL=${메인 서버의 URL}
VUE_APP_REDIRECT_URL=${OAUTH의 Redirect URL}
VUE_APP_SOCKET_URL=${SOCKET서버의 URL}
```

## Back (메인) 환경설정

### Back-End/fashionity/src/main/resources 폴더에 아래와 같은 정보를 추가한다.

****\*\*\*\*****\*\*\*\*****\*\*\*\*****application.properties****\*\*\*\*****\*\*\*\*****\*\*\*\*****

```
server.port= ${겹치지않는 원하는 포트}

spring.profiles.include=db,jwt,oauth,mail,s3

# 파일관련
spring.servlet.multipart.max-file-size= ${입력받는 파일의 최대 크기}
spring.servlet.multipart.maxRequestSize= ${요청받을 수 있는 최대 크기}
```

****\*\*****\*\*\*\*****\*\*****application-db.properties****\*\*****\*\*\*\*****\*\*****

```
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
# 아래 커넥션에 필요한 정보는 docker-compose.yml에서 환경변수로 주입할 예정
# spring.datasource.url= ${db url}
# spring.datasource.password= ${db password}
# spring.datasource.username= ${db username}

spring.jpa.hibernate.ddl-auto=none
spring.jpa.database-platform=org.hibernate.dialect.MySQL5Dialect
spring.jpa.properties.hibernate.format_sql=false
spring.jpa.show-sql=false
```

**********\*\*\*\***********\*\*\*\***********\*\*\*\***********application-jwt.properties**********\*\*\*\***********\*\*\*\***********\*\*\*\***********

```
secret-key: ${jwt secret-key}
expiration-hours: ${토큰 만료 시간}
issuer: ${발행자}
access-token-expire: ${accessToken 만료 시간}
refresh-token-expire: ${refreshToken 만료 시간}
```

****\*\*****\*\*\*\*****\*\*****application-mail.properties****\*\*****\*\*\*\*****\*\*****

```
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${계정}
spring.mail.password=${google app에서 발급받은 비밀번호}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.timeout=${time out 시간 설정}
spring.mail.properties.mail.smtp.starttls.enable=true
```

************\*\*************\*\*\*\*************\*\*************application-oauth.properties************\*\*************\*\*\*\*************\*\*************

```
spring.security.oauth2.client.registration.google.client-name=google
spring.security.oauth2.client.registration.google.client-id=${발급받은 구글 client-id}
spring.security.oauth2.client.registration.google.client-secret=${발급받은 구글 client-secret}
spring.security.oauth2.client.registration.google.scope=profile,email
spring.security.oauth2.client.registration.google.redirect-uri=${설정한 redirect uri}

spring.security.oauth2.client.registration.naver.client-name=naver
spring.security.oauth2.client.registration.naver.client-id=${발급받은 네이버 client-id}
spring.security.oauth2.client.registration.naver.client-secret=${발급받은 네이버 client-secret}
spring.security.oauth2.client.registration.naver.scope=profile,email
spring.security.oauth2.client.registration.naver.redirect-uri=${설정한 redirect uri}
spring.security.oauth2.client.registration.naver.authorization-grant-type=authorization_code

spring.security.oauth2.client.registration.kakao.client-name=kakao
spring.security.oauth2.client.registration.kakao.client-id=${발급받은 카카오 client-id}
spring.security.oauth2.client.registration.kakao.client-secret=${발급받은 카카오 client-secret}
spring.security.oauth2.client.registration.kakao.scope=profile_nickname, account_email, profile_image
spring.security.oauth2.client.registration.kakao.redirect-uri=${설정한 redirect-uri}
spring.security.oauth2.client.registration.kakao.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.kakao.client-authentication-method=POST

# provider
spring.security.oauth2.client.provider.naver.authorization-uri=https://nid.naver.com/oauth2.0/authorize
spring.security.oauth2.client.provider.naver.token-uri=https://nid.naver.com/oauth2.0/token
spring.security.oauth2.client.provider.naver.user-info-uri=https://openapi.naver.com/v1/nid/me
spring.security.oauth2.client.provider.naver.user-name-attribute=response
# 회원정보를 json으로 받는데 response라는 키값으로 네이버가 return해줌.(value에는 getAttributes()한 값들이 들어가있음)
spring.security.oauth2.client.provider.kakao.authorization-uri=https://kauth.kakao.com/oauth/authorize
spring.security.oauth2.client.provider.kakao.token-uri=https://kauth.kakao.com/oauth/token
spring.security.oauth2.client.provider.kakao.user-info-uri=https://kapi.kakao.com/v2/user/me
spring.security.oauth2.client.provider.kakao.user-name-attribute=id
```

****\*\*****\*\*****\*\*****application-s3.properties****\*\*****\*\*****\*\*****

```
cloud.aws.stack.auto=false
cloud.aws.region.static=${s3 지역 설정}
cloud.aws.credentials.access-key=${s3 accessToken}
cloud.aws.credentials.secret-key=${s3 secretKey}
cloud.aws.s3.bucket=${사용할 버킷 이름}
```

### Back-End/socketserver/src/main/resources 아래에 아래와 같은 파일을 추가한다.

********\*\*\*\*********\*\*\*\*********\*\*\*\*********application.properties********\*\*\*\*********\*\*\*\*********\*\*\*\*********

```docker
server.port=${소켓서버로 사용할 포트 지정}

spring.devtools.livereload.enabled=true
spring.devtools.restart.enabled=false
spring.freemarker.cache=false
spring.jackson.serialization.fail-on-empty-beans=false
```

# Docker 설정 파일 작성

### Back-End/fashionity 폴더에 아래와 같은 파일 작성

**\*\*\*\***\*\*\*\***\*\*\*\***Dockerfile**\*\*\*\***\*\*\*\***\*\*\*\***

```docker
FROM openjdk:11
ENV TZ=Asia/Seoul
ARG JAR_FILE=/build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
```

### Back-End/socket 폴더에 아래와 같은 파일 작성

**Dockerfile**

```docker
FROM openjdk:11
ENV TZ=Asia/Seoul
ARG JAR_FILE=/build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
```

### Front-End/fashionity 폴더에 아래와 같은 파일 작성

**Dockerfile**

```docker
FROM node:14 as build-stage
ENV TZ=Asia/Seoul
WORKDIR /app

COPY package*.json ./
RUN npm install
COPY . .
# RUN npm run build

#FROM nginx:stable-alpine as production-stage
#COPY nginx.conf /etc/nginx/nginx.conf
#COPY --from=build-stage /app/dist /usr/share/nginx/html
#EXPOSE 80

#CMD ["nginx", "-g","daemon off;"]

EXPOSE ${사용할 port 지정}
CMD ["npm","run","serve"]
```

### 최상위 폴더에 다음과 같은 파일 작성

******\*\*\*\*******\*\*\*\*******\*\*\*\*******docker-compose.yml******\*\*\*\*******\*\*\*\*******\*\*\*\*******

```yaml
version: "3"

services:
  back:
    image: back
    ports:
      - "8080:${메인서버에서 지정한 포트}"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mariadb://db:3306/fashionity?useUnicode=true&allowPublicKeyRetrieval=true&useSSL=false&characterEncoding=UTF-8
      SPRING_DATASOURCE_USERNAME: ${사용자 이름}
      SPRING_DATASOURCE_PASSWORD: ${사용자 비밀번호}
    depends_on:
      - db
  front:
    image: front
    ports:
      - "3333:${Front-End Dockerfile에서 지정한 Expose값}"
  db:
    image: mysql
    volumes:
      - ~/docker/mysql/etc/mysql/conf.d:/etc/mysql/conf.d:ro
      - ~/docker/mysql/var/lib/mysql:/var/lib/mysql
      - ~/docker/mysql/var/log/mysql:/var/log/mysql
    environment:
      TZ: Asia/Seoul
      MYSQL_DATABASE: fashionity
      MYSQL_ROOT_HOST: "%"
      MYSQL_ROOT_PASSWORD: ${루트 패스워드 지정}
    command:
      [
        "--character-set-server=utf8mb4",
        "--collation-server=utf8mb4_unicode_ci",
      ]
    ports:
      - "3306:3306"
  socket:
    image: socket
    ports:
      - "8081:${소켓서버에서 지정한 port번호}"
```

# Nginx.conf 수정 및 재실행

### proxy pass 및 client_max_body_size 설정

```bash
# 이동
$ sudo su
$ vi /etc/nginx/nginx.conf
```

```bash
# http에 작성
server{
                server_name ${도메인 이름};
                location / {
                        proxy_set_header HOST $host;
                        proxy_pass http://localhost:${docker-compose에서 지정한 front 포트};
#                       proxy_redirect off;
                        proxy_set_header X-Real-IP $remote_addr;
                        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                }

                location /api/ {
                        proxy_pass http://localhost:${docker-compose에서 지정한 api서버 포트};
                }

                location /login/ {
                        proxy_pass http://localhost:${docker-compose에서 지정한 api서버 포트};
                }

                location /oauth2/ {
                        proxy_pass http://localhost:${docker-compose에서 지정한 api서버 포트};
                }

                location /ws/ {
                        proxy_pass http://localhost:${docker-compose에서 지정한 소켓서버 포트};

                        proxy_http_version 1.1;
                        proxy_set_header Upgrade $http_upgrade;
                        proxy_set_header Connection "Upgrade";
                        proxy_set_header Host $host;
                }

								# 하위는 certbot이 자동으로 설정해준 파일

        }
        client_max_body_size ${최대 사이즈 지정};
```

## nginx 재실행

```bash
$ sudo service nginx restart
```

## 프로젝트 최상위 폴더에 아래와같은 스크립트 작성

\***\*\*\*\*\***up.sh\***\*\*\*\*\***

```bash
sudo docker-compose down
sudo docker rmi front
sudo docker rmi back
sudo docker rmi socket
cd Back-End/fashionity
sudo chmod u+x gradlew
./gradlew bootjar
sudo docker build -t back .
cd ../socketserver
sudo chmod u+x gradlew
./gradlew bootjar
sudo docker build -t socket .
cd ../../Front-End/fashionity
sudo docker build -t front .
cd ../..
sudo docker-compose up -d
```

## 프로젝트 배포

프로젝트 최상위 폴더에서 아래와 같은 명령어 실행

```bash
$ sudo su
$ ./up.sh
```

## DB 유저 생성 및 권한 설정

**DB 도커 컨테이너의 /bin/bash로 진입**

```bash
$ sudo docker ps -a
>>
---
실행중인 도커 컨테이너들이 보임. 우리가 올린 db container의 이름 혹은 id값을 확인

$ sudo docker exec -it ${db container의 이름 or id} /bin/bash
```

************\*\*\*\*************\*\*\*\*************\*\*\*\*************컨테이너 속에서 db접속************\*\*\*\*************\*\*\*\*************\*\*\*\*************

```bash
$ mysql -u root -p
Enter password : ${docker-compose.yml에서 설정한 루트 패스워드}
```

### 유저 생성

```sql
use mysql;
create user '${docker-compose.yml에서 springboot environment에 작성한 유저 이름}'@'%'
identified by '${docker-compose.yml에서 springboot environment에 작성한 유저 password}';

flush privileges;

grant select,insert,update,delete on fashionity.* to '${docker-compose.yml에서 springboot environment에 작성한 유저 이름}'@'%'
```

### 서비스 재배포

```sql
$ sudo su
$ ./up.sh
```
