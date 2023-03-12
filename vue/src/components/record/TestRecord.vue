<template>
  <div class="d-flex p-2">

  </div>
  <div class="d-flex">
    <div class="col">
      <DayChart/>
    </div>
    <div class="Chart col">
      <LineBarChart :data="getRecords"/>
    </div>
  </div>
  <div class="d-flex gap-2">
    <div class="col">
      <MyTickets/>
    </div>
    <div class="col">
      <MyRanking/>
    </div>
  </div>
</template>

<script>
import DayChart from "@/components/record/DayChart";
import MyTickets from "@/components/record/MyTickets";
import myRanking from "@/components/record/MyRanking";
import LineBarChart from "@/components/record/LineBarChart";

export default {
  name: "TestRecord",
  components: {
    DayChart: DayChart,
    MyTickets: MyTickets,
    MyRanking: myRanking,
    LineBarChart: LineBarChart
  },
  methods: {
    getRecords() {
      let records = {
        labels: ['월', '화', '수', '목', '금', '토', '일'],
        memberData: {
          label: '백엔드',
          data: [4050, 3090, 3060, 3400, 6400, 3504, 6403]

        },
        studyData: {
          label: '백엔드 전체 평균',
          data: [5400, 4500, 4535, 4664, 6453, 4536, 4364]

        }
      }
      return records;
    }
  }
}
</script>

<style scoped>

</style>