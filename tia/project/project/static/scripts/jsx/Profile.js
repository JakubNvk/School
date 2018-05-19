import React, {Component} from 'react'


class Profile extends Component {

    constructor() {
        super();
        this.state = {
            profile: []
        };
    }

    componentDidMount() {
        let _this = this;

        // change <int:id>
        fetch('./api/profile/<int:id>', {
            credentials: 'same-origin'
        })
        .then(function(response) {
            return response.json();
        })
        .then(function(responseJson) {
            _this.setState({
                expeditions: responseJson.expeditions
            });
        });
    }

    render() {
    }

}

export default Profile