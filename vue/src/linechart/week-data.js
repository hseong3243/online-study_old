export const weekData = {
    type: "bar",
    data: {
        // labels: ["Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune"],
        labels: ["월", "화", "수", "목", "금", "토", "일"],
        datasets: [
            // {
            //     label: "휴식",
            //     data: [22101,22135,20047,20189,36000, 18752, 18752],
            //     backgroundColor: "rgba(54,73,93,.5)",
            //     borderColor: "#36495d",
            //     borderWidth: 3,
            //     tension: 0.2,
            //     fill: false
            // },
            // {
            //     label: "공부 시간",
            //     data: [18030, 19203, 15600, 16000, 12000, 12394, 26465],
            //     backgroundColor: "rgba(71, 183,132,.5)",
            //     borderColor: "#47b784",
            //     borderWidth: 3,
            //     tension: 0.2
            // },
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




export default weekData;