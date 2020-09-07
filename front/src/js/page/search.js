import React from "react";
import SearchTable from "../directive/search_table";
import LobbyModal from "../directive/lobby_modal";
import LobbyMenu from "../directive/lobby_menu";

class Search extends React.Component {

    constructor(props) {
        super(props);
        this.controller = props.controller;
        this.state = {
            modalMode: false
        }
    }

    toggleModal() {
        this.setState({modalMode: !this.state.modalMode})
    }

    componentDidMount() {
        this.controller.subscribe('LOBBY_LIST_CHANGED', message => {
            this.refs.searchTable.setLobbies(message.lobbies);
        });

        this.controller.subscribe('LOBBY_CREATED', message => {
            this.controller.goLobby(true, message.id);
        });

        this.controller.subscribe('JOINED_TO_LOBBY', message => {
            this.controller.goLobby(false, message.lobbyId);
        });

        this.controller.requestLobbies();
    }

    createLobby(numberGames) {
        this.controller.createLobby(numberGames)
    }

    joinLobby(id) {
        this.controller.joinLobby(id);
    }

    render() {
        const modalMode = this.state.modalMode;
        return (
            <div className="container-fluid">
                <LobbyMenu lobbyButtonCallback={this.toggleModal.bind(this)} />
                <SearchTable ref="searchTable" onRowClick={this.joinLobby.bind(this)}/>
                {modalMode && <LobbyModal onOk={this.createLobby.bind(this)} onCancel={this.toggleModal.bind(this)}/>}
            </div>
        );
    }
}

export default Search;