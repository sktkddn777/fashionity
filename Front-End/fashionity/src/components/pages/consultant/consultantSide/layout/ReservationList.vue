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
        <button @click="cancelReservation(reservation.id)">확인</button>
      </div>
    </div>
  </div>
</template>

<script>
import router from "@/router";

export default {
  name: "ReservationList",
  props: {
    reservations: Array,
  },
  methods: {
    cancelReservation(reservationSeq) {
      router.push({
        name: "RConsultantCheckDetail",
        params: { value: reservationSeq },
      });
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
  margin: 5px;
  display: flex;
  align-items: center;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 10px;
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
