<template lang="">
  <div>
    <div class="container-fluid" style="background-color: white">
      <div class="row" style="margin-top: 20px">
        <div class="col-3" id="logo">
          <router-link
            to="/"
            class="link"
            style="
              font-style: normal;
              font-size: 50px;
              padding-top: 30px;
              background-color: white;
              margin-top: 20px;
            "
            >fashionity</router-link
          >
        </div>
        <div class="col">
          <div class="row justify-content-end" style="margin-top: -20px">
            <div class="col-8"></div>
            <div class="col">
              <div class="row justify-content-end">
                <div></div>
                <div class="col-2"></div>

                <div class="col">
                  <div
                    v-if="!isLogin"
                    class="row"
                    @click="login"
                    style="cursor: pointer"
                  >
                    로그인
                  </div>
                  <div
                    v-else
                    class="row"
                    @click="logout"
                    style="cursor: pointer"
                  >
                    로그아웃
                  </div>
                </div>
                <div class="col">
                  <div v-if="!isLogin" @click="loginAlert">
                    <font-awesome-icon
                      :icon="['fas', 'circle-plus']"
                      style="color: #bdbdbd"
                    />
                  </div>
                  <div v-else>
                    <router-link to="/post/write" class="link">
                      <font-awesome-icon
                        :icon="['fas', 'circle-plus']"
                        style="color: #bdbdbd"
                      />
                    </router-link>
                  </div>
                </div>
                <div class="col">
                  <font-awesome-icon
                    :icon="['fas', 'video']"
                    @click="meeting"
                    style="color: #bdbdbd"
                  />
                </div>
                <!-- 여기부터 알람 -->
                <div class="col">
                  <button
                    type="button"
                    data-bs-toggle="offcanvas"
                    data-bs-target="#offcanvasRight"
                    aria-controls="offcanvasRight"
                    @click="updateAlarm"
                  >
                    <font-awesome-icon
                      :icon="['far', 'bell']"
                      style="color: #bdbdbd"
                      type="button"
                    />
                  </button>
                  <div
                    class="offcanvas offcanvas-end"
                    tabindex="-1"
                    id="offcanvasRight"
                    aria-labelledby="offcanvasRightLabel"
                  >
                    <div class="offcanvas-header">
                      <h5 class="offcanvas-title" id="offcanvasRightLabel">
                        알림
                      </h5>
                      <button
                        type="button"
                        class="btn-close"
                        data-bs-dismiss="offcanvas"
                        aria-label="Close"
                      ></button>
                    </div>
                    <div class="offcanvas-body">
                      <div
                        class="alert"
                        v-for="(alarm, index) in this.alarms"
                        :key="index"
                      >
                        <router-link :to="getAlarmLink(alarm)">
                          <div class="alert-image">
                            <img
                              :src="
                                alarm.imageUrl || '../img/unknown.e083a226.png'
                              "
                              alt=""
                              class="profile-comment"
                            />
                          </div>
                        </router-link>
                        <!-- 팔로잉 알람 -->
                        <div
                          class="alert-content"
                          v-if="alarm.type === 'FOLLOW'"
                        >
                          <span class="fw-bold">{{
                            alarm.publisher_nickname
                          }}</span>
                          님이 회원님을 팔로우 합니다.
                        </div>
                        <!-- 게시글 좋아요 알람 -->
                        <div
                          class="alert-content"
                          v-else-if="alarm.type === 'POST_LIKE'"
                        >
                          {{ alarm.title }}
                        </div>
                        <!-- 댓글 좋아요 알람 -->
                        <div
                          class="alert-content"
                          v-else-if="alarm.type === 'COMMENT_LIKE'"
                        >
                          {{ alarm.title }}<br />
                          {{ alarm.content }}
                        </div>
                        <!-- 게시글에 댓글 등록 알람 -->
                        <div
                          class="alert-content"
                          v-else-if="alarm.type === 'COMMENT_POST'"
                        >
                          {{ alarm.title }}<br />
                          {{ alarm.content }}
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="col">
                  <div v-if="!isLogin" class="row">
                    <router-link to="/user/login" class="link">
                      <img class="profile" src="@/assets/img/unknown.png"
                    /></router-link>
                  </div>
                  <div v-else class="row">
                    <router-link to="/profile" class="link">
                      <img class="profile" :src="getProfileUrl()"
                    /></router-link>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { mapState, mapGetters, mapActions } from "vuex";
import router from "@/router";
import axios from "axios";
const memberStore = "memberStore";
export default {
  setup() {},
  computed: {
    ...mapState(memberStore, ["isLogin", "loginUser"]),
    ...mapGetters(["checkUserInfo"]),
  },
  data() {
    return {
      keyword: "",
      alarms: [],
    };
  },
  components: {},
  methods: {
    ...mapActions(memberStore, ["logoutAction"]),

    logout() {
      this.logoutAction();
    },
    login() {
      router.push({ name: "UserLogin" });
    },
    getProfileUrl() {
      return this.loginUser.profileUri;
    },
    meeting() {
      router.push({ name: "ConsultingPage" });
    },
    updateAlarm() {
      let token = sessionStorage.getItem("token");
      axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/alarm`,
        headers:
          token === null
            ? null
            : {
                Authorization: `Bearer ${token}`,
              },
        method: "GET",
      })
        .then((data) => {
          this.alarms = data.data;
          console.log(this.alarms);
        })
        .catch(() => {
          this.alarms = [];
        });
    },
    getAlarmLink(alarm) {
      if (alarm.type === "FOLLOW") {
        return { path: `/profile/${alarm.publisher_nickname}` };
      } else {
        return { path: `/post/${alarm.post_seq}` };
      }
    },
    loginAlert() {
      alert("로그인해주세요.");
    },
  },
};
</script>
<style scoped>
.header-tab {
  font-style: normal;
  font-size: 25px;
  line-height: 38px;

  text-align: center;
}
#logo {
  font-style: normal;
  font-size: 50px;
}

.profile {
  padding-top: 1px;
  width: 20px;
  height: 20px;
  border-radius: 100%;
  object-fit: cover;
}
.alert {
  display: flex;
  justify-content: flex-start;
  margin-bottom: 0;
  padding: 13px;
  gap: 10px;
}
.profile-comment {
  height: 5vh;
  border-radius: 100%;
  object-fit: contain;
}
.alert-content {
  text-align: left;
}
</style>
