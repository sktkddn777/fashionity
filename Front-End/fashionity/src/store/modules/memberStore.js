import router from "@/router";
import { login, logout, register, tokenRegeneration } from "@/api/member";
import { useToast } from "vue-toastification";
import { useCookies } from "vue3-cookies";
import axios from "axios";

const toast = useToast();
const { cookies } = useCookies();

const memberStore = {
  namespaced: true,
  state: {
    loginUser: null,
    isLogin: false,
    isValidToken: false,
  },
  getters: {
    checkLoginUser: function (state) {
      return state.loginUser;
    },
    checkToken: function (state) {
      return state.isValidToken;
    },
  },
  mutations: {
    LOGIN(state, data) {
      state.loginUser = data;
      state.isLogin = true;
      sessionStorage.setItem("token", data.accessToken);
      sessionStorage.setItem("memberSeq", data.memberSeq);
    },
    LOGOUT(state) {
      sessionStorage.removeItem("token");
      sessionStorage.removeItem("memberSeq");
      state.loginUser = null;
      state.isLogin = false;
    },
    SET_IS_VALID_TOKEN(state, isValidToken) {
      state.isValidToken = isValidToken;
    },
    SET_USER_INFO(state, data) {
      state.loginUser = data;
    },
  },
  actions: {
    async loginAction({ commit }, user) {
      if (!user.id) {
        toast.error("아이디가 비었따!!", {
          position: "bottom-right",
          timeout: 2000,
        });
        return;
      }

      if (!user.password) {
        toast.error("비밀번호가 비었따!!", {
          position: "bottom-right",
          timeout: 2000,
        });
        return;
      }
      // login
      await login(
        user,
        ({ data }) => {
          console.log("login success");
          console.log(data)
          if (!data.profileUri)
            data.profileUri =
              "https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Animals/Front-Facing%20Baby%20Chick.png";
          commit("LOGIN", data);
          commit("SET_IS_VALID_TOKEN", true);
          cookies.set("refreshToken", data.refreshToken, "14d");
          router.push({ name: "main" });
        },
        ({ response }) => {
          if (response.data.code === "M001") {
            toast.error("존재하지 않는 사용자입니다.", {
              position: "bottom-right",
              timeout: 2000,
            });
          } else if (response.data.code === "A001") {
            toast.error("비밀번호가 일치하지 않습니다..", {
              position: "bottom-right",
              timeout: 2000,
            });
          }
        }
      );
    },
    async registerAction(context, user) {
      await register(
        user,
        ({ data }) => {
          toast.success("사랑합니다 " + data.id + "고객님");

          router.push({ name: "UserLogin" });
        },
        ({ response }) => {
          if (response.data.code === "M002") {
            toast.error("이미 존재하는 아이디입니다.", {
              position: "bottom-right",
              timeout: 2000,
            });
          } else if (response.data.code === "M003") {
            toast.error("이미 존재하는 이메일입니다.", {
              position: "bottom-right",
              timeout: 2000,
            });
          } else if (response.data.code === "M004") {
            toast.error("이미 존재하는 닉네임입니다.", {
              position: "bottom-right",
              timeout: 2000,
            });
          }
        }
      );
    },
    async logoutAction({ commit }) {
      console.log("logoutAction start");
      await logout(
        ({ data }) => {
          console.log("data: " + data);
          commit("LOGOUT");
          commit("SET_IS_VALID_TOKEN", false);
          cookies.remove("refreshToken");
        },
        ({ response }) => {
          console.log(response);
        }
      );
    },
    async getUserInfoAction({ commit, dispatch }) {
      axios({
        url: `${process.env.VUE_APP_API_URL} + "/api/v1/members";`,
        headers: {
          Authorization: `Bearer ${sessionStorage.getItem("token")}`,
        },
        method: "GET",
      })
        .then(({ data }) => {
          console.log("[getUserInfoAction] success " + data);
        })
        .catch(async ({ response }) => {
          console.log("[getUserInfoAction] fail " + response);
          commit("SET_IS_VALID_TOKEN", false);
          await dispatch("tokenRegeneration");
        });
    },

    async tokenRegeneration({ commit }) {
      const reissueRequest = {
        accessToken: sessionStorage.getItem("token"),
        memberSeq: sessionStorage.getItem("memberSeq"),
      };
      await tokenRegeneration(
        reissueRequest,
        ({ data }) => {
          let accessToken = data.accessToken;
          console.log("[tokenRegeneration] success : {}", accessToken);
          sessionStorage.setItem("token", accessToken);
          commit("SET_IS_VALID_TOKEN", true);
        },
        async ({ response }) => {
          console.log("[tokenRegeneration] fail : {}", response);
          await logout(
            ({ data }) => {
              console.log("[logout] " + data);
              alert("RefreshToken 기간 만료!!! 다시 로그인해 주세요.");
              commit("LOGOUT");
              commit("SET_IS_VALID_TOKEN", false);
              cookies.remove("refreshToken");
              router.push({ name: "login" });
            },
            (error) => {
              console.log(error);
              commit("SET_IS_LOGIN", false);
              commit("SET_USER_INFO", null);
            }
          );
        }
      );
    },
  },
};

export default memberStore;
