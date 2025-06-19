import React, { useState } from 'react'
import { NavLink, useLocation, useNavigate } from "react-router-dom"
import * as loginFunc from "./LoginFunction"
function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();
    const location = useLocation();
     const from = location.state?.from?.pathname || "/dashboard";
    const submitHandler = (e) => {
        e.preventDefault();
        loginFunc.logUserIn({ email, password })
            .then(response => response.data)
            .then(data => {
                const { token } = data;
                sessionStorage.setItem('authToken', token);
                navigate(from, { replace: true });  //dashboard navigation
            })
            .catch(err => {
                console.error("Login failed:", err);

            });
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-[linear-gradient(200deg,_#0066EE_60%,_#9383FB_100%)]">
            <div className="bg-white rounded-2xl shadow-xl p-8 w-full max-w-sm">
                <h2 className="text-2xl font-bold text-center mb-6 text-[#0066EE]">Login</h2>
                <form className="space-y-5" onSubmit={submitHandler}>
                    <div>
                        <label className="block mb-1 text-sm font-medium text-[#0066EE]">Email</label>
                        <input
                            type="email"
                            placeholder="Enter your email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            className="w-full px-4 py-2 rounded-lg border border-[#0066EE] text-[#0066EE] placeholder-[#0066EE] outline-none focus:ring-2 focus:ring-[#0066EE]"
                            required
                        />
                    </div>
                    <div>
                        <label className="block mb-1 text-sm font-medium text-[#0066EE]">Password</label>
                        <input
                            type="password"
                            placeholder="Enter your password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="w-full px-4 py-2 rounded-lg border border-[#0066EE] text-[#0066EE] placeholder-[#0066EE] outline-none focus:ring-2 focus:ring-[#0066EE]"
                            required
                        />
                    </div>
                    <button
                        type="submit"
                        className="w-full py-2 rounded-lg bg-[#0066EE] hover:bg-[#0055cc] text-white font-semibold transition"
                    >
                        Sign In
                    </button>
                </form>

                <p className="text-center text-sm text-[#0066EE] mt-6">
                    Don’t have an account?{" "}
                    <NavLink to="/register" className="font-semibold underline hover:text-[#0055cc]">
                        Create account
                    </NavLink>
                </p>
            </div>
        </div>
    );
}

export default Login