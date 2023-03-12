import { defineStore } from 'pinia'

export const useGroupStore = defineStore('group', {
    state: () => {
        return {
            category: 'INTRO',
            postCategory: 'ALL',
            title: '목록',
        }
    },
    persist: true,
    getters: {
        getCategory() {
            return this.category;
        },
        getPostCategory() {
            return this.postCategory;
        },
        getTitle() {
            return this.title;
        }
    },
    actions: {
        changeCategory(str) {
            this.category = str;
        },
        changePostCategory(str) {
            this.postCategory = str;
        },
        changeTitle(str) {
            this.title = str;
        }
    }
})