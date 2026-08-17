import { useState } from "react";
import {
    accessResource,
    getAccessLogs
} from "../services/api";

function Dashboard() {

    const username =
        localStorage.getItem(
            "username"
        );

    const [result, setResult] =
        useState(null);

    const [logs, setLogs] =
        useState([]);

    async function checkResource(
        resource
    ) {

        const response =
            await accessResource(
                resource
            );

        setResult(response.data);
    }

    async function loadLogs() {

        const response =
            await getAccessLogs();

        if (response.data.accessLogs) {

            setLogs(
                response.data.accessLogs
            );

        } else {

            setResult(
                response.data
            );
        }
    }

    function logout() {

        localStorage.removeItem(
            "token"
        );

        localStorage.removeItem(
            "username"
        );

        window.location.reload();
    }

    return (
        <div className="dashboard">

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
                        User: {username}
                    </span>

                    <button
                        onClick={logout}
                    >
                        Logout
                    </button>
                </div>

            </header>

            <main>

                <h2>
                    Laboratory Resources
                </h2>

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

                {result && (

                    <div className="decision">

                        <h2>
                            Access Decision
                        </h2>

                        <p>
                            Decision:
                            {" "}
                            {result.decision}
                        </p>

                        <p>
                            Resource:
                            {" "}
                            {result.resource}
                        </p>

                        <p>
                            Message:
                            {" "}
                            {result.message}
                        </p>

                    </div>

                )}

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
                                    <th>User</th>
                                    <th>Role</th>
                                    <th>Resource</th>
                                    <th>Decision</th>
                                    <th>Reason</th>
                                    <th>Time</th>
                                </tr>

                            </thead>

                            <tbody>

                                {logs.map(
                                    log => (

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