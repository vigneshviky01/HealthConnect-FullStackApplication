import axios from 'axios'

export function logUserIn(userCredentials) {

    let apiUrl = 'http://localhost:8080/api/auth/signin';
    return axios.post(apiUrl,userCredentials, {
        headers: {
            'Content-Type': 'application/json'
        }
    })
}
