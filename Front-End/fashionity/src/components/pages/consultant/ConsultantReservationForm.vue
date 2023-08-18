<template lang="">
  <div id="reservation-form" class="container-fluid scroll">
    <div
      class="row justify-content-center detail-title"
      style="margin-top: 1rem; margin-bottom: 3rem"
    >
      <h4><b>상세 예약 정보 등록</b></h4>
    </div>

    <!-- 예약 정보 form  -->
    <div class="row margin" align="left">
      <form action="">
        <h5><b>나이</b></h5>
        <input
          type="number"
          v-model="ageInput"
          class="input-form"
          style="width: 10vw; margin-bottom: 1.3rem"
        />
        <h5><b>성별</b></h5>
        <div class="radioElement" style="text-align: left">
          <fieldset style="padding-top: 10px">
            <label>
              <input
                type="radio"
                v-model="genderInput"
                name="gender"
                value="MALE"
                checked
              />
              <span> 남성</span>
            </label>

            <label>
              <input
                type="radio"
                v-model="genderInput"
                name="gender"
                value="FEMALE"
              />
              <span> 여성</span>
            </label>
          </fieldset>
        </div>

        <h5><b>신장</b></h5>
        <input
          type="number"
          v-model="heightInput"
          class="input-form"
          style="width: 10vw; margin-bottom: 1.3rem"
        />
        <h5><b>몸무게</b></h5>
        <input
          type="number"
          v-model="weightInput"
          class="input-form"
          style="width: 10vw; margin-bottom: 1.3rem"
        />

        <h5><b>퍼스널컬러</b></h5>
        <v-select
          clearable
          v-model="personalColorInput"
          :items="['UNKNOWN', 'SPRING', 'SUMMER', 'FALL', 'WINTER']"
          style="width: 25vw"
        ></v-select>

        <h5><b>추가 정보</b></h5>
        <textarea
          v-model="detailInput"
          name="reservationInfo"
          id="reservationInfo"
          cols="80"
          rows="3"
          style="
            background-color: #f0efef;
            border-radius: 4px;
            margin-bottom: 1.3rem;
          "
        ></textarea>

        <!-- 평소 나의 스타일 등록 -->
        <h5><b>사진 첨부</b></h5>
        <div class="row justify-content-center">
          <multi-image-upload @updateImg="updateImg"></multi-image-upload>
        </div>

        <!-- 평소 나의  등록 -->
      </form>
      <div class="row">
        <div class="col"></div>
        <div class="col-3">
          <button @click="submit" class="inactive-button">등록</button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import MultiImageUpload from "../shared/MultiImageUpload.vue";
import axios from "axios";
export default {
  data() {
    return {
      isValid: false,
      imgList: [],
      fileList: [],
      ageInput: Number,
      genderInput: "MALE",
      heightInput: Number,
      weightInput: Number,
      detailInput: "",
      personalColorInput: "UNKNOWN",
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

    async submit() {
      const reservationData = {
        images: this.fileList,
        scheduleSeq: this.$route.params.seq,
        personalColor: this.personalColorInput,
        gender: this.genderInput,
        height: this.heightInput,
        age: this.ageInput,
        detail: this.detailInput,
        consultantNickname: this.$route.params.nickname,
        weight: this.weightInput,
      };
      await this.callPostSaveAPI(reservationData);
    },
    async callPostSaveAPI(reservationData) {
      let formData = new FormData();
      formData.append("scheduleSeq", reservationData.scheduleSeq);
      formData.append("personalColor", reservationData.personalColor);
      formData.append("gender", reservationData.gender);
      formData.append("height", reservationData.height);
      formData.append("age", reservationData.age);
      formData.append("detail", reservationData.detail);
      formData.append("consultantNickname", reservationData.consultantNickname);
      formData.append("weight", reservationData.weight);

      // 이미지 업로드 처리
      for (let i = 0; i < reservationData.images.length; i++) {
        formData.append("images", reservationData.images[i]);
      }

      var token = sessionStorage.getItem("token");
      await axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/consultants/reservation`,
        headers:
          token === null
            ? null
            : {
                Authorization: `Bearer ${token}`,
                "Content-Type": "multipart/form-data",
              },
        method: "POST",
        data: formData,
      })
        .then(() => {
          this.$router.push({
            name: "consultant-myreservation",
          });
        })
        .catch((exeption) => {
          console.log("data임", exeption);
          alert("게시글이 등록되지 않았습니다.");
        });
    },
    updateImg(file) {
      this.fileList = file;
    },
  },
  created() {
    this.isVaild = false;
  },
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
  border-radius: 4px;
  background-color: #f0efef;
  /* border: none;
  border-bottom: 1px solid #cecece; 밑줄 스타일 지정 */
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

/* button {
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
} */
.active-button {
  width: 100px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #2191ff;
  color: #ffffff;
  display: flex;
  justify-content: center;
  align-items: center;
}
.inactive-button {
  width: 100px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #cecece;
  color: #ffffff;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
