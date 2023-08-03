import { createStore } from "vuex";
import axios from "axios";
import router from "@/router";
import { useToast } from "vue-toastification";

const toast = useToast();

const baseUrl = "http://localhost:8080/api/v1";

export default createStore({
  state: {
    loginUser: {}, // accessToken
    // TODO: userSeq 추가
  },
  mutations: {
    LOGIN(state, payload) {
      state.loginUser = payload;
      router.push({ name: "main" });
    },
    LOGOUT(state) {
      sessionStorage.removeItem("token");
      state.loginUser = {};
      router.push({ name: "main" });
    },
    GET_USER(state, payload) {
      state.loginUser = payload;
      router.push({ name: "ProfilePage" });
    },
  },
  actions: {
    login({ commit }, user) {
      const URL = `${baseUrl}/auth/login`;
      if (!user.id) {
        toast.error("아이디가 비었따!!", {
          position: "bottom-right",
          timeout: 2000,
        });
        return;
      }

      if (!user.pw) {
        toast.error("비밀번호가 비었따!!", {
          position: "bottom-right",
          timeout: 2000,
        });
        return;
      }
      axios({
        url: URL,
        method: "POST",
        data: {
          id: user.id,
          password: user.pw,
        },
      })
        .then((res) => {
          sessionStorage.setItem("token", res.data["accessToken"]);
          commit("LOGIN", res.data);
        })
        .catch((res) => {
          if (res.response.data.code === "M001")
            toast.error("아이디가 없따!!", {
              position: "bottom-right",
              timeout: 2000,
            });
          else if (res.response.data.code === "A001") {
            toast.error("비밀번호가 틀렸따!!", {
              position: "bottom-right",
              timeout: 2000,
            });
          }
        });
    },
  },
});
