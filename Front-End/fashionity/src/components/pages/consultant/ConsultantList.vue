<template lang="">
  <div class="container-fluid">
    <the-nav-bar-consultant></the-nav-bar-consultant>
    <div class="row justify-content-space-around">
      <div class="col-3">
        <input
          type="text"
          id="search"
          name="search"
          placeholder="검색어를 입력하세요"
          style="border: 1px solid #bdbdbd"
        />
      </div>
      <div class="col"></div>
      <!-- <div class="col-3">
        <span>최신순</span>
        <span> | </span>
        <span>인기순</span>
      </div> -->
    </div>

    <div class="row" style="height: 30px"></div>

    <div class="container">
      <div class="row" style="justify-content: center">
        <div class="col">
          <the-post></the-post>
        </div>
        <div class="col">
          <the-post></the-post>
        </div>
        <div class="col">
          <the-post></the-post>
        </div>
        <div class="col">
          <the-post></the-post>
        </div>
      </div>

      <div class="row" style="height: 30px"></div>

      <div class="row">
        <div class="col">
          <the-post></the-post>
        </div>
        <div class="col">
          <the-post></the-post>
        </div>
        <div class="col">
          <the-post></the-post>
        </div>
        <div class="col">
          <the-post></the-post>
        </div>
      </div>

      <div class="row" style="height: 40px"></div>
    </div>
  </div>
</template>
<script>
import thePost from "@/components/shared/ThePost";
import TheNavBarConsultant from "@/components/layout/TheNavBarConsultant.vue";

export default {
  data() {
    return {
      keyword: "",
    };
  },
  components: {
    thePost,
    TheNavBarConsultant,
  },
};
</script>
<style lang=""></style>
