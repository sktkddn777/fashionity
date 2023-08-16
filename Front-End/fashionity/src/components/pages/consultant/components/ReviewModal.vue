<template>
  <div>
    <div class="modal" :class="{ 'is-active': showModal }">
      <div class="modal-background" @click="closeModal"></div>
      <div class="modal-content">
        <h2>리뷰 작성</h2>
        <div>
          <span v-for="star in stars" :key="star" @click="selectStar(star)">
            <i :class="['fa', starClass(star)]"></i>
          </span>
        </div>
        <div>
          <textarea
            v-model="reviewText"
            rows="4"
            placeholder="리뷰 내용을 입력하세요"
            style="border: 1px solid #ccc"
            maxlength="200"
          ></textarea>
        </div>
        <button @click="submitReview">작성 완료</button>
      </div>
      <button
        class="modal-close is-large"
        aria-label="close"
        @click="closeModal"
      ></button>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    showModal: Boolean,
  },
  data() {
    return {
      selectedStar: 0,
      reviewText: "",
      stars: [1, 2, 3, 4, 5],
    };
  },
  methods: {
    closeModal() {
      this.$emit("close");
    },
    selectStar(star) {
      this.selectedStar = star;
    },
    starClass(star) {
      return star <= this.selectedStar ? "fa-star" : "fa-star-o";
    },
    submitReview() {
      console.log("줄 점수 : " + this.selectedStar);
      console.log(this.reviewText);
      // 여기에 리뷰 작성 api 넣어줘!!!
      this.closeModal();
    },
  },
};
</script>

<style scoped>
@import url("https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css");

.modal {
  display: none;
}

.modal.is-active {
  display: flex;
}

.modal-background {
  background-color: rgba(0, 0, 0, 0.5);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.3);
  max-width: 400px;
  margin: auto;
}

.modal-close {
  position: absolute;
  top: 10px;
  right: 10px;
}
</style>
