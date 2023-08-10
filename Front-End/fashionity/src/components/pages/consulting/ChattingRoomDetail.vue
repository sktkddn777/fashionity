<!-- src/components/ChatRoomDetail.vue -->
<template>
  <div class="container" v-cloak>
    <div>
      <h2>{{ room.name }}</h2>
    </div>
    <div class="input-group">
      <div class="input-group-prepend">
        <label class="input-group-text">내용</label>
      </div>
      <input
        type="text"
        class="form-control"
        v-model="message"
        v-on:keyup.enter="sendMessage"
      />
      <div class="input-group-append">
        <button class="btn btn-primary" type="button" @click="sendMessage">
          보내기
        </button>
      </div>
    </div>
    <ul class="list-group">
      <li class="list-group-item" v-for="(message, idx) in messages" :key="idx">
        {{ message.sender }} - {{ message.message }}
      </li>
    </ul>
  </div>
</template>

<script>
import axios from "axios";
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";

export default {
  name: "ChatRoomDetail",
  data() {
    return {
      roomId: "",
      room: {},
      sender: "",
      message: "",
      messages: [],
      sock: new SockJS("http://localhost:8080/ws/chat"),
      ws: null,
      reconnect: 0,
    };
  },
  created() {
    this.connect();
    this.roomId = localStorage.getItem("wschat.roomId");
    this.sender = localStorage.getItem("wschat.sender");
    this.findRoom();
  },
  methods: {
    findRoom() {
      axios
        .get("http://localhost:8080/chat/room/" + this.roomId)
        .then((response) => {
          this.room = response.data;
        });
    },
    sendMessage() {
      this.ws.send(
        "http://localhost:8080/app/chat/message",
        {},
        JSON.stringify({
          type: "TALK",
          roomId: this.roomId,
          sender: this.sender,
          message: this.message,
        })
      );
      this.message = "";
    },
    recvMessage(recv) {
      this.messages.unshift({
        type: recv.type,
        sender: recv.type == "ENTER" ? "[알림]" : recv.sender,
        message: recv.message,
      });
    },
    connect() {
      // const serverURL =
      //   "http://localhost:8080" + "/chatting/djEjsdladmldmltptus";
      this.ws = Stomp.over(this.sock);
      this.ws.connect(
        {},
        (frame) => {
          console.log("소켓 연결 성공", frame);
          this.ws.subscribe(
            "http://localhost:8080/topic/chat/room/" + this.roomId,
            (message) => {
              const recv = JSON.parse(message.body);
              this.recvMessage(recv);
            }
          );
          this.ws.send(
            "http://localhost:8080/app/chat/message",
            {},
            JSON.stringify({
              type: "ENTER",
              roomId: this.roomId,
              sender: this.sender,
            })
          );
        },
        (error) => {
          console.log("소켓 연결 실패", error);
          //   if (this.reconnect++ <= 5) {
          //     setTimeout(() => {
          //       console.log("connection reconnect");
          //       this.sock = new SockJS("/ws/chat");
          //       this.ws = Stomp.over(this.sock);
          //       this.connect();
          //     }, 10 * 1000);
          //   }
        }
      );
    },
  },
};
</script>

<style>
[v-cloak] {
  display: none;
}
</style>
