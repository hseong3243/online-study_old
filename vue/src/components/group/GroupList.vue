<template>
  <div class="d-flex justify-content-center">
    <div class="col-2 p-2">
      <div class="d-flex flex-column border border-1 rounded-1 w-100 mt-3">
        <div class="p-2 ps-3"><span class="fs-5">분류별</span></div>
        <div class="d-flex flex-column p-2">
          <div class="form-check">
            <input class="form-check-input" type="radio" name="flexRadioDefault" v-model="selectCategory" value="ALL">
            <label class="form-check-label">
              전체
            </label>
          </div>
          <div class="form-check">
            <input class="form-check-input" type="radio" name="flexRadioDefault" v-model="selectCategory" value="HIGH">
            <label class="form-check-label">
              고등학생
            </label>
          </div>
          <div class="form-check">
            <input class="form-check-input" type="radio" name="flexRadioDefault" v-model="selectCategory" value="UNIV">
            <label class="form-check-label">
              대학생
            </label>
          </div>
          <div class="form-check">
            <input class="form-check-input" type="radio" name="flexRadioDefault" v-model="selectCategory" value="CERT">
            <label class="form-check-label" >
              자격증
            </label>
          </div>
          <div class="form-check">
            <input class="form-check-input" type="radio" name="flexRadioDefault" v-model="selectCategory" value="LANG">
            <label class="form-check-label">
              어학
            </label>
          </div>
          <div class="form-check">
            <input class="form-check-input" type="radio" name="flexRadioDefault" v-model="selectCategory" value="GOV">
            <label class="form-check-label">
              공무원
            </label>
          </div>
          <div class="form-check">
            <input class="form-check-input" type="radio" name="flexRadioDefault" v-model="selectCategory" value="JOB">
            <label class="form-check-label">
              취업
            </label>
          </div>
          <div class="form-check">
            <input class="form-check-input" type="radio" name="flexRadioDefault" v-model="selectCategory" value="ETC">
            <label class="form-check-label">
              기타
            </label>
          </div>
        </div>
      </div>
    </div>
    <div class="col-10 p-2">
      <div class="d-flex justify-content-between">
        <div class="col-5">
          <div class="d-flex gap-1">
            <select class="form-select" style="width: 100px;" aria-label="Default select example" v-model="searchType">
              <option value="NAME">이름</option>
            </select>
            <div class="d-flex">
            <input type="text" class="form-control rounded-0 rounded-start" v-model="search" v-if="searchType==='NAME'">
            <button type="button" class="btn btn-outline-secondary rounded-0 rounded-end col-3" @click="searchGroups">검색</button>
            </div>
          </div>
        </div>
        <div>
          <button class="btn btn-outline-secondary" @click="showModal = true">그룹 만들기</button>
          <study-room-modal v-if="showModal">
            <div class="d-flex flex-column justify-content-between">
              <h3>그룹 생성</h3>
              <div class="mt-2 col-4">
                <select class="form-select" aria-label="Default select example" v-model="groupCategory">
                  <option value="ALL">분류</option>
                  <option value="HIGH">고등학생</option>
                  <option value="UNIV">대학생</option>
                  <option value="CERT">자격증</option>
                  <option value="LANG">어학</option>
                  <option value="GOV">공무원</option>
                  <option value="JOB">취업</option>
                  <option value="ETC">기타</option>
                </select>
              </div>
              <div>
                <span>그룹 이름</span>
                <input type="text" class="form-control" :value="groupName" @input="changeGroupName">
              </div>
              <div>
                <span>스터디</span>
                <div class="d-flex">
                <input type="text" class="form-control" :value="studyName" @input="findStudy">
                  <button type="button" class="col-2 btn btn-outline-secondary" @click="getStudies(this.studyName)">검색</button>
                </div>
                <div class="find-group-study d-flex flex-wrap align-items-start gap-1">
                  <div class="mt-1" v-for="study in studies" :key="study.studyId">
                    <button type="button"
                            class="btn btn-sm btn-outline-secondary"
                            @click="setStudy(study)" >{{ study.name }}</button>
                  </div>
                  <div v-if="!studyExist">
                    <span>해당 스터디가 존재하지 않습니다.</span><br/>
                    <span>입력하신 스터디를 추가하시겠습니까?</span>
                    <button type="button" class="btn btn-sm" @click="createStudy">예</button>
                  </div>
                </div>
              </div>
              <div class="d-flex justify-content-end gap-2 mt-2">
                <button type="button" class="btn btn-sm btn-outline-secondary" @click="showModal = false">취소</button>
                <button type="button" class="btn btn-sm btn-outline-secondary" @click="createGroup">생성</button>
              </div>
            </div>
          </study-room-modal>
        </div>
      </div>
      <div class="list-group list-group-horizontal mt-2">
        <label class="list-group-item">
          <input class="form-check-input me-1" type="radio" value="" v-model="order" @click="getGroupsOrderByMade()">
          생성순
        </label>
        <label class="list-group-item">
          <input class="form-check-input me-1" type="radio" value="members" v-model="order" @click="getGroupsOrderByMembers('members')">
          인원순
        </label>
        <label class="list-group-item">
          <input class="form-check-input me-1" type="radio" value="attend" v-model="order" @click="getGroupsOrderByMembers('attend')">
          출석률
        </label>
      </div>
      <div class="list-container">
        <template v-for="group in groups" :key="group.groupId">
          <group-card :group="group" v-if="dataReady"/>
        </template>
      </div>
      <div class="d-flex justify-content-center">
        <hr v-if="groupData && groupData.next">
        <button type="button" class="btn btn-sm btn-secondary" style="z-index: 1" @click="nextPage" v-if="groupData && groupData.next">더보기</button>
        <p style="z-index: 1" @click="nextPage" v-if="groupData && !groupData.next">마지막 페이지입니다.</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import GroupCard from "@/components/group/GroupCard";
import StudyRoomModal from "@/components/studyroom/StudyRoomModal";
import {useGroupStore} from "@/store/group";

export default {
  name: "GroupList",
  components: {
    GroupCard,
    StudyRoomModal
  },
  data() {
    return{
      groups: null,
      groupCategory: 'ALL',
      selectCategory: 'ALL',
      dataReady: false,
      showModal: false,
      studies: null,
      studyName: null,
      study: null,
      studyExist: true,
      groupName: null,
      search: null,
      groupData:null,
      order: "",
      searchType: 'NAME',
    }
  },
  setup() {
    const groupStore = useGroupStore();
    return {
      groupStore
    }
  },
  mounted() {
      this.groupStore.changeTitle('목록')
    this.getGroups(10, 0).then((result) => {
      this.groupData = result
      this.groups=result.data
      console.log(this.groupData)
    })

    this.dataReady = true;

    this.studies = this.getTestStudies()
  },
  watch: {
    selectCategory() {
      const category = this.selectCategory!=='ALL' ? this.selectCategory : null;
      axios.get('http://localhost:8000/group-service/groups', {
        params: {
          groupCategory: category,
          page: 0,
          size: 10,
          order:this.order
        }
      })
      .then(response => {
        const result = response.data;
        this.groups = result.data;
      })
    }
  },
  methods: {
    nextPage() {
      if(this.groupData.next) {
        const category = this.selectCategory!=='ALL' ? this.selectCategory : null;
        axios.get('http://localhost:8000/group-service/groups', {
          params: {
            page: this.groupData.number+1,
            size: 10,
            category: category,
            order: this.order,
            search: this.search
          }
        })
        .then(response =>{
          this.groupData = response.data
          this.groups = this.groups.concat(response.data.data)
        })
      }
    },
    searchGroups() {
      axios.get('http://localhost:8000/group-service/groups', {
        params: {
          page: 0,
          size: 10,
          search: this.search
        }
      })
      .then(response => {
        const data = response.data;
        this.groupData = data;
        this.groups = data.data;
      })
    },
    changeGroupName(e) {
      this.groupName = e.target.value
    },
    createStudy() {
      let data = {
        name: this.studyName
      }

      console.log(data.studyName)
      axios.post('http://localhost:8000/study-service/studies', data)
      .then(response => {
        const studyId = response.data;
        this.studies = []
        this.study = {
          studyId: studyId,
          name: this.studyName
        }

        this.studies = [this.study]
        this.studyExist = true;
      })
    },
    setStudy(study) {
      this.study = study;
      this.studyName = study.name
      this.studies = [study];
    },
    getStudies(study) {
      this.studies = []
      axios.get('http://localhost:8000/study-service/studies', {
        params: {
          studyName: study
        }
      }).then(response => {
        const result = response.data;
        this.studies = result.data;

        console.log(this.studies.length)
        this.studyExist = this.studies.length !== 0;
      })
    },

    findStudy(e) {
      this.studyName = e.target.value
    },
    getTestStudies() {
      const studies = []

      axios.get('http://localhost:8000/study-service/studies')
      .then(response => {
        const result = response.data;
        const studies = result.data;
        console.log(studies)

        return studies
      })
      .catch(error => {
        console.log(error);
      })

      return studies
    },
    async getGroups(size, page) {
      const response = await axios.get('http://localhost:8000/group-service/groups', {
        params: {
          size: size,
          page: page,
        }
      });

      return response.data;
    },
    createGroup() {
      let data = {
        name: this.groupName,
        studyId: this.study.studyId,
        groupCategory: this.groupCategory
      }
      axios.post('http://localhost:8000/group-service/groups', data)
      .then(response => {
        const groupId = response.data;

        this.$router.go('/groups/'+groupId)
      })
    },
    getGroupsOrderByMembers(order) {
      const category = this.selectCategory!=='ALL' ? this.selectCategory : null;
      axios.get('http://localhost:8000/group-service/groups', {
        params: {
          page: 0,
          size: 10,
          order: order,
          category: category,
          search: this.search
        }
      })
      .then(response => {
        this.groupData = response.data;
        this.groups = response.data.data
        console.log(this.groupData);
      })
    },
    getGroupsOrderByMade() {
      axios.get('http://localhost:8000/group-service/groups', {
        params: {
          page: 0,
          size: 10,
          search: this.search
        }
      })
          .then(response => {
            this.groupData = response.data;
            this.groups = response.data.data
            console.log(this.groupData);
          })
    }
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500&display=swap');
input {
  z-index: 1;
}
p {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 500;
}
.MyContainer {
  width: 1200px;
}
.room-card {
  border: 1px solid #D9D9D9;
  border-radius: 10px;
  box-shadow: 0 0.25rem 0.5rem rgba(0, 0, 0, 0.075) !important;
}
.room-card:hover {
  background: #D9D9D9;
}
.list-container {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 15px;
  margin-top: 10px;
}
.find-group-study {
}
.page-button {
  position: absolute;
  top: 942px;
  left: 1010px;
  text-align: center;
  align-self: center;
  z-index: 1;
}
.page-letter {
  position: absolute;
  top: 942px;
  left: 970px;
  text-align: center;
  align-self: center;
  z-index: 1;
}

</style>