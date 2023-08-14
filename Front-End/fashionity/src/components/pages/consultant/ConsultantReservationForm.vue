<template lang="">
  <div id="reservation-form" class="container-fluid scroll">
    <div class="row justify-content-center detail-title" style="margin-top: 5px">
      상세 예약 정보 등록
    </div>
    <!-- 불러오기 버튼 -->
    <div class="row justify-content-end" style="margin-bottom: 20px">
      <div class="col"></div>
      <div class="col-1">
        <div class="detail-button">불러오기</div>
      </div>
    </div>

    <!-- 예약 정보 form  -->
    <div class="row margin">
      <form action="">
        <!-- 나이, 성별 -->
        <div class="row justify-content-center">
          <div class="col">나이</div>
          <div class="col">성별</div>
        </div>
        <div class="row justify-content-center">
          <div class="col">
            <input type="text" class="input-form" style="width: 10vw" />
          </div>
          <div class="col">
            <fieldset style="padding-top: 10px">
              <label>
                <input type="radio" name="gender" value="m" checked />
                <span>남성</span>
              </label>

              <label>
                <input type="radio" name="gender" value="f" />
                <span>여성</span>
              </label>
            </fieldset>
          </div>
        </div>

        <div class="row margin" style="height: 30px"></div>

        <!-- 신장, 몸무게 -->
        <div class="row justify-content-center">
          <div class="col">신장</div>
          <div class="col">몸무게</div>
        </div>
        <div class="row justify-content-center">
          <div class="col">
            <input type="text" class="input-form" style="width: 10vw" />
          </div>
          <div class="col">
            <input type="text" class="input-form" style="width: 10vw" />
          </div>
        </div>

        <div class="row margin" style="height: 30px"></div>

        <!-- 퍼스널 컬러 -->

        <div class="row justify-content-center">
          <div class="col">퍼스널 컬러</div>
          <div class="col"></div>
        </div>

        <div class="row justify-content-center">
          <div class="col"></div>
          <div class="col-9 justify-content-center">
            <v-select
              class="row"
              clearable
              :items="['모름', '봄 : 웜톤', '여름 : 쿨톤', '가을 : 웜톤', '겨울 : 쿨톤']"
              style="width: 15vw"
            ></v-select>
          </div>
          <div class="col"></div>
        </div>

        <!-- 평소 나의 스타일 등록 -->
        <div class="row justify-content-center">
          <div class="col-9">
            <multi-image-upload @updateImg="updateImg"></multi-image-upload>
          </div>
        </div>

        <!-- 평소 나의  등록 -->
        <!-- <div class="row">
          <multi-image-upload></multi-image-upload>
        </div> -->
      </form>
      <div class="row">
        <div class="col"></div>
        <div class="col-3">
          <router-link class="link" to="/consultant/reservation/confirm" @propChange="propChange"
            ><button>NEXT</button></router-link
          >
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import MultiImageUpload from "../shared/MultiImageUpload.vue";

export default {
  data() {
    return {
      isValid: false,
      imgList: [],
      fileList: [],
    };
  },
  components: {
    MultiImageUpload,
  },
  methods: {
    async validate() {
      const { valid } = await this.$refs.form.validate();
      if (valid) {
        this.isValid = true;
      } else {
        this.isValid = false;
      }
    },
    updateImg(file) {
      this.fileList = file;
      console.log("파일임당", file);
    },
  },
  created() {
    this.isVaild = false;
  },
  // watch: {
  //   imgList() {
  //     console.log("이미지 바뀜", this.imgList);
  //   },
  // },
};
</script>
<style scoped>
#reservation-form {
  overflow: scroll;
  height: 300px;
  height: 65vh;
  margin-left: 4vw;
  padding-right: 5vw;
}
.detail-title {
  font-size: 20px;
}
.detail-button {
  width: 70px;
  font-size: 15px;
  color: #424242;
  background-color: #bdbdbd;
}

.scroll::-webkit-scrollbar {
  display: none;
}

.scroll {
  overflow: scroll;
  -ms-overflow-style: none; /* 인터넷 익스플로러 */
  scrollbar-width: none; /* 파이어폭스 */
}

.input-form {
  border: none;
  border-bottom: 1px solid black; /* 밑줄 스타일 지정 */
  padding: 5px;
}

label {
  font-size: 18px;
  line-height: 2rem;
  padding: 0.2em 0.4em;
}

[type="radio"],
span {
  vertical-align: middle;
}

[type="radio"] {
  appearance: none;
  border: max(2px, 0.1em) solid gray;
  border-radius: 50%;
  width: 1.25em;
  height: 1.25em;
  transition: border 0.5s ease-in-out;
}

[type="radio"]:checked {
  border: 0.4em solid #424242;
}

[type="radio"]:focus-visible {
  outline-offset: max(2px, 0.1em);
  outline: max(2px, 0.1em) dotted #424242;
}

[type="radio"]:hover {
  box-shadow: 0 0 0 max(4px, 0.2em) lightgray;
  cursor: pointer;
}

[type="radio"]:hover + span {
  cursor: pointer;
}

[type="radio"]:disabled {
  background-color: lightgray;
  box-shadow: none;
  opacity: 0.7;
  cursor: not-allowed;
}

[type="radio"]:disabled + span {
  opacity: 0.7;
  cursor: not-allowed;
}

/* Global CSS */
fieldset {
  display: flex;
  justify-content: center;
  border: none;
}

*,
*::before,
*::after {
  box-sizing: border-box;
}

button {
  background: black;
  color: #fff;
  border: none;
  position: relative;
  height: 40px;
  font-size: 1.2em;
  padding: 0 2em;
  cursor: pointer;
  transition: 800ms ease all;
  outline: none;
}
button:hover {
  background: #fff;
  color: #424242;
}
button:before,
button:after {
  content: "";
  position: absolute;
  top: 0;
  right: 0;
  height: 2px;
  width: 0;
  background: #424242;
  transition: 400ms ease all;
}
button:after {
  right: inherit;
  top: inherit;
  left: 0;
  bottom: 0;
}
button:hover:before,
button:hover:after {
  width: 100%;
  transition: 800ms ease all;
}
</style>
