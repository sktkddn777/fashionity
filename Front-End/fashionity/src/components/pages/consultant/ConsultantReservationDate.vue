<template lang="">
  <div class="container-fluid" style="height: 65vh">
    <v-calendar is-expanded></v-calendar>
    <!-- <ConsultantCalenerTempVue></ConsultantCalenerTempVue> -->
    <!-- <v-date-picker
        v-model="date"
        is-expanded
        is-range
        color="orange"
      ></v-date-picker> -->
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
