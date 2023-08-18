# 소셜 로그인

## OAuth2.0

---

사용자의 회원가입 및 로그인에 대한 번거로움을 개선하기 위해 `GOOGLE`, `NAVER` , `KAKAO` 를 이용한 소셜 로그인을 진행했다.

application-oauth.properties

```java
spring.security.oauth2.client.registration.google.client-name=google
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_SECRET}
spring.security.oauth2.client.registration.google.scope=profile,email
spring.security.oauth2.client.registration.google.redirect-uri=http://localhost:8080/login/oauth2/code/google

spring.security.oauth2.client.registration.naver.client-name=naver
spring.security.oauth2.client.registration.naver.client-id=${NAVER_ID}
spring.security.oauth2.client.registration.naver.client-secret=${NAVER_SECRET}
spring.security.oauth2.client.registration.naver.scope=profile,email
spring.security.oauth2.client.registration.naver.redirect-uri=http://localhost:8080/login/oauth2/code/naver
spring.security.oauth2.client.registration.naver.authorization-grant-type=authorization_code

spring.security.oauth2.client.registration.kakao.client-name=kakao
spring.security.oauth2.client.registration.kakao.client-id=${KAKAO_ID}
spring.security.oauth2.client.registration.kakao.client-secret=${KAKAO_SECRET}
spring.security.oauth2.client.registration.kakao.scope=profile_nickname, account_email, profile_image
spring.security.oauth2.client.registration.kakao.redirect-uri=http://localhost:8080/login/oauth2/code/kakao
spring.security.oauth2.client.registration.kakao.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.kakao.client-authentication-method=POST

# 네이버와 카카오에 대한 추가 정보
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

[카카오 개발자 센터](https://developers.kakao.com)

[네이버 개발자 센터](https://developers.naver.com/main/)

[구글 개발자 센터](https://console.cloud.google.com/apis)

위 Provider에서 내 애플리케이션을 만들고 발급받은 id, secret을 properties에 등록해준다.

그리고 authCode 를 받을 redirectUri 와 제공받을 사용자 정보의 범위를 추가로 등록해준다.

추가로 스프링 시큐리티는 구글 로그인에 대해서는 oauth 과정이 원활히 진행되도록 세팅이 되어 있지만, Naver와 Kakao는 해당 세팅이 되어 있지 않아 properties에 추가해줘야 한다.

Fashionity 서비스에서는 이메일이 unique하기에 소셜로그인을 성공적으로 수행하면, DB에서 이메일 존재 여부를 확인 후 회원가입 시키거나 DB에 있는 유저의 데이터를 제공해준다.
