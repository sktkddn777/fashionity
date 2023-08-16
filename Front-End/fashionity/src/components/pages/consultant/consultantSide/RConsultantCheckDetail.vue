<template>
  <div>유저 예약 정보랑 똑같이 가쟈!</div>

  <div class="container-fliud" style="height: 85vh">
    <div class="row">
      <div class="col-3" style="height: 85vh; margin-left: 5vw">
        <div class="row d-flex justify-content-center">
          <div class="col" style="margin-top: 10%; height: 50%">
            <img
              src="@/assets/img/imgtemp.jpg"
              alt=""
              class="profile"
              style="width: 50%"
            />
          </div>
        </div>

        <div class="row" style="height: 30px"></div>
        <div class="col" style="margin-top: 10%">
          <h2>예약자 정보</h2>
        </div>
        <div class="col" style="margin-top: 10%">
          <h3>나이</h3>
          <p>만 {{ memberAge }}</p>
        </div>
        <div class="col" style="margin-top: 10%">
          <h3>신장</h3>
          <p>{{ memberHeight }}</p>
        </div>
        <div class="col" style="margin-top: 10%">
          <h3>퍼스널컬러</h3>
          <p>{{ memberPersonalColor }} reservationDeatil</p>
        </div>
      </div>
      <div class="col-5" style="height: 75vh">
        <div
          class="col-reservation"
          style="display: flex; justify-content: left"
        >
          <div>예약번호 : {{ reservationSeq }}</div>
          <div>예약시간 : {{ reservationTime }}</div>
          <div>담당컨설턴트 : {{ consultantNickname }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
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
        this.reservationTime =
          data.consultantReservationDetails[0].reservationDateTime;
      })
      .catch((error) => {
        console.log(error);
      });
  },
  data() {
    return {
      reservationSeq: "",
      memberNickname: "",
      reservationTime: "",
      consultantNickname: "",
      memberAge: "",
      memberHeight: "",
      memberPersonalColor: "",
    };
  },
  // components: {},
  // props: {},
  // data: () => ({}),
  // methods: {},
};
</script>

<style scoped></style>
