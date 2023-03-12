<template>
<div>
  <canvas id="study-chart" width="400" height="350"/>
</div>
</template>

<script>
import Chart from 'chart.js'
import studyTimeData from "@/linechart/studyTimeData";
export default {
  name: "PlanetChart",
  data() {
    return {
      studyTimeData: studyTimeData
    }
  },
  props: ['memberData', 'date', 'dataType', 'colors', 'categoryData'],
  mounted() {
    const ctx = document.getElementById('study-chart');
    const days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]

    let endDate = this.date

    const map = new Map();
    this.studyTimeData.data.labels = [];
    if(this.dataType === 'WEEK') {
      for (let i = 6; i >= 0; i--) {
        let date = new Date(this.getDate(new Date(endDate), i));
        this.studyTimeData.data.labels.push(this.getDateLabel(date));
        map.set(this.getDate(date, 0), 6 - i);
      }
    }
    else {
      this.studyTimeData.data.labels = this.getLabels(this.date)
      const month = new Date(this.date).getMonth();
      for(let i=0; i<days[month]; i++) {
        const day = (i+1)<10 ? '0'+(i+1) : (i+1);
        const findDate = new Date(this.date).getFullYear() + '-' +(month+1) + '-' + day;
        map.set(findDate, i);
      }
    }

    let defaultMemberData = {
      label: '공부 시간',
      data: '',
      backgroundColor: this.colors[1],
      borderColor: '#ffffff',
      borderWidth: 2,
    }


    const records = this.memberData.records;

    let data = []
    data.length = 7;
    let time = [new Date().getTime(), 0];
    let dateTime = time.concat();
    let index = 0;
    records.forEach(record => {
      if (index !== map.get(record.date))
        dateTime = time.concat()
      index = map.get(record.date);
      dateTime[0] = Math.min(record.startTime, dateTime[0]);
      dateTime[1] = Math.max(record.endTime, dateTime[1]);

      data[index] = dateTime;
    })
    console.log(data)
    defaultMemberData.data = data;

    // console.log(defualtData)
    this.studyTimeData.data.datasets = [defaultMemberData]

    new Chart(ctx, this.studyTimeData);
  },
  watch: {
    memberData() {
      const ctx = document.getElementById('study-chart');
      const days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]

      let endDate = this.date

      const map = new Map();
      this.studyTimeData.data.labels = [];
      if(this.dataType === 'WEEK') {
        for (let i = 6; i >= 0; i--) {
          let date = new Date(this.getDate(new Date(endDate), i));
          this.studyTimeData.data.labels.push(this.getDateLabel(date));
          map.set(this.getDate(date, 0), 6 - i);
        }
      }
      else {
        this.studyTimeData.data.labels = this.getLabels(this.date)
        const month = new Date(this.date).getMonth();
        for(let i=0; i<days[month]; i++) {
          const day = (i+1)<10 ? '0'+(i+1) : (i+1);
          const findDate = new Date(this.date).getFullYear() + '-' +(month+1) + '-' + day;
          map.set(findDate, i);
        }
      }

      let defaultMemberData = {
        label: '공부 시간',
        data: '',
        backgroundColor: this.colors[1],
        borderColor: '#ffffff',
        borderWidth: 2,
      }


      const records = this.memberData.records;

      let data = []
      data.length = 7;
      let time = [new Date().getTime(), 0];
      let dateTime = time.concat();
      let index = 0;
      records.forEach(record => {
        if (index !== map.get(record.date))
          dateTime = time.concat()
        index = map.get(record.date);
        dateTime[0] = Math.min(record.startTime, dateTime[0]);
        dateTime[1] = Math.max(record.endTime, dateTime[1]);

        data[index] = dateTime;
      })
      console.log(data)
      defaultMemberData.data = data;

      // console.log(defualtData)
      this.studyTimeData.data.datasets = [defaultMemberData]

      new Chart(ctx, this.studyTimeData);
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
    getLabels(time) {
      const days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
      const date = new Date(time);
      if(date.getFullYear()%4 === 0) {
        days[2] = 29;
      }

      let labels = []
      for(let i=1; i<=days[date.getMonth()]; i++) {
        const str = date.getMonth()+1 + "/" + i
        labels.push(str)
      }

      return labels;
    }
  }
}
</script>

<style scoped>

</style>