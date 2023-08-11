const testStore = {
  namespaced: true,
  state: {
    meetingInfo: {
      userName: "",
      roomId: "",
    },
  },
  mutations: {
    setMeetingInfo(state, payload) {
      state.meetingInfo = payload;
    },
  },
  actions: {
    updateMeetingInfo({ commit }, meetingInfo) {
      commit("setMeetingInfo", meetingInfo);
    },
  },
};

export default testStore;
