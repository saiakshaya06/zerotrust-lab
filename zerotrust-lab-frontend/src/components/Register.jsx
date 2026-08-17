import { useState } from "react";
import { registerUser } from "../services/api";

function Register({ goToLogin }) {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [role, setRole] = useState("RESEARCHER");
    const [message, setMessage] = useState("");

    async function handleRegister(e) {

        e.preventDefault();

        if (!username || !password) {
            setMessage("Username and password are required.");
            return;
        }

        setMessage("Registering...");

        try {

            const data = await registerUser(
                username,
                password,
                role
            );

            console.log("REGISTER RESPONSE:", data);

            if (
                data &&
                data.message === "User registered successfully"
            ) {

                setMessage(
                    "Registration successful! Redirecting to login..."
                );

                setTimeout(() => {
                    goToLogin();
                }, 1000);

                return;
            }

            setMessage(
                data?.message || "Registration failed."
            );

        } catch (error) {

            console.error("REGISTER ERROR:", error);

            setMessage(
                "Registration failed. Please check the backend."
            );
        }
    }

    return (
        <div className="auth-container">

            <div className="auth-card">

                <h1>ZeroTrustLab</h1>

                <h2>Create Account</h2>

                <form onSubmit={handleRegister}>

                    <input
                        type="text"
                        placeholder="Username"
                        value={username}
                        onChange={(e) =>
                            setUsername(e.target.value)
                        }
                    />

                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) =>
                            setPassword(e.target.value)
                        }
                    />

                    <select
                        value={role}
                        onChange={(e) =>
                            setRole(e.target.value)
                        }
                    >

                        <option value="RESEARCHER">
                            Researcher
                        </option>

                        <option value="LAB_STAFF">
                            Lab Staff
                        </option>

                        <option value="INTERN">
                            Intern
                        </option>

                        <option value="LAB_ADMIN">
                            Lab Admin
                        </option>

                    </select>

                    <button type="submit">
                        Register
                    </button>

                </form>

                <p>{message}</p>

                <button
                    type="button"
                    className="link-button"
                    onClick={goToLogin}
                >
                    Already have an account? Login
                </button>

            </div>

        </div>
    );
}

export default Register;