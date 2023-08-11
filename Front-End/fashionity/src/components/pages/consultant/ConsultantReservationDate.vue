<template lang="">
  <div class="container-fluid" style="height: 65vh">
    <!-- <v-date-picker width="400"></v-date-picker> -->
    <!-- <ConsultantCalenerTempVue></ConsultantCalenerTempVue> -->
    <!-- 변경해유
    <v-date-picker></v-date-picker> -->
    <VDatePicker v-model="date" mode="date" />
    <!-- {{ date }} -->
  </div>
</template>
<script>
// import ConsultantCalenerTempVue from "./ConsultantCalenerTemp.vue";

// import { ref } from "vue";
// const date = ref(new Date());

export default {
  data() {
    return {
      date: "",
    };
  },

  props: {
    pageInfo: String,
  },
  mounted() {
    // this.pageInfo = "time";
    this.$emit("router-event", this.pageInfo); // 'custom-event'라는 이벤트를 발생시키고 데이터를 함께 보냅니다.
  },
};
</script>
<style scoped></style>
