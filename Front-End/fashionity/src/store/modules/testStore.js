import Vuex from "vuex";

export default new Vuex.Store({
    state: {
      meetingInfo: {
        userName: '',
        roomId: ''
      }
    },
    mutations: {
      setMeetingInfo(state, payload) {
        state.meetingInfo = payload;
      }
    },
    actions: {
      updateMeetingInfo({ commit }, meetingInfo) {
        commit('setMeetingInfo', meetingInfo);
      }
    }
  });