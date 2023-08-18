# Jwt

### 토큰 기반의 인증

---

💡 사용자의 인증은 JWT 기반으로 진행하였다.

네트워크에서의 흐름은 기본적으로 stateless 하기에 사용자의 정보를 요청에 가지고 있어야 한다.

세션 방식을 사용한다면, 사용자의 정보를 서버가 가지고 있어야 하고, MSA 방식으로 여러 서버를 두고 있을 경우 모든 사용자의 정보를 모든 서버에서 동기화 시켜줘야 하는 번거로움이 있다.

JWT는 세션 방식과 다르게 payload에 사용자 정보를 가지고 있고 이를 secretKey로 검증하기에 서버에서 사용자의 정보를 가지고 있을 필요가 없다.

🤔 토큰이 탈취되면 위험하지 않나?

이를 대비하기 위해 AccessToken과 RefreshToken을 구분하여 사용하였다.

AccessToken의 유효기간은 30분정도로 비교적 짧게 가져갔고, RefreshToken의 유효기간은 2주로 세팅하여 비교적 길게 가져갔다.

AccessToken을 실질적인 사용자의 정보가 담겨져 있고 RefreshToken은 단순히 AccessToken을 재발급 받기 위한 토큰으로 어떠한 사용자 정보도 담겨 있지 않다.

AccessToken의 짧은 유효기간으로 인해 토큰이 탈취되어도 비교적 위험이 적고 RefreshToken을 2주로 세팅하여 사용자는 토큰이 만료되어 로그아웃되는 일이 빈번하지 않아 서비스에 대한 만족도를 높일 수 있다.

추가로 RefreshToken은 쿠키에 저장하고 AccessToken은 헤더를 통해 클라이언트와 서버간 통신을 진행하는데, 쿠키는 HttpOnly Option으로 XSS공격과 같은 스크립트를 통한 토큰의 탈취를 방지할 수 있고, secure옵션을 통해 HTTPS 환경에서 암호화된 데이터로 쿠키가 전달될 수 있다.

물론 쿠키는 csrf 공격에 취약하다는 단점이 있지만, RefreshToken은 사용자에 대한 정보가 전혀 없기 때문에 권한을 가지고 있지 않다!

application-jwt.properties

```java
secret-key: ${SECRET_KEY}
expiration-hours: 3
issuer: bsrg
access-token-expire: 1800
refresh-token-expire: 1209600
```

### 토큰 사용

---

Fashionity 서비스는 로그인시 RefreshToken을 쿠키에 저장하고 AccessToken을 세션 스토리지에 저장한다.

이후 요청에서는 헤더에 Bearer {AccessToken} 형태로 토큰을 담아 요청을 보내고

AccessToken이 만료되었다는 서버 응답이 올 경우, RefreshToken을 통해 재발급을 진행한다.

RefreshToken의 유효기간이 만료될 경우 로그아웃이 되어 다시 로그인을 진행해야 한다.
