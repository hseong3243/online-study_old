<template>
  <div>
    <canvas id="line-bar-chart" ref="line-bar-chart"></canvas>
  </div>
</template>

<script>
import Chart from 'chart.js'
import lineBarData from "@/linechart/lineBarData";
import {useRecordStore} from "@/store/record";
export default {
  name: "PlanetChart",
  data() {
    return {
      lineBarData: lineBarData,
      memberStudyRecords: null,
      studyRecords: null,
      colors: [],
    }
  },
  props: ['date', 'dataType', 'studyData', 'from', 'members'],
  setup() {
    const recordStore = useRecordStore();
    return {
      recordStore
    }
  },
  watch: {
    studyData() {
    },
    date() {
      this.changeChart()
    }
  },
  mounted() {
    this.changeChart()
  },

  methods: {
    changeChart() {
      const ctx = document.getElementById('line-bar-chart');

      this.colors = ['#FFF1BF', '#FFB74D']
      const days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
      const month = new Date(this.date).getMonth();
      const map = new Map();

      this.lineBarData.data.labels = []
      switch (this.dataType) {
        case 'MONTH':
          this.lineBarData.data.labels = this.getLabels();
          for(let i=0; i<days[month]; i++) {
            const day = (i+1)<10 ? '0'+(i+1) : (i+1);
            const findDate = new Date(this.date).getFullYear() + '-' +(month+1) + '-' + day;
            map.set(findDate, i);
          }
          break;
        case 'WEEK':
          for(let i=6; i>=0; i--) {
            let date = new Date(this.getDate(new Date(this.date), i));
            this.lineBarData.data.labels.push(this.getDateLabel(date));
            map.set(this.getDate(date, 0), 6-i);
          }
          break;
      }


      let defaultMemberRecords = {
        type: 'bar',
        label: this.studyData[0].studies[0].name,
        data: [],
        backgroundColor: this.colors[1],
        borderColor: '#ffffff',
        borderWidth: 2,
        order: 1
      }
      let defaultStudyRecords = {
        type: 'line',
        label: this.studyData[1].studies[0].name + ' 전체 평균',
        data: [],
        borderColor: '#000000',
        fill: false,
        tension: 0.1,
      }
      let defaultStudyTopRecords = {
        type: 'line',
        label: '상위 10% 평균',
        data: [],
        borderColor: '#000000',
        fill: false,
        tension: 0.1,
      }

      const memberRecords = this.studyData[0].records;
      const studyRecords = this.studyData[1].records;




      let memberData = [];
      if(memberRecords.length <= 7)
        memberData.length = 7;
      else
        memberData.length = days[month];
      memberData.fill(0)
      memberRecords.forEach(record => {
        if(map.get(record.date) !== undefined) {
          const index = map.get(record.date);
          if(record.studyStatus === "STUDY") {
            memberData[index] += record.endTime - record.startTime
          }
        }
      })


      let studyData = [];
      if(studyRecords.length <= 7)
        studyData.length = 7;
      else
        studyData.length = days[month];
      studyData.fill(0);
      studyRecords.forEach(record => {
        if(map.get(record.date) !== undefined) {
          const index = map.get(record.date);
          if(record.studyStatus === "STUDY") {
            studyData[index] += record.endTime - record.startTime
          }
        }
      })

      const records = this.studyData[1].records;

      if(this.from==='group') {
        for(let i=0; i<studyData.length; i++) {
          studyData[i] = studyData[i]/this.members.length
        }
      }
      else {
        records.forEach(record => {
          const index = map.get(record.date);
          studyData[index] = studyData[index] / record.count
        })
      }


      let memberCount = []
      if(memberRecords.length <= 7)
        memberCount.length = 7;
      else
        memberCount.length = days[month]
      memberCount.fill(0)
      studyRecords.forEach(record => {
        if(map.get(record.date) !== undefined) {
          const index = map.get(record.date);
          memberCount[index] = memberCount[index] +1;
        }
      })

      if(this.from !== 'group') {
        if (this.recordStore.getCategory === 'WEEK') {
          this.lineBarData.options.responsive = false
          this.$refs["line-bar-chart"].width = 400;
          this.$refs["line-bar-chart"].height = 350;
        } else if (this.recordStore.getCategory === 'MONTH') {
          this.lineBarData.options.responsive = true

        }
      }else {
        defaultStudyRecords.label = this.studyData[1].studies[0].name + ' 그룹 평균'
      }


      defaultMemberRecords.data = memberData;
      defaultStudyRecords.data = studyData;

      this.lineBarData.data.datasets = [defaultMemberRecords, defaultStudyRecords]

      const studyTopRecords = this.studyData[2].data;
      defaultStudyTopRecords.data = studyTopRecords.map(record => record.time)
      this.lineBarData.data.datasets.push(defaultStudyTopRecords)

      new Chart(ctx, this.lineBarData);
    },
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
    getLabels() {
      const days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
      if(new Date(this.date).getFullYear()%4 === 0) {
        days[2] = 29;
      }

      let labels = []
      for(let i=1; i<=days[new Date(this.date).getMonth()]; i++) {
        const str = new Date(this.date).getMonth()+1 + "/" + i
        labels.push(str)
      }

      return labels;
    },
    getWeekLabels(date, period) {
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
    }

  }
}
</script>

<style scoped>
</style>