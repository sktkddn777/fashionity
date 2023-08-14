<template lang="">
  <div id="reservation-confirm" class="container-fluid justify-content-center">
    <div class="row" style="height: 8vh"></div>
    <div class="row justify-content-center">
      <div class="col bold">예약 날짜</div>
      <div class="col bold">7월 14일</div>
    </div>

    <div class="row justify-content-center">
      <div class="col bold">예약 시간</div>
      <div class="col bold">12시</div>
    </div>

    <div class="row justify-content-center">
      <div class="col bold">추가정보</div>
      <div class="col">힙하게 입고 싶어요</div>
    </div>
  </div>
</template>
<script>
export default {};
</script>
<style scoped>
#reservation-confirm {
  height: 300px;
  width: 40vw;
  margin-left: 7vw;
  padding-right: 7vw;
}

.bold {
  font-weight: bold;
}

.row {
  margin-bottom: 20px;
}
</style>
