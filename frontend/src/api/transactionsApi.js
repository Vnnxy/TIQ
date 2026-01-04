import axios from 'axios'

const axiosClient = axios.create({
    baseURL: 'http://localhost:8080/transactions',
    validateStatus: function (status) {

        return status >= 200 && status < 300
    }
})

/**
 * Consumes the /average endpoint.
 * @returns Data with the average amount 
 */
export async function getTransactionAverage(params) {
    try {
        const { data } = await axiosClient.get(`/average`, { params })
        return [null, data]
    }
    catch (e) {
        return [e]
    }
}
/**
 * Consumes the /total endpoint.
 * @returns Data with the total amount 
 */
export async function getTransactionTotal(params) {
    try {
        const { data } = await axiosClient.get(`/total`, { params })
        return [null, data]
    }
    catch (e) {
        return [e]
    }
}
/**
 * Consumes the /tb endpoint.
 * @returns Data with the top or bottom amount 
 */
export async function getTransactionTB(params) {
    try {
        const { data } = await axiosClient.get(`/tb`, { params })
        return [null, data]
    }
    catch (e) {
        return [e]
    }
}


export async function getTransactions(params) {
    try {
        const { data } = await axiosClient.get(``, { params })
        return [null, data]
    }
    catch (e) {
        return [e]
    }
}

export async function deleteTransactions(params) {
    try {
        const { data } = await axiosClient.delete(``, { params })
        return [null, data]
    }
    catch (e) {
        return [e]
    }
}

export async function getTransaction(id) {
    try {
        const { data } = await axiosClient.get(`/${id}`)
        return [null, data]
    }
    catch (e) {
        return [e]
    }
}

export async function deleteTransaction(id) {
    try {
        const { data } = await axiosClient.delete(`/${id}`)
        return [null, data]
    }
    catch (e) {
        return [e]
    }
}

export async function postTransaction(params) {
    try {
        const { data } = await axiosClient.post('', params)
        return [null, data]
    } catch (e) {
        console.error('postTransaction error:', e)
        return [e, null]
    }
}

export async function putTransaction(id, params) {
    try {
        const { data } = await axiosClient.put(`/${id}`, params)
        return [null, data]
    } catch (e) {
        console.error('putTransaction error:', e)
        return [e, null]
    }
}



