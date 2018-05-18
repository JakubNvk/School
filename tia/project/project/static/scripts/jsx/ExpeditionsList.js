import React, {Component} from 'react'


class ExpeditionsList extends Component {

    constructor() {
        super();
        this.state = {
            expeditions: null,
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
                expeditions: responseJson
            })
        });
    }

    render() {
        var expeditions = this.state.expeditions;
        var stringify = JSON.stringify(expeditions);
        var exp = JSON.parse(stringify);
        console.log('exp', exp)

        return(<div></div>)
    }

}

export default ExpeditionsList