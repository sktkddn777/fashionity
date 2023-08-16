<template>
	<div v-for = "follower in followers" :key = "follower.nickname">
		<follow-summary :follower="follower"/>
	</div>
</template>

<script>
import FollowSummary from "@/components/pages/user/FollowSummary.vue"
import axios from "axios";

let token = sessionStorage.getItem("token");

export default {
	data(){
		return{
			followers: [],
		}
	},
	components: {
		FollowSummary
	},
	mounted() {
		this.getFollowers();
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
		async getFollowers(){
			let nickname = this.$route.params.nickname;
			axios({
				method : 'get',
				url: `${process.env.VUE_APP_API_URL}/api/v1/members/${nickname}/followers`,
				headers: {Authorization:`Bearer ${token}`}
			})
			.then(async ({data}) => {
				for (let i = 0; i < data.followers.length; i++){
					data.followers[i].isFollowed = await this.getFollowInfo(data.followers[i].nickname)
				}
				this.followers = data.followers
			})
			.catch((error) => console.log(error))
		},
		
	}
}
</script>
