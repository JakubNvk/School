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

        $.ajax({
          url: './api/expeditions',
          dataType: 'json',
          cache: false,
          success: function(data) {
            this.setState({expeditions: data});
          }.bind(this),
          error: function(xhr, status, err) {
            console.error('./api/expeditions', status, err.toString());
          }.bind(this)
        });
    }

    render() {
        var expeditions = this.state.expeditions;
        console.log(expeditions)
        return (<div></div>)
    }
}

export default ExpeditionsList