<template>
  <div class="container">
    <div class="reservation-top">
      <img src="@/assets/img/imgtemp.jpg" alt="" class="profile" />
      <div class="reservation-info">
        <div class="info">예약번호 : {{ reservationSeq }}</div>
        <div class="info">예약시간 : {{ reservationTime }}</div>
        <div class="col">
          <button class="consultant-mylist-enter" @click="startMeeting">
            입장하기
          </button>
          <button class="consultant-mylist-cancel">예약취소</button>
        </div>
      </div>
    </div>

    <div align="left" class="reservation-bottom">
      <div style="margin-top: 10px">
        <h2>예약자 정보</h2>
      </div>
      <div style="margin-top: 10px">
        <h3>나이</h3>
        <p>만 {{ memberAge }}세</p>
      </div>
      <div style="margin-top: 10px">
        <h3>성별</h3>
        <p>{{ memberGender }}</p>
      </div>
      <div style="margin-top: 10px">
        <h3>신장</h3>
        <p>{{ memberHeight }}</p>
      </div>
      <div style="margin-top: 10px">
        <h3>퍼스널컬러</h3>
        <p>{{ memberPersonalColor }}</p>
      </div>

      <h3 style="margin-top: 10px">예약자 등록 이미지</h3>
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

      <h3 style="margin-top: 10px">내가 등록한 이미지</h3>
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
  </div>
</template>
<style scoped>
.container {
  display: flex;
  justify-content: flex-start;
}
.reservation-top {
  display: flex;
}

.reservation-bottom {
  align-items: flex-start;
}

.profile {
  flex: 1;
  width: 30vh;
  height: 30vh;
  border-radius: 20px;
  margin: 10px;
}

.reservation-info {
  flex: 3;
}

.info {
  text-align: left;
  font-size: 3vh;
}

.image-list {
  display: flex;
  max-width: 60vw;
  overflow-x: auto;
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
</style>

<script>
import axios from "axios";
import router from "@/router";
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
      style_images: [
        { url: "hyeonwook.jpg", alt: "현욱1" },
        { url: "hyeonwook2.jpg", alt: "현욱2" },
        { url: "hyeonwook3.jpg", alt: "현욱3" },
        { url: "postImg.jpg", alt: "지원" },
        { url: "hyeonwook.jpg", alt: "현욱1" },
        { url: "hyeonwook2.jpg", alt: "현욱2" },
        { url: "hyeonwook3.jpg", alt: "현욱3" },
        { url: "postImg.jpg", alt: "지원" },
      ],
    };
  },
  methods: {
    startMeeting() {
      router.push({ name: "Consulting-WebCam-View" });
    },
  },
  // components: {},
  // props: {},
  // data: () => ({}),
  // methods: {},
};
</script>

<style scoped></style>
