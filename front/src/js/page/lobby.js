import React from "react";
import '../../css/lobby.css';
import MessageWindow from "../directive/message_window";

class Lobby extends React.Component {

    constructor(props) {
        super(props);
        this.controller = this.props.controller;
        let params = new URL(window.location.href).searchParams;
        this.state = {
            host: params.get('host'),
            id: params.get('id'),
            users: [],
            modalMode: false
        }
    }

    componentDidMount() {
        this.controller.subscribe('lobby', 'LOBBY_MEMBERS_LIST_CHANGED', message => {
            this.setMembers(message.members)
        });
        this.controller.subscribe('lobby', 'LEAVE_LOBBY', message => {
            this.controller.goLobbySearch();
        });
        this.controller.subscribe('lobby', 'LOBBY_DESTROYED', message => {
            this.openModal('Lobby destroyed');
        });
        this.controller.subscribe('lobby', 'KICK', message => {
            this.openModal('You\'ve been kicked');
        });
        this.controller.subscribe('lobby', 'GAME_STARTED', message => {
            this.controller.goGame();
        });
    }

    openModal(message) {
        this.setState({modalMode: true});
        this.refs.modal.setMessage(message);
    }


    componentWillUnmount() {
        this.controller.unsubscribeAll();
    }

    setMembers(members) {
        this.setState({users: members})
    }

    leaveLobby() {
        this.controller.leaveLobby();
    }

    render() {
        const users = this.state.users.map((user, index) => <div className="user" key={index}>
            <span>{index + 1}</span>
            <span className="user-name">{user.name}</span>
            {(this.state.host === 'true') && !user.host ? <span className="kik-button" onClick={() => this.controller.kik(user.id)}>KIK</span> : <span/>}
        </div>);
        return (
            <div className="background">
                <span className="title">USERS</span>
                <div className="user-container">
                    {users}
                </div>

                <div className="button-container">
                    {this.state.host === 'true' && <button className="lobby-button" onClick={() => this.controller.startGame(this.state.id)} >START</button>}
                    <button className="lobby-button" onClick={this.leaveLobby.bind(this)}>LEAVE</button>
                </div>
                {this.state.modalMode && <MessageWindow onOk={() => this.controller.goLobbySearch()} ref='modal' />}
            </div>
        );
    }
}

export default Lobby;