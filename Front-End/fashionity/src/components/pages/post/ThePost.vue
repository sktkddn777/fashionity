<template lang="">
  <div id="post outer" class="container-fluid" style="background-color: white">
    <div class="row justify-content-center">
      <img
        src="../../../assets/img/postImg.jpg"
        alt=""
        style="
          top: 0;
          left: 0;
          height: 50%;
          max-width: 250px;
          border-radius: 20px;
        "
      />
      <div class="row align-self-center align-middle">
        <div
          class="col align-self-center align-middle"
          style="display: flex; justify-content: center; align-items: center"
        >
          <img class="profile" src="@/assets/img/unknown.png" />
          <span class="post-font">rlaxogus123</span>
        </div>
        <div class="col post-font align-self-center">좋아유</div>
      </div>
      <div class="row post-font justify-content-center">
        게시글이에요오오오오오오오오오오오오
      </div>
    </div>
  </div>
</template>
<script>
export default {};
</script>
<style>
.profile {
  height: 20px;
  border-radius: 70%;
  object-fit: contain;
}

.outer {
  display: flex;
  justify-content: center;
}

.post-font {
  font-size: 12px;
}
</style>
