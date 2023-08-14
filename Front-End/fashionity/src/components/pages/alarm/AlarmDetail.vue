<template>
  <div class="alert">
    <router-link :to="getAlarmLink(this.alarm)">
      <div class="alert-image">
        <img
          :src="this.alarm.imageUrl || '../img/unknown.e083a226.png'"
          alt=""
          class="profile-comment"
        />
      </div>
    </router-link>
    <!-- 팔로잉 알람 -->
    <div class="alert-content" v-if="this.alarm.type === 'FOLLOW'">
      <span class="fw-bold">{{ this.alarm.publisher_nickname }}</span>
      님이 회원님을 팔로우 합니다.
    </div>
    <!-- 게시글 좋아요 알람 -->
    <div class="alert-content" v-else-if="this.alarm.type === 'POST_LIKE'">
      {{ this.alarm.title }}
    </div>
    <!-- 댓글 좋아요 알람 -->
    <div class="alert-content" v-else-if="this.alarm.type === 'COMMENT_LIKE'">
      {{ this.alarm.title }}<br />
      {{ this.alarm.content }}
    </div>
    <!-- 게시글에 댓글 등록 알람 -->
    <div class="alert-content" v-else-if="this.alarm.type === 'COMMENT_POST'">
      {{ this.alarm.title }}<br />
      {{ this.alarm.content }}
    </div>
  </div>
</template>
<script>
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
  },
};
</script>
<style scoped>
.alert {
  display: flex;
  justify-content: flex-start;
  margin-bottom: 0;
  padding: 13px;
  gap: 10px;
}
.profile-comment {
  height: 5vh;
  border-radius: 100%;
  object-fit: contain;
}
.alert-content {
  text-align: left;
}
</style>
