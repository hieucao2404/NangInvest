/**
 * NangInvest AI Chat Widget
 * Integrates with the backend AIChatServlet
 */

class NangInvestChatWidget {
  constructor() {
    this.isOpen = false;
    this.messages = [];
    this.isTyping = false;

    this.initializeElements();
    this.attachEventListeners();
    this.addWelcomeMessage();
  }

  initializeElements() {
    this.chatToggle = document.getElementById("chat-toggle");
    this.chatBox = document.getElementById("chat-box");
    this.chatMessages = document.getElementById("chat-messages");
    this.messageInput = document.getElementById("message-input");
    this.sendButton = document.getElementById("send-button");
    this.chatMinimize = document.getElementById("chat-minimize");
    this.chatClose = document.getElementById("chat-close");
  }

  attachEventListeners() {
    if (this.chatToggle) {
      this.chatToggle.addEventListener("click", () => this.toggleChat());
    }

    if (this.chatMinimize) {
      this.chatMinimize.addEventListener("click", () => this.closeChat());
    }

    if (this.chatClose) {
      this.chatClose.addEventListener("click", () => this.closeChat());
    }

    if (this.sendButton) {
      this.sendButton.addEventListener("click", () => this.sendMessage());
    }

    if (this.messageInput) {
      this.messageInput.addEventListener("keydown", (e) => {
        if (e.key === "Enter" && !e.shiftKey) {
          e.preventDefault();
          this.sendMessage();
        }
      });

      this.messageInput.addEventListener("input", () =>
        this.autoResizeTextarea()
      );
    }
  }

  toggleChat() {
    if (this.isOpen) {
      this.closeChat();
    } else {
      this.openChat();
    }
  }

  openChat() {
    this.isOpen = true;
    if (this.chatBox) {
      this.chatBox.classList.remove("hidden");
    }
    if (this.chatToggle) {
      this.chatToggle.style.display = "none";
    }
    if (this.messageInput) {
      this.messageInput.focus();
    }
  }

  closeChat() {
    this.isOpen = false;
    if (this.chatBox) {
      this.chatBox.classList.add("hidden");
    }
    if (this.chatToggle) {
      this.chatToggle.style.display = "flex";
    }
  }

  addWelcomeMessage() {
    const welcomeMessage = {
      id: "welcome-msg",
      content: `Hi! I'm your AI investment assistant. I can help you with:

• **Course recommendations** - Find the perfect courses for your level
• **Investment basics** - Learn fundamental concepts
• **Book suggestions** - Get curated reading lists
• **Market insights** - Stay updated with trends

What would you like to learn about today? 😊`,
      type: "ai",
      timestamp: new Date(),
    };
    this.messages.push(welcomeMessage);
    if (this.chatMessages) {
      this.renderMessage(welcomeMessage);
    }
  }

  async sendMessage() {
    const message = this.messageInput ? this.messageInput.value.trim() : "";
    if (!message) return;

    // Add user message
    this.addMessage(message, "user");
    if (this.messageInput) {
      this.messageInput.value = "";
      this.autoResizeTextarea();
    }

    // Show typing indicator
    this.showTypingIndicator();

    try {
      // Send message to backend
      const response = await this.callBackendAPI(message);

      // Hide typing indicator
      this.hideTypingIndicator();

      // Add AI response
      if (response?.success) {
        this.addMessage(response.message, "ai");
      } else if (response?.error) {
        // Handle error response
        this.addMessage(response.error, "ai error");
      } else {
        this.addMessage(
          "I'm sorry, I couldn't process your request. Please try again.",
          "ai error"
        );
      }
    } catch (error) {
      console.error("Chat error:", error);
      this.hideTypingIndicator();

      // More detailed error handling
      let errorMessage = "Oops! I'm having trouble connecting. ";
      if (error.name === "TypeError" && error.message.includes("fetch")) {
        errorMessage += "Please check if the server is running.";
      } else if (error.message.includes("404")) {
        errorMessage += "Chat service not found. Please contact support.";
      } else if (error.message.includes("500")) {
        errorMessage += "Server error. Please try again later.";
      } else {
        errorMessage += "Please check your connection and try again.";
      }

      this.addMessage(errorMessage, "ai error");
    }
  }

  async callBackendAPI(message) {
    try {
      const formData = new FormData();
      formData.append("message", message);

      const response = await fetch("/NangInvest/api/chat", {
        method: "POST",
        body: formData,
        credentials: "same-origin",
      });

      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }

      const contentType = response.headers.get("content-type");
      if (contentType?.includes("application/json")) {
        return await response.json();
      } else {
        // If not JSON, treat as plain text
        const text = await response.text();
        return {
          success: true,
          message: text,
        };
      }
    } catch (error) {
      console.error("Backend API call failed:", error);
      throw error;
    }
  }

  addMessage(content, type) {
    const messageId = `msg-${Date.now()}-${Math.random()
      .toString(36)
      .substring(2, 9)}`;
    const message = {
      id: messageId,
      content: content,
      type: type,
      timestamp: new Date(),
    };

    this.messages.push(message);
    this.renderMessage(message);
    this.scrollToBottom();
  }

  renderMessage(message) {
    if (!this.chatMessages) return;

    const messageElement = document.createElement("div");
    messageElement.className = `message ${message.type}`;
    messageElement.setAttribute("data-id", message.id);

    const timeString = message.timestamp.toLocaleTimeString([], {
      hour: "2-digit",
      minute: "2-digit",
    });

    if (message.type === "ai" || message.type === "ai error") {
      messageElement.innerHTML = `
                <div class="message-avatar">🤖</div>
                <div class="message-content">
                    <div class="message-text">${this.formatMessageContent(
                      message.content
                    )}</div>
                    <span class="message-time">${timeString}</span>
                </div>
            `;
    } else {
      messageElement.innerHTML = `
                <div class="message-content">
                    <div class="message-text">${this.escapeHtml(
                      message.content
                    )}</div>
                    <span class="message-time">${timeString}</span>
                </div>
            `;
    }

    this.chatMessages.appendChild(messageElement);
  }

  formatMessageContent(content) {
    // Escape HTML first
    const escaped = this.escapeHtml(content);

    // Simple markdown-like formatting
    return escaped
      .replace(/\*\*(.*?)\*\*/g, "<strong>$1</strong>")
      .replace(/\*(.*?)\*/g, "<em>$1</em>")
      .replace(/\n/g, "<br>")
      .replace(/•\s(.*?)(?:<br>|$)/g, "<li>$1</li>")
      .replace(/(<li>.*?<\/li>)+/g, "<ul>$&</ul>");
  }

  escapeHtml(text) {
    const div = document.createElement("div");
    div.textContent = text;
    return div.innerHTML;
  }

  showTypingIndicator() {
    if (this.isTyping || !this.chatMessages) return;

    this.isTyping = true;
    const typingElement = document.createElement("div");
    typingElement.className = "typing-indicator";
    typingElement.id = "typing-indicator";
    typingElement.innerHTML = `
            <div class="message-avatar">🤖</div>
            <div class="typing-dots">
                <span></span>
                <span></span>
                <span></span>
            </div>
        `;

    this.chatMessages.appendChild(typingElement);
    this.scrollToBottom();
  }

  hideTypingIndicator() {
    this.isTyping = false;
    const typingElement = document.getElementById("typing-indicator");
    if (typingElement) {
      typingElement.remove();
    }
  }

  autoResizeTextarea() {
    if (!this.messageInput) return;

    this.messageInput.style.height = "auto";
    this.messageInput.style.height =
      Math.min(this.messageInput.scrollHeight, 100) + "px";
  }

  scrollToBottom() {
    if (this.chatMessages) {
      this.chatMessages.scrollTop = this.chatMessages.scrollHeight;
    }
  }
}

// Initialize chat widget when DOM is loaded
document.addEventListener("DOMContentLoaded", function () {
  // Only initialize if chat elements exist
  if (
    document.getElementById("chat-toggle") ||
    document.getElementById("chat-box")
  ) {
    window.chatWidget = new NangInvestChatWidget();
  }
});

// Export for use in other scripts if needed
window.NangInvestChatWidget = NangInvestChatWidget;
