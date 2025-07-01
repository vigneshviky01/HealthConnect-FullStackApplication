import axios from 'axios'

export function registerUser(newUserDetails){
    console.log(newUserDetails)
    let apiUrl = 'http://localhost:8080/api/auth/signup'
    return axios.post(apiUrl,newUserDetails,{
        headers:{
            'Content-Type': 'application/json'
        }
    })
}
