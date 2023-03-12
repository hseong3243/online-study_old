import {createWebHistory, createRouter} from 'vue-router'
import StudyRoomList from "@/components/StudyRoomList";
import MyLogin from "@/components/main/MyLogin";
import MySignUp from "@/components/main/MySignUp";
import myTest from  "@/components/myTest";
import StudyRoom from "@/components/StudyRoom";
import MyStudyRecord from "@/components/MyStudyRecord";
import MyMain from "@/components/MyMain";
import MyGroup from "@/components/MyGroup";
import StudyGroupRoomList from "@/components/StudyGroupRoomList";
import GroupList from "@/components/group/GroupList";
import GroupDetail from "@/components/group/GroupDetail";

const routes = [
    { path: "/", name: "main", component: MyMain},
    { path: "/login", name: "login", component: MyLogin},
    { path: "/signup", name: "signup", component: MySignUp},
    { path: "/studyrooms", name: "공부빵 목록", component: StudyRoomList, children: [
            {path: "", name:'roomList', component: StudyGroupRoomList},
            {path: ":id", name:'room', component:StudyRoom, props: true}
        ]},
    { path: '/groups', name: 'groups', component: MyGroup ,children: [
            {path: '', name:'groupList', component: GroupList},
            {path: ':groupId', name:'group', component: GroupDetail, props: true, children: [
                    {path: ':roomId', component: StudyRoom, props: true}
                ]},

        ]},
    { path: "/records", name: "집계", component: MyStudyRecord},
    { path: "/test", name: "test", component: myTest}
]
const router = createRouter({
    history: createWebHistory(),
    routes: routes
});


export default router;