# 도커 설치

### 업데이트 및 HTTP 패키지 설치

```bash
$ sudo apt update
$ sudo apt-get install -y ca-certificates \
    curl \
    software-properties-common \
    apt-transport-https \
    gnupg \
    lsb-release
```

### GPG 키 및 저장소 추가

```bash
$ sudo mkdir -p /etc/apt/keyrings
$ curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

$ echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
```

### 도커 엔진 설치

```bash
$ sudo apt update
$ sudo apt install docker-ce docker-ce-cli containerd.io
```

### 설치가능한 버전 확인

```bash
$ apt-cache madison docker-ce
```

### 도커 엔진 설치

위에서 확인한 설치가능한 버전을 아래에 대입하여 다운로드한다.

```bash
$ sudo apt-get install docker-ce=<VERSION_STRING> docker-ce-cli=<VERSION_STRING> containerd.io
```

### **버전에 맞는 설치파일(.deb) 내려받기**

https://download.docker.com/linux/ubuntu/dists/ 링크에서 `.deb` 파일을 통해 설치한다. 본인의 우분투 릴리즈 버전 선택 후 `pool/stable`로 이동하면 아키텍쳐(`amd64`, `arm64` 등)에 맞게 한번 더 들어가면 파일을 내려받을 수 있다.

### 설치

dpkg 명령어를 통해 해당 .deb파일을 설치한다

```bash
$ sudo dpkg -i /path/to/package.deb
```

### 웹에서 설치 스크립트를 받아 설치하는 방법

```bash
$ curl -fsSL https://get.docker.com -o get-docker.sh
$ sudo sh get-docker.sh
```

---

### 설치 확인

```bash
$ sudo docker version
>>
```

Docker version 24.0.5, build ced0996

````
## 도커 로그인

```bash
$ sudo docker login -u ${도커 아이디}
Password : ${도커 허브에서 가져온 Personal Access Token}
````
