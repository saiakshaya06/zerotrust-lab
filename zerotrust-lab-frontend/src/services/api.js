import axios from "axios";

const API_BASE_URL =
    "https://zerotrust-lab-backend.onrender.com";

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        "Content-Type": "application/json"
    }
});


// ==========================================
// REGISTER
// ==========================================

export function registerUser(data) {
    return api.post("/auth/register", data);
}


// ==========================================
// LOGIN
// ==========================================

export function loginUser(username, password) {

    return api.post("/auth/login", {
        username: username,
        password: password
    });
}


// ==========================================
// ACCESS LAB RESOURCE
// NO TOKEN
// ==========================================

export function accessResource(resource) {

    const username =
        localStorage.getItem("username");

    return api.get(resource, {
        headers: {
            "X-Username": username
        }
    });
}


// ==========================================
// ACCESS LOGS
// NO TOKEN
// ==========================================

export function getAccessLogs() {

    const username =
        localStorage.getItem("username");

    return api.get("/lab/admin/access-logs", {
        headers: {
            "X-Username": username
        }
    });
}


export default api;