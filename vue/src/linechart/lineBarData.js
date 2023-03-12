export const lineBarData = {
    type: "bar",
    data: {
        labels: ['월', '화', '수', '목', '금', '토', '일']
        ,
        datasets: [
            {
                type: 'line',
                label: '백엔드 전체 평균',
                data: [4050, 3090, 3060, 3400],
                borderColor: "#000000",
                fill: false,
                tension: 0.1
            },
            {
                type: 'bar',
                label: '백엔드',
                data: [5200, 4300, 4233, 6052],
                backgroundColor: "rgba(71, 183,132,.5)",
                borderColor: "#47b784",
                borderWidth: 3,
            },

        ]
    },
    options: {
        responsive: true,
        lineTension: 1,
        animation: {
            duration: 0
        },
        scales: {
            yAxes: [
                {
                    ticks: {
                        beginAtZero: true,
                        userCallback: function (v) {return epoch_to_hh_mm_ss(v)},
                        stepSize: 60*60,

                    }
                }
            ]
        },
        tooltips: {
            callbacks: {
                label: function (tooltipItem, data) {
                    return data.datasets[tooltipItem.datasetIndex].label + ': ' + epoch_to_hour_min(tooltipItem.yLabel)
                }
            }
        }
    }
};

function epoch_to_hh_mm_ss(epoch) {
    if(epoch >= 36000) {
        return new Date(epoch*1000).toISOString().substr(11, 8);
    }
    return new Date(epoch*1000).toISOString().substr(12, 7);
}

function epoch_to_hour_min(epoch) {
    let h = Math.floor(epoch/3600);
    let m = Math.floor(epoch%3600/60);
    // const date = new Date(epoch*1000);
    // const hour = date.getHours();
    // const min = date.getMinutes();
    return h + '시간 ' + m + '분';

}

// function getLabels() {
//     const days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
//     const date = new Date();
//     if(date.getFullYear()%4 === 0) {
//         days[2] = 29;
//     }
//
//     let labels = []
//     for(let i=1; i<=days[date.getMonth()]; i++) {
//         const str = date.getMonth()+1 + "/" + i
//         labels.push(str)
//     }
//
//     return labels;
// }

export default lineBarData;