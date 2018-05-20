import React, {Component} from 'react'
import { Media, Table } from 'react-bootstrap'


function getElementValue(elem_id) {
    return document.getElementById(elem_id).value;
}

class EditProfile extends Component {

    constructor(props) {
        super(props);
        this.state = {value: ''};
    }


    handleSubmit(event) {
        event.preventDefault();

        let profile_data = {
            age: parseInt(getElementValue("age")),
            skills: parseInt(getElementValue("skills")),
            region: getElementValue("region"),
            description: getElementValue("description")
        }
     
        $.ajax({
            url: './api/profile',
            type: 'PATCH',
            data: JSON.stringify(profile_data),
            contentType: 'application/json',
            dataType: 'json',
            success: function(result) {
                window.location.assign('/profile');
            }
        });
    }



    render() {
        return (
        <form onSubmit={this.handleSubmit}>

            <h2>Create expedition</h2>
            <div>
            <label><p>Age</p>
                <input type="text" id="age" name="age" required />
            </label>
            </div>

            <div>
            <label><p>Your skill level:</p>
                <select name="skills" id="skills" required >
                    <option selected value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10">10</option>
                </select>
            </label>
            </div>

            <div>
            <label><p>Your region:</p>
                <input type="text" name="region" id="region" required />
            </label>
            </div>

            <div>
            <label><p>Your description:</p>
                <input type="text" name="description" id="description" required />
            </label>
            </div>
            <div>
            <input type="submit" value="Submit" />
            </div>
        </form>
        );
    }

}

export default EditProfile