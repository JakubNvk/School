import React, {Component} from 'react'
import { Media, Table } from 'react-bootstrap'


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
            _this.setState({expeditions: data.expeditions});
          }.bind(this),
          error: function(xhr, status, err) {
            console.error('./api/expeditions', status, err.toString());
          }.bind(this)
        });
    }

    createRender() {
        let expeditions = this.state.expeditions;
        let table = [];

        for (let i = 0; i < expeditions.length; i++) {
            let expedition = expeditions[i];
            let members = expedition.members;

            let attendees = [];
            for (let j = 0; j < members.length; j++) {
                attendees.push(<p>{members[j].email}</p>);
            }
            
            table.push(
                <Media>
                    <Media.Body>
                        <Media.Heading componentClass='h2'>Expedition to: {expedition.description}</Media.Heading>
                        <Table striped bordered condensed hover>
                            <thead>
                              <tr>
                                <th>Created</th>
                                <th>Valid to</th>
                                <th>Difficulty</th>
                                <th>Attendees</th>
                              </tr>
                            </thead>
                            <tbody>
                                <tr>
                                  <td>{expedition.created_at}</td>
                                  <td>{expedition.valid_to}</td>
                                  <td>{expedition.min_difficulty}-{expedition.max_difficulty}</td>
                                  <td>{attendees}</td>
                                </tr>
                            </tbody>
                        </Table>
                    </Media.Body>
                </Media>
                );
        }
        return table;
    }

    render() {
        var expeditions = this.state.expeditions;
        
        if (expeditions == []) {
            return null
        } else {
            return (
                <div>
                    {this.createRender()}
                </div>
                )
        }
    }
}

export default ExpeditionsList