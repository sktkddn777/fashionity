<template>
  <div class="container" id="app" v-cloak>
    <div>
      <h2>채팅</h2>
    </div>
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
        @keyup="sendMessage"
      />
      <div class="input-group-append">
        <button class="btn btn-primary" type="button" @click="sendMessage">
          보내기
        </button>
      </div>
    </div>
    <ul class="list-group">
      <li class="list-group-item" v-for="(item, idx) in recvList" :key="idx">
        {{ item.userName }} - {{ item.content }}
      </li>
    </ul>
    <div></div>
  </div>
</template>

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