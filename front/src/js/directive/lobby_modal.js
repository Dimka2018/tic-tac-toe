import React from "react";
import '../../css/lobby_modal.css';

class LobbyModal extends React.Component {

    constructor(props) {
        super(props);
        this.onCancelClick = props.onCancel;
        this.onOkClick = props.onOk;
    }

    handleInput(event) {
        this.setState({value: event.target.value})
    }

    handleOk() {
        this.onOkClick.call(this, this.state.value);
    }

    render() {
        return (
            <div className="background">
                <div className="modal-container">
                    <div className="row mt-2vw flex justify-content-center align-items-center">
                        <span className="lobby-title font-weight-bold">Enter number of games</span>
                    </div>
                    <div className="row flex mt-5vw justify-content-center align-items-center">
                        <input className="game-input" type="number" onInput={this.handleInput.bind(this)} />
                    </div>

                    <div className="row flex mt-7vw justify-content-around align-items-center">
                        <button onClick={this.handleOk.bind(this)} className="modal-button">OK</button>
                        <button onClick={this.onCancelClick.bind(this)} className="modal-button">CANCEL</button>
                    </div>

                </div>
            </div>
        );
    }
}

export default LobbyModal