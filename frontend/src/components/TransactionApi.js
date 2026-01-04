import axios from 'axios'

const API = axios.create({
    baseUrl: 'http://'
})

export async function getTransaction(params) {
    try {
        const { data } = await API.get(`/${}`)
    }
}