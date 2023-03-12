<template>
  <div class="d-flex p-2">
    <div class="col border border-1 rounded bg-origin shadow-sm">
    <div class="Chart p-2 pt-4 pb-4 pe-3">
      <MonthChart :member-data="memberData" :date="date" :colors="colors" v-if="memberData && !compare"/>
      <line-bar-chart :study-data="studyData" :from="'record'" :date="date" :data-type="dataType"
                      v-if="studyData && compare"/>
    </div>
    </div>
  </div>
  <div class="d-flex">
    <div class="d-flex col p-2">
      <div class="d-flex justify-content-center border border-1 rounded flex-fill bg-origin shadow-sm">
      <DayChart :member-data="memberData" :data-type="dataType" :colors="colors" v-if="memberData"/>
      </div>
    </div>
    <div class="col p-2">
      <div class="d-flex justify-content-center border border-1 rounded bg-origin shadow-sm">
        <StudyTimeChart :member-data="memberData" :date="date" :category-data="categoryData"
                      :data-type="dataType" :colors="colors" v-if="memberData && categoryData === null"/>
        <study-time-line-chart :member-data="memberData" :date="date" :category-data="categoryData"
                               :data-type="dataType" :colors="colors" :category="category" v-if="memberData && categoryData !== null"/>
      </div>
    </div>
  </div>
</template>

<script>
import MonthChart from "@/components/record/MonthChart";
import DayChart from "@/components/record/DayChart";
import StudyTimeChart from "@/components/record/StudyTimeChart";
import axios from "axios";
import LineBarChart from "@/components/record/LineBarChart";
import {useTokenStore} from "@/store/token";
import StudyTimeLineChart from "@/components/record/StudyTimeLineChart";

export default {
  name: "MonthRecord",
  components: {
    MonthChart,
    DayChart: DayChart,
    StudyTimeChart,
    LineBarChart: LineBarChart,
    StudyTimeLineChart
  },
  data() {
    return {
      memberData: null,
      dataType: 'MONTH',
      compare: false,
      studyRecords: null,
      colors: [],
    }
  },
  props: ['studyData', 'categoryData', 'category', 'date'],
  watch: {
    studyData() {
      console.log(this.studyData)
    },
    categoryData() {
    },
    date() {
      this.getData()
    }
  },
  setup() {
    const tokenStore = useTokenStore();
    return {
      tokenStore
    }
  },
  mounted() {

    this.colors = ['#FFF1BF', '#FFB74D', '#FFF176', '#81C784', '#4FC3F7', '#9575CD', '#FF8A65', '#E57373'
      ,'#BA68C8', '#7986CB', '#64B5F6', '#FFECB3', '#AED581']
    this.getData()
  },

  methods: {
    getData() {
      axios.get('http://localhost:8000/record-service/records', {
        params: {
          memberId: this.tokenStore.getMemberId,
          period: 'MONTH',
          date: this.date
        }
      })
          .then((response) => {
            const data = response.data;
            this.memberData = data;
          })
    },
    getDate() {
      const date = new Date();
      const year = date.getFullYear();
      let month = date.getMonth()+1;
      month = month<10 ? '0'+month : month;
      let day = date.getDate();
      day = day<10 ? '0' +day : day

      return year + '-' + month + '-' + day
    },
    getLabels() {
      const days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
      const date = new Date();
      if(date.getFullYear()%4 === 0) {
        days[2] = 29;
      }

      let labels = []
      for(let i=1; i<=days[date.getMonth()]; i++) {
        const str = date.getMonth()+1 + "/" + i
        labels.push(str)
      }

      return labels;
    },
    getStudyData() {
      axios.get('http://localhost:8000/reocrd-service/records', {
        params: {
          memberId: 1,
          period: 'MONTH',
          date: this.getDate(),
          studyId: 2
        }
      })
      .then((response) => {
        return response.data;
      })
    },
    logStudyData() {
      this.compare = !this.compare;
    }
  }
}
</script>

<style scoped>
.bg-origin {
  background-color: white;
}
</style>