
# WebRTC 개요
## WebRTC란? 
어느 device에서나 별도의 플러그인 없이 peer to peer 방식(서버를 거치지 않고  디바이스 간 직접적으로 통신하는 방식)으로 비디오, 음성, 텍스트, 파일 등 데이터를 실시간으로 전송하는 오픈 소스 웹 기반 기술이다. 최소한의 지연시간을 가지며(=즉각적이며), RTC 송수신 과정 중에 전달되는 데이터나 메시지들은 저장되지 않는다.

Web Socket 등을 이용해서 직접적으로 IP를 연결하기 때문에 방화벽이 존재하거나,  NAT 환경에서는 연결이 불가능하다.따라서 방화벽을 통과시켜주거나 IP 주소를 다시 한번 변환해주는 STUN 서버나 TURN 서버를 사용해야 한다.

## Signaling Server
### Signaling
서로 다른 네트워크에 있는 peer 들을 연결시키기 위해 각 peer(디바이스)들의 IP주소와 미디어 포맷 협의를 받아 socket을 이용한 서버에 연결하는 것이다. 이 작업을 해주는 서버를 Signaling Server 라고 한다.
### Signaling 과정
1. 디바이스 간 서로의 주소 공유
2. 보안사항 및 방화벽 우회

## NAT(Network Address Translation)
NAT는 IP 패킷에 있는 출발지/목적지의 IP주소를 변환시켜서 전달하는 기술이다. 
인터넷 공유기를 거쳐 외부의 웹서버에 접근할 때, private IP 주소가 그대로 외부 인터넷으로 전달되면 웹 서버는 해당 요청 패킷을 어디로 전송해야 할 지 알 수가 없다는 문제가 발생하는데, 이를 해결하기 위해  NAT를 활용한다.

### IP 주소 변환 과정
1. 패킷 헤더에 출발지 IP 주소(client private IP 주소)와 목적지(Web Server) IP 주소를 기록한다
2. 공유기가 외부로 나가는 패킷을 인식하면 출발지(client)의 private IP 주소를 자신의 public IP 주소로 변경하고, 프로토콜, 출발지의 private IP 주소, 출발지의 public IP 주소, 목적지의 IP주소를 기록한 테이블을 보관한다
3. 웹 서버에서 데이터를 처리한 후, 응답하는 패킷에 출발지(Web Server)와 목적지(client)의 public IP 주소를 기록하여 보낸다
4. 공유기가 웹 서버가 보낸 패킷을 받으면, 기록해두었던 테이블을 참조하여 client 의 private IP 주소로 변경하여 패킷을 전달한다 

### NAT 사용의 이점
1. IP 주소 절약: 하나의 공인 IP 주소를 사용해서 여러명이 인터넷에 접속할 수 있게 된다
2. 보안: IP를 숨길 수 있어 공격이 어려워진다

## STUN(Session Traversal Utilities for NAT) Server 
서버에서 받은 public IP를 개인을 식별할 수 있는 IP로 변환하여 연결 가능한 프로토콜과 포트 등을 사용자에게 전달해주어 P2P 연결을 가능하게 해준다. 
(단, 보안이 강한 NAT나 방화벽을 사용하는 경우, P2P IP 연결이 불가능하다.)

## TURN(Traversal Using Relays around NAT)
STUN 프로토콜로는 자신의 정보를 알아낼 수 없을 때 사용하는 대안이다. 