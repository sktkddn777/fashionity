<template>
  <router-link
    :to="getAlarmLink(this.alarm)"
    style="text-decoration: none; color: #424242"
    @click="readUpdate(this.alarm)"
  >
    <div class="alert" :class="{ 'is-readed': this.alarm.is_readed }">
      <!-- 팔로잉 알람 -->
      <div class="alert-content" v-if="this.alarm.type === 'FOLLOW'">
        <div class="title">
          <span class="fw-bold">{{ this.alarm.publisher_nickname }}</span>
          님이 회원님을 팔로우 합니다.
        </div>
      </div>
      <!-- 게시글 좋아요 알람 -->
      <div
        class="col alert-content"
        v-else-if="this.alarm.type === 'POST_LIKE'"
      >
        <div class="title">{{ this.alarm.title }}</div>
        <div class="content">{{ this.alarm.content }}</div>
      </div>
      <!-- 댓글 좋아요 알람 -->
      <div class="alert-content" v-else-if="this.alarm.type === 'COMMENT_LIKE'">
        <div class="title">{{ this.alarm.title }}</div>
        <div class="content">
          {{ this.alarm.content }}
        </div>
      </div>
      <!-- 게시글에 댓글 등록 알람 -->
      <div class="alert-content" v-else-if="this.alarm.type === 'COMMENT_POST'">
        <div class="title">
          {{ this.alarm.title }}
        </div>
        <div class="content">
          {{ this.alarm.content }}
        </div>
      </div>
      <div class="alert-image">
        <img
          :src="this.alarm.imageUrl || '../img/unknown.e083a226.png'"
          alt=""
          class="profile-comment"
        />
      </div>
    </div>
  </router-link>
</template>
<script>
import axios from "axios";
export default {
  setup() {},
  props: ["alarm"],
  methods: {
    getAlarmLink(alarm) {
      if (alarm.type === "FOLLOW") {
        return { path: `/profile/${alarm.publisher_nickname}` };
      } else {
        return { path: `/post/${alarm.post_seq}` };
      }
    },
    readUpdate(alarm) {
      const token = sessionStorage.getItem("token");
      axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/alarm/${alarm.alarm_seq}`,
        method: "patch",
        headers:
          token === null
            ? null
            : {
                Authorization: `Bearer ${token}`,
              },
      }).then(() => {});

      this.$emit("detail-click");
    },
  },
};
</script>
<style scoped>
.alert {
  display: flex;
  justify-content: flex-start;
  margin-bottom: 1px;
  padding: 13px;
  gap: 10px;
  align-items: center;
  justify-content: center;
  width: 100%;
  border-bottom: 1px solid rgba(190, 190, 190, 0.1);
}
.is-readed {
  background-color: rgba(190, 190, 190, 0.1);
  opacity: 0.3;
}
.profile-comment {
  width: 50px;
  height: 50px;
  border-radius: 100%;
  object-fit: contain;
}
.alert-content {
  text-align: left;
}

.title {
  font-weight: 500;
}

.content {
  font-size: 13px;
  color: rgba(66, 66, 66, 0.7);
}
</style>
