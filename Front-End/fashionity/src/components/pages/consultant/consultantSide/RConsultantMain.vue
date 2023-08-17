<template>
  <the-nav-check-reservation></the-nav-check-reservation>
  <div class="container">
    <router-view></router-view>
  </div>
</template>

<script>
import TheNavCheckReservation from "./layout/TheNavCheckReservation.vue";
export default {
  name: "RConsultantMain",
  components: {
    TheNavCheckReservation,
  },
  // props: {},

  // data: () => ({}),
  // methods: {},
};
</script>

<style scoped>
</style>
