<template>
  <div class="d-flex">
    <canvas id="day-bar-chart" width="400"></canvas>
  </div>
</template>

<script>
import Chart from 'chart.js'
import dayBarData from "@/linechart/day-bar-data";

export default {
  name: "DayChart",
  data() {
    return {
      dayBarData
    }
  },
  mounted() {
    const ctx = document.getElementById('day-bar-chart');
    new Chart(ctx, this.dayBarData);
  }
}
</script>

<style scoped>

</style>