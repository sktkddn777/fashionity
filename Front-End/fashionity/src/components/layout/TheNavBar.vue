<template lang="">
  <div class="d-flex justify-content-end" id="navbar">
    <div class="p-2">
      <font-awesome-icon icon="circle-plus" style="color: #0f0f0f" />
    </div>
    <div class="p-2">Flex item 2</div>
    <div class="p-2">Flex item 3</div>
    <div class="p-2 box">
      <img class="profile" src="@/assets/img/unknown.png" />
    </div>
  </div>
</template>
<script>
export default {};
</script>
<style>
#navbar {
  margin: 0;
  padding: 0;
}
.box {
  width: 50px;
  height: 50px;
  border-radius: 70%;
  overflow: hidden;
}
.profile {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
