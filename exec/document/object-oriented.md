# 객체지향적 코드

💡자바의 객체지향적 특징을 극대화하기 위해 객체지향적 코드를 작성했다.

하나의 클래스는 하나의 역할을 수행하고 각 클래스는 독립성을 높이고 결합도를 낮추어 진행했다.

## 소셜 로그인

---

`OAuthProvider` Enum 파일에 Google, Naver, Kakao 클래스를 정의하고 Oauth 를 통해 제공받은 사용자 정보를 각 Provider 타입에 맞게 설정해주고 소셜 로그인된 사용자 정보를 가지고 있는 공통된 `OAuthUserInfo` 클래스를 만들었다.

```bash
@Getter
@RequiredArgsConstructor
@Slf4j
public enum OAuthProvider {
    GOOGLE("google") {
        @Override
        public OAuthUserInfo toUserInfo(OAuth2User oauth2User) {
            Map<String, Object> attributes = oauth2User.getAttributes();

            return OAuthUserInfo.builder()
                    .provider(GOOGLE.name)
                    .email(String.valueOf(attributes.get("email")))
                    .nickname(String.valueOf(attributes.get("name")))
                    .oauthId(String.valueOf(oauth2User.getName()))
                    .profileImgUrl(String.valueOf(attributes.get("picture")))
                    .build();
        }
    },

    NAVER("naver") {
        @Override
        public OAuthUserInfo toUserInfo(OAuth2User oauth2User) {
            Map<String, Object> attributes = oauth2User.getAttribute("response");
            return OAuthUserInfo.builder()
                    .provider(NAVER.name)
                    .email(String.valueOf(attributes.get("email")))
                    .nickname(String.valueOf(attributes.get("nickname")))
                    .oauthId(String.valueOf(attributes.get("id")))
                    .profileImgUrl(String.valueOf(attributes.get("profile_image")))
                    .build();
        }
    },
    KAKAO("kakao") {
        @Override
        public OAuthUserInfo toUserInfo(OAuth2User oauth2User) {
            Map<String, Object> attributes = oauth2User.getAttributes();
            Map<String, Object> properties = oauth2User.getAttribute("properties");
            Map<String, Object> account = oauth2User.getAttribute("kakao_account");
            log.info(attributes.toString());
            log.info(properties.toString());
            log.info(account.toString());
            return OAuthUserInfo.builder()
                    .provider(KAKAO.name)
                    .email(String.valueOf(account.get("email")))
                    .nickname(String.valueOf(properties.get("nickname")))
                    .oauthId(String.valueOf(attributes.get("id")))
                    .profileImgUrl(String.valueOf(properties.get("profile_image")))
                    .build();
        }
    };

    private static final Map<String, OAuthProvider> PROVIDERS =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(OAuthProvider::getName, Function.identity())));

    private final String name;

    public static OAuthProvider getOAuthProviderByName(String providerName) {
        if (!PROVIDERS.containsKey(providerName)) {
            throw new IllegalArgumentException("지원하지 않는 로그인입니다.");
        }
        return PROVIDERS.get(providerName);
    }

    public abstract OAuthUserInfo toUserInfo(OAuth2User oauth2User);
}
```

## 에러 처리

---

🤔에러 처리를 굳이 해야 하나?

서버에서 에러가 발생하면 에러 코드가 클라이언트로 바로 반환되는것 처럼 보이지만, 사실은 그렇지 않다.

`클라이언트 > 서버 > 🎇에러 발생 > 클라이언트 > 서버에게 에러에 대한 페이지 요청 > 클라이언트`

결국은 요청이 2번 진행되어 불필요한 요청이 발생한다.

이를 방지하기 위해 발생하는 에러를 서버에서 캐치해 클라이언트에 적절히 응답하는 작업이 필요하다.

💡전역 에러 처리

`@ControllerAdvice` 는 비즈니스 로직에서 발생하는 에러를 감지할 수 있다.

이후 `@ExceptionHandler`에서 특정 Exception의 형태에 맞춰 에러를 반환할 수 있다.

```java
@RestControllerAdvice
@Getter
public class GlobalExceptionHandler {

    // CustomException을 상속받은 모든 에러를 처리하는 Handler
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        e.printStackTrace();
        return ResponseEntity.status(errorCode.getStatus())
                .body(ErrorResponse.of(errorCode));
    }
}
```

```java
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ErrorResponse{

    private String code;
    private String message;

    public static ErrorResponse of (ErrorCode code) {
        return new ErrorResponse(code.getCode(), code.getMessage());
    }
}
```

일관된 에러 처리를 담당하기 위해 ErrorResponse 를 만들어줬고 에러 코트와 에러 메시지를 담아 보내준다.

```java
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력 값입니다."),
    MISSING_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C002", "인자가 부족합니다."),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "C003", "접근권한이 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C004", "사용할 수 없는 메서드입니다."),
    NOT_EXIST_API(HttpStatus.BAD_REQUEST, "C005", "요청 주소가 올바르지 않습니다."),
    INVALID_PATH_VALUE(HttpStatus.BAD_REQUEST,"C006","요청이 잘못됐습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C007", "서버 에러"),

		...
}
```

에러 코드와 에러 메시지를 ErroCode enum으로 관리해줬고 비즈니스 로직에 맞추어 발생할 수 있는 에러 상황에 대한 커스텀을 진행할 수 있게 했다.

객체지향적 코드를 통해 클래스별로 하나의 역할을 지정해 확장성 있는 개발을 진행할 수 있게 했다.
