import {axiosInstance} from "./api";

export const scheduleApi = {
    addScheduleRecord(scheduleRecord) {
        return axiosInstance.post("schedule", scheduleRecord);
    },

    getScheduleRecord(recordId) {
        return axiosInstance.get('schedule/' + recordId);
    },

    getGroupScheduleRecords(groupId, from, to) {
        return axiosInstance.get('schedule/group/' + groupId, {
            params: {
                from: from,
                to: to
            }
        });
    },

    getUserScheduleRecords(userId, from, to) {
        return axiosInstance.get('schedule/user/' + userId, {
            params: {
                from: from,
                to: to
            }
        });
    },

    editScheduleRecord(scheduleRecord) {
        return axiosInstance.put('schedule', scheduleRecord);
    },

    deleteScheduleRecord(recordId) {
        return axiosInstance.delete('schedule/' + recordId);
    }
}