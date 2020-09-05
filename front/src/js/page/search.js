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
            this.searchTable.setLobbies(message.lobbies);
        });

        this.controller.subscribe('LOBBY_CREATED', message => {
            this.controller.goLobby();
        });

        this.controller.requestLobbies();
    }

    createLobby(numberGames) {
        this.controller.createLobby(numberGames)
    }

    render() {
        const modalMode = this.state.modalMode;
        this.searchTable = <SearchTable/>;
        return (
            <div className="container-fluid">
                <LobbyMenu lobbyButtonCallback={this.toggleModal.bind(this)} />
                {this.searchTable}
                {modalMode && <LobbyModal onOk={this.createLobby.bind(this)} onCancel={this.toggleModal.bind(this)}/>}
            </div>
        );
    }
}

export default Search;