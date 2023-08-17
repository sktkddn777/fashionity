<template>
  <div class="container">
    <first-component
      class="compo"
      :reservation-seq="$route.query.sessionId"
      :image-list="$route.query.imageList"
    ></first-component>
    <second-component
      class="compo"
      :reservation-seq="$route.query.sessionId"
    ></second-component>
  </div>
</template>

<script>
import FirstComponent from "../components/pages/consulting/Consulting-WebCam.vue";
import SecondComponent from "../components/pages/consulting/TheChatting.vue";

export default {
  components: {
    FirstComponent,
    SecondComponent,
  },
  data() {
    return {
      reservationSeq: null,
    };
  },
};
</script>

<style scoped>
.container {
  display: flex;
}

.compo {
  flex: 3;
}
.compo:nth-child(2) {
  flex: 1;
}
</style>
