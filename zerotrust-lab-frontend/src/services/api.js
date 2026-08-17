import axios from "axios";

const API_URL =
    "https://zerotrust-lab-backend.onrender.com";

const api = axios.create({
    baseURL: API_URL,
    headers: {
        "Content-Type": "application/json"
    }
});


// ========================================
// REGISTER
// ========================================

export async function registerUser(
    username,
    password,
    role
) {
    try {

        const response = await api.post(
            "/auth/register",
            {
                username: username,
                password: password,
                role: role
            }
        );

        console.log(
            "REGISTER SUCCESS:",
            response.data
        );

        return response.data;

    } catch (error) {

        console.error(
            "REGISTER ERROR:",
            error
        );

        console.error(
            "STATUS:",
            error.response?.status
        );

        console.error(
            "RESPONSE:",
            error.response?.data
        );

        return {
            message:
                error.response?.data?.message ||
                "Registration failed. Please check the backend."
        };
    }
}


// ========================================
// LOGIN
// ========================================

export async function loginUser(
    username,
    password
) {
    try {

        const response = await api.post(
            "/auth/login",
            {
                username: username,
                password: password
            }
        );

        console.log(
            "LOGIN SUCCESS:",
            response.data
        );

        return response.data;

    } catch (error) {

        console.error(
            "LOGIN ERROR:",
            error
        );

        console.error(
            "STATUS:",
            error.response?.status
        );

        console.error(
            "RESPONSE:",
            error.response?.data
        );

        return {
            message:
                error.response?.data?.message ||
                "Login failed. Please check your username and password."
        };
    }
}


// ========================================
// VERIFY OTP
// ========================================

export async function verifyOtp(
    username,
    otp
) {
    try {

        const response = await api.post(
            "/auth/verify-otp",
            {
                username: username,
                otp: otp
            }
        );

        console.log(
            "OTP SUCCESS:",
            response.data
        );

        return response.data;

    } catch (error) {

        console.error(
            "OTP ERROR:",
            error
        );

        console.error(
            "STATUS:",
            error.response?.status
        );

        console.error(
            "RESPONSE:",
            error.response?.data
        );

        return {
            message:
                error.response?.data?.message ||
                "OTP verification failed."
        };
    }
}


// ========================================
// ACCESS LAB RESOURCE
// ========================================

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
            "ACCESS ERROR:",
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


// ========================================
// GET ACCESS LOGS
// ========================================

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
            "ACCESS LOG ERROR:",
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