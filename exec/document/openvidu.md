# OpenVidu

<aside>
💡 주의사항
포트 관리에 자신이 없다면 무조건 OpenVidu를 먼저 올리는것을 추천합니다.
OpneVidu가 사용하는 포트가 굉장히 많은데 그중 한 개라도 사용할 수없으면 OpenVidu가 돌아가지 않습니다!!

</aside>

OpenVidu가 사용하는 포트들

- 22
- 80
- 443
- 3478
- 5442
- 6379
- 8888
- 등등

설치 방법

1. OpenVidu On premises 설치

   ```jsx
   # 관리자 권한
   $ sudo su

   # openvidu가 설치되는 경로
   $ cd /opt

   # openvidu on promises 설치. 우리는 버전 2.20.0 사용
   $ curl https://s3-eu-west-1.amazonaws.com/aws.openvidu.io/install_openvidu_2.20.0.sh | bash

   $ exit

   $ cd openvidu
   ```

2. 기본 설정 .env

   ```jsx
   DOMAIN_OR_PUBLIC_IP=여기에 도메인
   OPENVIDU_SECRET=여기는 비밀번호

   # Certificate type:
   # - selfsigned:  Self signed certificate. Not recommended for production use.
   #                Users will see an ERROR when connected to web page.
   # - owncert:     Valid certificate purchased in a Internet services company.
   #                Please put the certificates files inside folder ./owncert
   #                with names certificate.key and certificate.cert
   # - letsencrypt: Generate a new certificate using letsencrypt. Please set the
   #                required contact email for Let's Encrypt in LETSENCRYPT_EMAIL
   #                variable.
   CERTIFICATE_TYPE=letsencrypt

   LETSENCRYPT_EMAIL=사용가능한이메일
   ```

   위 설정을 마친 후 /opt/openvidu에서 ./openvidu start하여 Https 인증서 발급

3. 만들어진 SSL 인증서를 pkcs12로 변경

   ```jsx
   cd /opt/openvidu/certificates/live/서버 도메인
   //위 경로에서
   openssl pkcs12 -export -in fullchain.pem -inkey privkey.pem -out 파일명.p12 --name 이름 -CAfile chain.pem -caname root
   ```

4. OpenVidu on premise를 /home/ubuntu 에서 Opnevidu 폴더 생성 후 git clone

   ```jsx
    git clone https://github.com/OpenVidu/openvidu.git -b v2.20.0
   ```

5. openvidu-server 폴더의 src/main/resources의 [application.properties](http://application.properties) 수정

   ```jsx
   server.address=0.0.0.0
   server.ssl.enabled=true
   server.ssl.key-store=classpath:여기에 발급받은 ssl 인증서
   server.ssl.key-store-password=여기에 인증서 발급받을 때 사용한 비밀번호
   server.ssl.key-store-type=PKCS12
   server.servlet.session.cookie.name=OVJSESSIONID

   logging.level.root=info
   spring.main.allow-bean-definition-overriding=true

   SUPPORT_DEPRECATED_API=true

   DOTENV_PATH=.

   DOMAIN_OR_PUBLIC_IP=여기에 서버 도메인
   OPENVIDU_SECRET=여기에 2번에서 설정한 비밀번호
   CERTIFICATE_TYPE=selfsigned
   HTTPS_PORT=5443 => 사용할 포트
   KMS_URIS=["ws://localhost:8888/kurento"]
   ```

6. 위 설정 완료 후 openvidu-server/docker/openvidu-server에서 아래 명령어 실행

   ```jsx
   //docker image를 생성하는 .sh
   chmod 777 create_imgae.sh

   ./create_image.sh 2.20.여기에 넣고싶은 버전
   ```

7. opt/openvidu에서 docker-compose.yml파일 수정

   ```jsx
   openvidu-server:
           image: openvidu/openvidu-server:2.20.여기에 위에서 설정한 버전과 동일하게
           restart: on-failure
           network_mode: host
           entrypoint: ['/usr/local/bin/entrypoint.sh']
           volumes:
               - /var/run/docker.sock:/var/run/docker.sock
               - ${OPENVIDU_RECORDING_PATH}:${OPENVIDU_RECORDING_PATH}
               - ${OPENVIDU_RECORDING_CUSTOM_LAYOUT}:${OPENVIDU_RECORDING_CUSTOM_LAYOUT}
               - ${OPENVIDU_CDR_PATH}:${OPENVIDU_CDR_PATH}
           env_file:
               - .env
           environment:
               - SERVER_SSL_ENABLED=true
               - SERVER_PORT=여기는 application.properties에 작성한 포트번
               - KMS_URIS=["ws://localhost:8888/kurento"]
               - COTURN_REDIS_IP=127.0.0.1
               - COTURN_REDIS_PASSWORD=${OPENVIDU_SECRET}
               - COTURN_IP=${COTURN_IP:-auto-ipv4}
           logging:
               options:
                   max-size: "${DOCKER_LOGS_MAX_SIZE:-100M}"
   ```

8. opt/openvidu에서 아래 명령어 실행으로 OpenVidu 서버 실행
