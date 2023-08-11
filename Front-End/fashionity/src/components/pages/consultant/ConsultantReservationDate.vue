<template lang="">
  <div class="container-fluid" style="height: 65vh; background-color: blue">
    <!-- <v-date-picker width="400"></v-date-picker> -->
    <!-- <ConsultantCalenerTempVue></ConsultantCalenerTempVue> -->
    변경해유
    <v-date-picker></v-date-picker>
  </div>
</template>
<script>
// import ConsultantCalenerTempVue from "./ConsultantCalenerTemp.vue";

export default {
  data() {
    return {
      keyword: "",
    };
  },
  components: {
    // ConsultantCalenerTempVue,
  },
};
</script>
<style></style>
