import {from, Observable} from "rxjs";
import {request} from "../api";

/** The shape of the response from the server on checking if an email address exists */
declare type CheckEmailExistsServiceResponse = {
    exists: boolean,
};

/**
 * Check if the given Email Address is already registered
 * @param email the email address to check
 * @return the response of the check
 */
export function checkEmail(email: string) : Observable<boolean> {
    const response = request<CheckEmailExistsServiceResponse>('/emails/{email}', {
        method: 'GET',
        urlParams: {
            email,
        }
    })
        .then(response => response.body.exists);

    return from(response);
}
