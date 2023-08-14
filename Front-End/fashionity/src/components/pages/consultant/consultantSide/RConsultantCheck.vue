<template>
  <VCalendar
    :attributes="attributes"
    @dayclick="handleDateClick"
    v-model="selectedDate"
    style="width: 1000px"
  />
</template>

<script>
import router from "@/router";
import axios from "axios";

export default {
  name: "RConsultantCheck",
  created() {
    const nickname = this.$store.getters["memberStore/checkLoginUser"].nickname;
    let token = sessionStorage.getItem("token");

    axios({
      url: `${process.env.VUE_APP_API_URL}/api/v1/consultants/${nickname}/reservations`,
      headers: { Authorization: `Bearer ${token}` },
      method: "GET",
    })
      .then(({ data }) => {
        let reservationInfo = {
          color: "blue",
          dates: "",
          isComplete: false,
        };
        console.log("data :" + data.consultantReservationSummaries);
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
          reservationInfo.dates = time[3] + ":" + time[4];

          attribute.popover.label =
            data.consultantReservationSummaries[i].memberNickname +
            " 일시 " +
            reservationInfo.dates;
          attribute.customData = reservationInfo;
          this.attributes.push(attribute);
        }
      })
      .catch((response) => {
        console.log(response);
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
      // Do something with the clicked date, like displaying details or performing an action

      // 예약이 같은 날 여러개인 경우는 일단 생각 안함ㅎ
      const reservationSeq = data.attributes[0].reservationSeq;
      router.push({
        name: "RConsultantCheckDetail",
        params: { value: reservationSeq },
      });
    },
  },
};
</script>
<style scoped></style>
