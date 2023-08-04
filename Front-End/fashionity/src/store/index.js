import { createStore } from "vuex";
import createPersistedState from "vuex-persistedstate";

import memberStore from "./modules/memberStore";

export default createStore({
  plugins: [
    createPersistedState({
      storage: sessionStorage,
    }),
  ],
  state: {},
  getters: {},
  mutations: {},
  actions: {},
  modules: {
    memberStore,
  },

  // state: {
  //   loginUser: {},
  // },
  // mutations: {
  //   LOGIN(state, payload) {
  //     console.log(payload);
  //     state.loginUser = payload;
  //     router.push({ name: "main" });
  //   },

  // },
  // actions: {
  //   login({ commit }, user) {
  //     const URL = `${baseUrl}/auth/login`;
  //     if (!user.id) {
  //       toast.error("아이디가 비었따!!", {
  //         position: "bottom-right",
  //         timeout: 2000,
  //       });
  //       return;
  //     }

  //     if (!user.pw) {
  //       toast.error("비밀번호가 비었따!!", {
  //         position: "bottom-right",
  //         timeout: 2000,
  //       });
  //       return;
  //     }
  //     // login
  //   },
  //   register(context, user) {
  //     const URL = `${baseUrl}/auth/register`;
  //     axios({
  //       url: URL,
  //       method: "POST",
  //       data: user,
  //     })
  //       .then((res) => {
  //         console.log(res);
  //         router.push({
  //           name: "UserRegisterDone",
  //           params: { id: res.data.id },
  //         });
  //       })
  //       .catch((res) => {
  //         console.log(res);
  //       });
  //   },
  // },
});
