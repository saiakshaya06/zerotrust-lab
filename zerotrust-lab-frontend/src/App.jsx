import { useState } from "react";

import Login from "./components/Login";
import Register from "./components/Register";
import OtpVerification from "./components/OtpVerification";
import Dashboard from "./components/Dashboard";

import "./App.css";

function App() {

    const token =
        localStorage.setItem("token", token);

    const [
        page,
        setPage
    ] = useState("login");

    if (token) {

        return <Dashboard />;
    }

    if (page === "register") {

        return (
            <Register
                goToLogin={() =>
                    setPage("login")
                }
            />
        );
    }

    if (page === "otp") {

        return (
            <OtpVerification />
        );
    }

    return (
        <Login
            goToRegister={() =>
                setPage("register")
            }
            goToOtp={() =>
                setPage("otp")
            }
        />
    );
}

export default App;