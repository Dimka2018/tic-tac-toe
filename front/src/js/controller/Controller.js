class Controller {

    constructor() {
        this.consumers = [];
        this.connectedCallbacks = [];
        this.connect();
    }

    connect() {
        this.socket = new WebSocket("ws://localhost:8080");
        this.socket.onopen = () => {
            this.connectedCallbacks.forEach(callback => callback());
        };

        this.socket.onclose = () => {

        };

        this.socket.onmessage = event => {
            console.log(event.data);
            let message = JSON.parse(event.data);
            this.consumers.filter(consumer => consumer.type === message.type)
                .forEach(consumer => {try {consumer.callback(message)} catch (e) {

            }});
        }
    }

    onConnected(callback) {
        this.connectedCallbacks.push(callback);
    }

    subscribe(messageType, callback) {
        this.consumers.push({type: messageType, callback: callback})
    }


    requestLobbies() {
        this.socket.send(JSON.stringify({type: 'requestLobbies'}));
    }

    joinLobby(lobby) {
        this.socket.send(JSON.stringify({type: 'joinLobby', lobby: lobby}))
    }

    createLobby(numberGames) {
        this.socket.send(JSON.stringify({type: 'createLobby', games: numberGames}));
    }

    leaveLobby(lobby) {
        this.socket.send(JSON.stringify({type: 'leaveLobby', lobby: lobby}))
    }

    startGame() {

    }

    leaveGame() {

    }

    goLobby() {
        window.location.href = '/lobby';
    }
}

export default Controller;