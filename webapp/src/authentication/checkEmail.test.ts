import {checkEmail} from "./checkEmail";
import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';


describe('Checking if an email is registered', () => {
    /** The Mock Axios layer */
    let mockAxios: MockAdapter;

    beforeAll(() => {
        mockAxios = new MockAdapter(axios);
    });

    afterAll(() => {
        mockAxios.restore();
    });

    it('Works correctly for a known email address', (done) => {
        mockAxios.onGet('/emails/graham%40grahamcox.co.uk').reply(200, {
            exists: true
        });

        checkEmail('graham@grahamcox.co.uk')
            .subscribe(value => {
                expect(value).toBe(true);
                done();
            });
    });

    it('Works correctly for an unknown email address', (done) => {
        mockAxios.onGet('/emails/graham%40grahamcox.co.uk').reply(200, {
            exists: false
        });

        checkEmail('graham@grahamcox.co.uk')
            .subscribe(value => {
                expect(value).toBe(false);
                done();
            });

    });

    it('Reacts correctly when an error occurs', (done) => {
        mockAxios.onGet('/emails/graham%40grahamcox.co.uk').networkError();

        checkEmail('graham@grahamcox.co.uk')
            .subscribe(
                value => {
                    fail('No value expected but got: ' + value);
                    done();
                },
                err => {
                    expect(err.toString()).toEqual('Error: Network Error');
                    done();
                }
            );

    });
});
