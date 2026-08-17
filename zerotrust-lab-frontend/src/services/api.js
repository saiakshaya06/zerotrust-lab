import axios from "axios";

const API_URL =
    "https://zerotrust-lab-backend.onrender.com";

const api = axios.create({
    baseURL: API_URL,
    headers: {
        "Content-Type": "application/json"
    }
});


// ================================
// REGISTER
// ================================

export async function registerUser(
    username,
    password,
    role
) {
    try {

        const response = await api.post(
            "/auth/register",
            {
                username,
                password,
                role
            }
        );

        console.log(
            "Backend registration response:",
            response.data
        );

        return response.data;

    } catch (error) {

        console.error(
            "Registration error:",
            error
        );

        console.error(
            "Backend response:",
            error.response?.data
        );

        return {
            message:
                error.response?.data?.message ||
                "Registration failed. Please check the backend."
        };
    }
}


// ================================
// LOGIN
// ================================

export async function loginUser(
    username,
    password
) {
    try {

        const response = await api.post(
            "/auth/login",
            {
                username,
                password
            }
        );

        return response.data;

    } catch (error) {

        console.error(
            "Login error:",
            error
        );

        return {
            message:
                error.response?.data?.message ||
                "Login failed. Please check your username and password."
        };
    }
}


// ================================
// VERIFY OTP
// ================================

export async function verifyOtp(
    username,
    otp
) {
    try {

        const response = await api.post(
            "/auth/verify-otp",
            {
                username,
                otp
            }
        );

        return response.data;

    } catch (error) {

        console.error(
            "OTP verification error:",
            error
        );

        return {
            message:
                error.response?.data?.message ||
                "OTP verification failed."
        };
    }
}


// ================================
// ACCESS LAB RESOURCE
// ================================

export async function accessResource(
    resource
) {

    try {

        const token =
            localStorage.getItem("token");

        const response = await api.get(
            resource,
            {
                headers: {
                    Authorization:
                        `Bearer ${token}`
                }
            }
        );

        return response;

    } catch (error) {

        console.error(
            "Access request error:",
            error
        );

        return {
            data: {
                decision: "DENIED",
                resource: resource,
                message:
                    error.response?.data?.message ||
                    "Access request failed."
            }
        };
    }
}


// ================================
// GET ACCESS LOGS
// ================================

export async function getAccessLogs() {

    try {

        const token =
            localStorage.getItem("token");

        const response = await api.get(
            "/lab/admin/access-logs",
            {
                headers: {
                    Authorization:
                        `Bearer ${token}`
                }
            }
        );

        return response;

    } catch (error) {

        console.error(
            "Access logs error:",
            error
        );

        return {
            data: {
                decision: "DENIED",
                message:
                    error.response?.data?.message ||
                    "Unable to load access logs."
            }
        };
    }
}