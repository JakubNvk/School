import React from 'react';
import ReactDOM from 'react-dom';
import ExpeditionsList from './ExpeditionsList'
import Profile from './Profile'
import Dashboard from './Dashboard'
import CreateExpedition from './CreateExpedition'
import EditProfile from './EditProfile'


var path = window.location.pathname;
if (path == '/dashboard') {
    ReactDOM.render(<Dashboard/>, document.getElementById('dashboard'));
} else if (path == '/expeditions') {
    ReactDOM.render(<ExpeditionsList/>, document.getElementById('expeditions'));
} else if (path == '/create_expedition') {
    ReactDOM.render(<CreateExpedition/>, document.getElementById('create_expedition'));
} else if (path == '/profile') {
    ReactDOM.render(<Profile/>, document.getElementById('profile'));
} else if (path == '/edit_profile') {
    ReactDOM.render(<EditProfile/>, document.getElementById('edit_profile'))
}