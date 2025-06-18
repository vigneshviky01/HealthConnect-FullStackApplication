import axios from 'axios'

export function logUserIn(userCredentials) {

    let apiUrl = 'http://localhost:8080/login';
    return axios.post(apiUrl,userCredentials, {
        headers: {
            'Content-Type': 'application/json'
        }
    })
}
