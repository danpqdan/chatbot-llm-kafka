let selectedUser = null;

function loadUserPreviews() {
    fetch('http://localhost:8080/messages')
        .then(response => response.json())
        .then(data => {
            const container = document.getElementById('messages-container');
            container.innerHTML = '';
            
            Object.entries(data).forEach(([username, messages]) => {
                const preview = document.createElement('div');
                preview.className = 'user-preview';
                preview.innerHTML = `
                    <h3>${username}</h3>
                    <p>Latest message: ${messages[messages.length - 1]?.message || 'No messages'}</p>
                `;
                preview.onclick = () => window.location.href = `conversation.html?user=${username}`;
                container.appendChild(preview);
            });
        });
}

function showResponseForm(username) {
    selectedUser = username;
    document.getElementById('response-form').style.display = 'block';
    document.getElementById('selected-user').textContent = username;
}

function sendResponse() {
    if (!selectedUser) return;
    
    const message = document.getElementById('response-message').value;
    
    fetch(`http://localhost:8080/messages/${selectedUser}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            message: message
        })
    })
    .then(response => {
        if (response.ok) {
            document.getElementById('response-message').value = '';
            document.getElementById('response-form').style.display = 'none';
            loadUserPreviews();
        }
    });
}

// Load previews every 5 seconds
loadUserPreviews();
setInterval(loadUserPreviews, 5000);
