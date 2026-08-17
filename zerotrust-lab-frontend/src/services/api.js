const API_URL =
    import.meta.env.VITE_API_URL ||
    "http://localhost:8080";

export async function registerUser(
    username,
    password,
    role
) {

    const response = await fetch(
        `${API_URL}/auth/register`,
        {
            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                username,
                password,
                role
            })
        }
    );

    return response.json();
}

export async function loginUser(
    username,
    password
) {

    const response = await fetch(
        `${API_URL}/auth/login`,
        {
            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                username,
                password
            })
        }
    );

    return response.json();
}

export async function verifyOtp(
    username,
    otp
) {

    const response = await fetch(
        `${API_URL}/auth/verify-otp`,
        {
            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                username,
                otp
            })
        }
    );

    return response.json();
}

export async function accessResource(
    resource
) {

    const token =
        localStorage.getItem("token");

    const response = await fetch(
        `${API_URL}${resource}`,
        {
            method: "GET",

            headers: {
                "Authorization":
                    `Bearer ${token}`
            }
        }
    );

    return {
        status: response.status,
        data: await response.json()
    };
}

export async function getAccessLogs() {

    const token =
        localStorage.getItem("token");

    const response = await fetch(
        `${API_URL}/lab/admin/access-logs`,
        {
            method: "GET",

            headers: {
                "Authorization":
                    `Bearer ${token}`
            }
        }
    );

    return {
        status: response.status,
        data: await response.json()
    };
}