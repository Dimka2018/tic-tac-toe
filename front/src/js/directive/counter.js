import React from "react";
import '../../css/counter.css';

class Counter extends React.Component {

    constructor(props) {
        super(props);
        this.state = {myScore: 0, enemyScore: 0}
    }

    render() {
        const {myScore, enemyScore} = this.state;
        return (
            <div className="score_container">
                <span className="score">{myScore}</span>
                <span className="separator">:</span>
                <span className="score">{enemyScore}</span>
            </div>
        );
    }
}

export default Counter;