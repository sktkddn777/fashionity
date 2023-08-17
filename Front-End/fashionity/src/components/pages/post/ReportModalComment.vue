<template lang="">
  <div>
    <v-list-item-title
      type="button"
      data-bs-toggle="modal"
      data-bs-target="#reportModal"
      >신고</v-list-item-title
    >
    <!-- report Modal -->
    <div
      v-if="showModal"
      class="modal fade"
      id="reportModal"
      tabindex="-1"
      aria-labelledby="reportModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h1 class="modal-title fs-5" id="reportModalLabel">신고</h1>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>
          <div class="modal-body">
            <div class="form-check">
              <input
                class="form-check-input"
                type="radio"
                name="category"
                id="slander"
                value="slander"
                v-model="reportCategory"
                checked
              />
              <label class="form-check-label" for="slander">
                욕설 / 비방
              </label>
            </div>
            <div class="form-check">
              <input
                class="form-check-input"
                type="radio"
                name="category"
                id="spam"
                value="spam"
                v-model="reportCategory"
              />
              <label class="form-check-label" for="spam">
                성희롱 / 음란물
              </label>
            </div>
            <div class="form-check">
              <input
                class="form-check-input"
                type="radio"
                name="category"
                id="ad"
                value="ad"
                v-model="reportCategory"
              />
              <label class="form-check-label" for="ad"> 상업 목적 광고 </label>
            </div>
            <div class="form-check">
              <input
                class="form-check-input"
                type="radio"
                name="category"
                id="pirate"
                value="pirate"
                v-model="reportCategory"
              />
              <label class="form-check-label" for="pirate"> 사진 도용 </label>
            </div>
            <div class="form-check">
              <input
                class="form-check-input"
                type="radio"
                name="category"
                id="etc"
                value="etc"
                v-model="reportCategory"
              />
              <label class="form-check-label" for="etc"> 기타 </label>
            </div>
            <div>
              <div class="post-detail-comment-submit">
                <input
                  v-model="reportContent"
                  class="form-control"
                  type="text"
                  placeholder="신고 사유를 입력해주세요."
                />
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button
              type="button"
              class="btn btn-outline-secondary"
              data-bs-dismiss="modal"
            >
              취소
            </button>
            <button
              type="button"
              class="btn btn-primary"
              @click="submitReport"
              data-bs-dismiss="modal"
            >
              신고
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import axios from "axios";
export default {
  props: ["commentSeq", "seq"],
  data() {
    return {
      reportCategory: "slander",
      reportContent: "",
      showModal: true,
    };
  },
  methods: {
    async submitReport() {
      await this.callCommentReportAPI();
      alert("신고가 접수되었습니다.");
    },
    async callCommentReportAPI() {
      let token = sessionStorage.getItem("token");
      let seq = this.$route.params.seq;
      let commentSeq = this.commentSeq;
      let body = {
        reportCategory: this.reportCategory,
        reportContent: this.reportContent,
      };
      await axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/posts/${seq}/comments/${commentSeq}/report`,
        headers:
          token === null
            ? null
            : {
                Authorization: `Bearer ${token}`,
              },
        method: "POST",
        data: body,
      })
        .then((data) => {
          console.log(data.data.success);
        })
        .catch((error) => console.log(error));
    },
  },
};
</script>
<style lang=""></style>
