# 시큐리티

## 시큐리티

---

회원의 인증 및 인가에 대한 처리를 간편한 설정으로 쉽게 하기 위해 도입

```bash
// 스프링 시큐리티 라이브러리
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'
testImplementation 'org.springframework.security:spring-security-test'
```

💡 시큐리티 버전5 사용.

버전6은 스프링 Bean 주입 방식으로 인증 및 인가 진행, 버전5는 필터를 추가하는 방식으로 인증 및 인가 진행

Fashionity 서비스는 스프링 2.7 버전을 사용하기에 3.0 버전부터 사용가능한 시큐리티 버전6은 사용하지 않고 버전 5를 사용함

주요 기능

- `.antMatchers`
  ```bash
  .antMatchers(HttpMethod.*GET*, "/api/v1/posts/**").permitAll()
  .antMatchers(HttpMethod.POST, "/api/v1/posts/**").authenticated()
  ```
  HTTP 메소드와 url을 적어 허용할지 인증을 진행할지 선택할 수 있다.
- `authenticationEntryPoint`
  ```bash
  .authenticationEntryPoint(jwtAuthenticationEntryPoint)
  ```
  AuthenticationEntryPoint 를 상속받은 커스텀 EntryPoint 클래스를 정의해 인증 과정에서 에러 발생시 EntryPoint로 에러 값을 잡을 수 있다.
  공통된 에러 반환 형태를 정의한 경우 커스텀한 EntryPoint 에서 공통된 에러 반환 형태로 클라이언트에 값을 넘겨주면 된다.
- `oauth2Login`

  ```bash
  .oauth2Login()
  .authorizationEndpoint().baseUri("/oauth2/authorization")
  .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
  ```

  OauthLogin 을 제공해주는 시큐리티 필터이다.

  EndPoint()를 통해 해당 Uri로 요청이 들어오면 oauth 로그인을 진행함을 명시해줄 수 있다.

  예시) `{도메인}/oauth2/authorization/{provider}`

  - provider는 카카오, 네이버와 같이 사용자 정보를 제공해주는 제 3자이다.

  Provider와의 안전한 통신을 위해 `AuthorizationRequestRepository` 를 구현한 커스텀 httpCookieOAuth2AuthorizationRequestRepository를 사용하여 provider로의 요청 쿠키에 필요한 정보를 담아 보낸다.

- `successHandler`
  ```bash
  .successHandler(oAuth2AuthenticationSuccessHandler)
  ```
  successHandler 필터를 통해 OAuth 로그인을 성공적으로 수행했을 경우 이후 로직을 커스텀한 successHandler에서 수행한다.
- `failureHandler`

  ```bash
  .failureHandler(oAuth2AuthenticationFailureHandler)
  ```

  failureHandler 필터를 통해 OAuth 로그인을 실패했을 경우 이후 로직을 커스텀한 failureHandler에서 수행한다.

- `addFilterBefore`
  ```bash
  .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
  ```
  addFilterBefore를 통해 특정 필터가 수행되기 전에 다른 필터가 수행되도록 한다.
  jwtAuthenticationFilter 는 헤더에 Bearer 토큰으로 넘어오는 AccessToken 값을 검증하고 SpringSecurityContext에 사용자 정보를 저장하는 역할을 수행한다.
  이를 통해 유효하지 않은 토큰을 가지고 오는 요청을 필터 단에서 막을 수 있다.

### 권한

---

특정 요청에 대한 권한이 필요하기에 시큐리티에서 사용자에 권한을 세팅해줬다.

Fashionity 서비스에서는 (USER, CONSULTANT, ADMIN) 을 만들어 각 권한별로 수행가능한 역할을 만들었고 이를 SecurityContext와 DB에 저장하였다.
