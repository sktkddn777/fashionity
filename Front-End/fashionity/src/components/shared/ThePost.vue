<template lang="">
  <div id="post outer" class="container-fluid" style="background-color: white">
    <div class="row justify-content-center">
      <img
        src="../../assets/img/postImg.jpg"
        alt=""
        style="top: 0; left: 0; height: 50%; max-width: 250px"
      />
      <div class="row">
        <div class="col box">
          <img class="profile" src="@/assets/img/unknown.png" />
        </div>
        <div class="col post-font">좋아유</div>
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
.box {
  height: 10px;
  width: 10px;
  border-radius: 70%;
  overflow: hidden;
}
.profile {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.outer {
  display: flex;
  justify-content: center;
}

.post-font {
  font-size: 10px;
}
</style>
