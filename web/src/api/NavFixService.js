import http from 'axios';

export default {
    name: 'navfixService',

    getNavaidsPage({ currentPageNo, pageSize, keyword }) {
        return http({
            method: 'post',
            url: '/navdata/navaid/keyword',
            data: {
                currentPageNo, pageSize, keyword
            }
        })
    },

    getWaypointsPage({ currentPageNo, pageSize, keyword }) {
        return http({
            method: 'post',
            url: '/navdata/waypoint/keyword',
            data: {
                currentPageNo, pageSize, keyword
            }
        })
    },
}