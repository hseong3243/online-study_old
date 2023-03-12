<template>
  <div class="d-flex">
    <div class="d-flex flex-column col p-2 position-relative">
      <div class="d-flex">
        <div class="d-flex border justify-content-center border-1 rounded  bg-origin shadow-sm bg-origin">
          <day-chart :member-data="memberData" :data-type="dataType" :colors="colors" v-if="dataReady"/>
        </div>
        <div class="col d-flex flex-column justify-content-evenly align-items-center">
          <div class="border border-2 rounded-1 shadow-sm m-2 p-3 pe-5 bg-origin">
            <div>
              <span>오늘 공부시간</span>
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

      </div>
      <div class="d-flex flex-column border border-1 rounded flex-fill bg-origin shadow-sm bg-origin mt-3">
        <div class="grid-board flex-fill p-2">
            <div class="d-flex col p-2" v-for="(study, i) in studies" :key="study.studyId">
            <div class="d-flex flex-column">
              <span>{{ study.name }}</span>
              <span ref="study">{{ studyTime[i] }}</span>
            </div>
            <div style="width: 15px; height: 15px;" :style="{backgroundColor: colorMap.get(study.name)}">
            </div>
          </div>
<!--          <div>-->
<!--            <div class="border rounded shadow-sm d-flex flex-column p-2" v-for="record in rawRecords" :key="record.startTime">-->
<!--              <span>{{studyMap.get(record.studyId)}}</span>-->
<!--              <div class="fs-6">-->
<!--              <span>{{Math.floor(record.startTime/3600)}}시 {{Math.floor(record.startTime%3600/60)}}분 ~</span>-->
<!--              <span>&nbsp;{{Math.floor(record.endTime/3600)}}시 {{Math.floor(record.endTime%3600/60)}}분</span>-->
<!--              </div>-->
<!--            </div>-->
<!--          </div>-->
        </div>
        <div class="d-flex flex-fill">
        </div>
      </div>
    </div>
    <div class="col-3">
      <StudyPlanner :member-data="memberData" :colors="colors" v-if="memberData"/>
    </div>
  </div>

</template>

<script>
import DayChart from "@/components/record/DayChart";
import StudyPlanner from "@/components/record/StudyPlanner";
import axios from "axios";
import {useTokenStore} from "@/store/token";

export default {
  name: "DayRecord",
  components: {
    StudyPlanner,
    DayChart: DayChart,
  },
  props: ['date'],
  data() {
    return{
      memberData: null,
      studies: [],
      colors: [],
      pickerShow: [false, false],
      dataType: "DAY",
      studyColor: null,
      dataReady: false,
      studyTime: [],
      rawRecords: [],
      studyMap: null,
      colorMap: null,
    }
  },
  setup() {
    const tokenStore = useTokenStore();
    return {
      tokenStore
    }
  },
  mounted() {
    this.colorMap = new Map();
    axios.get('http://localhost:8000/record-service/records', {
      params: {
        memberId: this.tokenStore.getMemberId,
        period: 'DAY',
        date: this.date
      }
    })
    .then((response) => {
      const data = response.data;
      this.$refs.studyTime.textContent = this.getTimeStringHMS(data.studyTime)
      this.$refs.restTime.textContent = this.getTimeStringHMS(data.restTime)

      this.memberData = data;
      const records = this.memberData.records;
      this.studies = data.studies
      this.colors = ['#FFF1BF', '#FFB74D', '#FFF176', '#81C784', '#4FC3F7', '#9575CD', '#FF8A65', '#E57373'
      ,'#BA68C8', '#7986CB', '#64B5F6', '#FFECB3', '#AED581']

      let sub = 0;

      let i = 0;
      this.studies.forEach(study => {
        const list = records.filter(record => (record.studyId===study.studyId) && (record.studyStatus==='STUDY'));
        let studyTime = 0;
        list.forEach(item => {
          studyTime += item.endTime-item.startTime
        })
        sub += studyTime;
        this.studyTime.push(this.epoch_to_hour_min(studyTime));
        this.colorMap.set(study.name, this.colors[i])
        i++;
      })

      this.$refs.studyTime.textContent = this.getTimeStringHMS(sub)

      const list = records.filter(record => record.studyStatus === 'REST');
      let restTime = 0;
      let count = 0;
      list.forEach(item => {
        restTime += item.endTime-item.startTime
        count++;
      })
      this.$refs.restTime.textContent = this.getTimeStringHMS(restTime/count)
      this.studyTime.push(this.epoch_to_hour_min(restTime))
      this.studies.push({studyId:0, name:'휴식'})
      this.colorMap.set('휴식', '#ffffff')
      this.dataReady = true;
    })

    this.getRawRecords()
  },
  methods: {
    getDayData() {
      let data = {
        title: '하루 공부 량',
        labels: ['휴식', '백엔드'],
        data: [1200, 6400]
      }
      return data;
    },
    getTimeStringHM(time) {
      const date = new Date(1970, 0, 1);
      date.setSeconds(time);
      const hours = date.getHours()<10 ? '0'+date.getHours() : date.getHours();
      const minutes = date.getMinutes()<10 ? '0'+date.getMinutes() : date.getMinutes();
      return hours + "시 " + minutes + "분";
    },
    getTimeStringHMS(time) {
      // const date = new Date(1970, 0, 1);
      let hours = Math.floor(time / 3600);
      let minutes = Math.floor(time / 60 - hours*60)
      let seconds = Math.floor(time%60)
      hours = hours<10 ? '0'+hours : hours;
      minutes = minutes<10 ? '0'+minutes : minutes;
      seconds = seconds<10 ? '0'+seconds : seconds;
      return hours + ":" + minutes + ":" + seconds;
    },
    getDate() {
      const date = new Date();
      const year = date.getFullYear();
      let month = date.getMonth()+1;
      month = month<10 ? '0'+month : month;
      let day = date.getDate();
      day = day<10 ? '0' +day : day

      if(date.getHours()<5)
        return year + '-' + month + '-' + (day-1)
      else
        return year + '-' + month + '-' + day
    },
    epoch_to_hour_min(epoch) {
      let h = Math.floor(epoch / 3600);
      let m = Math.floor(epoch % 3600 / 60);
      // const date = new Date(epoch*1000);
      // const hour = date.getHours();
      // const min = date.getMinutes();
      return h + '시간 ' + m + '분';
    },
    getRawRecords() {
      axios.get('http://localhost:8000/record-service/records/'+this.tokenStore.getMemberId)
      .then(response => {
        const data = response.data;
        this.rawRecords = data.records;
        this.studyMap = new Map();
        for(const study of data.studies) {
          this.studyMap.set(study.studyId, study.name);
        }
        this.studyMap.set(0, '휴식');
        this.rawRecords.forEach(record => {
          record.studyId = record.studyStatus==='REST' ? 0 : record.studyId
        })

      })
    }
  },
  watch: {
    colors() {
    }
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500&display=swap');
span {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 500;
}
.RecordTime {
  font-size: 25px;
}
.bg-origin {
  background-color: white;
}
Swatches {
  position: absolute;
}
.grid-board {
  display: grid;
  grid-template-columns: 1fr 1fr;
}
</style>