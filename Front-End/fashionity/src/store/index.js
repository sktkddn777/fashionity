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
});
