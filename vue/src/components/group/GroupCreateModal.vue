<template>
  <study-room-modal v-if="showModal">
    <div class="d-flex flex-column justify-content-between">
      <h3>그룹 생성</h3>
      <div class="mt-2 col-4">
        <select class="form-select" aria-label="Default select example" v-model="groupCategory">
          <option value="ALL">분류</option>
          <option value="HIGH">고등학생</option>
          <option value="INFO">대학생</option>
          <option value="CERT">자격증</option>
          <option value="LANG">어학</option>
          <option value="GOV">공무원</option>
          <option value="JOB">취업</option>
        </select>
      </div>
      <div>
        <span>그룹 이름</span>
        <input type="text" class="form-control">
      </div>
      <div>
        <span>스터디</span>
        <div class="d-flex">
          <input type="text" class="form-control" :value="studyName" @input="findStudy">
          <button type="button" class="col-2" @click="getStudies(this.studyName)">검색</button>
        </div>
        <div class="find-group-study d-flex flex-wrap align-items-start gap-1">
          <div class="mt-1" v-for="study in studies" :key="study.studyId">
            <button type="button"
                    class="btn btn-sm btn-outline-secondary"
                    @click="setStudy(study)" >{{ study.name }}</button>
          </div>
          <div v-if="!studyExist">
            <span>해당 스터디가 존재하지 않습니다.</span><br/>
            <span>하고자 하는 스터디를 입력해주세요.</span>
          </div>
        </div>
      </div>
      <div class="d-flex justify-content-end gap-2">
        <button type="button" class="btn btn-sm btn-outline-secondary" @click="showModal = false">취소</button>
        <button type="button" class="btn btn-sm btn-outline-secondary">생성</button>
      </div>
    </div>
  </study-room-modal>
</template>

<script>
import StudyRoomModal from "@/components/studyroom/StudyRoomModal";
import axios from "axios";

export default {
  name: "GroupCreateModal",
  components: {
    StudyRoomModal
  },
  data() {
    return {
      showModal: false,
      studies: null,
      studyName: null,
      study: null,
      studyExist: true,
      groupName: null,
      groupCategory: 'ALL',
    }
  },
  methods: {
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
    createStudy() {

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
  }

}
</script>

<style scoped>

</style>