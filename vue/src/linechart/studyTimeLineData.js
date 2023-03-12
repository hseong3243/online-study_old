export const studyTimeLineData = {
    type: "line",
    data: {
        labels: ['월', '화', '수', '목', '금', '토', '일']
        ,
        datasets: [
            {
                type: 'line',
                label: '백엔드 시작 평균',
                data: [4050, 3090, 3060, 3400],
                borderColor: "#000000",
                fill: false,
                tension: 0.1
            },
            {
                type: 'line',
                label: '백엔드 종료 평균',
                data: [34000, 33090, 29060, 31200],
                borderColor: "#000000",
                fill: false,
                tension: 0.1
            },
            {
                type: 'bar',
                label: '백엔드',
                data: [[5200, 23000], [4300, 20000], [4233, 25000], [6052, 25300]],
                backgroundColor: "rgba(71, 183,132,.5)",
                borderColor: "#47b784",
                borderWidth: 3,
            },

        ]
    },
    options: {
        responsive: false,
        lineTension: 1,
        animation: {
            duration: 0
        },
        scales: {
            yAxes: [
                {
                    ticks: {
                        userCallback: function (v) {return epoch_to_hh_mm_ss(v)},
                        stepSize: 120*60,
                        min:3600*5,
                        max:3600*29,
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
    if(epoch >= 3600*24)
        epoch -= 3600*24;

    if(epoch >= 36000) {
        return new Date(epoch*1000).toISOString().substr(11, 8);
    }
    return new Date(epoch*1000).toISOString().substr(12, 7);
}

function epoch_to_hour_min(epoch) {
    let h;
    let m;
    if(epoch.length > 6) {
        const arr = epoch.split(" ");
        const regex = /[^0-9]/g;
        arr[0] = arr[0].replace(regex, "");
        arr[1] = arr[1].replace(regex, "");
        h = Math.floor((arr[1]-arr[0])/3600);
        m = Math.floor((arr[1]-arr[0])%3600/60);
        return h + '시간 ' + m + '분';
    }
    else {
        h = Math.floor(epoch / 3600);
        m = Math.floor(epoch % 3600 / 60);
        return h+ '시 ' + m + '분'
    }
    // const date = new Date(epoch*1000);
    // const hour = date.getHours();
    // const min = date.getMinutes();


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

export default studyTimeLineData;