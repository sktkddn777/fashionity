<template lang="">
  <div class="container-fluid" style="height: 65vh">
    <VDatePicker v-model="date" mode="date" />
    <div class="row">
      <div class="col"></div>
      <div class="col-3">
        <router-link
          class="link"
          :to="{
            name: 'consultantTime',
            query: { date: this.date },
          }"
          ><button>NEXT</button></router-link
        >
        <!-- <div>{{ this.scheduleList }}</div> -->
        <div>{{ this.date }}</div>
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
  },
  components: {
    // ConsultantCalenerTempVue,
  },
};
</script>
<style scoped>
button {
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
}
</style>
