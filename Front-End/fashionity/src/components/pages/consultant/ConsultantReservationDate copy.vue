<template lang="">
  <div class="container-fluid" style="height: 65vh">
    <v-date-picker width="400"></v-date-picker>
    <!-- <ConsultantCalenerTempVue></ConsultantCalenerTempVue> -->
    <!-- <v-date-picker
        v-model="date"
        is-expanded
        is-range
        color="orange"
      ></v-date-picker> -->
    <!-- 
    <div class="d-flex justify-center">
      <v-locale-provider locale="sv">
        <v-date-picker></v-date-picker>
      </v-locale-provider> -->
    <!-- </div> -->
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
