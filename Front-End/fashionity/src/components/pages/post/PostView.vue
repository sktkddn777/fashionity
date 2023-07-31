<template lang="">
  <div
    class="container-fluid"
    style="
      padding-top: 20px;
      border-style: solid;
      border-width: 1px;
      border-color: #bdbdbd white white white;
    "
  >
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
      <div class="col-3">
        <span>최신순</span>
        <span> | </span>
        <span>인기순</span>
      </div>
    </div>

    <div class="container">
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
    </div>
  </div>
</template>
<script>
import thePost from "@/components/shared/ThePost";

export default {
  data() {
    return {
      keyword: "",
    };
  },
  components: {
    thePost,
  },
};
</script>
<style lang=""></style>
