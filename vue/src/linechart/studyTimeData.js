export const studyTimeData = {
    type: "bar",
    data: {
        // labels: ["Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune"],
        labels: null
        ,
        datasets: [
            {
                label: "공부 시간",
                data: getData(),
                backgroundColor: "rgba(71, 183,132,.5)",
                borderColor: "#47b784",
                borderWidth: 3,
                tension: 0
            }
        ]
    },
    options: {
        responsive: false,
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
}



function epoch_to_hh_mm_ss(epoch) {
    if(epoch >= 3600*24)
        epoch -= 3600*24;

    if(epoch >= 36000) {
        return new Date(epoch*1000).toISOString().substr(11, 8);
    }
    return new Date(epoch*1000).toISOString().substr(12, 7);
}

function epoch_to_hour_min(epoch) {
    const arr = epoch.split(" ");
    const regex = /[^0-9]/g;
    arr[0] = arr[0].replace(regex, "");
    arr[1] = arr[1].replace(regex, "");
    let h = Math.floor((arr[1]-arr[0])/3600);
    let m = Math.floor((arr[1]-arr[0])%3600/60);
    // const date = new Date(epoch*1000);
    // const hour = date.getHours();
    // const min = date.getMinutes();
    return h + '시간 ' + m + '분';

}

function getData() {
    const timeData = [[18030, 19203], [15600, 16000], [12000, 12394],[12000, 12394], [26465,34252],[18030, 19203], [15600, 16000]]
    return timeData;
}








export default studyTimeData;