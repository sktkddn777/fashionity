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
        <img
          :src="profileUrl || require('@/assets/img/unknown.png')"
          class="image-box"
          width="60"
          height="60"
        />
      </div>
      <div>{{ follower.nickname }}</div>
    </div>
    <button
      v-if="nickname !== myNickname"
      id="followbtn"
      :class="isFollowed ? 'inactive-button' : 'active-button'"
      @click="toggleFollow()"
    >
      {{ isFollowed ? "팔로잉" : "팔로우" }}
    </button>
  </div>
</template>

<script>
import axios from "axios";

export default {
  props: ["follower"],
  data() {
    return {
      profileUrl: this.follower.profileUrl,
      nickname: this.follower.nickname,
      isFollowing: this.follower.isFollowing,
      isFollowed: this.follower.isFollowed, // 로그인한 유저가 팔로우하는지
      myNickname: this.$store.getters["memberStore/checkLoginUser"].nickname,
    };
  },

  methods: {
    async toggleFollow() {
      if (!this.isFollowed) {
        await this.followAPI(this.follower.nickname);
      } else {
        await this.unfollowAPI(this.follower.nickname);
      }
    },
    async followAPI(nickname) {
      let token = sessionStorage.getItem("token");
      let body = { nickname: nickname };
      axios({
        method: "post",
        url: `${process.env.VUE_APP_API_URL}/api/v1/follows`,
        headers: { Authorization: `Bearer ${token}` },
        data: body,
      }).then((res) => {
        this.success = res.data.success;
        this.isFollowed = !this.isFollowed;
      });
    },
    async unfollowAPI(nickname) {
      let token = sessionStorage.getItem("token");
      let body = { nickname: nickname };
      axios({
        method: "delete",
        url: `${process.env.VUE_APP_API_URL}/api/v1/follows`,
        headers: { Authorization: `Bearer ${token}` },
        data: body,
      }).then((res) => {
        this.success = res.data.success;
        this.isFollowed = !this.isFollowed;
      });
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
