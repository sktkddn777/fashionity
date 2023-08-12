<template>
  <div
    class="follower"
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
      <div class="profile-photo" style="margin-right: 1.2rem">
        <img :src = "profileUrl" class="image-box" width="60" height="60" />
      </div>
      <div>{{ follower.nickname }}</div>
    </div>
    <button
      v-if = "nickname !== myNickname"
      id="followbtn"
      :class="isFollowed ? 'inactive-button' : 'active-button'"
      @click="toggleFollow()"
    >
      {{ isFollowed ? '팔로잉' : '팔로우' }}
    </button>
  </div>
</template>

<script>
import axios from "axios";

let token = sessionStorage.getItem("token");
const followbtn = document.querySelector("#followvbtn");

export default {
  props: ["follower"],
  data() {
    return {
      profileUrl: this.follower.profileUrl,
      nickname: this.follower.nickname,
      isFollowing: this.follower.isFollowing,
      isFollowed: this.follower.isFollowed, // 로그인한 유저가 팔로우하는지
      myNickname : this.$store.getters["memberStore/checkLoginUser"].nickname
    };
  },
  
  methods: {
    async toggleFollow() {
      if (!this.isFollowed) {
        await this.followAPI(this.follower.nickname);
      } else {
        await this.unfollowAPI(this.follower.nickname);
      }
      if (this.isFollowed == true) {followbtn.innerText = "팔로잉";} 
      else if (this.follower.nickname === this.myNickname) {followbtn.innerText = "팔로잉";} //{followbtn.style.display = "none"}
      else {followbtn.innerText = "팔로우";}
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
