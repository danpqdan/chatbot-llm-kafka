let stompClient = null;
let currentUser = null;

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
    
    connectWebSocket();
}

function connectWebSocket() {
    const socket = new SockJS('http://localhost:9090/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function(response) {
            const message = JSON.parse(response.body);
            displayMessage(message);
        });
    }, function(error) {
        console.log('WebSocket Error: ' + error);
    });
}

function displayMessage(message) {
    const chatBox = document.getElementById('chat-box');
    const messageElement = document.createElement('div');
    messageElement.textContent = message.message;
    messageElement.className = 'message message-received';
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
        message: messageText,
        timestamp: new Date()
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
