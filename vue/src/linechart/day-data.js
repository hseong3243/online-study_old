
export const dayData = {
    type: 'doughnut',
    data: {
        // labels: ["Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune"],
        labels: ["공부", "휴식"],
        datasets: [
            {
                label: "평균 공부 시간",
                data: [120, 60],
                backgroundColor: ["#F6E2BD"],
                borderWidth: 3,
                color: '#FFCE56'
            }
        ]
    },
    options: {
        responsive: false,
        animation: {
            duration: 0
        },
        title: {
            display: true,
            text: '하루 공부 량'
        },
        zoomOutPercentage: 55,
        plugins: {
            legend: false,
            outlabels: {
                text: '%l %p',
                color: 'white',
                stretch: 45,
                font: {
                    resizable: true,
                    minSize: 12,
                    maxSize: 18
                }
            }
        },
        tooltips: {
            callbacks: {
                label: function (tooltipItem, data) {
                    const label = data.datasets[tooltipItem.datasetIndex]
                    return data.labels[tooltipItem.index] + ': ' + epoch_to_hour_min(label.data[tooltipItem.index])
                }
            }
        },
        layout: {
            padding: 20
        }
    }
};

// function epoch_to_hh_mm_ss(epoch) {
//     return new Date(epoch*1000).toISOString().substr(12, 7);
// }

function epoch_to_hour_min(epoch) {
    let h = Math.floor(epoch/3600);
    let m = Math.floor(epoch%3600/60);
    // const date = new Date(epoch*1000);
    // const hour = date.getHours();
    // const min = date.getMinutes();
    return h + '시간 ' + m + '분';

}

export default dayData;