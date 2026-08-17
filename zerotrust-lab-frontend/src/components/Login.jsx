import { useState } from "react";
import { loginUser } from "../services/api";

function Login({
    goToRegister,
    goToOtp
}) {

    const [username, setUsername] =
        useState("");

    const [password, setPassword] =
        useState("");

    const [message, setMessage] =
        useState("");

    async function handleLogin(e) {

        e.preventDefault();

        const data =
            await loginUser(
                username,
                password
            );

        if (
            data.message &&
            data.message.includes("MFA")
        ) {

            localStorage.setItem(
                "otpUsername",
                username
            );

            goToOtp();

            return;
        }

        if (data.token) {

            localStorage.setItem(
                "token",
                data.token
            );

            localStorage.setItem(
                "username",
                username
            );

            window.location.reload();

            return;
        }

        setMessage(data.message);
    }

    return (
        <div className="auth-container">

            <div className="auth-card">

                <h1>ZeroTrustLab</h1>

                <h2>Login</h2>

                <form
                    onSubmit={handleLogin}
                >

                    <input
                        type="text"
                        placeholder="Username"
                        value={username}
                        onChange={
                            e =>
                                setUsername(
                                    e.target.value
                                )
                        }
                    />

                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={
                            e =>
                                setPassword(
                                    e.target.value
                                )
                        }
                    />

                    <button type="submit">
                        Login
                    </button>

                </form>

                <p>{message}</p>

                <button
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