<template>
  <div id="main-container" class="container">
    <div id="join" v-if="!session">
      <div id="join-dialog" class="jumbotron vertical-center">
        <h1>컨설팅 대기중...</h1>

        <div class="form-group">
          <p>
            <!-- 참가자 닉네임 or 아이디 -->
            <label>Participant</label>
            <input
              v-model="myUserName"
              class="form-control"
              type="text"
              required
            />
          </p>
          <p>
            <!-- 참가할 세션 -->
            <label>Session</label>
            <input
              v-model="mySessionId"
              class="form-control"
              type="text"
              required
            />
          </p>
          <!-- 세션에 참가하는 버튼 -->
          <p class="text-center">
            <button class="btn btn-lg btn-success" @click="joinSession()">
              Join!
            </button>
          </p>
        </div>
      </div>
    </div>

    <!-- 세션 종료 버튼 -->
    <div id="session" v-if="session">
      <div id="session-header">
        <h1 id="session-title">{{ mySessionId }}</h1>
        <input
          class="btn btn-large btn-danger"
          type="button"
          id="buttonLeaveSession"
          @click="leaveSession"
          value="컨설팅 종료"
        />
      </div>
      <div id="main-video" class="col-md-6">
        <!-- mainStreamManager : 포커싱을 맞춰주는 화면 -->
        <!-- <user-video :stream-manager="mainStreamManager" /> -->
      </div>
      <!-- 비디오 -->
      <div id="video-container" class="video-container" style="display: flex">
        <div class="user-video">
          <user-video
            :stream-manager="publisher"
            @click="updateMainVideoStreamManager(publisher)"
          />
          <!-- 일반 유저의 비디오에 띄어질 퍼스널컬러 이미지 -->
          <div v-if="userData.memberRole[1] !== 'CONSALTANT'">
            <img
              :src="require(`@/assets/img/${selectedImage_personal}`)"
              alt="Selected Image"
              v-if="selectedImageVisible_personal"
              @click="showImage_personal(null)"
              class="personal_color"
            />
          </div>
        </div>
        <!-- 컨설턴트가 유저에게 띄우는 퍼스널컬러 이미지 -->
        <div class="user-video">
          <user-video
            v-for="sub in subscribers"
            :key="sub.stream.connection.connectionId"
            :stream-manager="sub"
            @click="updateMainVideoStreamManager(sub)"
            class="user-video-container"
          />
          <div v-if="userData.memberRole[1] === 'CONSALTANT'">
            <img
              :src="require(`@/assets/img/${selectedImage_personal}`)"
              alt="Selected Image"
              v-if="selectedImageVisible_personal"
              @click="showImage_personal(null)"
              class="personal_color"
            />
          </div>
        </div>
      </div>
      <!-- 이미지 div -->
      <div v-if="userData.memberRole[1] === 'CONSULTANT'">
        <button @click="toggleDiv('colorDiv')" class="image-button">
          퍼스널 컬러
        </button>
        <button @click="toggleDiv('styleDiv')" class="image-button">
          등록 이미지
        </button>
        <!-- 퍼스널 컬러 -->
        <div class="image-list" v-if="showColorDiv">
          <div
            class="image-item"
            v-for="(image, index) in color_images"
            :key="index"
          >
            <img
              :src="require(`@/assets/img/${image.url}`)"
              :alt="image.alt"
              class="image"
              @click="showImage_personal(index)"
            />
          </div>
        </div>
        <!-- 등록 이미지 -->
        <div class="image-list" v-if="showStyleDiv">
          <div
            class="image-item"
            v-for="(image, index) in style_images"
            :key="index"
          >
            <img
              :src="require(`@/assets/img/${image.url}`)"
              :alt="image.alt"
              :class="['image', { highlighted: selectedIndex_image === index }]"
              @click="showImage_image(index)"
            />
          </div>
        </div>
        <button @click="showImage_image(null)" class="image-button">
          사진 닫기
        </button>
      </div>
      <!-- 일반유저가 보는 이미지 화면 -->
      <div v-if="userData.memberRole[1] !== 'CONSULTANT'">
        여기에 일반유저가 볼 수 있는 이미지!
        <img
          :src="require(`@/assets/img/${selectedImage_image}`)"
          alt="Selected Image"
          v-if="selectedImageVisible_image"
          class="image"
        />
      </div>
    </div>
  </div>
</template>
<style scoped>
@import url("./Consulting-WebCam.css");
.image-list {
  display: flex;
  max-width: 60vw;
  overflow-x: auto;
}

.image-item {
  border: 1px solid #ccc;
  flex: 0 0 auto;
  margin-right: 10px;
}
.image {
  max-width: 25vh;
  max-height: 25vh;
}

.user-video {
  position: relative;
}

.user-video-container {
  display: block;
  width: 100%;
  height: auto;
}

/* 퍼스널 컬러 이미지를 비디오의 가운데에 놓기위한 style */
.personal_color {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.image-button {
  background-color: blue;
  color: white;
  border-radius: 10px;
  padding: 10px;
  margin: 5px;
}

.highlighted {
  border: 2px solid red;
}
</style>
<script>
import axios from "axios";
import { OpenVidu } from "openvidu-browser";
import UserVideo from "./components/UserVideo";
import router from "@/router";
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import { mapGetters } from "vuex";

axios.defaults.headers.post["Content-Type"] = "application/json";

const OPENVIDU_SERVER_URL = "https://i9a108.p.ssafy.io:5443";
const OPENVIDU_SERVER_SECRET = "rlaxogus123";

export default {
  name: "App",

  components: {
    UserVideo,
  },

  data() {
    return {
      OV: undefined,
      session: undefined,
      mainStreamManager: undefined,
      publisher: undefined,
      subscribers: [],
      mySessionId: "djEjsdladmldmltptus",
      myUserName: null,
      showColorDiv: false,
      showStyleDiv: false,
      selectedImage_personal: null,
      selectedImage_image: null,
      selectedImageVisible_personal: false,
      selectedImageVisible_image: false,
      selectedIndex_personal: null,
      selectedIndex_image: null,
      userData: null,
      color_images: [
        { url: "spring_warm.png", alt: "봄웜" },
        { url: "summer_cool.png", alt: "여름쿨" },
        { url: "fall_warm.png", alt: "가을웜" },
        { url: "winter_cool.png", alt: "겨울쿨" },
      ],
      style_images: [
        { url: "hyeonwook.jpg", alt: "현욱1" },
        { url: "hyeonwook2.jpg", alt: "현욱2" },
        { url: "hyeonwook3.jpg", alt: "현욱3" },
        { url: "postImg.jpg", alt: "지원" },
        { url: "hyeonwook.jpg", alt: "현욱1" },
        { url: "hyeonwook2.jpg", alt: "현욱2" },
        { url: "hyeonwook3.jpg", alt: "현욱3" },
        { url: "postImg.jpg", alt: "지원" },
      ],
    };
  },
  computed: {
    ...mapGetters("memberStore", ["checkLoginUser"]),
  },
  created() {
    this.myUserName = this.checkLoginUser.nickname;
    this.userData = this.checkLoginUser;
    console.log("유저 닉네임 : " + this.userData.nickname);
    console.log("유저 권한 : " + this.userData.memberRole);
    console.log("유저 권한 : " + this.userData.memberRole[1]);
    this.joinSession();
    this.connect();
  },

  methods: {
    joinSession() {
      // --- Get an OpenVidu object ---
      this.OV = new OpenVidu();

      // --- Init a session ---
      this.session = this.OV.initSession();
      console.log("---------------------접속한 세션 : " + this.session);
      // const testJson = JSON.stringify(this.session);
      // console.log(testJson);
      console.log(Object.entries(this.session));

      // --- Specify the actions when events take place in the session ---

      // On every new Stream received...
      this.session.on("streamCreated", ({ stream }) => {
        const subscriber = this.session.subscribe(stream);
        this.subscribers.push(subscriber);
      });

      // On every Stream destroyed...
      this.session.on("streamDestroyed", ({ stream }) => {
        const index = this.subscribers.indexOf(stream.streamManager, 0);
        if (index >= 0) {
          this.subscribers.splice(index, 1);
        }
      });

      // On every asynchronous exception...
      this.session.on("exception", ({ exception }) => {
        console.warn(exception);
      });

      // --- Connect to the session with a valid user token ---

      // 'getToken' method is simulating what your server-side should do.
      // 'token' parameter should be retrieved and returned by your own backend
      this.getToken(this.mySessionId).then((token) => {
        this.session
          .connect(token, { clientData: this.myUserName })
          .then(() => {
            // --- Get your own camera stream with the desired properties ---

            let publisher = this.OV.initPublisher(undefined, {
              audioSource: undefined, // The source of audio. If undefined default microphone
              videoSource: undefined, // The source of video. If undefined default webcam
              publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
              publishVideo: true, // Whether you want to start publishing with your video enabled or not
              resolution: "640x480", // The resolution of your video
              frameRate: 30, // The frame rate of your video
              insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
              mirror: true, // Whether to mirror your local video or not
            });

            this.mainStreamManager = publisher;
            this.publisher = publisher;

            // --- Publish your stream ---

            this.session.publish(this.publisher);
          })
          .catch((error) => {
            console.log(
              "There was an error connecting to the session:",
              error.code,
              error.message
            );
          });
      });

      window.addEventListener("beforeunload", this.leaveSession);
    },

    leaveSession() {
      // --- Leave the session by calling 'disconnect' method over the Session object ---
      if (this.session) this.session.disconnect();

      this.session = undefined;
      this.mainStreamManager = undefined;
      this.publisher = undefined;
      this.subscribers = [];
      this.OV = undefined;

      window.removeEventListener("beforeunload", this.leaveSession);
      router.push({ name: "consultant-myreservation" });
    },

    updateMainVideoStreamManager(stream) {
      if (this.mainStreamManager === stream) return;
      this.mainStreamManager = stream;
    },

    /**
     * --------------------------
     * SERVER-SIDE RESPONSIBILITY
     * --------------------------
     * These methods retrieve the mandatory user token from OpenVidu Server.
     * This behavior MUST BE IN YOUR SERVER-SIDE IN PRODUCTION (by using
     * the API REST, openvidu-java-client or openvidu-node-client):
     *   1) Initialize a Session in OpenVidu Server	(POST /openvidu/api/sessions)
     *   2) Create a Connection in OpenVidu Server (POST /openvidu/api/sessions/<SESSION_ID>/connection)
     *   3) The Connection.token must be consumed in Session.connect() method
     */

    getToken(mySessionId) {
      console.log("---------------------getToken : " + mySessionId);
      return this.createSession(mySessionId).then((sessionId) =>
        this.createToken(sessionId)
      );
    },

    // See https://docs.openvidu.io/en/stable/reference-docs/REST-API/#post-openviduapisessions
    createSession(sessionId) {
      return new Promise((resolve, reject) => {
        axios.defaults.withCredentials = false;
        console.log("---------------------createSession : " + sessionId);
        axios
          .post(
            `${OPENVIDU_SERVER_URL}/openvidu/api/sessions`,
            JSON.stringify({
              customSessionId: sessionId,
            }),
            {
              auth: {
                username: "OPENVIDUAPP",
                password: OPENVIDU_SERVER_SECRET,
              },
            }
          )
          .then((response) => response.data)
          .then((data) => resolve(data.id))
          .catch((error) => {
            if (error.response.status === 409) {
              resolve(sessionId);
            } else {
              console.warn(
                `No connection to OpenVidu Server. This may be a certificate error at ${OPENVIDU_SERVER_URL}`
              );
              if (
                window.confirm(
                  `No connection to OpenVidu Server. This may be a certificate error at ${OPENVIDU_SERVER_URL}\n\nClick OK to navigate and accept it. If no certificate warning is shown, then check that your OpenVidu Server is up and running at "${OPENVIDU_SERVER_URL}"`
                )
              ) {
                location.assign(`${OPENVIDU_SERVER_URL}/accept-certificate`);
              }
              reject(error.response);
            }
          });
      });
    },

    createToken(sessionId) {
      console.log("---------------------createToken : " + sessionId);
      return new Promise((resolve, reject) => {
        axios
          .post(
            `${OPENVIDU_SERVER_URL}/openvidu/api/sessions/${sessionId}/connection`,
            {},
            {
              auth: {
                username: "OPENVIDUAPP",
                password: OPENVIDU_SERVER_SECRET,
              },
            }
          )
          .then((response) => response.data)
          .then((data) => resolve(data.token))
          .catch((error) => reject(error.response));
      });
    },

    // 컨설턴트만 보이는 버튼
    toggleDiv(divType) {
      if (divType === "colorDiv") {
        this.showColorDiv = !this.showColorDiv;
        this.showStyleDiv = false;
      } else if (divType === "styleDiv") {
        this.showStyleDiv = !this.showStyleDiv;
        this.showColorDiv = false;
      }
    },

    // 컨설턴트가 퍼스널컬러 이미지 클릭 시 보이게
    showImage_personal(index) {
      if (index !== null) {
        this.selectedIndex_personal = index;
        this.selectedImage_personal = this.color_images[index].url;
        this.selectedImageVisible_personal = true;
        this.send(this.selectedIndex_personal, "personal");
      } else {
        this.selectedImageVisible_personal = false;
        this.send(null, "personal");
      }
    },

    // 컨설턴트가 등록된 이미지 클릭 시 보이게
    showImage_image(index) {
      if (index !== null) {
        this.selectedIndex_image = index;
        this.selectedImage_image = this.style_images[index].url;
        this.selectedImageVisible_image = true;
        this.send(this.selectedIndex_image, "image");
      } else {
        this.selectedImageVisible_image = false;
        this.send(null, "image");
      }
    },

    // 소켓 연결
    connect() {
      console.log("방 정보 : " + this.roomId);
      const serverURL = `${process.env.VUE_APP_SOCKET_URL}`;
      //  + "/chatting/djEjsdladmldmltptus"
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`);
      this.stompClient.connect(
        {},
        (frame) => {
          // 소켓 연결 성공
          this.connected = true;
          console.log("이미지 소켓 연결 성공", frame);
          this.stompClient.subscribe(
            `/chatting/send/${this.mySessionId}`,
            (res) => {
              const receiveData = JSON.parse(res.body);
              if (receiveData.type == "personal") {
                console.log(
                  "받아온 이미지 인덱스 퍼스널 : " + receiveData.content
                );
                this.selectedIndex_personal = receiveData.content;
              } else if (receiveData.type == "image") {
                console.log(
                  "받아온 이미지 인덱스 이미지 : " + receiveData.content
                );
                this.selectedIndex_image = receiveData.content;
              }
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

    // 소켓으로 메세지 보내기
    send(index, type) {
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          userName: this.myUserName,
          content: index,
          // roomId: "djEjsdladmldmltptus",
          roomId: this.mySessionId,
          type: type,
        };
        console.log("소켓으로 보낼 데이터 : ");
        console.log(msg);
        this.stompClient.send(
          `/chatting/send/${this.mySessionId}`,
          JSON.stringify(msg)
        );
      }
    },
  },

  // 소켓에서 content가 올 때 변경이 될 수 있도록
  watch: {
    selectedIndex_personal(newVal) {
      console.log("퍼스널 새로운 값 : " + newVal);
      this.showImage_personal(newVal);
    },
    selectedIndex_image(newVal) {
      console.log("이미지 새로운 값 : " + newVal);
      this.showImage_image(newVal);
    },
  },
};
</script>
