<template lang="">
  <div>
    <div class="container-fluid">
      <div class="row">
        <div class="col-3" id="logo">
          <router-link
            to="post"
            class="link"
            style="font-style: normal; font-size: 50px; padding-top: 20px"
            >fashionity</router-link
          >
        </div>
        <div class="col">
          <div class="row justify-content-end">
            <div class="col-8"></div>
            <div class="col">
              <div class="row justify-content-end">
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
                  <font-awesome-icon
                    :icon="['fas', 'circle-plus']"
                    style="color: #bdbdbd"
                  />
                </div>
                <div class="col">
                  <font-awesome-icon
                    :icon="['fas', 'video']"
                    style="color: #bdbdbd"
                  />
                </div>
                <div class="col">
                  <font-awesome-icon
                    :icon="['far', 'bell']"
                    style="color: #bdbdbd"
                  />
                </div>

                <div class="col">
                  <div v-if="!isLogin" class="row">
                    <router-link to="/user/login" class="link">
                      <img class="profile" src="@/assets/img/unknown.png"
                    /></router-link>
                  </div>
                  <div v-else class="row">
                    <router-link to="/profile" class="link">
                      <img class="profile" src="@/assets/img/panda.png"
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

const memberStore = "memberStore";

export default {
  setup() {},
  computed: {
    ...mapState(memberStore, ["isLogin", "userInfo"]),
    ...mapGetters(["checkUserInfo"]),
  },
  data() {
    return {
      keyword: "",
    };
  },
  methods: {
    ...mapActions(memberStore, ["logoutAction"]),

    logout() {
      const user = {
        memberSeq: sessionStorage.getItem("memberSeq"),
      };

      this.logoutAction(user);
    },
    login() {
      router.push({ name: "UserLogin" });
    },
  },
};
</script>
<style>
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
  width: 100%;
  height: 100%;
  border-radius: 70%;
  object-fit: cover;
}
</style>
