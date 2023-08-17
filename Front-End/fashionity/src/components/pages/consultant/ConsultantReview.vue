<template lang="">
  <div id="consultant-reservation">
    <!-- 별점, 더보기 -->
    <div class="row">
      <div class="col" style="text-align: left">
        <div class="row">
          <div class="col-1">
            <font-awesome-icon :icon="['fas', 'star']" style="color: #424242" />
          </div>
          <div class="col-1">
            <span>{{ this.review.reviewGrade }}/5.0</span>
          </div>
          <div class="col"></div>
        </div>
      </div>
      <div class="col" style="text-align: right">
        <font-awesome-icon
          :icon="['fas', 'ellipsis-vertical']"
          style="color: #000000"
        />
      </div>
    </div>
    <div class="row" style="height: 1vh"></div>

    <!-- 리뷰 내용 -->
    <div class="row">
      이것은 리뷰인가 테스트인가. 지금까지 이런 리뷰는 없었다. 수원 왕갈비 리뷰
    </div>

    <div class="row" style="height: 1vh"></div>

    <!-- 작성자 -->
    <div class="row">
      <div class="col" style="text-align: left">
        <span>작성자 : {{ this.review.memberNickName }}</span>
      </div>

      <div
        v-if="this.review.updateaAt"
        class="col"
        style="text-align: right; color: #bdbdbd"
      >
        <span>{{ this.review.updateaAt }}</span>
      </div>

      <div v-else class="col" style="text-align: right; color: #bdbdbd">
        <span>{{ this.review.createAt }}</span>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {};
  },
  props: {
    review: [],
  },
};
</script>
<style>
#consultant-reservation {
  margin: 20px 15px 0px 15px;
  padding-bottom: 10px;
  border-style: solid;
  border-width: 1px;
  border-color: white white #bdbdbd white;
}
</style>
