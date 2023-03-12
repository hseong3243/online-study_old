<template>
  <div class="d-flex flex-fill justify-content-between">
    <div>
      <span ref="seat">{{this.seat}}</span>
    </div>
    <div id="time1">
      <span ref="time">00 : 00</span>
    </div>
  </div>
</template>

<script>
export default {
  name: "StudyTime"
  ,data() {
    return {
      studyTime: null,
    }
  },
  props: ['seat', 'time'],
  mounted() {
  }
}
</script>

<style scoped>

</style>