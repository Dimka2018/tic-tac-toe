import React, {Component} from "react";
import '../../css/message_window.css';

class MessageWindow extends Component {

    constructor(props) {
        super(props);
        this.state = {
            message: ''
        }
    }

    setMessage(message) {
        this.setState({message: message})
    }

    render() {
        return (
            <div className='message-window-background'>
                <div className="message-container">
                    <div className="message">{this.state.message}</div>
                    <button onClick={() => this.props.onOk()}>OK</button>
                </div>
            </div>
        );
    }
}
export default MessageWindow;