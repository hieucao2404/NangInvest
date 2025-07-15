import React, { useState, useRef, useEffect } from "react";
import "./AIChatbox.css";
import { sendMessage } from "../services/ChatService";
import MarkdownRenderer from "./MarkdownRenderer";

const AIChatbox = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [messages, setMessages] = useState([
    {
      id: "welcome-msg",
      content:
        "Hello! I'm NangInvest's AI Learning Assistant. How can I help you today?",
      type: "ai",
    },
  ]);
  const [inputMessage, setInputMessage] = useState("");
  const [isTyping, setIsTyping] = useState(false);
  const messagesEndRef = useRef(null);

  useEffect(() => {
    scrollToBottom();
  }, [messages, isTyping]);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  const handleSend = async () => {
    if (inputMessage.trim() === "") return;

    // Add user message with unique ID
    const userMessageId = `msg-${Date.now()}-${Math.random()
      .toString(36)
      .substring(2, 9)}`;
    setMessages((prev) => [
      ...prev,
      {
        id: userMessageId,
        content: inputMessage,
        type: "user",
      },
    ]);

    // clear input
    setInputMessage("");

    // show typing indicator
    setIsTyping(true);

    try {
      // call backend API
      const response = await sendMessage(inputMessage);

      // Hide typing indicator
      setIsTyping(false);
      if (response.success) {
        // add AI response with unique ID
        const aiMessageId = `msg-${Date.now()}-${Math.random()
          .toString(36)
          .substring(2, 9)}`;
        setMessages((prev) => [
          ...prev,
          {
            id: aiMessageId,
            content: response.message,
            type: "ai",
          },
        ]);
      } else {
        // handle error
        const errorMessageId = `msg-${Date.now()}-${Math.random()
          .toString(36)
          .substring(2, 9)}`;
        setMessages((prev) => [
          ...prev,
          {
            id: errorMessageId,
            content:
              "I'm sorry, I couldn't process your request. Please try again.",
            type: "ai error",
          },
        ]);
      }
    } catch (error) {
      console.error("Chat error:", error);
      // hide typing indicator
      setIsTyping(false);
      // show error message
      const errorMessageId = `msg-${Date.now()}-${Math.random()
        .toString(36)
        .substring(2, 9)}`;
      setMessages((prev) => [
        ...prev,
        {
          id: errorMessageId,
          content:
            "Oops! I am having trouble connecting. Please check your connection and try again.",
          type: "ai error",
        },
      ]);
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  return (
    <>
      {/*chat button when closed*/}
      {!isOpen && (
        <button className="chat-button" onClick={() => setIsOpen(true)}>
          <div className="chat-button-avatar">🤖</div>
          <span className="chat-button-tooltip">Chat with AI Assistant</span>
        </button>
      )}
      {/* Chatbox(when open)*/}
      <div className={`nanginvest-chatbox ${isOpen ? "" : "hidden"}`}>
        {/*Header*/}
        <div className="chatbox-header">
          <div className="chatbox-title">
            <div className="chatbox-avatar">🤖</div>
            <div>
              <div className="chatbox-name">AI Assistant</div>
              <div className="chatbox-status">Powered by NangInvest AI</div>
            </div>
          </div>
          <div className="chatbox-controls">
            <button className="minimize-btn" onClick={() => setIsOpen(false)}>
              -
            </button>
            <button className="close-btn" onClick={() => setIsOpen(false)}>
              x
            </button>
          </div>
        </div>

        <div className="chatbox-messages">
          {messages.map((message) => (
            <div key={message.id} className={`message ${message.type} visible`}>
              {message.type === "ai" && (
                <div className="message-avatar">🤖</div>
              )}
              <div className="message-content">
                {message.type === "ai" ? (
                  <MarkdownRenderer content={message.content} />
                ) : (
                  message.content
                )}
                <div className="message-time">
                  {new Date().toLocaleTimeString([], {
                    hour: "2-digit",
                    minute: "2-digit",
                  })}
                </div>
              </div>
            </div>
          ))}

          {/* Typing indicator */}
          {isTyping && (
            <div className="typing-indicator">
              <span></span>
              <span></span>
              <span></span>
            </div>
          )}

          <div ref={messagesEndRef} />
        </div>
        {/* Input area */}
        <div className="chatbox-input">
          <textarea
            placeholder="Ask me about courses, books, or investing..."
            value={inputMessage}
            onChange={(e) => setInputMessage(e.target.value)}
            onKeyDown={handleKeyDown}
            rows={1}
          />
          <button className="send-btn" onClick={handleSend}>
            <svg
              viewBox="0 0 24 24"
              width="24"
              height="24"
              stroke="currentColor"
              strokeWidth="2"
              fill="none"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <line x1="22" y1="2" x2="11" y2="13"></line>
              <polygon points="22 2 15 22 11 13 2 9 22 2"></polygon>
            </svg>
          </button>
        </div>
      </div>
    </>
  );
};

export default AIChatbox;
