import { useState } from "react";
import { loginUser } from "../services/api";

function Login({ goToRegister }) {

    const [username, setUsername] =
        useState("");

    const [password, setPassword] =
        useState("");

    const [message, setMessage] =
        useState("");

    const [loading, setLoading] =
        useState(false);


    async function handleLogin(e) {

        e.preventDefault();

        if (!username || !password) {

            setMessage(
                "Username and password are required."
            );

            return;
        }

        setLoading(true);
        setMessage("Logging in...");


        try {

            const data =
                await loginUser(
                    username,
                    password
                );


            console.log(
                "Login response:",
                data
            );


            // ==========================================
            // LOGIN SUCCESS
            // ==========================================

            if (data && data.token) {

                localStorage.setItem(
                    "token",
                    data.token
                );

                localStorage.setItem(
                    "username",
                    username
                );


                // Remove old OTP data
                localStorage.removeItem(
                    "otpUsername"
                );


                // Open dashboard
                window.location.reload();

                return;
            }


            // ==========================================
            // LOGIN FAILED
            // ==========================================

            setMessage(
                data?.message ||
                "Invalid username or password."
            );

        } catch (error) {

            console.error(
                "Login component error:",
                error
            );

            setMessage(
                "Login failed. Please try again."
            );

        } finally {

            setLoading(false);

        }
    }


    return (

        <div className="auth-container">

            <div className="auth-card">

                <h1>
                    ZeroTrustLab
                </h1>

                <h2>
                    Login
                </h2>


                <form
                    onSubmit={handleLogin}
                >

                    <input
                        type="text"
                        placeholder="Username"
                        value={username}
                        onChange={(e) =>
                            setUsername(
                                e.target.value
                            )
                        }
                    />


                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) =>
                            setPassword(
                                e.target.value
                            )
                        }
                    />


                    <button
                        type="submit"
                        disabled={loading}
                    >

                        {loading
                            ? "Logging in..."
                            : "Login"
                        }

                    </button>

                </form>


                <p>
                    {message}
                </p>


                <button
                    type="button"
                    className="link-button"
                    onClick={goToRegister}
                >
                    Create new account
                </button>

            </div>

        </div>
    );
}

export default Login;