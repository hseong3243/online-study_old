<template>
<div>
  <canvas ref="weekData" id="planet-chart" width="400" height="350"></canvas>
</div>
</template>

<script>
import Chart from 'chart.js'
import weekData from "@/linechart/week-data";
export default {
  name: "PlanetChart",
  data() {
    return {
      weekData: weekData,
    }
  },
  props: ['memberData', 'date', 'colors'],
  mounted() {
    const ctx = document.getElementById('planet-chart');
    //const ctx = this.$refs.weekData;

    // const studyRecords = this.memberData.studyRecords;

    let endDate = this.date



    const map = new Map();
    this.weekData.data.labels = [];
    for(let i=6; i>=0; i--) {
      let date = new Date(this.getDate(new Date(endDate), i));
      this.weekData.data.labels.push(this.getDateLabel(date));
      map.set(this.getDate(date, 0), 6-i);
    }

    let studies = this.memberData.studies;
    // studies = studies.filter(study => study !== '휴식')
    const records = this.memberData.records;

    this.weekData.data.datasets = [];

    let restData = [];
    restData.length = 7;
    restData.fill(0)
    let rest = false;
    for(const i in studies) {
      let data = [];
      data.length = 7;
      data.fill(0)
      let defaultData = {
        label: '',
        data: '',
        backgroundColor: '',
        borderColor: '#ffffff',
        borderWidth: 1,
      }
      if(studies[i] !== '휴식') {
        defaultData.backgroundColor = this.colors[i]
      }
      else
        defaultData.backgroundColor = '#E5E5E5';
      const studyRecord = records.filter(record => record.studyId === studies[i].studyId);
      defaultData.label = studies[i].name;
      studyRecord.forEach(record => {
        const index = map.get(record.date);
        if(record.studyStatus === 'STUDY')
          data[index] += record.endTime - record.startTime
        else if(record.studyStatus === 'REST') {
          restData[index] += record.endTime - record.startTime
          rest = true;
        }
      })
      defaultData.data = data;

      this.weekData.data.datasets.push(defaultData);
    }
    if(rest) {
      let defaultData = {
        label: '휴식',
        data: restData,
        backgroundColor: '#E5E5E5',
        borderColor: '#E5E5E5',
        borderWidth: 2,
      }
      this.weekData.data.datasets.push(defaultData);
    }

    new Chart(ctx, this.weekData);
  },
  methods: {
    bg_rgba() {
      const r = Math.floor(Math.random() * 256);
      const g = Math.floor(Math.random() * 256);
      const b = Math.floor(Math.random() * 256);
      return ["rgba(" + r + "," + g + "," + b + ",.5)", "rgba(" + r + "," + g + "," + b + ",1)"]
    },
    getDate(date, period) {
      const days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
      let year = date.getFullYear();
      let month = date.getMonth();
      let day = date.getDate();

      day = day - period;
      if(day<1) {
        month--;
        if(month<0) {
          month=11;
          year--;
        }
        day = days[month]+day;
      }

      month = month+1;
      month = month<10 ? '0'+month : month;
      day = day<10 ? '0'+day : day;

      return year + '-' + month + '-' + day;
    },
    getDateLabel(date) {
      let month = date.getMonth();
      let day = date.getDate();


      return (month+1) + '-' + day
    }
  }
}
</script>

<style scoped>

</style>