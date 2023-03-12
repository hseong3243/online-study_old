export const dayBarData = {
    type: "horizontalBar",
    data: {
        labels: ['백엔드', '토익', '휴식'],
        datasets: [
            {
                label: 'My First Dataset',
                data: [[4526,5378], [5378, 6852], [6852, 8590]],
                fill: false,
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(255, 159, 64, 0.2)',
                    'rgba(255, 205, 86, 0.2)',

                ],
                borderColor: [
                    'rgb(255, 99, 132)',
                    'rgb(255, 159, 64)',
                    'rgb(255, 205, 86)',

                ],
                borderWidth: 1
            },
            {
                label: 'My First Dataset',
                data: [[8590,12052], [16085, 18079], [12052, 16085]],
                fill: false,
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(255, 159, 64, 0.2)',
                    'rgba(255, 205, 86, 0.2)',

                ],
                borderColor: [
                    'rgb(255, 99, 132)',
                    'rgb(255, 159, 64)',
                    'rgb(255, 205, 86)',

                ],
                borderWidth: 1
            },
        ]
    },
    options: {
        responsive: false,
        legend: {
            display: false
        },
        scales: {
            xAxes: [{
                stacked: false,
                ticks: {
                    userCallback: function (v) {return epoch_to_hh_mm_ss(v)},
                    stepSize: 60*60,
                }
            }],
            yAxes: [{
                stacked: true,
            }]
        },
    }
}

function epoch_to_hh_mm_ss(epoch) {
    if(epoch >= 36000) {
        return new Date(epoch*1000).toISOString().substr(11, 8);
    }
    return new Date(epoch*1000).toISOString().substr(12, 7);
}

// function epoch_to_hour_min(epoch) {
//     let h = Math.floor(epoch/3600);
//     let m = Math.floor(epoch%3600/60);
//     // const date = new Date(epoch*1000);
//     // const hour = date.getHours();
//     // const min = date.getMinutes();
//     return h + '시간 ' + m + '분';
//
// }

export default dayBarData;