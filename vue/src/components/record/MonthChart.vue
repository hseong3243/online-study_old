<template>
<div>
  <canvas id="planet-chart"></canvas>
</div>
</template>

<script>
import Chart from 'chart.js'
import month_data from "@/linechart/month_data";
export default {
  name: "PlanetChart",
  data() {
    return {
      monthData: month_data
    }
  },
  props: ['memberData', 'date', 'colors'],
  mounted() {
    const ctx = document.getElementById('planet-chart');
    const days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]

    const month = new Date(this.date).getMonth();


    const map = new Map();
    for(let i=0; i<days[month]; i++) {
      const day = (i+1)<10 ? '0'+(i+1) : (i+1);
      const findDate = new Date(this.date).getFullYear() + '-' +(month+1) + '-' + day;
      map.set(findDate, i);
    }

    const studies = this.memberData.studies;
    const records = this.memberData.records;

    let restData = [];
    restData.length = days[month];
    restData.fill(0)
    let rest = false;
    this.monthData.data.datasets = []
    for(const i in studies) {
      let data = [];
      data.length = days[month];
      data.fill(0)
      let defaultData = {
        label: '',
        data: '',
        backgroundColor: this.colors[i],
        borderColor: '#ffffff',
        borderWidth: 1,
      }
      const studyRecord = records.filter(record => record.studyId === studies[i].studyId);
      defaultData.label = studies[i].name;
      studyRecord.forEach(record => {
        if(map.get(record.date) !== undefined) {
          const index = map.get(record.date);
          if(record.studyStatus === "STUDY") {
            data[index] += record.endTime - record.startTime
          }
          else if(record.studyStatus === "REST") {
            rest = true
            restData[index] += record.endTime - record.startTime
          }
        }
      })
        defaultData.data = data.filter(() => true);
        this.monthData.data.datasets.push(defaultData);
    }
    if(rest) {
      let defaultData = {
        label: '휴식',
        data: restData,
        backgroundColor: '#E5E5E5',
        borderColor: '#E5E5E5',
        borderWidth: 2,
      }
      this.monthData.data.datasets.push(defaultData);
    }
    new Chart(ctx, this.monthData);
  },
  watch: {
    memberData() {
      const ctx = document.getElementById('planet-chart');
      const days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]

      const month = new Date(this.date).getMonth();


      const map = new Map();
      for(let i=0; i<days[month]; i++) {
        const day = (i+1)<10 ? '0'+(i+1) : (i+1);
        const findDate = new Date(this.date).getFullYear() + '-' +(month+1) + '-' + day;
        map.set(findDate, i);
      }

      const studies = this.memberData.studies;
      const records = this.memberData.records;

      let restData = [];
      restData.length = days[month];
      restData.fill(0)
      let rest = false;
      this.monthData.data.datasets = []
      for(const i in studies) {
        let data = [];
        data.length = days[month];
        data.fill(0)
        let defaultData = {
          label: '',
          data: '',
          backgroundColor: this.colors[i],
          borderColor: '#ffffff',
          borderWidth: 1,
        }
        const studyRecord = records.filter(record => record.studyId === studies[i].studyId);
        defaultData.label = studies[i].name;
        studyRecord.forEach(record => {
          if(map.get(record.date) !== undefined) {
            const index = map.get(record.date);
            if(record.studyStatus === "STUDY") {
              data[index] += record.endTime - record.startTime
            }
            else if(record.studyStatus === "REST") {
              rest = true
              restData[index] += record.endTime - record.startTime
            }
          }
        })
        defaultData.data = data.filter(() => true);
        this.monthData.data.datasets.push(defaultData);
      }
      if(rest) {
        let defaultData = {
          label: '휴식',
          data: restData,
          backgroundColor: '#E5E5E5',
          borderColor: '#E5E5E5',
          borderWidth: 2,
        }
        this.monthData.data.datasets.push(defaultData);
      }

      let labels = []
      for(let i=1; i<=days[new Date(this.date).getMonth()]; i++) {
        const str = new Date(this.date).getMonth()+1 + "/" + i
        labels.push(str)
      }
      this.monthData.data.labels = labels

      new Chart(ctx, this.monthData);
    }
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
    },
  }
}
</script>

<style scoped>

</style>