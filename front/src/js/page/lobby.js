import React from "react";
import '../../css/lobby.css';

class Lobby extends React.Component {

    constructor(props) {
        super(props);
        this.controller = this.props.controller;
        this.state = {
            host: props.host,
            users: []
        }
    }

    componentDidMount() {
        this.controller.subscribe('LOBBY_MEMBERS_LIST_CHANGED', message => {
            this.setMembers({users: message.members})
        });
    }

    setMembers(members) {
        this.setState({users: members})
    }

    render() {
        const users = this.state.users.map((user, index) => <div className="user" key={index}>
            <span>{index}</span>
            <span>{user.name}</span>
            <span className="kik-button">KIK</span>
        </div>);
        return (
            <div className="background">
                <span className="title">USERS</span>
                <div className="user-container">
                    {users}
                </div>

                <div className="button-container">
                    <button className="lobby-button">START</button>
                    <button className="lobby-button">LEAVE</button>
                </div>
            </div>
        );
    }
}

export default Lobby;