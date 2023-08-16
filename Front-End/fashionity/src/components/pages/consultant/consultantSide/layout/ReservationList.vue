<template>
  <div class="reservation-list" style="border: 1px solid #ccc">
    <div
      class="reservation-item"
      v-for="reservation in reservations"
      :key="reservation.id"
    >
      <img :src="reservation.profileImage" alt="Profile" />
      <div class="reservation-info">
        <p>{{ reservation.time }}</p>
        <button @click="cancelReservation(reservation.id)">취소</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ReservationList",
  props: {
    reservations: Array,
  },
  methods: {
    cancelReservation(reservationId) {
      console.log(reservationId);
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