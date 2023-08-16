<template>
  <div class="container-fluid">
    <the-nav-bar-mypage></the-nav-bar-mypage>
    <div class="centered-container">
      <img src="@/assets/img/membernotfound.jpg" style="width: 20rem" />
      <br />
      <h1 class="text-center"><b>멤버없삼!!!ㅋ</b></h1>
    </div>
  </div>
</template>
<script>
import TheNavBarMypage from "@/components/layout/TheNavBarMypage.vue";
export default {
  components: {
    TheNavBarMypage,
  },
};
</script>
<style>
.centered-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 26rem;
}

.text-center {
  text-align: center;
}
</style>
