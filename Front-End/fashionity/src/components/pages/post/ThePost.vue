<template lang="">
  <div id="post outer" class="container-fluid" style="background-color: white">
    <div class="row justify-content-center">
      <img
        :src="post.images[0]" 
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
          <span class="post-font">{{post.name}}</span>
        </div>
        <div class="col post-font align-self-center">좋아유</div>
      </div>
      <div class="row post-font justify-content-center">
        {{post.content}}
      </div>
    </div>
  </div>
</template>
<script>
export default {
  props:['post'],
};
</script>
<style>
.profile {
  height: 20px;
  width: 20px;
  border-radius: 70%;
  object-fit: contain;
  margin-right: 10px;
}

.outer {
  display: flex;
  justify-content: center;
}

.post-font {
  font-size: 12px;
}
</style>
