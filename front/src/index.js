import React from 'react';
import ReactDOM from 'react-dom';
import './css/index.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Game from "./js/page/game";
import Search from "./js/page/search";
import Lobby from "./js/page/lobby";
import Controller from "./js/controller/Controller";
import {Route} from 'react-router-dom';
import {Router} from "react-router-dom";
import history from "./js/utils/history";
import Username from "./js/page/username";

let hist = history;
const controller = new Controller(hist);
const routing = (
    <Router history={hist}>
        <div>
            <Route exact path="/" component={props =>
                <Username controller={controller} />
            }/>
            <Route exact path="/search" component={props =>
                <Search controller={controller} />
            }/>
            <Route path='/lobby' component={props =>
                <Lobby controller={controller}/>
            }/>
            <Route path='/game' component={props =>
                <Game controller={controller}/>
            }/>
        </div>
    </Router>
);

controller.onConnected(() => {
    ReactDOM.render(routing, document.getElementById('root'));
});
