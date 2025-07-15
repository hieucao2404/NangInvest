/**
 * NangInvest AI Chat Service
 * Handles communication with the backend AIChatServlet
 */

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || '';
const API_URL = `${API_BASE_URL}/api/chat`;

/**
 * Send a message to the AI and get a response
 * @param {string} message - The user's message
 * @returns {Promise<Object>} - The AI response object
 */
export const sendMessage = async (message) => {
  try {
    console.log(`Sending message to ${API_URL}`);
    const formData = new FormData();
    formData.append("message", message);

    const response = await fetch(API_URL, {
      method: "POST",
      body: formData,
      credentials: "same-origin",
    });

    return await response.json();
  } catch (error) {
    console.error("Error sending message:", error);
    throw error;
  }
};

/**
 * Get chat history if needed
 * @returns {Promise<Array>} - Array of message objects
 */
export const getChatHistory = async () => {
  try {
    const response = await fetch(`${API_URL}/history`, {
      method: "GET",
      credentials: "same-origin",
    });

    return await response.json();
  } catch (error) {
    console.error("Error fetching chat history:", error);
    return [];
  }
};
