# mail sender

## Java Mail Sender

---

application-mail.properties

```bash
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=silaegippo1
spring.mail.password=dlvlyjyytgufuwnq
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.starttls.enable=true
```

mail 라이브러리 추가

```bash
implementation 'org.springframework.boot:spring-boot-starter-mail'
```

메일을 보내려면 SMTP 서버가 있어야 한다. Fashionity 서비스는 gmail을 통한 이메일 전송을 택했다.

메일 서비스용 gmail 계정을 만들고 해당 메일에서 모든 pop 수신을 했다.

메일 서비스가 필요한 이유는 아이디 찾기 및 비밀번호 찾기 기능을 수행하기 위해서다.

아이디 찾기 : 사용자 이메일 바탕으로 메일로 아이디 보내줌

비밀번호 찾기 : 사용자 아이디 + 사용자 이메일 바탕으로 메일로 임시 발급한 비밀번호 보내줌

```bash
MimeMessage mimeMessage = javaMailSender.createMimeMessage();
MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "utf-8");
mimeMessageHelper.setTo(dto.getEmail());
mimeMessageHelper.setSubject("[FASHIONITY] 찾으시는 아이디입니다.");
mimeMessageHelper.setFrom("bsrg@fashionity.com");
mimeMessageHelper.setText(createMailForm("찾으시는 아이디", member.getId()), true);

javaMailSender.send(mimeMessage);
```

위와 같은 코드를 통해 간단히 mail을 보낼 수 있다.
