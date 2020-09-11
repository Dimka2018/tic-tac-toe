import React from "react";
import '../../css/search_table.css';

class SearchTable extends React.Component {

    constructor(props) {
        super(props);
        this.onRowClick = props.onRowClick;
        this.state = {
            lobbies: []
        }
    }


    setLobbies(lobbies) {
        this.setState({lobbies: lobbies})
    }

    render() {
        const rows = this.state.lobbies.map((lobby, index) =>
            <tr key={lobby.id} onClick={() => this.onRowClick(lobby.id)}>
                <td>{index + 1}</td>
                <td>{lobby.id}</td>
                <td>{lobby.hostName}</td>
                <td>{lobby.games}</td>
            </tr>);
        return (
            <div className="table-wrapper-scroll-y search-table">
                <table className="table search-table-content">
                    <thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">Id</th>
                        <th scope="col">Host</th>
                        <th scope="col">Games</th>
                    </tr>
                    </thead>
                    <tbody>
                    {rows}
                    </tbody>
                </table>
            </div>
        );
    }
}

export default SearchTable;