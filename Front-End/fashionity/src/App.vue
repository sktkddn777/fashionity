<template>
  <v-app class="first">
    <div class="total-body">
      <!-- <img alt="Test Image" src="./assets/img/testImage.jpeg"> -->
      <the-header id="total-header"></the-header>

      <!-- <section id="wrapper">
      <div id="content">content</div>
    </section> -->
      <router-view id="total-view"></router-view>
      <the-footer id="total-footer"></the-footer>
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
.total-body {
  padding: 0px;
  display: flex;
  flex-direction: column;
}

#total-header {
}
#total-view {
}
#total-footer {
}

#wrapper {
  height: auto;
  min-height: 100%;
  padding-bottom: (footer높이);
}

#content {
  height: 1000px;
  overflow-y: scroll;
  -ms-overflow-style: none; /* 인터넷 익스플로러 */
  scrollbar-width: none; /* 파이어폭스 */
}
#content::-webkit-scrollbar {
  display: none; /* 크롬, 사파리, 오페라, 엣지 */
}

/* Webkit 브라우저 (Chrome, Safari) */
::-webkit-scrollbar {
  width: 10px; /* 스크롤바의 너비 */
  height: 10px; /* 가로 스크롤바의 높이 */
}

::-webkit-scrollbar-thumb {
  background-color: #bdbdbd; /* 스크롤바 색상 */
  border-radius: 8px; /* 스크롤바 모서리 둥글게 */
}

::-webkit-scrollbar-thumb:hover {
  background-color: #9e9e9e; /* 스크롤바 색상 (마우스 호버 시) */
}

/* Firefox 브라우저 */
* {
  scrollbar-width: thin; /* 스크롤바의 너비 조정 */
}

* {
  scrollbar-color: #bdbdbd transparent; /* 스크롤바 색상 */
}

*:hover {
  scrollbar-color: #9e9e9e transparent; /* 스크롤바 색상 (마우스 호버 시) */
}

/* 가로 스크롤바 스타일링 */
.horizontal-scroll-container {
  white-space: nowrap; /* 요소 내용을 한 줄로 나타냄 */
  overflow-x: auto; /* 가로 스크롤바 표시 */
}
</style>
