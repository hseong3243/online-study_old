export const monthData = {
    type: "bar",
    data: {
        // labels: ["Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune"],
        labels: getLabels()
        ,
        datasets: [
            {
                label: "휴식",
                data: [22101,22135,20047,20189,36000, 18752, 18752],
                // backgroundColor: "rgba(54,73,93,.5)",
                borderColor: "#36495d",
                borderWidth: 3,
                borderRadius: 5,
                tension: 0,
                fill: false
            },
            {
                label: "백엔드",
                data: [18030, 19203, 15600, 16000, 12000, 12394, 26465],
                backgroundColor: "rgba(71, 183,132,.5)",
                borderColor: "#47b784",
                borderWidth: 3,
                borderRadius: 5,
                tension: 0
            },
            {
                label: "토익",
                data: [7999, 6977, 5767, 4578, 9654, 4765, 5635],
                backgroundColor: "rgba(183, 183,132,.5)",
                borderColor: "#47b784",
                borderWidth: 3,
                borderRadius: 5,
                tension: 0
            }
        ]
    },
    options: {
        responsive: true,
        lineTension: 1,
        animation: {
            duration: 0
        },
        scales: {
            xAxes: [
                {
                    stacked: true,
                    autoSkip: true,
                }
            ],
            yAxes: [
                {
                    stacked: true,
                    ticks: {
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


function getLabels() {
    const days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
    const date = new Date();
    if(date.getFullYear()%4 === 0) {
        days[2] = 29;
    }

    let labels = []
    for(let i=1; i<=days[date.getMonth()]; i++) {
        const str = date.getMonth()+1 + "/" + i
        labels.push(str)
    }

    return labels;
}

export default monthData;