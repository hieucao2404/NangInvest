// src/App.js
import React from "react";
import "./components/App.css";
import AIChatbox from "./components/AIChatbox";

function App() {
  return (
    <div className="app">
      <header className="app-header">
        <h1>NangInvest</h1>
        <p>Your Investment Education Platform</p>
      </header>
      <main className="app-main">
        <div className="container">
          <h2>Welcome to NangInvest</h2>
          <p>
            This is your AI-powered investment education platform. Use the chat
            assistant to get personalized recommendations and answers to your
            investment questions.
          </p>
        </div>
      </main>
      <AIChatbox />
    </div>
  );
}

export default App;
