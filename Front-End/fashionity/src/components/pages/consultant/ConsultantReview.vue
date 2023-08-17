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
        <font-awesome-icon :icon="['fas', 'ellipsis-vertical']" style="color: #000000" />
      </div>
    </div>
    <div class="row" style="height: 1vh"></div>

    <!-- 리뷰 내용 -->
    <div class="row">
      {{ this.review.reviewContent }}
    </div>

    <div class="row" style="height: 1vh"></div>

    <!-- 작성자 -->
    <div class="row">
      <div class="col" style="text-align: left">
        <span>익명</span>
      </div>

      <div v-if="this.review.updatedAt" class="col" style="text-align: right; color: #bdbdbd">
        <span>{{ this.review.updateAt }}</span>
      </div>

      <div v-else class="col" style="text-align: right; color: #bdbdbd">
        <span>{{ this.review.createdAt }}</span>
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
