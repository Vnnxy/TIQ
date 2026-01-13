import axios from 'axios'

const axiosClient = axios.create({
    baseURL: '/merchants',
    validateStatus: function (status) {

        return status >= 200 && status < 300
    }
})


/**
 * Consumes the /merchants endpoint. Returns multiple transactions.
 * @param  params offset, limit
 * @returns Array of merchants
 */
export async function getMerchants(params) {
    try {
        const { data } = await axiosClient.get(``, { params })
        return [null, data]
    }
    catch (e) {
        return [e]
    }
}


/**
 * Consumes the merchants/{id} endpoint. Retrieves a single merchant
 * @param  id merchant id
 * @returns A merchant or null.
 */
export async function getMerchant(id) {
    try {
        const { data } = await axiosClient.get(`/${id}`)
        return [null, data]
    }
    catch (e) {
        if (e.response && e.response.status === 404) {
            return [null, null]
        }
        return [e]
    }
}
/**
 * Consumes the merchant/{id} endpoint. Deletes a single transaction
 * @param  id merchant id
 * @returns A merchant or null.
 */
export async function deleteMerchant(id) {
    try {
        const { data } = await axiosClient.delete(`/${id}`)
        return [null, data]
    }
    catch (e) {
        if (e.response?.status === 409) {
            alert("This merchant is being referenced by transactions. ")
        }
    }
}
/**
 * Consumes the /merchants endpoint. Creates a single merchant
 * @param  id merchant id
 * @returns A merchant or null.
 */
export async function postMerchant(params) {
    try {
        const { data } = await axiosClient.post('', params)
        return [null, data]
    } catch (e) {
        console.error('postMerchant error:', e)
        return [e, null]
    }
}
/**
 * Consumes the merchant/{id} endpoint. Updates a single transaction
 * @param  id merchant id
 * @param params the merchant
 * @returns A merchant or null.
 */
export async function putMerchant(id, params) {
    try {
        const { data } = await axiosClient.put(`/${id}`, params)
        return [null, data]
    } catch (e) {
        console.error('putMerchant error:', e)
        return [e, null]
    }
}



