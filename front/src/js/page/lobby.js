import React from "react";
import '../../css/lobby.css';

class Lobby extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            host: props.host,
            users: [
                {id: 1, name: 'Dimka'},
                {id: 1, name: 'Dimka'},
                {id: 1, name: 'Dimka'},
                {id: 1, name: 'Dimka'},
                {id: 1, name: 'Dimka'},
                {id: 1, name: 'Dimka'},
                {id: 1, name: 'Dimka'},
                {id: 1, name: 'Dimka'},
                {id: 1, name: 'Dimka'},
                {id: 1, name: 'Dimka'},
                {id: 1, name: 'Dimka'}
            ]
        }
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