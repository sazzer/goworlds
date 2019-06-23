import uriParser from 'uri-template';
import axios, {AxiosRequestConfig, AxiosResponse, Method} from 'axios';
import contentTypeParser, {ParsedMediaType} from 'content-type';

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
    contentType: ParsedMediaType,
};

/**
 * Error returned making an HTTP Request
 */
export class RequestError<T> extends Error {
    /** The status code */
    status: number;

    /** The content type */
    contentType: ParsedMediaType | undefined;

    /** The response body */
    body: T;

    /**
     * Construct the error
     * @param status the status code
     * @param contentType the content type
     * @param body the body
     */
    constructor(status: number, contentType: ParsedMediaType | undefined, body: T) {
        super('An error occurred making a request');
        this.status = status;
        this.contentType = contentType;
        this.body = body;
    }
}

/**
 * Error indicating an RFC-7807 Response was received
 */
export class ProblemError extends Error {
    /** The status code */
    status: number;

    /** The problem type */
    type: string;

    /** The extra details */
    details: any;


    /**
     * Construct the error
     * @param message The error message
     * @param status The status code
     * @param type The type
     * @param details The details
     */
    constructor(message: string, status: number, type: string, details: any) {
        super(message);
        this.status = status;
        this.type = type;
        this.details = details;
    }
}

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

        const contentTypeHeader = (httpResponse.headers || {})['content-type'];
        const contentType = contentTypeParser.parse(contentTypeHeader);

        return {
            status: httpResponse.status,
            body: httpResponse.data,
            contentType,
        }
    } catch (e) {
        console.log('Error making HTTP request: %s', url, e);

        if (e.response) {
            const contentTypeHeader = e.response.headers['content-type'];
            const contentType = contentTypeHeader && contentTypeParser.parse(contentTypeHeader);

            if (contentType.type === 'application/problem+json') {
                throw new ProblemError(e.response.data.title, e.response.status, e.response.data.type, e.response.data);
            } else {
                throw new RequestError(e.response.status, contentType, e.response.data);
            }
        } else {
            throw e;
        }
    }
}
