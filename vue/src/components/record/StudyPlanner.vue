<template>
  <div class="d-flex p-2">
    <table class="table table-sm table-bordered shadow-sm bg-white mb-0">
      <tbody>
      <tr ref="5">
        <th>5</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="6">
        <th>6</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="7">
        <th>7</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="8">
        <th>8</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="9">
        <th>9</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="10">
        <th>10</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="11">
        <th>11</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="12">
        <th>12</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="13">
        <th>13</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="14">
        <th>14</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="15">
        <th>15</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="16">
        <th>16</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="17">
        <th>17</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="18">
        <th>18</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="19">
        <th>19</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="20">
        <th>20</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="21">
        <th>21</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="22">
        <th>22</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="23">
        <th>23</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="24">
        <th>24</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="25">
        <th>1</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="26">
        <th>2</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="27">
        <th>3</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      <tr ref="28">
        <th>4</th>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
      </tbody>
    </table>
  </div>

</template>

<script>
export default {
  name: "StudyPlanner",
  props: ['memberData', 'colors'],
  data() {
    return {
      studies: null,
    }
  },
  mounted() {
    const studyColors = new Map();
    const studies = this.memberData.studies;
    for(const i in studies) {
      studyColors.set(studies[i].studyId, this.colors[i]);
    }
    const memberRecords = this.memberData.records;
    new Map();
    memberRecords.forEach(record => {
      if(record.studyStatus==='STUDY') {
        const startTime = this.getTime(record.startTime);
        const endTime = this.getTime(record.endTime);

        const startMin = Math.ceil(startTime[1] / 10);
        console.log(startTime, ' ', endTime, ' ', startMin)
        let children = this.$refs[startTime[0]].children;



        if(startTime[0] !== endTime[0]) {
          for (let j = startMin; j <= 6; j++) {
            children[j].style.backgroundColor = studyColors.get(record.studyId)
          }
        }
        for (let i = startTime[0] + 1; i < endTime[0]; i++) {
          children = this.$refs[i].children;
          for (let j = 1; j <= 6; j++) {
            if (children[j].tagName !== 'TH') {
              children[j].style.backgroundColor = studyColors.get(record.studyId)
            }
          }
        }
        const endMin = Math.round(endTime[1] / 10);
        children = this.$refs[endTime[0]].children;
        if(startTime[0] !== endTime[0]) {
          for (let j = 1; j <= endMin; j++) {
            children[j].style.backgroundColor = studyColors.get(record.studyId)
          }
        }

        children = this.$refs[endTime[0]].children;
        if(startTime[0] === endTime[0]) {
          for(let j =startMin; j<=endMin; j++) {
            children[j].style.backgroundColor = studyColors.get(record.studyId)
          }
            }
      }

    })
  },
  methods: {
    randomColor() {
      return '#' + Math.round(Math.random() * 0xffffff).toString(16);
    },
    changeColor() {
      const el = document.getElementById('7').children;
      for (const i of el) {
        if(i.tagName !== 'TH') {
          i.setAttribute('class', 'table-success')
        }
      }
    },
    getTime(time) {
      const hours = Math.floor(time/3600);
      const min = Math.floor(time%3600/60)
      return [hours, min];
    },
    getTest(time) {
      console.log(time, ' ',Math.floor(time/3600), ' ',60-time%60)
    }
  },
  watch: {
    colors() {
      const records = this.memberData.records;
      records.forEach(record => {
        const startTime = this.getTime(record.startTime);
        const endTime = this.getTime(record.endTime);
        const startMin = Math.ceil(startTime[1]/10);
        let children = this.$refs[startTime[0]].children;
        for(let j=startMin; j<=6; j++) {
          children[j].style.backgroundColor = this.colors.hex
        }
        for(let i=startTime[0]+1; i<endTime[0]; i++) {
          children = this.$refs[i].children;
          for(let j=1; j<=6; j++) {
            if(children[j].tagName !== 'TH') {
              children[j].style.backgroundColor = this.colors.hex
            }
          }
        }
        const endMin = Math.round(endTime[1]/10);
        children = this.$refs[endTime[0]].children;
        for(let j=1; j<=endMin; j++) {
          children[j].style.backgroundColor = this.colors.hex
        }
      })

    }
  }
}
</script>

<style scoped>
th {
  font-size: 14px;
}
</style>