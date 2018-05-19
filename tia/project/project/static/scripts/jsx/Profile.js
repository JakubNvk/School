import React, {Component} from 'react'
import { Media, Table } from 'react-bootstrap'


class Profile extends Component {

    constructor() {
        super();
        this.state = {
            profile: []
        };
    }

    componentDidMount() {
        let _this = this;

        $.ajax({
          url: './api/profile/<int:id>',
          dataType: 'json',
          cache: false,
          success: function(data) {
            _this.setState({profile: data.profile});
          }.bind(this),
          error: function(xhr, status, err) {
            console.error('./api/profile/<int:id>', status, err.toString());
          }.bind(this)
        });
    }

    createRender() {
        let profile = this.state.profile;
        let table = [];

        table.push(
            <Media>
                <Media.Body>
                    <Media.Heading componentClass='h2'>{profile.user.email}</Media.Heading>
                    <Table striped bordered condensed hover>
                        <thead>
                            <tr>
                                <th>Age</th>
                                <th>Skill level</th>
                                <th>Region</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>{profile.age}</td>
                                <td>{profile.skills}</td>
                                <td>{profile.region}</td>
                            </tr>
                        </tbody>
                    </Table>
                </Media.Body>
            </Media>
            );
        
        return table;
    }

    render() {
        var profile = this.state.profile;
        
        if (profile == []) {
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

export default Profile