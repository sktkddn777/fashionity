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
		async getFollowInfo(nickname) {
      // 로그인한 유저의 팔로우 정보
      return axios({
        method: "get",
        url: `${process.env.VUE_APP_API_URL}/api/v1/follows/${nickname}`,
        headers: { Authorization: `Bearer ${token}` },
      }).then(({ data }) => {
				return data.isFollowing
      });
    },
		async getFollowings(){
			let nickname = this.$route.params.nickname;
			axios({
				method : 'get',
				url: `${process.env.VUE_APP_API_URL}/api/v1/members/${nickname}/followings`,
				headers: {Authorization:`Bearer ${token}`}
			})
			.then(async ({data}) => {
				for (let i = 0; i < data.followings.length; i++){
					data.followings[i].isFollowed = await this.getFollowInfo(data.followings[i].nickname)
				}
				this.followings = data.followings
			})
			.catch((error) => console.log(error))
		},
		
	}
};
</script>
