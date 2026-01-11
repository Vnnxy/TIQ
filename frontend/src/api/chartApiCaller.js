
// Base api url
const BASE_URL = 'https://quickchart.io/chart'
//Builds the url
function buildUrl(config) {
    return `${BASE_URL}?c=${encodeURIComponent(JSON.stringify(config))}`
}
//Base style for axis
const BASE_STYLE = {
    options: {
        plugins: {
        },
        scales: {
        }
    }
}



// Clients
export function clientsChart(items, view = 'total') {
    const keyMap = {
        total: 'total',
        avg: 'averageTransaction',
        count: 'transactionCount'
    }

    const labelMap = {
        total: 'Total Amount',
        avg: 'Average Transaction',
        count: 'Transaction Count'
    }

    return buildUrl({
        type: 'bar',
        data: {
            labels: items.map(i => `Client ${i.clientId}`),
            datasets: [{
                label: labelMap[view],
                data: items.map(i => i[keyMap[view]]),
                backgroundColor: '#1976D2',
            }]
        },
        options: {
            ...BASE_STYLE.options,
            plugins: {
                title: {
                    display: true,
                    text: `Client Summary - ${labelMap[view]}`,
                },
            },
            legend: {
                labels: {
                    fontColor: 'white'
                }
            },
            scales: {
                xAxes: [{
                    ticks: {
                        fontColor: 'white'
                    }
                }],
                yAxes: [{
                    ticks: {
                        fontColor: 'white'
                    }
                }]
            }

        }
    });
}


//Merchants
export function merchantsChart(items) {
    return buildUrl({
        ...BASE_STYLE,
        type: 'bar',
        data: {
            labels: items.map(i => `${i.city}, ${i.state}`),
            datasets: [{
                label: 'Total per Merchant',
                data: items.map(i => i.total),
                backgroundColor: '#813'
            }]
        },
        options: {
            legend: {
                labels: {
                    fontColor: 'white'
                }
            },
            scales: {
                xAxes: [{
                    ticks: {
                        fontColor: 'white'
                    }
                }],
                yAxes: [{
                    ticks: {
                        fontColor: 'white'
                    }
                }]
            }
        }

    })
}

//States
export function statesChart(items) {
    const limitedItems = items.slice(0, 10);
    const total = limitedItems.reduce((sum, i) => sum + i.total, 0);

    const chartConfig = {
        type: 'outlabeledPie',
        data: {
            labels: limitedItems.map(i => i.merchantState),
            datasets: [{
                data: limitedItems.map(i => i.total),
                backgroundColor: [
                    '#1976D2', '#2E7D32', '#F57C00', '#C62828', '#7B1FA2',
                    '#0097A7', '#FBC02D', '#5D4037', '#455A64', '#E91E63'
                ],
                borderColor: '#FFFFFF',
                borderWidth: 1
            }]
        },
        options: {
            plugins: {

                outlabels: {
                    text: (context) => {
                        const value = context.dataset.data[context.dataIndex];
                        const percentage = ((value / total) * 100).toFixed(1);
                        return `${context.chart.data.labels[context.dataIndex]}\n${percentage}%`;
                    },
                    color: 'white',
                }
            },
            legend: {
                labels: {
                    fontColor: 'white'
                }
            },

        }
    };

    return buildUrl(chartConfig);
}


//years
export function yearsChart(items, detail = 'quarter') {
    return buildUrl({
        ...BASE_STYLE,
        type: 'line',
        data: {
            labels: items.map(i => formatPeriod(i.period, detail)),
            datasets: [
                {
                    label: 'Total',
                    data: items.map(i => i.total),
                    backgroundColor: "#739",
                    borderColor: "rgba(183, 140, 201, 1)"
                }
            ]
        },
        options: {
            legend: {
                labels: {
                    fontColor: 'white'
                }
            },
            scales: {
                xAxes: [{
                    ticks: {
                        fontColor: 'white'
                    }
                }],
                yAxes: [{
                    ticks: {
                        fontColor: 'white'
                    }
                }]
            }
        }
    })
}

//Formats quartes/year
function formatPeriod(period, detail) {
    const d = new Date(period)
    const year = d.getFullYear()
    const month = d.getMonth() + 1

    if (detail === 'quarter') {
        const q = Math.floor((month - 1) / 3) + 1
        return `Q${q} ${year}`
    }

    return `${month}/${year}`
}
