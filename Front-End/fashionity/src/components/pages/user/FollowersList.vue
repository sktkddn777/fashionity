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
			followers: []
		}
	},
	components: {
		FollowSummary
	},
	mounted() {
		this.getFollowers();
	},
	methods: {
		getFollowers(){
			let nickname = this.$route.params.nickname;
			axios({
				method : 'get',
				url: `${process.env.VUE_APP_API_URL}/api/v1/members/${nickname}/followers`,
				headers: {Authorization:`Bearer ${token}`}
			})
			.then(({data}) => {
				console.log(data.followers)
				this.followers = data.followers
			})
			.catch((error) => console.log(error))
		}
	}
}
</script>
