<template>
  <router-link :to="getUrl" class="btn align-items-start room-card d-flex flex-column">
      <span>{{this.room.name}}</span>
      <span>{{this.room.ticketDtos.length}} / 28</span>
  </router-link>
</template>

<script>
export default {
  name: "StudyRoomInfo",
  props: {
    room: {
      type: Object,
      default: () => {
        return {
          roomId: '',
          name: '',
          description: '',
          ticketDtos: [],
        }
      }
    }
  },
  computed: {
    getUrl() {
      return "/studyrooms/"+this.room.roomId
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
  font-weight: 400;
}
</style>