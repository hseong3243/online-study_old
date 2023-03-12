<template>
<div class="d-flex">
  <canvas id="day-chart" width="350" height="350"></canvas>
</div>
</template>

<script>
import Chart from 'chart.js'
import dayData from "@/linechart/day-data";

export default {
  name: "DayChart",
  data() {
    return {
      doughnutData: dayData,
    }
  },
  props: ['memberData', 'dataType', 'colors'],
  mounted() {
    const ctx = document.getElementById('day-chart');
    switch (this.dataType){
      case "WEEK": {
        this.doughnutData.options.title.text = '주간 공부 량'
        break;
      }
        case "DAY": {
          this.doughnutData.options.title.text = "일간 공부 량"
          break;
        }
        case "MONTH": {
          this.doughnutData.options.title.text = "월간 공부 량"
          break;
        }

    }


    const rest = '휴식'
    if(this.memberData.studies.length !== 0) {
      const studies = this.memberData.studies;
      const records = this.memberData.records;

      const studyNames = studies.map(study => study.name);

      this.doughnutData.data.labels = studyNames;
      this.doughnutData.data.datasets[0].data = [];
      this.doughnutData.data.datasets[0].backgroundColor = [];


      let studyTime = 0;
      let restTime = 0;
      let i =0;
      studies.forEach(study => {
        studyTime = 0;
        const studyRecords = records.filter(record => record.studyId === study.studyId);
        studyRecords.forEach(record => {
          if(record.studyStatus === 'STUDY')
            studyTime = studyTime + record.endTime - record.startTime;
          else if(record.studyStatus === 'REST')
            restTime = restTime + record.endTime - record.startTime;
        })
        this.doughnutData.data.datasets[0].data.push(studyTime);
        if(study.name !== '휴식') {
          this.doughnutData.data.datasets[0].backgroundColor.push(this.colors[i])
          i++
        }
      })
      if(restTime !== 0){
        this.doughnutData.data.labels.push(rest)
        this.doughnutData.data.datasets[0].data.push(restTime)
      }
    }else {
      this.doughnutData.data.labels = ['공부']
      this.doughnutData.data.datasets[0].data = [1]
    }

    new Chart(ctx, this.doughnutData);
  }
}
</script>

<style scoped>

</style>