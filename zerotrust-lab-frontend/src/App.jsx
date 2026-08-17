import { useState } from "react";

import Login from "./components/Login";
import Register from "./components/Register";
import Dashboard from "./components/Dashboard";

import "./App.css";


function App() {

    const token =
        localStorage.getItem("token");

    const [page, setPage] =
        useState("login");


    // ==========================================
    // USER ALREADY LOGGED IN
    // ==========================================

    if (token) {

        return <Dashboard />;

    }


    // ==========================================
    // REGISTER PAGE
    // ==========================================

    if (page === "register") {

        return (

            <Register
                goToLogin={() =>
                    setPage("login")
                }
            />

        );

    }


    // ==========================================
    // LOGIN PAGE
    // ==========================================

    return (

        <Login
            goToRegister={() =>
                setPage("register")
            }
        />

    );
}


export default App;