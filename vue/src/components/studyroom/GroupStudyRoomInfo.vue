<template>
  <router-link :to="getUrl" class="btn room-card d-flex flex-column gap-1 pt-2 pb-2">
    <div class="d-flex justify-content-between">
      <span>{{room.name}}</span>
    </div>
    <div class="d-flex">
      <span class="text-secondary">공부&nbsp;</span>
      <span>{{ groupStudyMap.get(room.groupId).name }}</span>
    </div>
    <div class="d-flex">
      <span class="text-secondary">인원&nbsp;</span>
      <span>{{ room.count }}&nbsp;/&nbsp;28</span>
    </div>
  </router-link>
</template>

<script>
export default {
  name: "GroupStudyRoomInfo",
  props: ['room', 'groupStudyMap'],
  computed: {
    getUrl() {
      return "/studyrooms/" + this.room.roomId
    }
  },
  mounted() {
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500&display=swap');
span {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 500;
}
</style>