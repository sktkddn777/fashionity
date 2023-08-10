<template>
  <div id="comment">
    <!--댓글 프로필 이미지-->
    <div class="post-detail-header">
      <div class="post-detail-header-img">
        <img
          src="../../../assets/img/hyeonwook.jpg"
          alt=""
          class="profile-comment"
        />
      </div>
      <!--댓글 내용-->
      <div>
        <div class="fw-bold" style="text-align: left">hyeonwook_12</div>
        <div style="text-align: left; font-size: 15px">
          안녕하세요ㅎ 선팔하고 갑니다ㅎ
        </div>
      </div>
      <!--댓글 정보-->
      <div class="post-detail-comment-info">
        <div style="color: grey; font-size: 13px">1시간 전</div>
        <div>
          <v-menu>
            <template v-slot:activator="{ props }">
              <font-awesome-icon
                v-bind="props"
                :icon="['fas', 'ellipsis']"
                style="color: #999999"
              />
            </template>

            <v-list>
              <v-list-item>
                <router-link to="/post/modify" class="link">
                  <v-list-item-title type="button">수정</v-list-item-title>
                </router-link>
                <v-list-item-title
                  type="button"
                  data-bs-toggle="modal"
                  data-bs-target="#deleteModal"
                  >삭제</v-list-item-title
                >
                <v-list-item-title
                  type="button"
                  data-bs-toggle="modal"
                  data-bs-target="#reportModal"
                  >신고</v-list-item-title
                >

                <!-- delete Modal -->
                <div
                  class="modal fade"
                  id="deleteModal"
                  tabindex="-1"
                  aria-labelledby="deleteModalLabel"
                  aria-hidden="true"
                >
                  <div class="modal-dialog">
                    <div class="modal-content">
                      <div class="modal-header">
                        <button
                          type="button"
                          class="btn-close"
                          data-bs-dismiss="modal"
                          aria-label="Close"
                        ></button>
                      </div>
                      <div class="modal-body" style="text-align: center">
                        정말 삭제하시겠습니까?
                      </div>
                      <div class="modal-footer">
                        <button
                          type="button"
                          class="btn btn-outline-secondary"
                          data-bs-dismiss="modal"
                        >
                          아니오
                        </button>
                        <button type="button" class="btn btn-primary">
                          &nbsp;&nbsp;네&nbsp;&nbsp;
                        </button>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- report Modal -->
                <div
                  class="modal fade"
                  id="reportModal"
                  tabindex="-1"
                  aria-labelledby="reportModalLabel"
                  aria-hidden="true"
                >
                  <div class="modal-dialog">
                    <div class="modal-content">
                      <div class="modal-header">
                        <h1 class="modal-title fs-5" id="reportModalLabel">
                          신고
                        </h1>
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
                            name="exampleRadios"
                            id="exampleRadios1"
                            value="option1"
                            checked
                          />
                          <label class="form-check-label" for="exampleRadios1">
                            욕설 / 비방
                          </label>
                        </div>
                        <div class="form-check">
                          <input
                            class="form-check-input"
                            type="radio"
                            name="exampleRadios"
                            id="exampleRadios2"
                            value="option2"
                          />
                          <label class="form-check-label" for="exampleRadios2">
                            성희롱 / 음란물
                          </label>
                        </div>
                        <div class="form-check">
                          <input
                            class="form-check-input"
                            type="radio"
                            name="exampleRadios"
                            id="exampleRadios3"
                            value="option3"
                          />
                          <label class="form-check-label" for="exampleRadios3">
                            상업 목적 광고
                          </label>
                        </div>
                        <div class="form-check">
                          <input
                            class="form-check-input"
                            type="radio"
                            name="exampleRadios"
                            id="exampleRadios4"
                            value="option4"
                          />
                          <label class="form-check-label" for="exampleRadios4">
                            사진 도용
                          </label>
                        </div>
                        <div class="form-check">
                          <input
                            class="form-check-input"
                            type="radio"
                            name="exampleRadios"
                            id="exampleRadios5"
                            value="option5"
                          />
                          <label class="form-check-label" for="exampleRadios5">
                            기타
                          </label>
                        </div>
                        <div>
                          <div class="post-detail-comment-submit">
                            <input
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
                        <button type="button" class="btn btn-primary">
                          신고
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </v-list-item>
            </v-list>
          </v-menu>
          <div class="post-detail-like-icon">
            <font-awesome-icon
              :icon="['far', 'heart']"
              style="color: #999999"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {};
</script>
<style scoped>
.post-detail-profile {
  height: 7vh;
  border-radius: 100%;
  object-fit: contain;
}
.profile-comment {
  height: 5vh;
  border-radius: 100%;
  object-fit: contain;
}
.post-detail-header {
  margin: auto;
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}
.post-detail-like {
  display: flex;
  gap: 5px;
  margin-top: 10px;
  margin-bottom: 10px;
}
.post-detail-content {
  margin-bottom: 10px;
}
.post-detail-content-hashtag {
  display: flex;
}
.post-detail-comment-cnt {
  color: grey;
  text-align: left;
  margin-bottom: 10px;
}
.post-detail-comment-content {
  display: flex;
}
.post-detail-comment-info {
  margin-left: auto;
  display: flex;
  gap: 10px;
}
.post-detail-comment-submit {
  display: flex;
}
</style>
