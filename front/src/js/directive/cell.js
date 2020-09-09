import React from "react";
import '../../css/cell.css'

class Cell extends React.Component {

    constructor(props) {
        super(props);
        this.onActivate = props.onActivate;
        this.state = {
            active: true
        }
    }

    handleClick() {
        if (this.state.active) {
            this.setState({active: false, icon: "x"});
            this.onActivate(this.props.row, this.props.col);
        }
    }

    setActiveState(num) {
        let icon;
        if (num === 1) {
            icon = 'x';
            this.setState({active: false, icon: icon})
        } else if (num === 2) {
            icon = 'o';
            this.setState({active: false, icon: icon})
        } else {
            this.setState({active: true, icon: ''})
        }
    }

    setActive() {
        if (!this.state.icon) {
            this.setState({active: true})
        }
    }

    setInactive() {
        this.setState({active: false})
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