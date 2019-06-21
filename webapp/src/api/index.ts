import uriParser from 'uri-template';
import axios, {AxiosRequestConfig, AxiosResponse, Method} from 'axios';

/** The Base URI for the API */
const API_URI = process.env.REACT_APP_API_URI;
/**
 * The type of a Request to make
 */
export type Request = {
    method?: Method,
    urlParams?: any,
    body?: any,
    clientId?: string,
};

/**
 * The type of a Response to return from a request
 */
export type Response<T> = {
    status: number,
    body: T,
};

/**
 * Actually make a request to the service
 * @param url the URL to request
 * @param params the request parameters to provide
 */
export async function request<T>(url: string, params: Request = {}) : Promise<Response<T>> {
    /** The expanded URL */
    const expandedUrl = uriParser.parse(url).expand(params.urlParams);

    /** The parameters to pass to Axios */
    const fetchParams: AxiosRequestConfig = {
        method : params.method || 'GET',
        baseURL: API_URI,
        headers: {},
        data: params.body,
    };

    if (params.clientId) {
        fetchParams.auth = {
            username: params.clientId,
            password: ''
        };
    }

    try {
        const httpResponse: AxiosResponse = await axios(expandedUrl, fetchParams);

        return {
            status: httpResponse.status,
            body: httpResponse.data,
        }
    } catch (e) {
        console.log('Error making HTTP request: %s', url, e);
        throw e;
    }
}
