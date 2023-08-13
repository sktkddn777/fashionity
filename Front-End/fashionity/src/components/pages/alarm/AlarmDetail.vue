<template>
  <div class="alert">
    <div class="alert-image">
      <img
        src="../../../assets/img/hyeonwook.jpg"
        alt=""
        class="profile-comment"
      />
    </div>
    <div class="alert-content">
      <span class="fw-bold">hyeonwook12</span>
      님이 회원님의 게시글에 댓글을 작성했습니다.
    </div>
  </div>
</template>
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
