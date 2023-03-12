import { defineStore } from 'pinia'

export const useRecordStore = defineStore('record', {
    state: () => {
        return {
            category: 'WEEK',
            recordCategory: 'ALL',
        }
    },
    persist: true,
    getters: {
        getCategory() {
            return this.category;
        },
        getRecordCategory() {
            return this.recordCategory;
        }
    },
    actions: {
        changeCategory(str) {
            this.category = str;
        },
        changeRecordCategory(str) {
            this.recordCategory = str;
        }
    }
})