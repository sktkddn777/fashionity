<template lang="">
  <div id="footer">
    <div class="container">
      <div class="row">
        <div class="col-3 justify-content-center">
          <div class="row" style="text-align: left">
            <div class="footer-bold">이용 안내</div>
            <div class="footer-normal">이용 정책</div>
            <div class="footer-normal">패널티 정책</div>
            <div class="footer-normal">커뮤니티 가이드라인</div>
          </div>
        </div>
        <div class="col-3 justify-content-center">
          <div class="row" style="text-align: left">
            <div class="footer-bold">고객 지원</div>
            <div class="footer-normal">공지사항</div>
            <div class="footer-normal">서비스 소개</div>
            <div class="footer-normal">고객 센터</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "TheFooter",
  components: {},
};
</script>

<style>
.footer {
  margin-top: 20px;
  height: 200px;
  position: relative;
  transform: translateY(-100%);
  position: relative;
}

.footer-bold {
  margin-bottom: 20px;
  padding-top: 20px;
  font-style: normal;
  font-weight: 600;
  font-size: 20px;
}

.footer-normal {
  font-style: normal;
  font-weight: 400;
  font-size: 18px;
  line-height: 27px;
  color: #bdbdbd;
}
</style>
