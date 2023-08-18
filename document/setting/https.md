# HTTPS 적용

### 준비물

- 도메인 ( 해당 서버에 연결되어 있어야함 )
- Linux 환경

## 80번 포트 및 443포트 방화벽 해제

```bash
$ sudo ufw allow 80
$ sudo ufw allow 80/tcp
$ sudo ufw allow 443
$ sudo ufw allow 443/tcp
$ sudo ufw enable
$ sudo ufw status
>>
```

root@ip-172-26-10-132:/home/ubuntu/20230817# ufw status
Status: active

To Action From

---

80 ALLOW Anywhere
80/tcp ALLOW Anywhere
22 ALLOW Anywhere
80 (v6) ALLOW Anywhere (v6)
22 (v6) ALLOW Anywhere (v6)
443 ALLOW Anywhere
443 (v6) ALLOW Anywhere (v6)

````

## 기본 라이브러리 설치

```bash
$ sudo apt-get install -y build-essential
$ sudo apt-get install curl
````

## Nginx 설치

```bash
$ sudo apt-get install nginx
```

## nginx.conf 접근

```bash
$ sudo su
$ vi /etc/nginx/nginx.conf
```

## nginx.conf 설정 추가

```bash
include /etc/nginx/conf.d/*.conf;
include /etc/nginx/site-enabled/*;

# include 아래 내용 추가
server {
       server_name [도메인 주소]
       listen 80;
       location / {
                proxy_set_header HOST $host;
                proxy_pass http://127.0.0.1:8080;
                proxy_redirect off;
       }
}
```

## nginx 재실행

```bash
$ sudo service nginx restart
```

## certbot 설치 및 SSL 인증서 발급

```bash
$ sudo apt-get remove certbot
$ sudo snap install --classic certbot

# nginx 자동 설정
$ sudo certbot --nginx
```

요구하는 yes or no, email, domain을 입력하며 SSL 인증서 설치

같은 도메인으로 많은 요청이 들어오면 안내해주는 날까지 기다려야함

## nginx.conf 파일 수정을 통한 redirect

```bash
# nginx.conf 파일
# 아래 코드 추가
server {
    if ($host = [도메인 주소]) {
        return 301 https://$host$request_uri;
    } # managed by Certbot
    server_name [도메인 주소]
    listen 80;
# 아래 내용 주석 또는 삭제
#    return 404; # managed by Certbot
# 추가
	  return 301 https://$host$request_uri;
```

80번 포트 (HTTP) 로 들어온 요청을 443포트 (HTTPS) 로 REDIRECT
