<template>
  <div class="container">
    <div class="reservation-top">
      <img src="@/assets/img/imgtemp.jpg" alt="" class="profile-img" />
      <div class="reservation-info">
        <div class="info">예약번호 : {{ reservationSeq }}</div>
        <div class="info">예약시간 : {{ reservationTime }}</div>
      </div>
      <div class="button-wrapper">
        <button class="consultant-mylist-enter" @click="startMeeting">
          입장하기
        </button>
        <button class="consultant-mylist-cancel">예약취소</button>
      </div>
    </div>

    <div style="margin-top: 10px">
      <h2>예약자 정보</h2>
    </div>
    <div class="reservation-bottom">
      <div class="info-detail" style="margin-top: 10px">
        <h3>나이</h3>
        <p>만 {{ memberAge }}세</p>
      </div>
      <div class="info-detail" style="margin-top: 10px">
        <h3>성별</h3>
        <p>{{ memberGender }}</p>
      </div>
    </div>
    <div class="reservation-bottom">
      <div class="info-detail" style="margin-top: 10px">
        <h3>신장</h3>
        <p>{{ memberHeight }}</p>
      </div>
      <div class="info-detail" style="margin-top: 10px">
        <h3>몸무게</h3>
        <p>{{ memberWeight }}</p>
      </div>
    </div>
    <div class="reservation-bottom">
      <div class="info-detail" style="margin-top: 10px">
        <h3>퍼스널컬러</h3>
        <p>{{ memberPersonalColor }}</p>
      </div>
      <div class="info-detail" style="margin-top: 10px"></div>
    </div>

    <h3 style="margin-top: 10px">예약자 등록 이미지</h3>
    <div class="reservation-bottom">
      <div class="image-list">
        <div
          class="image-item"
          v-for="(image, index) in style_images"
          :key="index"
        >
          <img
            :src="require(`@/assets/img/${image.url}`)"
            :alt="image.alt"
            class="image"
          />
        </div>
      </div>
    </div>
    <br />
    <h3 style="margin-top: 10px">내가 등록한 이미지</h3>
    <div v-if="fileList.length === 0" class="image-save">
      <multi-image-upload @updateImg="updateImg"></multi-image-upload>
      <div style="text-align: end">
        <button type="button" class="active-button" @click="submitPost">
          <span>등록</span>
        </button>
      </div>
    </div>
    <div v-else class="image-list">
      <div class="image-item" v-for="(image, index) in fileList" :key="index">
        <img
          :src="require(`@/assets/img/${image.url}`)"
          :alt="image.alt"
          class="image"
        />
      </div>
    </div>
  </div>
</template>
<style scoped>
.reservation-top {
  display: flex;
  justify-content: center;
  align-items: center;
}

.reservation-bottom {
  display: flex;
}

.profile-img {
  flex: 1;
  max-width: 20vh;
  max-height: 20vh;
  border-radius: 20px;
  margin: 10px;
}

.reservation-info {
  flex: 2;
}

.button-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.info {
  text-align: left;
  font-size: 3vh;
}

.info-detail {
  flex: 1;
}

.image-list {
  margin: auto;
  display: flex;
  max-width: 60vw;
  overflow-x: auto;
}
.image-save {
  margin: auto;
  max-width: 60vw;
}

.image-item {
  border: 1px solid #ccc;
  flex: 0 0 auto;
  margin-right: 10px;
}
.image {
  max-width: 25vh;
  max-height: 25vh;
}

.consultant-mylist-enter {
  background-color: red;
  margin: 5px;
  color: white;
  border-radius: 10px;
  padding: 10px;
  max-width: 20vh;
}
.consultant-mylist-cancel {
  background-color: gray;
  color: white;
  border-radius: 10px;
  padding: 10px;
  margin: 5px;
  max-width: 20vh;
}
.active-button {
  width: 100px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #2191ff;
  color: #ffffff;
}
</style>

<script>
import axios from "axios";
import router from "@/router";
import MultiImageUpload from "../../shared/MultiImageUpload.vue";
export default {
  name: "RConsultantCheckDetail",
  props: ["value"],
  created() {
    const reservationSeq = this.value;
    const nickname = this.$store.getters["memberStore/checkLoginUser"].nickname;
    const token = sessionStorage.getItem("token");

    axios({
      url: `${process.env.VUE_APP_API_URL}/api/v1/consultants/${nickname}/reservations/${reservationSeq}`,
      headers: { Authorization: `Bearer ${token}` },
      method: "GET",
    })
      .then(({ data }) => {
        console.log(data);
        this.reservationSeq = data.reservationSeq;
        this.consultantNickname = data.consultantNickname;
        this.memberNickname =
          data.consultantReservationDetails[0].memberNickname;
        const dateTime =
          data.consultantReservationDetails[0].reservationDateTime;
        this.reservationTime = `${dateTime[0]}년 ${dateTime[1]}월 ${dateTime[2]}일 ${dateTime[3]}시`;
        console.log(this.reservationTime);
      })
      .catch((error) => {
        console.log(error);
      });
  },
  data() {
    return {
      reservationSeq: "1",
      memberNickname: "hyeonwook",
      reservationTime: "",
      consultantNickname: "",
      memberAge: "24",
      memberHeight: "170",
      memberPersonalColor: "여름 쿨톤",
      memberGender: "남",
      memberWeight: "60",
      fileList: [],
    };
  },
  methods: {
    startMeeting() {
      router.push({ name: "Consulting-WebCam-View" });
    },
    async submitPost() {
      const imgData = {
        images: this.fileList,
        reservationSeq: this.reservationSeq,
      };
      await this.callImageSaveAPI(imgData);
      this.navigateToDetail();
    },
    async callImageSaveAPI(imgData) {
      let formData = new FormData();
      for (let i = 0; i < imgData.images.length; i++) {
        formData.append("images", imgData.images[i]);
      }
      formData.append("reservationSeq", imgData.reservationSeq);
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
        method: "PATCH",
        data: formData,
      })
        .then((data) => {
          console.log(data);
        })
        .catch(() => {
          alert("사진이 등록되지 않았습니다.");
        });
    },
    updateImg(file) {
      this.fileList = file;
    },
    navigateToDetail() {
      this.$router.push(`"/detail/${this.reservationSeq}"`);
    },
  },
  components: {
    MultiImageUpload,
  },
  // props: {},
  // data: () => ({}),
  // methods: {},
};
</script>

<style scoped></style>
