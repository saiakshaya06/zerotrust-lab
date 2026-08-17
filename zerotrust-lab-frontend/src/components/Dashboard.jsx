import { useState } from "react";

import {
    accessResource,
    getAccessLogs
} from "../services/api";


function Dashboard() {

    const username =
        localStorage.getItem("username");


    const [result, setResult] =
        useState(null);

    const [logs, setLogs] =
        useState([]);


    // ==========================================
    // CHECK RESOURCE
    // ==========================================

    async function checkResource(resource) {

        setResult(null);

        const response =
            await accessResource(resource);

        setResult(response.data);
    }


    // ==========================================
    // LOAD ACCESS LOGS
    // ==========================================

    async function loadLogs() {

        const response =
            await getAccessLogs();

        if (
            response.data &&
            response.data.accessLogs
        ) {

            setLogs(
                response.data.accessLogs
            );

        }

        setResult(
            response.data
        );
    }


    // ==========================================
    // LOGOUT
    // ==========================================

    function logout() {

        localStorage.removeItem(
            "token"
        );

        localStorage.removeItem(
            "username"
        );

        localStorage.removeItem(
            "otpUsername"
        );

        window.location.reload();
    }


    return (

        <div className="dashboard">

            {/* ================================= */}
            {/* HEADER */}
            {/* ================================= */}

            <header>

                <div>

                    <h1>
                        ZeroTrustLab
                    </h1>

                    <p>
                        Zero Trust Laboratory
                        Access Control
                    </p>

                </div>


                <div>

                    <span>
                        Welcome, {username}
                    </span>

                    <button
                        onClick={logout}
                    >
                        Logout
                    </button>

                </div>

            </header>


            <main>

                {/* ================================= */}
                {/* ACCESS INFORMATION */}
                {/* ================================= */}

                <h2>
                    Your Laboratory Access
                </h2>

                <p>
                    Select a resource to check
                    whether you are allowed to
                    access it.
                </p>


                {/* ================================= */}
                {/* RESOURCES */}
                {/* ================================= */}

                <div className="resource-grid">

                    <button
                        onClick={() =>
                            checkResource(
                                "/lab/research"
                            )
                        }
                    >
                        Research
                    </button>


                    <button
                        onClick={() =>
                            checkResource(
                                "/lab/experiments"
                            )
                        }
                    >
                        Experiments
                    </button>


                    <button
                        onClick={() =>
                            checkResource(
                                "/lab/intern"
                            )
                        }
                    >
                        Intern Resources
                    </button>


                    <button
                        onClick={() =>
                            checkResource(
                                "/lab/equipment"
                            )
                        }
                    >
                        Equipment
                    </button>


                    <button
                        onClick={() =>
                            checkResource(
                                "/lab/operations"
                            )
                        }
                    >
                        Operations
                    </button>


                    <button
                        onClick={() =>
                            checkResource(
                                "/lab/admin"
                            )
                        }
                    >
                        Administration
                    </button>

                </div>


                {/* ================================= */}
                {/* ACCESS DECISION */}
                {/* ================================= */}

                {result && (

                    <div className="decision">

                        <h2>
                            Access Decision
                        </h2>


                        <p>
                            <strong>
                                Decision:
                            </strong>{" "}
                            {result.decision}
                        </p>


                        <p>
                            <strong>
                                Resource:
                            </strong>{" "}
                            {result.resource}
                        </p>


                        <p>
                            <strong>
                                User:
                            </strong>{" "}
                            {result.user ||
                                username}
                        </p>


                        <p>
                            <strong>
                                Message:
                            </strong>{" "}
                            {result.message}
                        </p>

                    </div>

                )}


                {/* ================================= */}
                {/* ACCESS LOGS */}
                {/* ================================= */}

                <div className="logs-section">

                    <button
                        onClick={loadLogs}
                    >
                        View Access Logs
                    </button>


                    {logs.length > 0 && (

                        <table>

                            <thead>

                                <tr>

                                    <th>
                                        User
                                    </th>

                                    <th>
                                        Role
                                    </th>

                                    <th>
                                        Resource
                                    </th>

                                    <th>
                                        Decision
                                    </th>

                                    <th>
                                        Reason
                                    </th>

                                    <th>
                                        Time
                                    </th>

                                </tr>

                            </thead>


                            <tbody>

                                {logs.map(
                                    (log) => (

                                        <tr
                                            key={
                                                log.id
                                            }
                                        >

                                            <td>
                                                {
                                                    log.username
                                                }
                                            </td>

                                            <td>
                                                {
                                                    log.role
                                                }
                                            </td>

                                            <td>
                                                {
                                                    log.resource
                                                }
                                            </td>

                                            <td>
                                                {
                                                    log.decision
                                                }
                                            </td>

                                            <td>
                                                {
                                                    log.reason
                                                }
                                            </td>

                                            <td>
                                                {
                                                    log.timestamp
                                                }
                                            </td>

                                        </tr>

                                    )
                                )}

                            </tbody>

                        </table>

                    )}

                </div>

            </main>

        </div>

    );
}


export default Dashboard;