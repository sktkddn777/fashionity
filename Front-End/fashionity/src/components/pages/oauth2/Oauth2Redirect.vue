<template>
  <div></div>
</template>

<script>
import router from "@/router";
import jwt_decode from "jwt-decode";
import store from "@/store";
import axios from "axios";

export default {
  created() {
    const token = this.$route.query.accessToken;
    let user = jwt_decode(token);

    axios({
      url: `${process.env.VUE_APP_API_URL}/api/v1/members`,
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(({ data }) => {
        const oauthUserInfo = {
          accessToken: token,
          memberSeq: user.id,
          profileUri: data.profileUrl,
        };
        store.commit("memberStore/LOGIN", oauthUserInfo);
        store.commit("memberStore/SET_IS_VALID_TOKEN", true);
        router.push({ name: "main" });
      })
      .catch((error) => {
        console.log(error);
      });
  },
};
</script>
