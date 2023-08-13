<template>
  <the-nav-check-reservation></the-nav-check-reservation>
  <router-view></router-view>
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

<style scoped></style>
