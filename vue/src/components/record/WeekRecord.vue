<template>
  <div class="d-flex">
    <div class="d-flex col p-2">
      <div class="d-flex justify-content-center border border-1 rounded flex-fill bg-origin shadow-sm">
        <DayChart :member-data="memberData" :data-type="dataType" :colors="colors" v-if="memberData"/>
      </div>
    </div>
    <div class="col d-flex flex-column justify-content-evenly p-2">
      <div class="d-flex justify-content-evenly p-2">
        <div class="border border-2 rounded-1 shadow-sm m-2 p-3 pe-5 bg-origin">
          <div>
            <span>주간 공부시간</span>
          </div>
          <div class="">
            <span class="RecordTime" ref="studyTime">00 : 00 : 00</span>
          </div>
        </div>
        <div class="border border-2 rounded-1 shadow-sm m-2 p-3 pe-5 bg-origin">
          <div>
            <span>평균 휴식 시간</span>
          </div>
          <div class="">
            <span class="RecordTime" ref="restTime">00 : 00 : 00</span>
          </div>
        </div>
      </div>
      <div class="d-flex justify-content-evenly p-2">
        <div class="border border-2 rounded-1 shadow-sm m-2 p-3 pe-5 bg-origin">
          <div>
            <span>공부 시작 시간</span>
          </div>
          <div>
            <span class="RecordTime" ref="startTime">00 : 00 : 00</span>
          </div>
        </div>
        <div class="border border-2 rounded-1 shadow-sm m-2 p-3 pe-5 bg-origin">
          <div>
            <span>공부 종료 시간</span>
          </div>
          <div>
            <span class="RecordTime" ref="endTime">00 : 00 : 00</span>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="d-flex">
    <div class="col p-2">
      <div class="d-flex justify-content-center border border-1 rounded flex-fill bg-origin shadow-sm">
        <WeekChart :member-data="memberData" :date="date" :colors="colors" v-if="memberData && !compare"/>
        <line-bar-chart :study-data="studyData" :from="'record'" :date="date" :data-type="dataType"
                        v-if="studyData && compare" ref="lineBar"/>
      </div>
    </div>
    <div class="d-flex col p-2">
      <div class="d-flex justify-content-center border border-1 rounded flex-fill bg-origin shadow-sm">
        <StudyTimeChart :member-data="memberData" :date="date" :category-data="categoryData"
                        :data-type="dataType" :colors="colors" v-if="memberData && categoryData===null"/>
        <study-time-line-chart :member-data="memberData" :date="date" :category-data="categoryData"
                               :data-type="dataType" :colors="colors" :category="category" v-if="memberData && categoryData !== null"/>
      </div>
    </div>
  </div>
</template>

<script>
import WeekChart from "@/components/record/WeekChart";
import DayChart from "@/components/record/DayChart";
import StudyTimeChart from "@/components/record/StudyTimeChart";
import axios from "axios";
import {useTokenStore} from "@/store/token";
import LineBarChart from "@/components/record/LineBarChart";
import StudyTimeLineChart from "@/components/record/StudyTimeLineChart";

export default {
  name: "WeekRecord",
  data() {
    return {
      doughnutTitle: "주간 공부 량",
      memberData: null,
      dataType: 'WEEK',
      colors: [],
      compare: false,
      categoryReady: false,
    }
  },
  components: {
    WeekChart,
    DayChart: DayChart,
    StudyTimeChart: StudyTimeChart,
    LineBarChart,
    StudyTimeLineChart
  },
  props: ['studyData', 'categoryData', 'category', 'date'],
  setup() {
    const tokenStore = useTokenStore();
    return {
      tokenStore,
    }
  },
  mounted() {
    console.log(this.studyData)

    this.colors = ['#FFF1BF', '#FFB74D', '#FFF176', '#81C784', '#4FC3F7', '#9575CD', '#FF8A65', '#E57373'
      ,'#BA68C8', '#7986CB', '#64B5F6', '#FFECB3', '#AED581']
    this.getData();
  },
  watch: {
    date() {
      this.getData();
    }
  },
  methods: {
    getData() {
      axios.get("http://localhost:8000/record-service/records", {
        params: {
          memberId: this.tokenStore.getMemberId,
          period: 'WEEK',
          date: this.date
        }
      })
          .then((response) => {
            const data = response.data;

            console.log(data);

            this.memberData = data;
            const startTime = data.startTimeAvg;
            const endTime = data.endTimeAvg;
            const studyTime = data.studyTime;
            let restTime = data.restTime;

            const records = data.records;

            const list = records.filter(record => record.studyStatus==='REST');
            let count = list.length
            restTime = restTime/count;


            this.$refs.startTime.textContent = this.getTimeStringHM(startTime);
            this.$refs.endTime.textContent = this.getTimeStringHM(endTime);
            this.$refs.studyTime.textContent = this.getTimeStringHMS(studyTime)
            this.$refs.restTime.textContent = this.getTimeStringHMS(restTime)

          })
    },
    getDate() {
      const date = new Date();
      const year = date.getFullYear();
      let month = date.getMonth() + 1;
      month = month < 10 ? '0' + month : month;
      let day = date.getDate();
      day = day < 10 ? '0' + day : day

      return year + '-' + month + '-' + day
    },
    getWeekStudyTime() {
      let data = {
        type: 'WEEK',
        labels: ['월', '화', '수', '목', '금', '토', '일'],
        datasets: [
          {
            data: [[45000, 67500], [46300, 72050], [48090, 71067],
              [51022, 69768], [50020, 74565], [46075, 65475],
              [48209, 75465]]
          }
        ]
      }

      return data;
    },
    getWeekTime() {
      let data = {
        title: '주간 공부 량',
        labels: ['휴식', '백엔드'],
        data: [1200, 6400]
      }
      return data;
    },
    getTimeStringHM(time) {
      const date = new Date(1970, 0, 1);
      date.setSeconds(time);
      const hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
      const minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
      return hours + "시 " + minutes + "분";
    },
    getTimeStringHMS(time) {
      // const date = new Date(1970, 0, 1);
      let hours = Math.floor(time / 3600);
      let minutes = Math.floor(time / 60 - hours * 60)
      let seconds = Math.floor(time % 60)
      hours = hours < 10 ? '0' + hours : hours;
      minutes = minutes < 10 ? '0' + minutes : minutes;
      seconds = seconds < 10 ? '0' + seconds : seconds;
      return hours + ":" + minutes + ":" + seconds;
    },
  }

}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500&display=swap');
span {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 300;
}
.bg-origin {
  background-color: white;
}
.RecordTime {
  font-size: 24px;
}
</style>