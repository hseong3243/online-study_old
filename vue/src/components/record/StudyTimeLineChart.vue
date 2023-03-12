<template>
  <div>
    <canvas id="study-time-line-chart" width="400" height="350"></canvas>
  </div>
</template>

<script>
import Chart from 'chart.js'
import {useRecordStore} from "@/store/record";
import studyTimeLineData from "@/linechart/studyTimeLineData";
export default {
  name: "PlanetChart",
  data() {
    return {
      studyTimeLineData: studyTimeLineData
    }
  },
  props: ['memberData', 'date', 'dataType', 'colors', 'categoryData', 'category'],
  setup() {
    const recordStore = useRecordStore();
    return {
      recordStore
    }
  },
  mounted() {
    this.getData()
  },
  watch: {
    date() {
      this.getData()
    },
    categoryData() {
      const ctx = document.getElementById('study-time-line-chart');

      const days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]

      let endDate = this.date

      const map = new Map();
      this.studyTimeLineData.data.labels = [];
      if(this.dataType === 'WEEK') {
        for (let i = 6; i >= 0; i--) {
          let date = new Date(this.getDate(new Date(endDate), i));
          this.studyTimeLineData.data.labels.push(this.getDateLabel(date));
          map.set(this.getDate(date, 0), 6 - i);
        }
      }
      else {
        this.studyTimeLineData.data.labels = this.getLabels(this.date)
        const month = new Date(this.date).getMonth();
        for(let i=0; i<days[month]; i++) {
          const day = (i+1)<10 ? '0'+(i+1) : (i+1);
          const findDate = new Date(this.date).getFullYear() + '-' +(month+1) + '-' + day;
          map.set(findDate, i);
        }
      }

      let defaultMemberData = {
        type:'bar',
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
      defaultMemberData.data = data;

      const categoryRecords = this.categoryData.records;

      const categoryLabel = [' 시작 평균', ' 종료 평균']
      const category = this.categoryToString()

      this.studyTimeLineData.data.datasets = []
      for(let i=0; i<2; i++) {
        let defaultCategoryData = {
          type: 'line',
          label: category + categoryLabel[i],
          data: [4050, 3090, 3060, 3400],
          borderColor: "#000000",
          fill: false,
          tension: 0.1
        }

        let categoryData = []
        categoryData.length = 7;
        index = 0;
        categoryRecords.forEach(categoryRecord => {
          let list = [categoryRecord.startTime, categoryRecord.endTime]
          categoryData[index] = list[i];
          index++;
        })
        defaultCategoryData.data = categoryData
        this.studyTimeLineData.data.datasets.push(defaultCategoryData)
      }

      console.log(categoryRecords)


      this.studyTimeLineData.data.datasets.push(defaultMemberData)

      new Chart(ctx, this.studyTimeLineData);
    }
  },

  methods: {
    getData() {
      const ctx = document.getElementById('study-time-line-chart');

      const days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]

      // let endDate = this.date

      const map = new Map();
      this.studyTimeLineData.data.labels = [];
      if(this.dataType === 'WEEK') {
        for (let i = 6; i >= 0; i--) {
          // let date = new Date(this.getDate(new Date(endDate), i));
          let date = new Date(this.date)
          this.studyTimeLineData.data.labels.push(this.getDateLabel(date));
          map.set(this.getDate(date, 0), 6 - i);
        }
      }
      else {
        this.studyTimeLineData.data.labels = this.getLabels(this.date)
        const month = new Date(this.date).getMonth();
        for(let i=0; i<days[month]; i++) {
          const day = (i+1)<10 ? '0'+(i+1) : (i+1);
          const findDate = new Date(this.date).getFullYear() + '-' +(month+1) + '-' + day;
          map.set(findDate, i);
        }
      }

      let defaultMemberData = {
        type:'bar',
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
      defaultMemberData.data = data;

      const categoryRecords = this.categoryData.records;

      const categoryLabel = [' 시작 평균', ' 종료 평균']
      const category = this.categoryToString()

      this.studyTimeLineData.data.datasets = []
      for(let i=0; i<2; i++) {
        let defaultCategoryData = {
          type: 'line',
          label: category + categoryLabel[i],
          data: [4050, 3090, 3060, 3400],
          borderColor: "#000000",
          fill: false,
          tension: 0.1
        }

        let categoryData = []
        categoryData.length = 7;
        index = 0;
        categoryRecords.forEach(categoryRecord => {
          let list = [categoryRecord.startTime, categoryRecord.endTime]
          categoryData[index] = list[i];
          index++;
        })
        defaultCategoryData.data = categoryData
        this.studyTimeLineData.data.datasets.push(defaultCategoryData)
      }

      console.log(categoryRecords)


      this.studyTimeLineData.data.datasets.push(defaultMemberData)

      new Chart(ctx, this.studyTimeLineData);
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
    },
    categoryToString(){
      switch (this.category) {
        case 'HIGH':
          return '고등학생'
        case 'UNIV':
          return '대학생'
        case 'CERT':
          return '자격증'
        case 'LANG':
          return '어학'
        case 'GOV':
          return '공무원'
        case 'JOB':
          return '취업'
        case 'ETC':
          return '기타'
      }
    }

  }
}
</script>

<style scoped>
</style>