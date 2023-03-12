<template>
  <div>
    <div class="bg-secondary">
      <div class="HeaderContainer m-auto p-2">
        <span class="text-white fs-3">프로필 변경</span>
      </div>
    </div>
    <div class="m-auto test mt-3 d-flex align-items-start">
        <div>
          <div class="border rounded shadow-sm p-3">
            <h4>그룹 관리</h4>
            <h5>내 그룹</h5>
            <div class="my-groups" v-if="dataReady">
              <div class="border rounded shadow-sm room-card d-flex flex-column p-3" v-for="group in myGroups" :key="group.groupId">
                <div class="d-flex justify-content-between">
                  <span ref="name" class="fs-4 group-name" v-text="group.name"></span>
                  <span ref="size" class="fs-5 col-3">{{ group.members.length }} / {{group.level*56}}</span>
                </div>
                <div class="d-flex">
                  <span class="text-secondary">공부&nbsp;</span>
                  <span ref="study" v-html="group.study.name"></span>
                </div>
                <div class="d-flex">
                  <span class="text-secondary">출석율&nbsp;</span>
                  <span ref="attend">{{attendMap.get(group.groupId)}}%</span>
                </div>
                <div class="d-flex">
                  <span class="text-secondary">생성일&nbsp;</span>
                  <span ref="attend">{{createdMap.get(group.groupId)}}</span>
                </div>
                <div class="d-flex gap-1 justify-content-end">
                  <router-link :to="'/groups/'+group.groupId" class="btn btn-sm btn-outline-secondary">이동</router-link>
                  <button type="button" class="btn btn-outline-secondary btn-sm" @click="exitGroup">그룹 삭제</button>
                </div>
              </div>
            </div>
            <h5 class="mt-1">소속된 그룹</h5>
            <div class="my-groups" v-if="dataReady">
              <div class="border rounded shadow-sm room-card d-flex flex-column p-3" v-for="group in othersGroups" :key="group.groupId">
                <div class="d-flex justify-content-between">
                  <span ref="name" class="fs-4" v-html="group.name"></span>
                  <span ref="size" class="fs-5">{{ group.members.length }} / {{group.level*56}}</span>
                </div>
                <div class="d-flex">
                  <span class="text-secondary">공부&nbsp;</span>
                  <span ref="study" v-html="group.study.name"></span>
                </div>
                <div class="d-flex">
                  <span class="text-secondary">출석율&nbsp;</span>
                  <span ref="attend">{{attendMap.get(group.groupId)}}%</span>
                </div>
                <div class="d-flex">
                  <span class="text-secondary">생성일&nbsp;</span>
                  <span ref="attend">{{createdMap.get(group.groupId)}}</span>
                </div>
                <div class="d-flex">
                  <span class="text-secondary">가입일&nbsp;</span>
                  <span ref="attend">{{joinedMap.get(group.groupId)}}</span>
                </div>
                <div class="d-flex gap-1 justify-content-end">
                  <router-link :to="'/groups/'+group.groupId" class="btn btn-sm btn-outline-secondary">이동</router-link>
                  <button type="button" class="btn btn-sm btn-outline-secondary" @click="exitGroup(group)">탈퇴</button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="grid-container">
          <div class="border rounded shadow-sm p-3">
            <div>
            <h4>닉네임</h4>
            <input type="text" class="form-control" v-model="nickname">
              <span>조건: 7자 이하</span>
            </div>
            <button type="button" class="float-end mt-2 btn btn-outline-secondary" @click="changeNickname">변경하기</button>
          </div>
          <div class="border rounded shadow-sm p-3 d-flex flex-column justify-content-between">
            <div>
            <h4>회원 카테고리</h4>
            <select class="form-select" aria-label="Default select example" v-model="member.studyCategory" v-if="dataReady">
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
            <button type="button" class="float-end mt-2 btn btn-outline-secondary align-self-end" @click="changeCategory">변경하기</button>
          </div>
          <div class="border rounded shadow-sm p-3">
            <h4>비밀번호 변경</h4>
            <div v-if="!showChangePassword">
              <span>현재 비밀번호</span>
              <input type="password" class="form-control" v-model="password"/>
            </div>
            <div v-if="showChangePassword">
              <span>비밀번호</span>
              <input type="password" class="form-control" v-model="updatePassword"/>
              <span>비밀번호 확인</span>
              <input type="password" class="form-control" v-model="updatePasswordCheck"/>
            </div>
            <button type="button" class="mt-3 float-end btn btn-outline-secondary" @click="checkPassword" v-if="!showChangePassword">변경하기</button>
            <div class="mt-3 float-end d-flex gap-2" v-if="showChangePassword">
              <button type="button" class="btn btn-outline-secondary" @click="cancelChangePassword" >취소</button>
              <button type="button" class="btn btn-outline-secondary" @click="changePassword" >변경</button>
            </div>
          </div>
          <div class="border rounded shadow-sm p-3 d-flex flex-column justify-content-between">
            <h4>탈퇴</h4>
            <button type="button" class="float-end btn btn-outline-secondary align-self-end">탈퇴하기</button>
          </div>
        </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import {useTokenStore} from "@/store/token";
export default {
  name: "myTest",
  components: {
  },
  setup() {
    const store = useTokenStore();
    return {
      store,
    }
  },
  data() {
    return {
      member: null,
      nickname: null,
      dataReady: false,
      password: null,
      showChangePassword: false,
      updatePassword: null,
      updatePasswordCheck: null,
      attendMap: null,
      createdMap: null,
      myGroups: [],
      othersGroups: [],
      joinedMap: null,
    }
  },
  mounted() {
    if(this.store.getMemberId == null) {
      alert('로그인 후 이용 가능합니다.')
      this.$router.push('/')
    }

    axios.get('http://localhost:8000/member-service/members/'+this.store.getMemberId)
    .then(response => {
      this.member = response.data;

      this.nickname = this.member.name
      console.log(this.member)

      const groups = this.member.groups;

      this.attendMap = new Map();
      this.createdMap = new Map();
      this.joinedMap = new Map();

      groups.forEach(group => {
        if(group.memberId === this.store.getMemberId){
          this.myGroups.push(group)
        }
        else
          this.othersGroups.push(group)

        const createdDay = this.getCreatedDay(group.createdAt);
        const number = this.findAttendance(group);
        this.attendMap.set(group.groupId, number);
        this.createdMap.set(group.groupId, createdDay);
        const member = group.members.filter(member => member.memberId===this.store.getMemberId);
        this.joinedMap.set(group.groupId, member[0].joinedAt)

      })


      this.dataReady = true;
    })
  },
  methods: {
    changeNickname() {
      if(this.nickname.length>7){
        this.$router.go()
        alert('닉네임은 7자 이하여야 합니다.')
      }
      let data = {
        name: this.nickname
      }
      axios.post('http://localhost:8000/member-service/members/'+this.member.memberId, data)
      .then(() => {
        this.$router.go()
      })
    },
    changeCategory() {
      let data = {
        studyCategory: this.member.studyCategory
      }
      axios.post('http://localhost:8000/member-service/members/'+this.member.memberId, data)
          .then(() => {
            this.$router.go()
          })
    },
    checkPassword() {
      let data = {
        password: this.password
      }
      axios.post('http://localhost:8000/member-service/members/'+this.member.memberId+'/check', data)
          .then(() => {
            this.showChangePassword = true
          })
      .catch(() => {
        alert("비밀번호가 일치하지 않습니다!")
      })
    },
    changePassword() {
      if(this.updatePassword !== this.updatePasswordCheck)
        alert("동일한 비밀번호를 입력해주세요.")
      let data = {
        updatePassword: this.updatePassword,
        updatePasswordCheck: this.updatePasswordCheck
      }
      axios.post('http://localhost:8000/member-service/members/'+this.member.memberId, data)
          .then(() => {
            alert("비밀번호가 변경되었습니다.")
            this.showChangePassword = false
            this.$router.go()
          })
          .catch(() => {
            alert("비밀번호가 일치하지 않습니다!")
          })
    },
    cancelChangePassword() {
      this.password = null;
      this.showChangePassword=false;
    },
    exitGroup(group) {
      axios.delete('http://localhost:8000/group-service/groups/'+group.groupId+'/members')
      .then(() => {
        alert(group.name+'그룹을 탈퇴했습니다.')
      })
      .catch(() => {
        alert('잠시 후 다시 시도해주세요.')
      })
    },
    findAttendance(group) {
      const time = Date.now();
      const now = new Date(time);
      const month1 = now.getMonth();
      const date1 = now.getDate();
      const nowHours = now.getHours();
      const membersSize = group.members.length;
      const attendedMember = []

      group.members.forEach(member => {
        const attend = new Date(member.attend * 1000);
        const month2 = attend.getMonth();
        const date2 = attend.getDate();
        const attendHours = attend.getHours();


        if (nowHours < 5 && attendHours < 5) {
          if (month1 === month2 && date1 === date2)
            attendedMember.push(member)
        } else if (nowHours < 5 && attendHours >= 5) {
          if (month1 === month2 && date1 - 1 === date2)
            attendedMember.push(member)
        } else if (nowHours >= 5 && attendHours >= 5) {
          if (month1 === month2 && date1 === date2)
            attendedMember.push(member)
        }
      })

      return Math.floor(attendedMember.length / membersSize * 100);
    },
    getCreatedDay(createdAt) {
      const date = new Date(0);
      date.setUTCSeconds(createdAt)

      const fullYear = date.getFullYear();
      const month = date.getMonth()+1;
      const day = date.getDate();

      return fullYear + '-' + month + '-' + day;
    }
  }
}


</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500&display=swap');
span {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 300;
}
.room-card {
  width: 280px;
}
.intro-card {
  color: black;
  text-align: left;
}
.btn-tag {
  font-size: 12px;
  padding: 5px 5px;
}
.test {
  width: 1100px;
  display: grid;
  grid-template-columns: 1fr;
  padding: 10px;
  gap: 10px;
}
.my-groups{
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}
.group-name {
  display: inline-block;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.grid-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px
}
.HeaderContainer {
  width: 1100px;
}
</style>