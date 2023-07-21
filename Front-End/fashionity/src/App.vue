<template>
  <div>
    <!-- <img alt="Test Image" src="./assets/img/testImage.jpeg"> -->
    <h2>font test</h2>
    <h2>한글 테스트</h2>

    <TheFooter></TheFooter>

    

  </div>
  

</template>

<script>
import TheFooter from './components/TheFooter.vue';


export default {
  name: 'App',
  components: {
    TheFooter,
  }
}
</script>

<style>
@import "./App.css";
</style>
