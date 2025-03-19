let currentUser = new URLSearchParams(window.location.search).get('user');

function loadConversation() {
    document.getElementById('current-user').textContent = currentUser;
    
    fetch(`http://localhost:8080/messages/${currentUser}`)
        .then(response => response.json())
        .then(data => {
            if (!Array.isArray(data)) {
                data = [];
            }
            const container = document.getElementById('conversation-messages');
            container.innerHTML = '';
            
            data.forEach(msg => {
                const messageDiv = document.createElement('div');
                messageDiv.className = `message ${msg.sender === 'Employee Support' ? 'message-sent' : 'message-received'}`;
                messageDiv.innerHTML = `
                    <strong>${msg.sender}</strong>
                    <p>${msg.message}</p>
                `;
                container.appendChild(messageDiv);
            });
            
            container.scrollTop = container.scrollHeight;
        })
        .catch(error => console.error('Error loading conversation:', error));
}
function sendResponse() {
    const message = document.getElementById('response-message').value;
    
    fetch(`http://localhost:8080/messages/${currentUser}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            sender: 'Employee Support',
            message: message
        })
    })
    .then(response => {
        if (response.ok) {
            document.getElementById('response-message').value = '';
            loadConversation();
        }
    });
}

// Load conversation every 2 seconds
loadConversation();
setInterval(loadConversation, 2000);
