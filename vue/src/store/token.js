import { defineStore } from 'pinia'

export const useTokenStore = defineStore('token', {
    state: () => {
        return {
            token: null,
            memberId: null,
            memberName: null,
        }
    },
    persist: true,
    getters: {
        getToken() {
            return this.token;
        },
        getMemberId() {
            return this.memberId;
        },
        getMemberName() {
            return this.memberName;
        },
        getTest() {
            return this.test;
        },
        isLogin() {
            return this.token !== null;
        },

    },
    actions: {
        setToken(token) {
            this.token = token;
        },
        setTest(test) {
            this.test = test;
        },
        isEqualMember(memberId) {
            return this.memberId === memberId
        },
        logout() {
            this.token = null;
            this.memberId = null;

        }
    }
})