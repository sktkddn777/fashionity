<template lang="">
  <div class="container-fluid" style="height: 65vh">
    <VDatePicker v-model="date" mode="date" />
    <div class="row">
      <div class="col"></div>
      <div class="col-3">
        <button
          v-if="date"
          class="active-button"
          style="font-size: 1rem"
          @click="buttonClick"
        >
          다음
        </button>
        <button v-else class="inactive-button" style="font-size: 1rem">
          다음
        </button>
      </div>
    </div>
  </div>
</template>
<script>
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
  components: {},
  methods: {
    buttonClick() {
      this.$router.push({
        name: "consultantTime",
        params: { date: this.date, nickname: this.nickname },
      });
    },
  },
};
</script>
<style scoped>
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
