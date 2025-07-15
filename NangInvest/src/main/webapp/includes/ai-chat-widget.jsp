<%-- AI Chat Widget Include --%> <%-- This file can be included in any JSP page
to add the AI chat functionality --%>

<!-- AI Chat Widget -->
<div id="ai-chat-widget">
  <!-- Chat Toggle Button (shown when chat is closed) -->
  <button id="chat-toggle" class="chat-button">
    <div class="chat-button-avatar">🤖</div>
    <span class="chat-button-tooltip">Chat with AI Assistant</span>
  </button>

  <!-- Chat Box (hidden by default) -->
  <div id="chat-box" class="nanginvest-chatbox hidden">
    <!-- Chat Header -->
    <div class="chatbox-header">
      <div class="chatbox-title">
        <div class="chatbox-avatar">🤖</div>
        <div>
          <div class="chatbox-name">AI Assistant</div>
          <div class="chatbox-status">Powered by NangInvest AI</div>
        </div>
      </div>
      <div class="chatbox-controls">
        <button id="chat-minimize" class="minimize-btn" title="Minimize">
          -
        </button>
        <button id="chat-close" class="close-btn" title="Close">×</button>
      </div>
    </div>

    <!-- Chat Messages Container -->
    <div id="chat-messages" class="chatbox-messages">
      <!-- Welcome message will be added by JavaScript -->
    </div>

    <!-- Typing Indicator -->
    <div id="typing-indicator" class="typing-indicator hidden">
      <div class="message ai-message">
        <div class="message-avatar">🤖</div>
        <div class="message-content">
          <div class="typing-dots">
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>
      </div>
    </div>

    <!-- Chat Input Area -->
    <div class="chatbox-input">
      <textarea
        id="message-input"
        placeholder="Ask me about courses, books, or investing..."
        rows="1"
        maxlength="1000"
      ></textarea>
      <button id="send-button" class="send-btn" title="Send message">
        <svg
          viewBox="0 0 24 24"
          width="20"
          height="20"
          stroke="currentColor"
          stroke-width="2"
          fill="none"
          stroke-linecap="round"
          stroke-linejoin="round"
        >
          <line x1="22" y1="2" x2="11" y2="13"></line>
          <polygon points="22,2 15,22 11,13 2,9 22,2"></polygon>
        </svg>
      </button>
    </div>

    <!-- Quick Actions -->
    <div class="quick-actions">
      <button
        class="quick-action-btn"
        data-message="What courses do you recommend for beginners?"
      >
        📚 Courses
      </button>
      <button
        class="quick-action-btn"
        data-message="Tell me about investment basics"
      >
        📈 Basics
      </button>
      <button class="quick-action-btn" data-message="What books should I read?">
        📖 Books
      </button>
      <button class="quick-action-btn" data-message="Show me my learning path">
        �️ Path
      </button>
    </div>
  </div>
</div>

<!-- Chat Widget Styles -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/chat.css" />

<!-- Chat Widget JavaScript -->
<script src="${pageContext.request.contextPath}/js/chat.js"></script>
