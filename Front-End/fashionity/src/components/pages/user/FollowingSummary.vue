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
      <div class="profile-photo" style="margin-right: 1.2rem">
        <img
          :src="profileUrl || require('@/assets/img/unknown.png')"
          class="image-box"
          width="60"
          height="60"
        />
      </div>
      <div>{{ following.nickname }}</div>
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
// import { computed } from 'vue'
// import { useStore } from 'vuex'
import axios from "axios";

export default {
  props: ["following"],
  data() {
    return {
      profileUrl: this.following.profileUrl,
      nickname: this.following.nickname,
      isFollowing: this.following.isFollowing,
      isFollowed: this.following.isFollowed,
      myNickname: this.$store.getters["memberStore/checkLoginUser"].nickname,
    };
  },
  methods: {
    async toggleFollow() {
      if (!this.isFollowed) {
        await this.followAPI(this.following.nickname);
      } else {
        await this.unfollowAPI(this.following.nickname);
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
      })
        .then((res) => {
          this.success = res.data.success;
          this.isFollowed = !this.isFollowed;
        })
        .catch((error) => console.log(error));
    },
    async unfollowAPI(nickname) {
      let token = sessionStorage.getItem("token");
      let body = { nickname: nickname };
      axios({
        method: "delete",
        url: `${process.env.VUE_APP_API_URL}/api/v1/follows`,
        headers: { Authorization: `Bearer ${token}` },
        data: body,
      })
        .then((res) => {
          this.success = res.data.success;
          this.isFollowed = !this.isFollowed;
        })
        .catch((error) => console.log(error));
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
