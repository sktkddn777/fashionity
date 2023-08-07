<template>
  <div></div>
</template>

<script>
import router from "@/router";
import jwt_decode from "jwt-decode";
import store from "@/store";

export default {
  created() {
    const token = this.$route.query.accessToken;
    let user = jwt_decode(token);

    const data = {
      accessToken: token,
      memberSeq: user.id,
    };
    store.commit("memberStore/LOGIN", data);
    router.push({ name: "main" });
  },
};
</script>
