<template>
  <div v-for="following in followings" :key="following.nickname">
    <following-summary :following="following" />
  </div>
</template>

<script>
import FollowingSummary from "@/components/pages/user/FollowingSummary.vue";
import axios from "axios";

let token = sessionStorage.getItem("token");

export default {
  data() {
    return {
      followings: [],
    };
  },
  components: {
    FollowingSummary,
  },
  mounted() {
    this.getFollowings();
  },
  methods: {
    getFollowings() {
      let nickname = this.$route.params.nickname;
      axios({
        method: "get",
        url: `${process.env.VUE_APP_API_URL}/api/v1/members/${nickname}/followings`,
        headers: { Authorization: `Bearer ${token}` },
      })
        .then(({ data }) => {
          this.followings = data.followings;
        })
        .catch((error) => console.log(error));
    },
  },
};
</script>
