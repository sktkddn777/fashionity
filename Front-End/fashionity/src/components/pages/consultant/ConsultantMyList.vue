<template lang="">
  <div class="container-fluid">
    <!-- 이용 예정 예약 -->
    
    <div class="row justify-content-center block">
      <div class="col-4" style="font-weight: bold">이용 예정</div>
      <div class="col"></div>
    </div>
    <div class="before-list" v-for="(reservation, index) in reservationList_before" :key="index">
      <div class="row align-items-center block">
        <div class="col-4">
          <div class="row justify-content-end">
            <img class="profile" :src="reservation.consultantProfileUrl" alt="" />
          </div>
        </div>
        <div class="col">
          <div class="row" style="font-size: 15px">
            예약 일시 : {{reservation.reservationDateTime[0]}}년
                       {{reservation.reservationDateTime[1]}}월
                      {{reservation.reservationDateTime[2]}}일
                        {{reservation.reservationDateTime[3]}}시
          </div>
          <div class="row" style="font-size: 15px">담당 컨설턴트 : {{reservation.consultantNickname}}</div>
        </div>
        <div class="col">
          <button class="consultant-mylist-enter" @click="startMeeting(index)">입장하기</button>
          <button class="consultant-mylist-cancel">예약취소</button>
        </div>
      </div>
    </div>

<div class="row" style="height: 8vh"></div>

<!-- 지난 예약 -->
<div class="row justify-content-center block">
  <div class="col-4" style="font-weight: bold">지난 예약 목록</div>
  <div class="col"></div>
</div>
<div class="after-list" v-for="(reservation, index) in reservationList_after" :key="index">
  <div class="row align-items-center block">
    <div class="col-4">
      <div class="row justify-content-end">
        <img class="profile" :src="reservation.consultantProfileUrl" alt="" />
      </div>
    </div>
    <div class="col">
      <div class="row" style="font-size: 15px">
            예약 일시 : {{reservation.reservationDateTime[0]}}년
                        {{reservation.reservationDateTime[1]}}월
                        {{reservation.reservationDateTime[2]}}일
                        {{reservation.reservationDateTime[3]}}시
          </div>
      <div class="row" style="font-size: 15px">담당 컨설턴트 : {{reservation.consultantNickname}}</div>
    </div>
    <div class="col">
      <button class="consultant-mylist-write-review" @click="openModal">후기 작성</button>
    </div>
  </div>
  <review-modal-vue :show-modal="showModal" @close="closeModal" :reservation-seq="reservation.reservationSeq"></review-modal-vue>
</div>
  </div>
</template>

<script>
import ReviewModalVue from "./components/ReviewModal.vue";
import axios from "axios";

export default {
  components: {
    ReviewModalVue,
  },
  data() {
    return {
      showModal: false,
      reservationList_before: [],
      reservationList_after: [],
    };
  },
  created() {
    this.getReservationList();
  },
  methods: {
    startMeeting(index) {
      const sessionId =
        this.reservationList_before[index].reservationSeq + 73576;
      console.log("비밀 번호 : " + sessionId);
      this.$router.push({
        name: "Consulting-WebCam-View",
        query: { sessionId },
      });
    },
    openModal() {
      this.showModal = true;
    },
    closeModal() {
      this.showModal = false;
    },
    getReservationList() {
      const currentDate = new Date();
      let token = sessionStorage.getItem("token");
      axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/consultants/reservations`,
        headers:
          token === null
            ? null
            : {
                Authorization: `Bearer ${token}`,
              },
        method: "GET",
      })
        .then((response) => {
          console.log("여기욤");
          console.log(response);
          const reservations = response.data.userReservationSummaries;
          if (reservations[0].reservationSeq != null) {
            for (let i = 0; i < reservations.length; i++) {
              const givenDate = reservations[i].reservationDateTime;
              const reservattionDate = new Date(
                givenDate[0],
                givenDate[1] - 1,
                givenDate[2],
                givenDate[3],
                givenDate[4]
              );
              if (reservattionDate >= currentDate) {
                this.reservationList_before.push(reservations[i]);
              } else {
                this.reservationList_after.push(reservations[i]);
              }
            }
          }
        })
        .catch((exception) => {
          let data = exception.response;
          console.log("이거야!!!");
          console.log(exception);
          console.log(exception.response);
          if (data.status === 401) {
            //유효기간이 다 된 토큰이면 일단 보여주셈
            axios({
              url: `${process.env.VUE_APP_API_URL}/api/v1/consultants/reservations`,
              method: "GET",
            }).then((response) => {
              // const newPosts = response.data.posts;
              // this.posts = [...this.posts, ...newPosts];
              // this.loadingNextPage = false;
              // this.page++;
              console.log("실패");
              console.log(response);
            });
          }
        });
    },
  },
};
</script>

<style scoped>
.block {
  margin-bottom: 15px;
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

.consultant-mylist-write-review {
  background-color: blue;
  color: white;
  border-radius: 10px;
  padding: 10px;
  margin-left: 10px;
}

.consultant-mylist-modify-review {
  background-color: gray;
  color: white;
  border-radius: 10px;
  padding: 10px;
  margin-left: 10px;
}
.profile {
  width: 90px;
  border-radius: 70%;
  object-fit: contain;
  margin-right: 40px;
}
</style>
