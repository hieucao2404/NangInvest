import React from "react";
import "./App.css";
import AIChatbox from "./AIChatbox";

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <h1>NangInvest</h1>
        <p>Your Investment Education Platform</p>
      </header>
      <main className="App-main">
        <div className="container">
          <h2>Welcome to NangInvest</h2>
          <p>
            This is your AI-powered investment education platform. Use the chat
            assistant to get personalized recommendations and answers to your
            investment questions.
          </p>
        </div>
      </main>
      {/* Include the AI Chatbox component */}
      <AIChatbox />
    </div>
  );
}

export default App;
