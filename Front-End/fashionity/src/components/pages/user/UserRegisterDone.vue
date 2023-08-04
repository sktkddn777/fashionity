<template>
  <div class="signup-container">
    <div class="signup-content">
      <v-icon class="signup-content-icon">mdi-check</v-icon>
      <div class="signup-content-text-top">회원가입이 완료되었습니다.</div>
      <div class="signup-content-text-bottom">
        {{ id }}님의 회원가입 축하드립니다.
      </div>
    </div>
    <div @click="toLogin" class="signup-button b_lightgray_l">
      <div>로그인하러 가기</div>
    </div>
  </div>
</template>

<script>
import { reactive, onBeforeMount } from "vue";
import router from "@/router";

export default {
  props: {
    id: {
      type: String,
    },
  },
  setup(props) {
    const state = reactive({
      id: "",
    });

    onBeforeMount(() => {
      state.id = props.id;
    });
    const toLogin = () => {
      router.push({
        name: "UserLogin",
      });
    };

    return {
      state,
      toLogin,
    };
  },
};
</script>
