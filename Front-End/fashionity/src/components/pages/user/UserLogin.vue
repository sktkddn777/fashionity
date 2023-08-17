<template>
  <div
    class="container-fluid d-flex align-items-center justify-content-center"
    style="height: 65vh"
  >
    <div class="row d-flex d-flex align-items-center justify-content-center">
      <v-sheet width="300" class="mx-auto">
        <v-form ref="form" @keyup.enter="login">
          <v-text-field
            v-model="id"
            :counter="20"
            label="아이디를 입력하시오"
            required
          ></v-text-field>
          <span class="register-id-check"></span>
          <v-text-field
            v-model="password"
            :counter="20"
            label="비밀번호를 입력하시오"
            required
            :type="'password'"
            density="compact"
          ></v-text-field>
          <div class="d-flex flex-column">
            <v-btn color="blue" class="login-button" block @click="login">
              로그인
            </v-btn>
          </div>
          <div class="d-flex" style="justify-content: space-between">
            <v-btn variant="text" style="font-size: small" @click="register"
              >회원가입</v-btn
            >
            <v-btn
              variant="text"
              style="font-size: small"
              @click="findIdByEmail"
              >아이디찾기</v-btn
            >
            <v-btn
              variant="text"
              style="font-size: small"
              @click="reissuePasswordByEmail"
              >비번찾기</v-btn
            >
          </div>

          <div class="flex-column">
            <v-btn
              color="#00BF18"
              block
              style="margin-bottom: 10px; margin-top: 20px"
            >
              <img src="@/assets/img/naver.png" style="height: 30px" />
              <a
                :href="getNaverLoginUrl()"
                style="padding-left: 53px; padding-right: 73px"
                >네이버로 로그인</a
              >
            </v-btn>

            <v-btn color="#FFE812" block style="margin-bottom: 10px">
              <img src="@/assets/img/kakao.png" style="height: 30px" />
              <a
                :href="getKakaoLoginUrl()"
                style="padding-left: 53px; padding-right: 73px"
                >카카오로 로그인</a
              >
            </v-btn>
            <v-btn color="#FFFFFF" block>
              <img src="@/assets/img/google.png" style="height: 25px" />
              <a
                :href="getGoogleLoginUrl()"
                style="padding-left: 65px; padding-right: 75px"
                >구글로 로그인</a
              >
            </v-btn>
          </div>
        </v-form>
      </v-sheet>
    </div>
  </div>
</template>

<script>
import router from "@/router";
import { computed } from "vue";
import { useStore, mapActions } from "vuex";
const memberStore = "memberStore";

export default {
  name: "UserLogin",

  setup() {
    const store = useStore();
    const loginUser = computed(() => store.state.loginUser);

    return {
      loginUser,
    };
  },

  data: () => ({
    id: "",
    password: "",
    host: "",
    redirectUri: "",
  }),

  beforeMount() {
    this.host = process.env.VUE_APP_API_URL;
    this.redirectUri = process.env.VUE_APP_REDIRECT_URL;
  },

  methods: {
    ...mapActions(memberStore, ["loginAction"]),
    getGoogleLoginUrl() {
      return `${this.host}/oauth2/authorization/google?redirect_uri=${this.redirectUri}`;
    },
    getKakaoLoginUrl() {
      return `${this.host}/oauth2/authorization/kakao?redirect_uri=${this.redirectUri}`;
    },
    getNaverLoginUrl() {
      return `${this.host}/oauth2/authorization/naver?redirect_uri=${this.redirectUri}`;
    },
    async login() {
      const user = {
        id: this.id,
        password: this.password,
      };
      await this.loginAction(user);
    },
    register() {
      router.push({ name: "UserRegister" });
    },
    findIdByEmail() {
      router.push({ name: "UserFindId" });
    },
    reissuePasswordByEmail() {
      router.push({ name: "UserReissuePw" });
    },
  },
};
</script>
<style scoped>
a {
  text-decoration-line: none;
}

a:link {
  color: black;
}
a:visited {
  color: black;
}
a:hover {
  color: black;
}
a:active {
  color: black;
}
</style>
