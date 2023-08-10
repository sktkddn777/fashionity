<template>
  <div class="container" id="app" v-cloak>
    <div class="chat-header">
      <h2>채팅</h2>
    </div>
    <div class="chat-content">
      <div class="input-group">
        <div class="input-group-prepend">
          <label class="input-group-text">세션</label>
        </div>
        <input type="text" class="form-control" v-model="roomSession" />
        <div class="input-group-prepend">
          <label class="input-group-text">내용</label>
        </div>
        <input
          type="text"
          class="form-control"
          v-model="message"
          @keyup.enter="sendMessage"
        />
        <div class="input-group-append">
          <button class="btn btn-primary" type="button" @click="sendMessage">
            보내기
          </button>
        </div>
      </div>
      <ul class="message-list" ref="messageList">
        <li class="message-item" v-for="(item, idx) in recvList" :key="idx">
          <div class="message-content">
            <span class="user-name">{{ item.userName }}</span>
            <span class="message-text">{{ item.content }}</span>
          </div>
        </li>
      </ul>
    </div>
    <div class="footer"></div>
  </div>
</template>

<style>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  height: 100vh;
  background-color: #f4f4f4;
}

.chat-header {
  margin-bottom: 20px;
}

.chat-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.input-group {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.input-group-prepend,
.input-group-append {
  display: flex;
  align-items: center;
  margin-right: 10px;
}

.form-control {
  padding: 10px;
}

.btn-primary {
  padding: 10px 20px;
}

.message-list {
  list-style: none;
  padding: 0;
  width: 100%;
  max-height: 400px; /* 최대 높이 */
  overflow-y: auto; /* 스크롤바 표시 */
}

.message-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border: 1px solid #ddd;
  margin-bottom: 10px;
}

.message-content {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-weight: bold;
  margin-bottom: 5px;
}

.message-text {
  font-size: 14px;
}
</style>

<script>
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";

export default {
  name: "TheChatting",
  data() {
    return {
      userName: "태현",
      message: "",
      recvList: [],
      roomSession: "kth",
    };
  },
  created() {
    // Chatting.vue가 생성되면 소켓 연결을 시도합니다.
    this.connect();
  },
  methods: {
    sendMessage(e) {
      if (e.keyCode === 13 && this.userName !== "" && this.message !== "") {
        this.send();
        this.message = "";
      }
    },
    send() {
      console.log("Send message:" + this.message);
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          userName: this.userName,
          content: this.message,
          // roomId: "djEjsdladmldmltptus",
          roomId: this.roomSession,
        };
        this.stompClient.send(
          "/chatting/receive/" + this.roomSession,
          JSON.stringify(msg),
          {}
        );
      }
    },
    connect() {
      const serverURL = "http://localhost:8080";
      //  + "/chatting/djEjsdladmldmltptus"
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`);
      this.stompClient.connect(
        {},
        (frame) => {
          // 소켓 연결 성공
          this.connected = true;
          console.log("소켓 연결 성공", frame);
          // 서버의 메시지 전송 endpoint를 구독합니다.
          // 이런형태를 pub sub 구조라고 합니다.
          console.log("과연? : " + this.roomSession);
          this.stompClient.subscribe(
            "/chatting/send/" + this.roomSession,
            (res) => {
              console.log("구독으로 받은 메시지 입니다.", res.body);

              // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
              this.recvList.push(JSON.parse(res.body));
            }
          );
        },
        (error) => {
          // 소켓 연결 실패
          console.log("소켓 연결 실패", error);
          this.connected = false;
        }
      );
    },
  },
};
</script>