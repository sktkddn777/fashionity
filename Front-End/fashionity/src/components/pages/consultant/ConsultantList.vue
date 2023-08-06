<template lang="">
  <div class="container-fluid">
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
    </div>

    <div class="row" style="height: 30px"></div>

    <div class="container">
      <div class="row" style="justify-content: center">
        <div class="col">
          <router-link class="link" to="/consultant/reservation/"
            ><consultant-block-vue></consultant-block-vue
          ></router-link>
        </div>
        <div class="col">
          <router-link class="link" to="/consultant/reservation/"
            ><consultant-block-vue></consultant-block-vue
          ></router-link>
        </div>
        <div class="col">
          <router-link class="link" to="/consultant/reservation/"
            ><consultant-block-vue></consultant-block-vue
          ></router-link>
        </div>
        <div class="col">
          <router-link class="link" to="/consultant/reservation/"
            ><consultant-block-vue></consultant-block-vue
          ></router-link>
        </div>
      </div>

      <div class="row" style="height: 30px"></div>

      <div class="row">
        <div class="col">
          <router-link class="link" to="/consultant/reservation/"
            ><consultant-block-vue></consultant-block-vue
          ></router-link>
        </div>
        <div class="col">
          <router-link class="link" to="/consultant/reservation/"
            ><consultant-block-vue></consultant-block-vue
          ></router-link>
        </div>
        <div class="col">
          <router-link class="link" to="/consultant/reservation/"
            ><consultant-block-vue></consultant-block-vue
          ></router-link>
        </div>
        <div class="col">
          <router-link class="link" to="/consultant/reservation/"
            ><consultant-block-vue></consultant-block-vue
          ></router-link>
        </div>
      </div>

      <div class="row" style="height: 40px"></div>
    </div>
  </div>
</template>
<script>
import ConsultantBlockVue from "./ConsultantBlock.vue";

export default {
  data() {
    return {
      keyword: "",
    };
  },
  components: {
    ConsultantBlockVue,
  },
};
</script>
<style lang=""></style>
