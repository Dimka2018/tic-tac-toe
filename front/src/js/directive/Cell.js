import React from "react";
import '../../css/cell.css'

class Cell extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            active: true
        }
    }

    handleClick() {
        if (this.state.active) {
            this.setState({active: false, icon: "x"});
        }
    }

    render() {
        const activeClass = this.state.active ? "active" : "inactive";
        const icon = this.state.icon;
        return (
            <div onClick={this.handleClick.bind(this)} className={`cell ${activeClass}`}>
                {(icon === "x") && <div className="symbol">X</div>}
                {(icon === "o") && <div className="symbol">O</div>}
            </div>
        );
    }
}

export default Cell;