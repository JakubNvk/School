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
        var expeditions = this.state.expeditions;
        var stringify = JSON.stringify(expeditions);
        var exp = JSON.parse(stringify);

        // return (
            // <div>
            // {this.state.expeditions.map(function(exp) {
                // return (
                // <div key={e.id} className="e">
                    // <p>Description: {e.description}</p>
                    // <p>Min_difficulty: {e.min_difficulty}</p>
                    // <p>Max_difficulty: {e.max_difficulty}</p>
                    // <p>Members: {e.members}</p>
                // </div>
                // );
            // })}
            // </div>
        // )
    }

}

export default ExpeditionsList