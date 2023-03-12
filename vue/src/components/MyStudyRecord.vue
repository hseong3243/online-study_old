<template>
  <div>
    <div class="bg-dark">
      <div class="HeaderContainer m-auto p-2">
        <span class="text-white fs-3">공부 기록</span>
      </div>
    </div>
    <div class="bg-grey pt-3">
    <div class="MyContainer m-auto d-flex justify-content-center">
      <div class="col-2 d-flex flex-column align-items-center p-2">
  <!--      <div class="list-group list-group-horizontal">-->
  <!--        <a href="#" class="list-group-item link-dark" @click="changeData = 'DAY'">일별</a>-->
  <!--        <a href="#" class="list-group-item link-dark" @click="changeData = 'WEEK'">주별</a>-->
  <!--        <a href="#" class="list-group-item link-dark" @click="changeData = 'MONTH'">월별</a>-->
  <!--        <a href="#" class="list-group-item link-dark" @click="changeData = 'TEST'">테스트</a>-->
  <!--      </div>-->
        <div class="d-flex align-self-end">
          <select class="form-select" aria-label="Default select example"
                  v-model="recordStore.getCategory" @change="setChangeData">
            <option value="DAY">일간</option>
            <option value="WEEK">주간</option>
            <option value="MONTH">월간</option>
          </select>
        </div>
        <div class="mt-2">
          <date-picker v-model:value="date" valueType="format" format="YYYY-MM-DD" style="width: 160px"></date-picker>
        </div>
        <div class="d-flex flex-column border border-1 rounded-1 w-100 mt-2">
          <div class="p-2 ps-3"><span class="fs-5">분류별</span></div>
          <div class="d-flex flex-column p-2">
            <div class="form-check">
              <input class="form-check-input" type="radio" name="flexRadioDefault" value="ALL" v-model="recordCategory">
              <label class="form-check-label">
                전체
              </label>
            </div>
            <div class="form-check">
              <input class="form-check-input" type="radio" name="flexRadioDefault" value="HIGH" v-model="recordCategory">
              <label class="form-check-label">
                고등학생
              </label>
            </div>
            <div class="form-check">
              <input class="form-check-input" type="radio" name="flexRadioDefault" value="UNIV" v-model="recordCategory">
              <label class="form-check-label">
                대학생
              </label>
            </div>
            <div class="form-check">
              <input class="form-check-input" type="radio" name="flexRadioDefault" value="CERT" v-model="recordCategory">
              <label class="form-check-label" >
                자격증
              </label>
            </div>
            <div class="form-check">
              <input class="form-check-input" type="radio" name="flexRadioDefault" value="LANG" v-model="recordCategory">
              <label class="form-check-label">
                어학
              </label>
            </div>
            <div class="form-check">
              <input class="form-check-input" type="radio" name="flexRadioDefault" value="GOV" v-model="recordCategory">
              <label class="form-check-label">
                공무원
              </label>
            </div>
            <div class="form-check">
              <input class="form-check-input" type="radio" name="flexRadioDefault" value="JOB" v-model="recordCategory">
              <label class="form-check-label">
                취업
              </label>
            </div>
            <div class="form-check">
              <input class="form-check-input" type="radio" name="flexRadioDefault" value="ETC" v-model="recordCategory">
              <label class="form-check-label">
                기타
              </label>
            </div>
          </div>
        </div>
        <div class="d-flex flex-column border border-1 rounded-1 w-100 mt-3" v-if="recordCount">
          <div class="p-2 ps-3">
            <span class="fs-5" v-if="recordStore.getCategory==='MONTH'">이번 1달간</span>
            <span class="fs-5" v-if="recordStore.getCategory==='WEEK'">지난 1주일간..</span>
          </div>
          <div class="d-flex flex-column p-2">
            <span>{{recordCount.studyCount}}명의 사용자들이</span>
            <span>{{studyCount}}을/를 공부하였습니다.</span>
          </div>
        </div>
      </div>
      <div class="col-9">
        <div class="d-flex flex-column">
        <div class="d-flex align-items-center p-2">
          <form class="d-flex">
            <div>
              <input class="form-control rounded-start rounded-0" id="studies" v-model="study"/>
            </div>
            <div>
              <button type="button" class="btn btn-outline-secondary rounded-end rounded-0" @click="searchStudy">검색</button>

            </div>
            <div class="ps-2 d-flex gap-1 align-self-center">
              <template v-for="study in recentStudies" :key="study.studyId">
                <button type="button" class="btn btn-outline-secondary rounded-5" name="tag" @click="[getStudyData(study), getRecordCount(study)]">#{{study.name}}</button>
              </template>
            </div>
          </form>
        </div>
        <div class="d-flex flex-wrap align-items-center gap-1 ps-2">

          <template v-for="study in studies" :key="study.studyId">
            <button type="button" class="btn btn-outline-secondary rounded-5" name="tag" @click="[getStudyData(study), getRecordCount(study)]">#{{study.name}}</button>
          </template>
        </div>
        </div>
        <DayRecord :date="date" v-if="recordStore.getCategory==='DAY'"/>
        <WeekRecord v-if="recordStore.getCategory==='WEEK'" :study-data="studyData" :category-data="categoryData" :category="recordCategory" :date="date" ref="weekRecord"/>
        <MonthRecord v-if="recordStore.getCategory==='MONTH'" :study-data="studyData" :category-data="categoryData" :category="recordCategory" :date="date" ref="monthRecord"/>
      </div>
    </div>
    </div>
  </div>
</template>

<script>
import DayRecord from "@/components/record/DayRecord";
import WeekRecord from "@/components/record/WeekRecord";
import MonthRecord from "@/components/record/MonthRecord";
import axios from "axios";
import {useTokenStore} from "@/store/token";
import {useRecordStore} from "@/store/record";
import DatePicker from "vue-datepicker-next";

export default {
  name: 'MyStudyRecord',
  data() {
    return{
      studies: null,
      recordData: null,
      studyData: [null, null, null],
      study: null,
      recordCategory: 'ALL',
      categoryData: null,
      recentStudies: [],
      recordCount: null,
      studyCount: null,
      date: this.getDate(),
    }
  },
  components: {
    MonthRecord,
    DayRecord,
    WeekRecord,
    DatePicker,
  },
  setup() {
    const tokenStore = useTokenStore();
    const recordStore = useRecordStore();
    return {
      tokenStore,
      recordStore
    }
  },
  mounted() {
    if(this.tokenStore.getMemberId === null) {
      alert('로그인 후에 이용 가능합니다.')
      this.$router.push('/')
    }

    this.study = null
    this.getRecentStudies()
  },
  methods: {
    searchStudy() {
      axios.get('http://localhost:8000/study-service/studies', {
        params: {
          studyName: this.study
        }
      })
      .then(response => {
        const result = response.data;
        const data = result.data;
        this.studies = data;
        console.log(this.studies)
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
    async getStudyData(study) {
      this.study = study.name;
      this.studyCount = study.name;

      const memberResponse = await axios.get('http://localhost:8000/record-service/records', {
        params: {
          memberId: this.tokenStore.getMemberId,
          period: this.recordStore.getCategory,
          date: this.date,
          studyId: study.studyId
        }
      })

      const memberStudyRecords = memberResponse.data;

      const studyResponse = await axios.get('http://localhost:8000/record-service/records', {
        params: {
          period: this.recordStore.getCategory,
          date: this.date,
          studyId: study.studyId
        }
      });

      const studyRecords = studyResponse.data;

      const studyTopResponse = await axios.get('http://localhost:8000/record-service/records/ranked', {
        params: {
          period: this.recordStore.getCategory,
          date: this.date,
          studyStatus: 'STUDY',
          page:0, size: 10,
          studyId: study.studyId,
          byDate: 'yes'
        }
      });

      const studyTopRecords = studyTopResponse.data;

      this.studyData[0] = memberStudyRecords;
      this.studyData[1] = studyRecords;
      this.studyData[2] = studyTopRecords;

      if(this.recordStore.getCategory==='MONTH')
        this.$refs.monthRecord.compare = !this.$refs.monthRecord.compare;
      else if(this.recordStore.getCategory==='WEEK')
        this.$refs.weekRecord.compare = !this.$refs.weekRecord.compare;
    }
    ,
    setChangeData(e) {
      this.recordCount = null;
      this.recordStore.changeCategory(e.target.value);
    },
    getRecentStudies() {
      axios.get('http://localhost:8000/study-service/tickets/studies', {
        params: {
          memberId: this.tokenStore.getMemberId,
          day: 7
        }
      })
          .then(response => {
            this.recentStudies = response.data.data;
            console.log(this.recentStudies)
          })
    },
    getRecordCount(study) {
      axios.get('http://localhost:8000/record-service/count', {
        params: {
          date: this.date,
          studyId: study.studyId,
          period: this.recordStore.getCategory,
        }
      })
          .then(response => {
            const data = response.data;
            this.recordCount = data;
            console.log(this.recordCount);
          })
    }
  },
  watch: {
    studyData() {
      console.log(this.studyData)
    },
    recordCategory() {
      const category = this.recordCategory
      if(this.recordCategory === 'ALL') {
        this.$router.go()
      }
      axios.get('http://localhost:8000/record-service/records', {
        params: {
          period: this.recordStore.getCategory,
          date: this.date,
          studyCategory: category
        }
      })
      .then(response => {
        this.categoryData = response.data
        console.log(this.categoryData)
      })
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
button {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 300;
}
button[name=tag] {
  font-size: 11px;
}
.HeaderContainer {
  width: 1100px;
}
.MyContainer {
  width: 1100px;
}

.bg-grey{
  width: 1100px;
  background: #F8F9FA;
  height: 100%;
}
@media (min-width: 1100px) {
  .bg-grey {
    width: 100%;
  }

}
  @media (max-width: 1100px) {
    .bg-grey{
      background: #F8F9FA;
      height: 100%;
    }
  }
.bg-dark {
  width: 1100px;
}
@media (min-width: 1100px) {
  .bg-dark {
    width: 100%;
  }

}
.background {
  width: 100%;
  background: #F6E2BD;
}
@media (max-width: 1100px) {
  .background {
    width: 1200px;
  }
}
</style>