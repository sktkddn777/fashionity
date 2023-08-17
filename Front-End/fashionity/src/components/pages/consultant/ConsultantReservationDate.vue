<template lang="">
  <div class="container-fluid" style="height: 65vh">
    <VDatePicker v-model="date" mode="date" />
    <div class="row">
      <div class="col"></div>
      <div class="col-3">
        <button v-if = "date" class = "active-button" style = "font-size:1rem" @click="buttonClick">다음</button>
        <button v-else class = "inactive-button" style = "font-size:1rem">다음</button>
      </div>
    </div>
  </div>
</template>
<script>
// import ConsultantCalenerTempVue from "./ConsultantCalenerTemp.vue";

export default {
  data() {
    return {
      date: "",
    };
  },
  props: {
    scheduleList: [],
    nickname: String,
  },
  components: {
    // ConsultantCalenerTempVue,
  },
  methods: {
    buttonClick() {
      // console.log("눌러짐");
      this.$router.push({
        name: "consultantTime",
        params: { date: this.date, nickname: this.nickname },
      });
    },
  },
};
</script>
<style scoped>
/* button {
  background: black;
  color: #fff;
  border: none;
  position: relative;
  height: 40px;
  font-size: 1.2em;
  padding: 0 2em;
  cursor: pointer;
  transition: 800ms ease all;
  outline: none;
}
button:hover {
  background: #fff;
  color: #424242;
}
button:before,
button:after {
  content: "";
  position: absolute;
  top: 0;
  right: 0;
  height: 2px;
  width: 0;
  background: #424242;
  transition: 400ms ease all;
}
button:after {
  right: inherit;
  top: inherit;
  left: 0;
  bottom: 0;
}
button:hover:before,
button:hover:after {
  width: 100%;
  transition: 800ms ease all;
} */
.active-button {
  width: 100px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #2191ff;
  color: #ffffff;
}
.inactive-button {
  width: 100px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #cecece;
  color: #ffffff;
}
</style>
