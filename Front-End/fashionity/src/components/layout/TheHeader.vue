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
                <div class="col">
                  <div class="row">
                    <router-link
                      to="/user/login"
                      class="link"
                      ><img class="profile" src="@/assets/img/unknown.png"
                    /></router-link>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- <div class="row justify-content-center">
        <div class="col col-lg-2 header-tab">HOME</div>
        <div class="col col-lg-2 header-tab">CONSULTING</div>
        <div class="col col-lg-2 header-tab">MY PAGE</div>
      </div> -->
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      keyword: "",
    };
  },
};
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

.profile {
  padding-top: 1px;
  width: 100%;
  height: 100%;
  border-radius: 70%;
  object-fit: cover;
}
</style>
