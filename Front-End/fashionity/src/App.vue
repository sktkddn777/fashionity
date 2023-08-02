<template>
  <v-app class="first">
    <div>
      <!-- <img alt="Test Image" src="./assets/img/testImage.jpeg"> -->
      <the-header></the-header>

      <!-- <section id="wrapper">
      <div id="content">content</div>
    </section> -->
      <router-view></router-view>
      <the-footer></the-footer>
    </div>
  </v-app>
</template>

<script>
import TheFooter from "./components/layout/TheFooter.vue";
import TheHeader from "./components/layout/TheHeader.vue";

export default {
  name: "App",
  components: {
    TheHeader,
    TheFooter,
  },
};
</script>

<style>
@import "./App.css";

* {
  margin: 0px;
  padding: 0px;
}

#wrapper {
  height: auto;
  min-height: 100%;
  padding-bottom: (footer높이);
}

#content {
  height: 1000px;
  background-color: lightgray;
  overflow-y: scroll;
  -ms-overflow-style: none; /* 인터넷 익스플로러 */
  scrollbar-width: none; /* 파이어폭스 */
}
#content::-webkit-scrollbar {
  display: none; /* 크롬, 사파리, 오페라, 엣지 */
}
</style>
