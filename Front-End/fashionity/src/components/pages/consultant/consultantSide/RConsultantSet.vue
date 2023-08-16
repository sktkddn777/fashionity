<template>
  <div class="container">
    <VCalendar
      :attributes="attributes"
      @dayclick="handleDateClick"
      style="width: 100vh"
    />

    <reservation-list-vue class="rlist" :reservations="reservationData" />
  </div>
  <div class="time-input-wrapper">
    <div>
      <label for="time-picker">시작시간:</label>
      <input
        type="time"
        id="time-picker"
        v-model="startTime"
        @input="handleStartTimeChange"
      />
    </div>
    <div>
      <label for="time-picker">종료시간:</label>
      <input
        type="time"
        id="time-picker"
        v-model="endTime"
        @input="handleEndTimeChange"
      />
    </div>
    <div>
      <v-btn @click="saveConsultantSchedule">등록하기</v-btn>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 10px;
}
</style>

<script>
import axios from "axios";
import { ref } from "vue";
import { useToast } from "vue-toastification";
import ReservationListVue from "./layout/ReservationList.vue";

const toast = useToast();

export default {
  setup() {
    const startTime = ref("");
    const endTime = ref("");

    const handleStartTimeChange = (event) => {
      const inputTime = event.target.value;
      const fixedTime = `${inputTime.split(":")[0]}:00`; // 분을 0으로 고정
      startTime.value = fixedTime;
    };

    const handleEndTimeChange = (event) => {
      const inputTime = event.target.value;
      const fixedTime = `${inputTime.split(":")[0]}:00`; // 분을 0으로 고정
      endTime.value = fixedTime;
    };

    return {
      startTime,
      endTime,
      handleStartTimeChange,
      handleEndTimeChange,
    };
  },
  name: "RConsultantSet",
  components: {
    ReservationListVue,
  },
  created() {},
  data() {
    return {
      selectedDate: null,
      reservationData: [
        {
          id: 1,
          profileImage: "profile1.jpg",
          time: "10:00 AM",
        },
        {
          id: 2,
          profileImage: "profile2.jpg",
          time: "2:00 PM",
        },
        {
          id: 1,
          profileImage: "profile1.jpg",
          time: "10:00 AM",
        },
        {
          id: 2,
          profileImage: "profile2.jpg",
          time: "2:00 PM",
        },
        {
          id: 1,
          profileImage: "profile1.jpg",
          time: "10:00 AM",
        },
        {
          id: 2,
          profileImage: "profile2.jpg",
          time: "2:00 PM",
        },
      ],
    };
  },
  methods: {
    handleDateClick({ date }) {
      // Do something with the clicked date, like displaying details or performing an action

      // 예약이 같은 날 여러개인 경우는 일단 생각 안함ㅎ
      this.selectedDate = date;
    },

    saveConsultantSchedule() {
      if (this.selectedDate === null) {
        toast.error("날짜를 선택하세용용뇽");
        return;
      }
      if (this.startTime === "") {
        toast.error("시작 시간을 선택하세용용뇽");
        return;
      }
      if (this.endTime === "") {
        toast.error("끝나는 시간을 선택하세용용뇽");
        return;
      }

      let timeArr = [];
      let startHour = this.startTime.split(":")[0];
      let endHour = this.endTime.split(":")[0];
      for (startHour; startHour <= endHour; startHour++) {
        const year = this.selectedDate.getFullYear();
        let month = parseInt(this.selectedDate.getMonth()) + 1;
        const date = this.selectedDate.getDate();

        if (month < 10) month = "0" + month;
        timeArr.push(`${year}-${month}-${date} ${startHour}:00:00`);
      }

      const data = {
        availableDateTimes: timeArr,
      };

      axios({
        url:
          process.env.VUE_APP_API_URL +
          "/api/v1/consultants/reservation/schedule",
        headers: { Authorization: `Bearer ${sessionStorage.getItem("token")}` },
        method: "POST",
        data: data,
      })
        .then((response) => {
          console.log("success: " + response);
        })
        .catch((error) => {
          console.log("fail: " + error);
        });
    },
  },
};
</script>

