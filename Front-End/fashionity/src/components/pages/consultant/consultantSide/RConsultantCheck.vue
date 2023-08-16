<template>
  <div class="container">
    <VCalendar
      :attributes="attributes"
      @dayclick="handleDateClick"
      v-model="selectedDate"
      style="width: 100vh"
      class="calendar"
    />
    <reservation-list-vue class="rlist" :reservations="reservationData" />
  </div>
</template>

<script>
import router from "@/router";
import axios from "axios";
import ReservationListVue from "./layout/ReservationList.vue";

export default {
  name: "RConsultantCheck",
  components: {
    ReservationListVue,
  },
  created() {
    const nickname = this.$store.getters["memberStore/checkLoginUser"].nickname;
    let token = sessionStorage.getItem("token");

    axios({
      url: `${process.env.VUE_APP_API_URL}/api/v1/consultants/${nickname}/reservations`,
      headers: { Authorization: `Bearer ${token}` },
      method: "GET",
    })
      .then(({ data }) => {
        console.log(data);
        let reservationInfo = {
          color: "blue",
          dates: "",
          isComplete: false,
          memberNickname: "",
        };
        for (let i = 0; i < data.consultantReservationSummaries.length; i++) {
          let attribute = {
            reservationSeq: "",
            // Attribute type definitions
            content: "blue", // Boolean, String, Object
            dot: true, // Boolean, String, Object

            customData: null,
            // We also need some dates to know where to display the attribute
            // We use a single date here, but it could also be an array of dates,
            //  a date range or a complex date pattern.
            dates: null,
            // Think of `order` like `z-index`
            order: 0,
            popover: {
              label: "",
            },
          };
          let time = data.consultantReservationSummaries[i].reservationDateTime;
          attribute.dates = new Date(time[0], time[1] - 1, time[2]);
          attribute.reservationSeq =
            data.consultantReservationSummaries[i].reservationSeq;
          reservationInfo.dates = `${time[1]}-${time[2]} ${time[3]}:00:00`;
          reservationInfo.memberNickname =
            data.consultantReservationSummaries[i].memberNickname;

          attribute.popover.label =
            data.consultantReservationSummaries[i].memberNickname +
            " 일시 " +
            reservationInfo.dates;
          attribute.customData = reservationInfo;
          this.attributes.push(attribute);
        }
      })
      .catch(({ response }) => {
        if (response.data.code === "C003") {
          alert(response.data.message);
          router.push({ name: "main" });
        }
        console.log("[error] = {}", response);
      });
  },
  data() {
    return {
      reservationSeq: null,
      selectedDate: null,
      attributes: [],
    };
  },
  methods: {
    handleDateClick(data) {
      console.log(data);
      this.reservationData = [];
      this.selectedDate = data.date;

      for (let i = 0; i < data.attributes.length; i++) {
        this.reservationData.push({
          id: data.attributes[i].reservationSeq,
          time: data.attributes[i].customData.dates,
          profileImage: "@/assets/img/hyeonwook.jpg",
        });
      }
    },
  },
};
</script>
<style scoped>
.container {
  display: flex;
}

.calendar {
  flex: 2;
}

.rlist {
  flex: 1;
}
</style>
