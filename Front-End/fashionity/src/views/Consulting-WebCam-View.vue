<template>
  <div class="container">
    <first-component class="compo"></first-component>
    <second-component class="compo"></second-component>
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
};
</script>

<style>
.container {
  display: flex;
}

.compo {
  flex: 1;
}
/* .compo:nth-child(2) {
  flex: 1;
} */
</style>
