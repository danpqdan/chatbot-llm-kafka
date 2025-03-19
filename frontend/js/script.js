let stompClient = null;
let currentUser = null;

async function loadPreviousMessages(username) {
    try {
        const response = await fetch(`http://localhost:9090/messages/${username}`);
        const messages = await response.json();
        if (Array.isArray(messages)) {
            messages.forEach(message => displayMessage(message));
        }
    } catch (error) {
        console.error('Error loading messages:', error);
    }
}

function registerUser() {
    const username = document.getElementById('username').value;
    if (!username) {
        alert('Please enter a username');
        return;
    }

    currentUser = username;
    document.getElementById('login-form').style.display = 'none';
    document.getElementById('chat-container').style.display = 'block';
    document.getElementById('current-user').textContent = `Logged in as: ${username}`;

    loadPreviousMessages(username);
    connectWebSocket(username);
}

function connectWebSocket(username) {
    const socket = new SockJS('http://localhost:9090/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        // Alterando a rota para mensagens privadas
        stompClient.subscribe(`/user/${username}/messages`, function (response) {
            const message = JSON.parse(response.body);
            displayMessage(message);
        });
    });

    // Se a conexão cair, tentar reconectar
    socket.onclose = function () {
        console.warn('WebSocket disconnected, attempting to reconnect...');
        setTimeout(() => connectWebSocket(username), 5000);
    };
}

function displayMessage(message) {
    const chatBox = document.getElementById('chat-box');
    const messageElement = document.createElement('div');
    messageElement.className = `message ${message.sender === 'Employee Support' ? 'message-support' : 'message-user'}`;
    messageElement.innerHTML = `
        <strong>${message.sender}</strong>
        <p>${message.message}</p>
    `;
    chatBox.appendChild(messageElement);
    chatBox.scrollTop = chatBox.scrollHeight;
}

document.getElementById('messageForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    if (!currentUser) {
        alert('Please login first');
        return;
    }

    const messageText = document.getElementById('message').value;
    const statusDiv = document.getElementById('status');

    const requestData = {
        sender: currentUser,
        message: messageText
    };

    try {
        const response = await fetch('http://localhost:9090/message', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        });

        if (response.ok) {
            statusDiv.textContent = 'Message sent successfully!';
            statusDiv.className = 'status success';
            document.getElementById('message').value = '';
        } else {
            const errorData = await response.text();
            statusDiv.textContent = `Error: ${errorData}`;
            statusDiv.className = 'status error';
        }
    } catch (error) {
        statusDiv.textContent = `Error: ${error.message}`;
        statusDiv.className = 'status error';
    }
});
