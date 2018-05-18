import React, {Component} from 'react'


class ExpeditionsList extends Component {

    constructor() {
        super();
        this.state = {
            expeditions: [],
        };
    }

    componentDidMount() {
        fetch('http://127.0.0.1:5000/api/expeditions')
        .then(results => {
            return results.json();
        }).then(data => {
            this.setState({expeditions: data.results});
            console.log(this.state.expeditions);
        })
    }

    render() {
        return (
            <div id='exp'>
                {this.state.expeditions}
            </div>
            )
    }

}

export default ExpeditionsList