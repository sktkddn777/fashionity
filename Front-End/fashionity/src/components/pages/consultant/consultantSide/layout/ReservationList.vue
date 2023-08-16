<template>
  <div class="reservation-list" style="border: 1px solid #ccc">
    <div
      class="reservation-item"
      v-for="reservation in reservations"
      :key="reservation.id"
    >
      <img src="@/assets/img/hyeonwook.jpg" alt="Profile" />
      <div class="reservation-info">
        <p>{{ reservation.time }}</p>
        <button @click="cancelReservation(reservation.id)">취소</button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ReservationList",
  props: {
    reservations: Array,
  },
  methods: {
    cancelReservation(scheduleSeq) {
      let token = sessionStorage.getItem("token");

      axios({
        url: `${process.env.VUE_APP_API_URL}/api/v1/consultants/reservation/myschedule`,
        headers: { Authorization: `Bearer ${token}` },
        method: "DELETE",
        params: {
          scheduleSeq: scheduleSeq,
        },
      })
        .then((data) => {
          console.log(data);
        })
        .catch(({ response }) => {
          if (response.data.code === "SCH001")
            alert("존재하지 않는 스케줄입니다.");
          if (response.data.code === "C007")
            alert("이미 예약이 걸려있는 스케줄입니다. 예약부터 취소하세요");
        });
      // 예약 취소 로직
    },
  },
};
</script>

<style scoped>
.reservation-list {
  height: 50vh;
  overflow-y: auto;
}

.reservation-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border: 1px solid #ccc;
}

img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 10px;
}

.reservation-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}
</style>
