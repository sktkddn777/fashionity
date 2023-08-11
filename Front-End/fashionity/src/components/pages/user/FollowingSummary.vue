<template>
  <div
    class="following"
    style="
      display: flex;
      justify-content: space-between;
      margin: 1.2rem;
      align-items: center;
    "
  >
    <div
      class="profile-info"
      style="display: flex; flex-direction: row; align-items: center"
    >
      <!-- <div>{{ following.profileUrl }}</div> -->
      <div class="profile-photo" style="margin-right: 1.2rem">
        <img class="image-box" width="60" height="60" />
      </div>
      <div>{{ following.nickname }}</div>
    </div>
    <button
      id="followbtn"
      :class="isFollowed ? 'inactive-button' : 'active-button'"
      @click="toggleFollow()"
    >
      팔로우
    </button>
  </div>
</template>

<script>
// import { computed } from 'vue'
// import { useStore } from 'vuex'
import axios from "axios";

let token = sessionStorage.getItem("token");

export default {
  props: ["following"],
  data() {
    return {
      profileUrl: this.following.profileUrl,
      nickname: this.following.nickname,
      isFollowing: this.following.isFollowing,
    };
  },
  // setup() {
  //   const store = useStore();
  //   this.test = computed(() => store.state.loginUser);
  // },
  // computed: {
  // 	...mapGetters(memberStore, ['checkLoginUser']),
  // },
  methods: {
    async toggleFollow() {
      console.log(this.nickname);
      if (!this.isFollowing) {
        await this.followAPI(this.nickname);
      } else {
        await this.unfollowAPI(this.nickname);
      }
      const followbtn = document.querySelector("#followvbtn");
      if (this.isFollowing == true) {
        followbtn.innerText = "팔로잉";
      }
      // else if(this.nickname === this.checkLoginUser){document.getElementById('followbtn').style.visibility ='hidden'}
      else {
        followbtn.innerText = "팔로우";
      }
    },
    async followAPI(nickname) {
      let body = { nickname: nickname };
      axios({
        methods: "post",
        url: `${process.env.VUE_APP_API_URL}/api/v1/follows`,
        headers: { Authorization: `Bearer ${token}` },
        data: body,
      }).then((res) => (this.success = res.data.success));
    },
    async unfollowAPI(nickname) {
      let body = { nickname: nickname };
      axios({
        methods: "delete",
        url: `${process.env.VUE_APP_API_URL}/api/v1/follows`,
        headers: { Authorization: `Bearer ${token}` },
        data: body,
      }).then((res) => (this.success = res.data.success));
    },
  },
};
</script>
<style scoped>
.image-box {
  border-radius: 50%;
}
.active-button {
  width: 100px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #2191ff;
  color: #ffffff;
}
.inactive-button {
  width: 100px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #cecece;
  color: #ffffff;
}
</style>
