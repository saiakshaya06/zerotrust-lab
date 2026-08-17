import axios from "axios";

// =====================================================
// BACKEND URL
// =====================================================

const API_BASE_URL =
    "https://zerotrust-lab-backend.onrender.com";

// =====================================================
// AXIOS INSTANCE
// =====================================================

const api = axios.create({
    baseURL: API_BASE_URL,

    headers: {
        "Content-Type": "application/json"
    }
});

// =====================================================
// ADD JWT TOKEN AUTOMATICALLY
// =====================================================

api.interceptors.request.use(
    (config) => {

        const token =
            localStorage.getItem("token");

        if (token) {
            config.headers.Authorization =
                `Bearer ${token}`;
        }

        return config;
    },

    (error) => {
        return Promise.reject(error);
    }
);

// =====================================================
// REGISTER
// =====================================================

export function registerUser(data) {

    return api.post(
        "/auth/register",
        data
    );
}

// =====================================================
// LOGIN
// =====================================================

export function loginUser(username, password) {

    return api.post(
        "/auth/login",
        {
            username: username,
            password: password
        }
    );
}

// =====================================================
// ACCESS RESOURCE
// =====================================================

export function accessResource(resource) {

    return api.get(
        resource
    );
}

// =====================================================
// ACCESS LOGS
// =====================================================

export function getAccessLogs() {

    return api.get(
        "/lab/admin/access-logs"
    );
}

// =====================================================
// EXPORT
// =====================================================

export default api;