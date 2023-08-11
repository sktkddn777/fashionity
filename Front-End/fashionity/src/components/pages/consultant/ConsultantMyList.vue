<template lang="">
  <div class="container-fluid">
    <!-- 이용 예정 예약 -->
    <div class="row justify-content-center block">
      <div class="col-4" style="font-weight: bold">이용 예정</div>
      <div class="col"></div>
    </div>
    <div class="row align-items-center block">
      <div class="col-4">
        <div class="row justify-content-end">
          <img class="profile" src="../../../assets/img/hyeonwook.jpg" alt="" />
        </div>
      </div>
      <div class="col">
        <div class="row" style="font-size: 15px">
          예약 일시 : 2023년 08월 17시 10시
        </div>
        <div class="row" style="font-size: 15px">담당 컨설턴트 : 김현욱</div>
      </div>
      <div class="col">
        <button class="consultant-mylist-enter">입장하기</button>
        <button class="consultant-mylist-cancel">예약취소</button>
      </div>
    </div>

    <div class="row" style="height: 8vh"></div>

    <!-- 지난 예약 -->
    <div class="row justify-content-center block">
      <div class="col-4" style="font-weight: bold">지난 예약 목록</div>
      <div class="col"></div>
    </div>
    <div class="row align-items-center block">
      <div class="col-4">
        <div class="row justify-content-end">
          <img class="profile" src="../../../assets/img/panda.png" alt="" />
        </div>
      </div>
      <div class="col">
        <div class="row" style="font-size: 15px">
          예약 일시 : 2023년 07월 17시 15시
        </div>
        <div class="row" style="font-size: 15px">담당 컨설턴트 : 푸바오</div>
      </div>
      <div class="col">
        <button class="consultant-mylist-write-review">후기 작성</button>
      </div>
    </div>
    <div class="row align-items-center block">
      <div class="col-4">
        <div class="row justify-content-end">
          <img class="profile" src="../../../assets/img/panda.png" alt="" />
        </div>
      </div>
      <div class="col">
        <div class="row" style="font-size: 15px">
          예약 일시 : 2023년 07월 17시 15시
        </div>
        <div class="row" style="font-size: 15px">담당 컨설턴트 : 아이바오</div>
      </div>
      <div class="col">
        <button class="consultant-mylist-modify-review">후기 수정</button>
      </div>
    </div>
  </div>
</template>
<script scoped>
export default {};
</script>
<style scoped>
.block {
  margin-bottom: 15px;
}
.consultant-mylist-enter {
  background-color: red;
  color: white;
  border-radius: 10px;
  padding: 10px;
}
.consultant-mylist-cancel {
  background-color: gray;
  color: white;
  border-radius: 10px;
  padding: 10px;
  margin-left: 10px;
}

.consultant-mylist-write-review {
  background-color: blue;
  color: white;
  border-radius: 10px;
  padding: 10px;
  margin-left: 10px;
}

.consultant-mylist-modify-review {
  background-color: gray;
  color: white;
  border-radius: 10px;
  padding: 10px;
  margin-left: 10px;
}
.profile {
  width: 90px;
  border-radius: 70%;
  object-fit: contain;
  margin-right: 40px;
}
</style>
