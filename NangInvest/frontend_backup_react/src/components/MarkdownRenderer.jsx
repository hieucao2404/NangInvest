import React from "react";

const MarkdownRenderer = ({ content }) => {
  // Simple markdown parsing for basic formatting
  const parseMarkdown = (text) => {
    if (!text) return "";

    // Process bold text
    let html = text.replace(/\*\*(.*?)\*\*/g, "<strong>$1</strong>");

    // Process italic text
    html = html.replace(/\*(.*?)\*/g, "<em>$1</em>");

    // Process line breaks
    html = html.replace(/\n/g, "<br>");

    // Process bullet points
    html = html.replace(/•\s(.*?)(?:<br>|$)/g, "<li>$1</li>");
    html = html.replace(/<li>(.*?)<\/li>(?:<br>|$)(<li>)/g, "<li>$1</li>$2");
    html = html.replace(/(<li>.*?<\/li>)(?:<br>|$)/g, "<ul>$1</ul>");

    // Process numbered lists
    html = html.replace(/(\d+)\.\s(.*?)(?:<br>|$)/g, '<li value="$1">$2</li>');
    html = html.replace(
      /<li value=".*?">(.*?)<\/li>(?:<br>|$)(<li value=")/g,
      "<li>$1</li>$2"
    );
    html = html.replace(
      /(<li value=".*?">.*?<\/li>)(?:<br>|$)/g,
      "<ol>$1</ol>"
    );

    return html;
  };

  return (
    <div
      className="markdown-content"
      dangerouslySetInnerHTML={{ __html: parseMarkdown(content) }}
    />
  );
};

export default MarkdownRenderer;
