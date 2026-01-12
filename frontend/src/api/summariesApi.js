import axios from 'axios'

const axiosClient = axios.create({
    baseURL: '/summaries',
    validateStatus: function (status) {

        return status >= 200 && status < 300
    }
})


/**
 * Consumes the /summaries/clients endpoint. Returns multiple client summaries.
 * @param  params year, month, view, limit, offset
 * @returns Array of Clients summary
 */
export async function getClientsSummary(params) {
    try {
        const { data } = await axiosClient.get(`/clients`, { params })
        return [null, data]
    }
    catch (e) {
        return [e]
    }
}
/**
 * Consumes the /summaries/merchants endpoint. Returns multiple merchant summaries.
 * @param  params year, month, state, city,  limit, offset
 * @returns Array of merchants summary
 */
export async function getMerchantsSummary(params) {
    try {
        const { data } = await axiosClient.get(`/merchants`, { params })
        return [null, data]
    }
    catch (e) {
        return [e]
    }
}
/**
 * Consumes the /summaries/state endpoint. Returns multiple state summaries.
 * @param  params year, month
 * @returns Array of Clients summary
 */
export async function getStateSummary(params) {
    try {
        const { data } = await axiosClient.get(`/state`, { params })
        return [null, data]
    }
    catch (e) {
        return [e]
    }
}
/**
 * Consumes the /summaries/year endpoint. Returns multiple year summaries.
 * @param  params start, end, detail
 * @returns Array of year summary
 */
export async function getYearSummary(params) {
    try {
        const { data } = await axiosClient.get(`/year`, { params })
        return [null, data]
    }
    catch (e) {
        return [e]
    }
}


