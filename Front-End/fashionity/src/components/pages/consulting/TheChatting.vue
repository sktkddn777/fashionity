<template>
  <div class="chat-container" style="border: 1px solid #ccc">
    <div class="message-list" ref="messageList">
      <div class="message" v-for="(message, index) in recvList" :key="index">
        <div class="message-child" align="left">{{ message.userName }}</div>
        <div class="message-child" align="right">{{ message.content }}</div>
      </div>
    </div>
    <div class="input-container">
      <input
        class="message-input"
        v-model="message"
        @keyup.enter="sendMessage"
        placeholder="메세지를 입력하세요..."
      />
      <button class="send-button" @click="sendMessage">보내기</button>
    </div>
  </div>
</template>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 80vh;
}

.message-list {
  flex: 1;
  max-height: 80vh;
  overflow-y: auto;
}

.message {
  padding: 10px;
  border: 1px solid #ccc;
  display: flex;
  margin: 5px;
  border-radius: 20px;
}

.input-container {
  display: flex;
  align-items: center;
  padding: 10px;
  background-color: #f5f5f5;
}

.message-input {
  flex: 1;
  padding: 5px;
}

.message-child {
  flex: 1;
}

.send-button {
  padding: 5px 10px;
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
}
</style>

<script>
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import { mapGetters } from "vuex";
// import { mapState } from "vuex";

export default {
  name: "TheChatting",
  props: {
    reservationSeq: null,
  },
  data() {
    return {
      userName: null,
      message: "",
      recvList: [],
      roomId: null,
      userData: null,
    };
  },
  computed: {
    ...mapGetters("memberStore", ["checkLoginUser"]),
  },
  created() {
    this.roomId = this.reservationSeq;
    this.userName = this.checkLoginUser.nickname;
    this.userData = this.checkLoginUser;
    // Chatting.vue가 생성되면 소켓 연결을 시도합니다.
    this.connect();
  },

  mounted() {
    this.scrollToBottom();
  },
  updated() {
    this.scrollToBottom();
  },
  methods: {
    // 소켓으로 메세지 전송
    sendMessage() {
      if (this.userName !== "" && this.message !== "") {
        this.send();
        this.message = "";
        this.$nextTick(() => {
          this.scrollToBottom();
        });
      }
    },

    // 메세지가 많이 와서 스크롤이 생성될 때 항상 최신 메세지를 보여주도록
    scrollToBottom() {
      this.$nextTick(() => {
        const messageList = this.$refs.messageList;
        if (messageList) {
          messageList.scrollTop = messageList.scrollHeight;
        }
      });
    },

    // 메세지 전송
    send() {
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          userName: this.userName,
          content: this.message,
          // roomId: "djEjsdladmldmltptus",
          roomId: this.roomId,
          type: "message",
        };
        this.stompClient.send(
          `/chatting/send/${this.roomId}`,
          JSON.stringify(msg)
        );
      }
    },

    // 소켓 연결
    connect() {
      const serverURL = `${process.env.VUE_APP_SOCKET_URL}`;
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect(
        {},
        () => {
          // 소켓 연결 성공
          this.connected = true;
          this.stompClient.subscribe(`/chatting/send/${this.roomId}`, (res) => {
            const receiveData = JSON.parse(res.body);
            if (receiveData.type == "message") {
              this.recvList.push(receiveData);
            }
          });
        },
        (error) => {
          // 소켓 연결 실패
          console.log("소켓 연결 실패", error);
          this.connected = false;
        }
      );
    },
  },
  watch: {
    messages() {
      this.$nextTick(() => {
        this.scrollToBottom();
      });
    },
  },
};
</script>
