<template lang="">
  <div>
    <div class="container-fluid">
      <div class="row">
        <div class="col-3" id="logo">Fashionity</div>
        <div class="col">
          <div class="row justify-content-end">
            <div class="col-8"></div>
            <div class="col">
              <div class="row justify-content-end">
                <div class="col-6"></div>
                <div class="col">22</div>
                <div class="col">22</div>
                <div class="col">22</div>
                <div class="col box">
                  <img class="profile" src="@/assets/img/unknown.png" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="row justify-content-center">
        <div class="col col-lg-2 header-tab">HOME</div>
        <div class="col col-lg-2 header-tab">CONSULTING</div>
        <div class="col col-lg-2 header-tab">LIVE</div>
        <div class="col col-lg-2 header-tab">MY PAGE</div>
      </div>
    </div>
  </div>
</template>
<script>
export default {};
</script>
<style>
.header-tab {
  font-style: normal;
  font-size: 25px;
  line-height: 38px;

  text-align: center;
}
#logo {
  font-style: normal;
  font-size: 50px;
}

.box {
  width: 30px;
  height: 30px;
  border-radius: 70%;
  overflow: hidden;
}
.profile {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
