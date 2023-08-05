// import jwtDecode from "jwt-decode";
import router from "@/router";
import { login, logout, register } from "@/api/member";
import { useToast } from "vue-toastification";

const toast = useToast();

const memberStore = {
  namespaced: true,
  state: {
    loginUser: {},
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
      state.loginUser = {};
      state.isLogin = false;
    },
    SET_IS_VALID_TOKEN: (state, isValidToken) => {
      state.isValidToken = isValidToken;
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
          console.log("data: " + data);

          commit("LOGIN", data);
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
    async logoutAction({ commit }, user) {
      await logout(
        user,
        ({ data }) => {
          console.log("data: " + data);
          commit("LOGOUT");
        },
        ({ response }) => {
          console.log(response);
        }
      );
    },
    // async userConfirm({ commit }, user) {
    //   await login(
    //     user,
    //     ({ data }) => {
    //       if (data.message === "success") {
    //         let accessToken = data["access-token"];
    //         let refreshToken = data["refresh-token"];
    //         commit("SET_IS_LOGIN", true);
    //         commit("SET_IS_LOGIN_ERROR", false);
    //         commit("SET_IS_VALID_TOKEN", true);
    //         sessionStorage.setItem("access-token", accessToken);
    //         sessionStorage.setItem("refresh-token", refreshToken);
    //       } else {
    //         commit("SET_IS_LOGIN", false);
    //         commit("SET_IS_LOGIN_ERROR", true);
    //         commit("SET_IS_VALID_TOKEN", false);
    //       }
    //     },
    //     (error) => {
    //       console.log(error);
    //     }
    //   );
    // },
    // async getUserInfo({ commit, dispatch }, token) {
    //   let decodeToken = jwtDecode(token);
    //   await findById(
    //     decodeToken.userid,
    //     ({ data }) => {
    //       if (data.message === "success") {
    //         commit("SET_USER_INFO", data.userInfo);
    //       } else {
    //         console.log("유저 정보 없음!!!!");
    //       }
    //     },
    //     async (error) => {
    //       console.log(
    //         "getUserInfo() error code [토큰 만료되어 사용 불가능.] ::: ",
    //         error.response.status
    //       );
    //       commit("SET_IS_VALID_TOKEN", false);
    //       await dispatch("tokenRegeneration");
    //     }
    //   );
    // },
    // async tokenRegeneration({ commit, state }) {
    //   console.log(
    //     "토큰 재발급 >> 기존 토큰 정보 : {}",
    //     sessionStorage.getItem("access-token")
    //   );
    //   await tokenRegeneration(
    //     JSON.stringify(state.userInfo),
    //     ({ data }) => {
    //       if (data.message === "success") {
    //         let accessToken = data["access-token"];
    //         console.log("재발급 완료 >> 새로운 토큰 : {}", accessToken);
    //         sessionStorage.setItem("access-token", accessToken);
    //         commit("SET_IS_VALID_TOKEN", true);
    //       }
    //     },
    //     async (error) => {
    //       if (error.response.status === 401) {
    //         console.log("갱신 실패");
    //         await logout(
    //           state.userInfo.userid,
    //           ({ data }) => {
    //             if (data.message === "success") {
    //               console.log("리프레시 토큰 제거 성공");
    //             } else {
    //               console.log("리프레시 토큰 제거 실패");
    //             }
    //             alert("RefreshToken 기간 만료!!! 다시 로그인해 주세요.");
    //             commit("SET_IS_LOGIN", false);
    //             commit("SET_USER_INFO", null);
    //             commit("SET_IS_VALID_TOKEN", false);
    //             router.push({ name: "login" });
    //           },
    //           (error) => {
    //             console.log(error);
    //             commit("SET_IS_LOGIN", false);
    //             commit("SET_USER_INFO", null);
    //           }
    //         );
    //       }
    //     }
    //   );
    // },
    // async userLogout({ commit }, userid) {
    //   await logout(
    //     userid,
    //     ({ data }) => {
    //       if (data.message === "success") {
    //         commit("SET_IS_LOGIN", false);
    //         commit("SET_USER_INFO", null);
    //         commit("SET_IS_VALID_TOKEN", false);
    //       } else {
    //         console.log("유저 정보 없음!!!!");
    //       }
    //     },
    //     (error) => {
    //       console.log(error);
    //     }
    //   );
    // },
  },
};

export default memberStore;
