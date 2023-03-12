<template>
  <div class="StudyRoomRight d-flex flex-column ps-1">
    <div class="StudyRoomBoard d-flex flex-fill mb-1">
      <StudyRoomMyInfo :ticket="this.ticket" :seat="seat" :room-id="id" :group-study="groupStudy" @send="send" v-if="this.ticket"/>
      <div class="Board border border-2 flex-fill p-2 d-flex flex-column">
        <div class="col d-flex flex-column justify-content-center">
        <p class="text-center">현재 공부방</p>
        <span class="text-center" v-if="dataReady">{{room.name}}</span>
        </div>
        <div class="col d-flex flex-column justify-content-center">
        <p class="text-center">나의 공부방</p>
        <span class="text-center" v-if="dataReady">{{ticket.roomName}}</span>
          <router-link :to="getUrl"></router-link>
        </div>
      </div>
      <StudyRoomAnoterInfo :ticket="this.anotherTicket"/>
    </div>
    <div class="StudyRoom d-flex flex-column border border-2 mt-1 justify-content-between">
      <div class="d-flex justify-content-between">
        <div class="d-flex">
          <div class="Desk card" id="desk1">
            <div class="d-flex flex-fill justify-content-between">
              <div>
                <span>1</span>
              </div>
              <div id="time1">
                <span></span>
              </div>
            </div>
            <div class="d-flex justify-content-center" id="icon1">
            </div>
            <div id="username1" class="text-center">
              <span class="username"> </span>
            </div>
            <a href="#" class="stretched-link" id="1" @click="clickSeat(1)"></a>
          </div>
          <div class="Desk card" id="desk2">
            <div class="d-flex flex-fill justify-content-between">
              <div>
                <span>2</span>
              </div>
              <div id="time2">
                <span ref="time2"></span>
              </div>
            </div>
            <div class="d-flex justify-content-center" id="icon2">
            </div>
            <div id="username2" class="text-center">
              <span class="username"> </span>
            </div>
            <a href="#" class="stretched-link" id="2" @click="clickSeat(2)"></a>
          </div>
          <div class="Desk card" id="desk3">
            <div class="d-flex flex-fill justify-content-between">
              <div>
                <span>3</span>
              </div>
              <div id="time3">
                <span ref="time3"></span>
              </div>
            </div>
            <div class="d-flex justify-content-center" id="icon3">
            </div>
            <div id="username3"  class="text-center">
              <span> </span>
            </div>
            <a href="#" class="stretched-link" id="3" @click="clickSeat(3)"></a>
          </div>
          <div class="Desk card" id="desk4">
            <div class="d-flex flex-fill justify-content-between">
              <div>
                <span>4</span>
              </div>
              <div id="time4">
                <span ref="time4"></span>
              </div>
            </div>
            <div class="d-flex justify-content-center" id="icon4">
            </div>
            <div id="username4"  class="text-center">
              <span class="username"> </span>
            </div>
            <a href="#" class="stretched-link" id="4" @click="clickSeat(4)"></a>
          </div>
        </div>
        <div class="d-flex">
          <div class="Desk card" id="desk5">
            <div class="d-flex flex-fill justify-content-between">
              <div>
                <span>5</span>
              </div>
              <div id="time5">
                <span ref="time5"></span>
              </div>
            </div>
            <div class="d-flex justify-content-center" id="icon5">
            </div>
            <div id="username5"  class="text-center">
              <span class="username"> </span>
            </div>
            <a href="#" class="stretched-link" id="5" @click="clickSeat(5)"></a>
          </div>
          <div class="Desk card" id="desk6">
            <div class="d-flex flex-fill justify-content-between">
              <div>
                <span>6</span>
              </div>
              <div id="time6">
                <span ref="time6"></span>
              </div>
            </div>
            <div class="d-flex justify-content-center" id="icon6">
            </div>
            <div id="username6"  class="text-center">
              <span class="username"> </span>
            </div>
            <a href="#" class="stretched-link" id="6" @click="clickSeat(6)"></a>
          </div>
          <div class="Desk card" id="desk7">
            <div class="d-flex flex-fill justify-content-between">
              <div>
                <span>7</span>
              </div>
              <div id="time7">
                <span ref="time7"></span>
              </div>
            </div>
            <div class="d-flex justify-content-center" id="icon7">
            </div>
            <div id="username7" class="text-center">
              <span class="username"> </span>
            </div>
            <a href="#" class="stretched-link" id="7" @click="clickSeat(7)"></a>
          </div>
          <div class="Desk card" id="desk8">
            <div class="d-flex flex-fill justify-content-between">
              <div>
                <span>8</span>
              </div>
              <div id="time8">
                <span ref="time8"></span>
              </div>
            </div>
            <div class="d-flex justify-content-center" id="icon8">
            </div>
            <div id="username8" class="text-center">
              <span class="username"> </span>
            </div>
            <a href="#" class="stretched-link" id="8" @click="clickSeat(8)"></a>
          </div>
        </div>
      </div>
      <div>
        <div class="d-flex justify-content-evenly">
          <div class="d-flex">
            <div class="Desk2 card" id="desk9">
              <div class="d-flex flex-fill justify-content-between">
                <div>
                  <span>9</span>
                </div>
                <div id="time9">
                  <span ref="time9"></span>
                </div>
              </div>
              <div class="d-flex justify-content-center" id="icon9">
              </div>
              <div id="username9" class="text-center">
                <span class="username"> </span>
              </div>
              <a href="#" class="stretched-link" id="9" @click="clickSeat(9)"></a>
            </div>
            <div class="Desk2 card" id="desk10">
              <div class="d-flex flex-fill justify-content-between">
                <div>
                  <span>10</span>
                </div>
                <div id="time10">
                  <span ref="time10"></span>
                </div>
              </div>
              <div class="d-flex justify-content-center" id="icon10">
              </div>
              <div id="username10" class="text-center">
                <span class="username"> </span>
              </div>
              <a href="#" class="stretched-link" id="10" @click="clickSeat(10)"></a>
            </div>
            <div class="Desk2 card" id="desk11">
              <div class="d-flex flex-fill justify-content-between">
                <div>
                  <span>11</span>
                </div>
                <div id="time11">
                  <span ref="time11"></span>
                </div>
              </div>
              <div class="d-flex justify-content-center" id="icon11">
              </div>
              <div id="username11" class="text-center">
                <span class="username"> </span>
              </div>
              <a href="#" class="stretched-link" id="11" @click="clickSeat(11)"></a>
            </div>
          </div>
          <div class="d-flex">
            <div class="Desk2 card" id="desk12">
              <div class="d-flex flex-fill justify-content-between">
                <div>
                  <span>12</span>
                </div>
                <div id="time12">
                  <span ref="time12"></span>
                </div>
              </div>
              <div class="d-flex justify-content-center" id="icon12">
              </div>
              <div id="username12" class="text-center">
                <span class="username"> </span>
              </div>
              <a href="#" class="stretched-link" id="12" @click="clickSeat(12)"></a>
            </div>
            <div class="Desk2 card" id="desk13">
              <div class="d-flex flex-fill justify-content-between">
                <div>
                  <span>13</span>
                </div>
                <div id="time13">
                  <span ref="time13"></span>
                </div>
              </div>
              <div class="d-flex justify-content-center" id="icon13">
              </div>
              <div id="username13" class="text-center">
                <span class="username"> </span>
              </div>
              <a href="#" class="stretched-link" id="13" @click="clickSeat(13)"></a>
            </div>
            <div class="Desk2 card" id="desk14">
              <div class="d-flex flex-fill justify-content-between">
                <div>
                  <span>14</span>
                </div>
                <div id="time14">
                  <span ref="time14"></span>
                </div>
              </div>
              <div class="d-flex justify-content-center" id="icon14">
              </div>
              <div id="username14" class="text-center">
                <span class="username"> </span>
              </div>
              <a href="#" class="stretched-link" id="14" @click="clickSeat(14)"></a>
            </div>
          </div>
        </div>
        <div class="d-flex justify-content-evenly">
          <div class="d-flex">
            <div class="Desk card" id="desk15">
              <div class="d-flex flex-fill justify-content-between">
                <div>
                  <span>15</span>
                </div>
                <div id="time15">
                  <span ref="time15"></span>
                </div>
              </div>
              <div class="d-flex justify-content-center" id="icon15">
              </div>
              <div id="username15" class="text-center">
                <span class="username"> </span>
              </div>
              <a href="#" class="stretched-link" id="15" @click="clickSeat(15)"></a>
            </div>
            <div class="Desk card" id="desk16">
              <div class="d-flex flex-fill justify-content-between">
                <div>
                  <span>16</span>
                </div>
                <div id="time16">
                  <span ref="time16"></span>
                </div>
              </div>
              <div class="d-flex justify-content-center" id="icon16">
              </div>
              <div id="username16" class="text-center">
                <span class="username"> </span>
              </div>
              <a href="#" class="stretched-link" id="16" @click="clickSeat(16)"></a>
            </div>
            <div class="Desk card" id="desk17">
              <div class="d-flex flex-fill justify-content-between">
                <div>
                  <span>17</span>
                </div>
                <div id="time17">
                  <span ref="time17"></span>
                </div>
              </div>
              <div class="d-flex justify-content-center" id="icon17">
              </div>
              <div id="username17" class="text-center">
                <span class="username"> </span>
              </div>
              <a href="#" class="stretched-link" id="17" @click="clickSeat(17)"></a>
            </div>
          </div>
          <div class="d-flex">
            <div class="Desk card" id="desk18">
              <div class="d-flex flex-fill justify-content-between">
                <div>
                  <span>18</span>
                </div>
                <div id="time18">
                  <span ref="time18"></span>
                </div>
              </div>
              <div class="d-flex justify-content-center" id="icon18">
              </div>
              <div id="username18" class="text-center">
                <span class="username"> </span>
              </div>
              <a href="#" class="stretched-link" id="18" @click="clickSeat(18)"></a>
            </div>
            <div class="Desk card" id="desk19">
              <div class="d-flex flex-fill justify-content-between">
                <div>
                  <span>19</span>
                </div>
                <div id="time19">
                  <span ref="time19"></span>
                </div>
              </div>
              <div class="d-flex justify-content-center" id="icon19">
              </div>
              <div id="username19" class="text-center">
                <span class="username"> </span>
              </div>
              <a href="#" class="stretched-link" id="19" @click="clickSeat(19)"></a>
            </div>
            <div class="Desk card" id="desk20">
              <div class="d-flex flex-fill justify-content-between">
                <div>
                  <span>20</span>
                </div>
                <div id="time20">
                  <span ref="time20"></span>
                </div>
              </div>
              <div class="d-flex justify-content-center" id="icon20">
              </div>
              <div id="username20" class="text-center">
                <span class="username"> </span>
              </div>
              <a href="#" class="stretched-link" id="20" @click="clickSeat(20)"></a>
            </div>
          </div>
        </div>
      </div>
      <div class="d-flex justify-content-between">
        <div class="d-flex">
          <div class="Desk2 card" id="Desk21">
            <div class="d-flex flex-fill justify-content-between">
              <div>
                <span>21</span>
              </div>
              <div id="time21">
                <span ref="time21"></span>
              </div>
            </div>
            <div class="d-flex justify-content-center" id="icon21">
            </div>
            <div id="username21" class="text-center">
              <span class="username"> </span>
            </div>
            <a href="#" class="stretched-link" id="21" @click="clickSeat(21)"></a>
          </div>
          <div class="Desk2 card" id="desk22">
            <div class="d-flex flex-fill justify-content-between">
              <div>
                <span>22</span>
              </div>
              <div id="time22">
                <span ref="time22"></span>
              </div>
            </div>
            <div class="d-flex justify-content-center" id="icon22">
            </div>
            <div id="username22" class="text-center">
              <span class="username"> </span>
            </div>
            <a href="#" class="stretched-link" id="22" @click="clickSeat(22)"></a>
          </div>
          <div class="Desk2 card" id="desk23">
            <div class="d-flex flex-fill justify-content-between">
              <div>
                <span>23</span>
              </div>
              <div id="time23">
                <span ref="time23"></span>
              </div>
            </div>
            <div class="d-flex justify-content-center" id="icon23">
            </div>
            <div id="username23" class="text-center">
              <span class="username"> </span>
            </div>
            <a href="#" class="stretched-link" id="23"  @click="clickSeat(23)"></a>
          </div>
          <div class="Desk2 card" id="desk24">
            <div class="d-flex flex-fill justify-content-between">
              <div>
                <span>24</span>
              </div>
              <div id="time24">
                <span ref="time24"></span>
              </div>
            </div>
            <div class="d-flex justify-content-center" id="icon24">
            </div>
            <div id="username24" class="text-center">
              <span class="username"> </span>
            </div>
            <a href="#" class="stretched-link" id="24" @click="clickSeat(24)"></a>
          </div>
        </div>
        <div class="d-flex">
          <div class="Desk2 card" id="desk25">
            <div class="d-flex flex-fill justify-content-between">
              <div>
                <span>25</span>
              </div>
              <div id="time25">
                <span ref="time25"></span>
              </div>
            </div>
            <div class="d-flex justify-content-center" id="icon25">
            </div>
            <div id="username25" class="text-center">
              <span class="username"> </span>
            </div>
            <a href="#" class="stretched-link" id="25" @click="clickSeat(25)"></a>
          </div>
          <div class="Desk2 card" id="desk26">
            <div class="d-flex flex-fill justify-content-between">
              <div>
                <span>26</span>
              </div>
              <div id="time26">
                <span ref="time26"></span>
              </div>
            </div>
            <div class="d-flex justify-content-center" id="icon26">
            </div>
            <div id="username26" class="text-center">
              <span class="username"> </span>
            </div>
            <a href="#" class="stretched-link" id="26" @click="clickSeat(26)"></a>
          </div>
          <div class="Desk2 card" id="desk27">
            <div class="d-flex flex-fill justify-content-between">
              <div>
                <span>27</span>
              </div>
              <div id="time27">
                <span ref="time27"></span>
              </div>
            </div>
            <div class="d-flex justify-content-center" id="icon27">
            </div>
            <div id="username27" class="text-center">
              <span class="username"> </span>
            </div>
            <a href="#" class="stretched-link" id="27" @click="clickSeat(27)"></a>
          </div>
          <div class="Desk2 card" id="desk28">
            <div class="d-flex flex-fill justify-content-between">
              <div>
                <span>28</span>
              </div>
              <div id="time28">
                <span ref="time28"></span>
              </div>
            </div>
            <div class="d-flex justify-content-center" id="icon28">
            </div>
            <div id="username28" class="text-center">
              <span class="username"> </span>
            </div>
            <a href="#" class="stretched-link" id="28" @click="clickSeat(28)"></a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import StudyRoomMyInfo from "@/components/studyroom/StudyRoomMyInfo";
import Stomp from 'webstomp-client';
import SockJS from 'sockjs-client';
import study_icon from "@/assets/img/study_icon.png";
import rest_icon from "@/assets/img/rest_icon.png";
import StudyRoomAnoterInfo from "@/components/studyroom/StudyRoomAnoterInfo";
import {useTokenStore} from "@/store/token";
// import {useTokenStore} from "@/store/token";

export default {
  name: "StudyRoom",
  data() {
    return {
      modal: false,
      ticket:'',
      tickets:'',
      anotherTicket:'',

      seat: null,
      room: null,
      dataReady: false,

      seats: [],
    }
  },
  created() {
    this.connect()
  },
  components: {
    StudyRoomMyInfo,
    StudyRoomAnoterInfo,
  },
  props: ['id', 'groupStudy'],
  computed: {
    getStudyImg() {
      return study_icon;
    },
    getRestImg() {
      return rest_icon;
    }
  },
  setup() {
    const tokenStore = useTokenStore();
    return {
      tokenStore
    }
  },

  mounted() {

    this.seats = []
    this.seats.length = 29

    // const store = useTokenStore();
    // let memberId = store.memberId;
    this.tickets = [];
    // 자신의 유효한 티켓 정보를 요청
    axios.get("http://localhost:8000/study-service/tickets/"+this.tokenStore.getMemberId+"/valid")
        .then((response) => {
          this.ticket = response.data;
        })
        .catch((error) => {
          console.log(error);
          this.ticket = null;
        })
    // 해당 룸 정보를 요청(다른 유저들의 티켓 정보를 포함하고 있음)
    axios.get("http://localhost:8000/study-service/rooms/"+this.id)
    .then((response) => {
      const result = response.data;
      const tickets = result.ticketDtos;

      for (const i in tickets) {
        const seat = tickets[i].seat;
        this.tickets[seat-1] = tickets[i];


        const name = document.getElementById('username' + tickets[i].seat).firstChild;
        name.textContent = tickets[i].memberName;

        const time = document.getElementById('time' + tickets[i].seat).firstChild;


        if(tickets[i].studyStatus === 'STUDY') {
          const studyTime = tickets[i].studyTime + (Date.now()/1000) - tickets[i].startTime;
          time.textContent = this.getStudyTime(studyTime);
          this.seats[seat] = setInterval(() => {
            const studyTime = tickets[i].studyTime + (Date.now() / 1000) - tickets[i].startTime;
            time.textContent = this.getStudyTime(studyTime);
          }, 1000)
        }
        else if(tickets[i].studyStatus === 'REST') {
          const studyTime = tickets[i].studyTime
          time.textContent = this.getStudyTime(studyTime);
        }

        const icon = document.getElementById("icon" + tickets[i].seat);
        icon.innerHTML = '';
        const element = document.createElement('img');
        if(tickets[i].studyStatus === "STUDY") {
          element.setAttribute('src', require('@/assets/img/study_icon_update.png'))
        } else if(tickets[i].studyStatus === "REST") {
          element.setAttribute('src', require('@/assets/img/rest_icon.png'))
        }
        icon.append(element);
        console.log(this.ticket)
      }

    })

    this.getRoom()

  },
  watch: {
    id() {
      this.$router.go();
    }
  },
  methods: {
    connect() {
      const serverURL = "http://localhost:8000/study-service/ws"
      let socket = new SockJS(serverURL)
      this.ws = Stomp.over(socket);
      this.ws.connect({}, (() => {
          this.ws.subscribe("/topic/room/" + this.id, (message => {
            let data = JSON.parse(message.body);
              if(data.ticketStatus === "VALID") {
                this.tickets[data.seat-1] = data

                const username = document.getElementById('username'+data.seat).firstChild
                username.textContent=data.memberName
                const icon = document.getElementById("icon" + data.seat)
                const element = document.createElement('img')
                const time = document.getElementById("time"+data.seat).firstChild

                if(data.studyStatus === "STUDY") {
                  const studyTime = data.studyTime + (Date.now()/1000) - data.startTime
                  time.textContent = this.getStudyTime(studyTime)

                  this.seats[data.seat] = setInterval(() => {
                    const studyTime = data.studyTime + (Date.now()/1000) - data.startTime
                    time.textContent = this.getStudyTime(studyTime)
                  }, 20000)

                  element.setAttribute('src', require('@/assets/img/study_icon_update.png'))
                } else if(data.studyStatus === "REST") {
                    const studyTime = data.studyTime
                    time.textContent = this.getStudyTime(studyTime);
                    element.setAttribute('src', require('@/assets/img/rest_icon.png'))
                }
                icon.innerHTML = '';
                icon.append(element)
              } else if(data.ticketStatus === "EXPIRE") {
                this.tickets.splice(data.seat-1, 1);
                const username = document.getElementById("username"+data.seat).firstChild;
                username.textContent = ''
                const time = document.getElementById("time"+data.seat).firstChild;
                clearInterval(this.seats[data.seat])
                time.textContent=''
                const icon = document.getElementById("icon"+data.seat);
                icon.innerHTML = '';
                console.log(icon)
              }

            console.log(data);
          }))

      }))
    },
    send(ticketId) {
      this.ws.send('/pub/update', JSON.stringify({
        roomId: this.id,
        ticketId: ticketId
      }), {})
    },
    closeModal() {
      this.modal = false;
    },

    updateTicket(studyStatus) {
      axios.post("http://localhost:8000/study-service/tickets/"+this.ticket.ticketId, studyStatus)
        .then(response => {
          console.log(response);
          this.send();
        })
    },
    expireTicket() {
      axios.post("http://localhost:8000/study-service/tickets/"+this.ticket.ticketId + "/expire")
          .then(response => {
            console.log(response);
            this.send();
          })
    },

    changeIcon() {
      const ticket = this.tickets[this.updatedTicketIndex]
      const icon = document.getElementById("icon" + ticket.seat);
      const element = icon.firstChild
      if(ticket.ticketStatus === "STUDY") {
        element.setAttribute('src', require('@/assets/img/study_icon_update.png'))
      } else if(ticket.ticketStatus === "REST") {
        element.setAttribute('src', require('@/assets/img/rest_icon.png'))
      }
    },
    openModal() {
      this.modal = true;
    },
    getTimeInterval(studyTime) {
      this.getStudyTime(studyTime)
    },
    getStudyTime(studyTime) {
      let h = Math.floor(studyTime/3600);
      let m = Math.floor(studyTime%3600/60)
      let s = Math.floor(studyTime - (h*3600) - (m*60));

      if(s>=60) {
        m++;
      }
      if(m>=60) {
        h++
      }

      h = h<10 ? '0'+h : h;
      m = m<10 ? '0'+m : m;

      return h +':' +m;

    },
    clickSeat(seat) {
      if(this.tickets[seat-1] !== undefined) {
        this.anotherTicket = this.tickets[seat - 1];
      }
      else {
        this.seat = seat
      }
    },
    getAnotherTicketInfo(i) {
      return this.tickets[i];
    },
    getRoom() {
      axios.get('http://localhost:8000/study-service/rooms/'+this.id)
      .then(response => {
        this.room = response.data;
        this.dataReady = true;
      })
    }
    ,getUrl() {
    }
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@400;500&display=swap');
span {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 500;
}
p {
  font-family: 'Noto Sans KR', sans-serif;
  font-weight: 300;
}
.StudyRoomRight {
  width: 900px;
}
.StudyRoom {
  height: 537px;
}
.Board {
  width: 293px;
  height: 190px;
}
.BoardTitle {
  font-style: normal;
  font-weight: 400;
  font-size: 14px;
  line-height: 16px;
  color: #575757;
}
.btn-bread {
  background-color: #F6E2BD;
  border-radius: 10px;
}
.Desk {
  width: 100px;
  height: 115px;
  padding: 5px;

  border-width: 3px 3px 1px 3px;
  border-style: solid;
  border-color: #000000;

  display: flex;
  flex-direction: column;

}
.Desk2 {
  width: 100px;
  height: 115px;
  padding: 5px;

  border-width: 1px 3px 3px 3px;
  border-style: solid;
  border-color: #000000;

  display: flex;
  flex-direction: column;
}
.username {
  white-space: nowrap;
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>