import React, {Component} from 'react'


class ExpeditionsList extends Component {

    constructor() {
        super();
        this.state = {
            expeditions: []
        };
    }

    componentDidMount() {
        let _this = this;

        fetch('./api/expeditions', {
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
        // var expeditions = this.state.expeditions;
        // var stringify = JSON.stringify(expeditions);
        // var exp = JSON.parse(stringify);

        return(<div></div>)
    }

}

export default ExpeditionsList