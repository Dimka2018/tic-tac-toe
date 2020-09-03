import React from "react";
import SearchTable from "../directive/search_table";
import LobbyModal from "../directive/lobby_modal";
import LobbyMenu from "../directive/lobby_menu";

class Search extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            modalMode: false
        }
    }

    toggleModal() {
        this.setState({modalMode: !this.state.modalMode})
    }

    render() {
        const modalMode = this.state.modalMode;
        return (
            <div className="container-fluid">
                <LobbyMenu lobbyButtonCallback={this.toggleModal.bind(this)} />
                <SearchTable/>
                {modalMode && <LobbyModal onCancel={this.toggleModal.bind(this)}/>}
            </div>
        );
    }
}

export default Search;