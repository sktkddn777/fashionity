<template lang="">
  <div>
    <div class="container-fluid">
      <div class="row">
        <div class="col-3" id="logo">
          <router-link
            to="/"
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
                <div></div>
                <div class="col-6"></div>
                <div class="col">
                  <router-link to="post/write" class="link">
                    <font-awesome-icon
                      :icon="['fas', 'circle-plus']"
                      style="color: #bdbdbd"
                    />
                  </router-link>
                </div>
                <div class="col">
                  <font-awesome-icon
                    :icon="['fas', 'video']"
                    @click="meeting"
                    style="color: #bdbdbd"
                  />
                </div>
                <div class="col">
                  <button
                    type="button"
                    data-bs-toggle="offcanvas"
                    data-bs-target="#offcanvasRight"
                    aria-controls="offcanvasRight"
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
                      <div class="alert">
                        <div class="alert-image">
                          <img
                            src="../../assets/img/hyeonwook.jpg"
                            alt=""
                            class="profile-comment"
                          />
                        </div>
                        <div class="alert-content">
                          <span class="fw-bold">hyeonwook12</span>
                          님이 회원님의 게시글에 댓글을 작성했습니다.
                        </div>
                      </div>
                      <div class="alert">
                        <div class="alert-image">
                          <img
                            src="../../assets/img/hyeonwook.jpg"
                            alt=""
                            class="profile-comment"
                          />
                        </div>
                        <div class="alert-content">
                          누군가 회원님의 게시글을 스크랩했습니다.
                        </div>
                      </div>
                      <div class="alert">
                        <div class="alert-image">
                          <img
                            src="../../assets/img/hyeonwook.jpg"
                            alt=""
                            class="profile-comment"
                          />
                        </div>
                        <div class="alert-content">
                          <span class="fw-bold">2_kyeong</span>
                          님이 회원님을 팔로우했습니다.
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
  components: {},
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
    meeting() {
      router.push({ name: "ConsultingPage" });
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
