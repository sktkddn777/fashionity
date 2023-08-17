<template lang="">
  <div class="header">
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
            >Fashionity</router-link
          >
        </div>
        <div class="col">
          <div class="row justify-content-end">
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
                <!-- 여기부터 알람 -->
                <the-alarm></the-alarm>

                <div class="col">
                  <div v-if="!isLogin" class="row">
                    <router-link to="/user/login" class="link">
                      <img class="profile" src="@/assets/img/unknown.png"
                    /></router-link>
                  </div>
                  <div v-else class="row">
                    <router-link :to="profileLink">
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
// import axios from "axios";
import TheAlarm from "../pages/alarm/TheAlarm.vue";
const memberStore = "memberStore";
export default {
  setup() {},
  computed: {
    ...mapState(memberStore, ["isLogin", "loginUser"]),
    ...mapGetters(["checkUserInfo"]),
    profileLink() {
      return `/profile/${this.myNickname}`;
    },
  },
  created() {
    if (this.$store.getters["memberStore/checkLoginUser"] !== null) {
      this.myNickname =
        this.$store.getters["memberStore/checkLoginUser"].nickname;
    }
  },
  data() {
    return {
      keyword: "",
      alarms: [],
    };
  },
  components: {
    TheAlarm,
  },
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
    loginAlert() {
      alert("로그인해주세요.");
    },
  },
};
</script>
<style scoped>
.header {
  margin-bottom: 50px;
}
.header-tab {
  font-style: normal;
  font-size: 25px;
  line-height: 38px;

  text-align: center;
}
#logo {
  font-style: normal;
  font-size: 50px;
  font-family: "IBM Plex Sans KR";
  color: #424242;
  font-weight: 600;
}

.profile {
  padding-top: 1px;
  width: 20px;
  height: 20px;
  border-radius: 100%;
  object-fit: cover;
}
.profile-comment {
  height: 5vh;
  border-radius: 100%;
  object-fit: contain;
}
</style>
