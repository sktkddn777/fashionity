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
                name="type"
                id="slander"
                value="slander"
                v-model="type"
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
                name="type"
                id="spam"
                value="spam"
                v-model="type"
              />
              <label class="form-check-label" for="spam">
                성희롱 / 음란물
              </label>
            </div>
            <div class="form-check">
              <input
                class="form-check-input"
                type="radio"
                name="type"
                id="ad"
                value="ad"
                v-model="type"
              />
              <label class="form-check-label" for="ad"> 상업 목적 광고 </label>
            </div>
            <div class="form-check">
              <input
                class="form-check-input"
                type="radio"
                name="type"
                id="pirate"
                value="pirate"
                v-model="type"
              />
              <label class="form-check-label" for="pirate"> 사진 도용 </label>
            </div>
            <div class="form-check">
              <input
                class="form-check-input"
                type="radio"
                name="type"
                id="etc"
                value="etc"
                v-model="type"
              />
              <label class="form-check-label" for="etc"> 기타 </label>
            </div>
            <div>
              <div class="post-detail-comment-submit">
                <input
                  v-model="content"
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
            <button type="button" class="btn btn-primary" @click="submitReport">
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
  data() {
    return {
      seq: "",
      type: "",
      content: "",
      showModal: "true",
    };
  },
  methods: {
    async submitReport() {
      const reportData = {
        type: this.type,
        content: this.content,
      };
      await this.callPostReportAPI(reportData);
      this.showModal = false;
    },
    async callPostReportAPI(reportData) {
      let token = sessionStorage.getItem("token");
      let seq = this.$route.params.seq;
      axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/posts/${seq}/report`,
        headers:
          token === null
            ? null
            : {
                Authorization: `Bearer ${token}`,
              },
        method: "POST",
        data: reportData,
      }).then((data) => {
        console.log(data.data.success);
      });
    },
  },
};
</script>
<style lang=""></style>
