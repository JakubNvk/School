import React, {Component} from 'react'
import { Media, Table } from 'react-bootstrap'


function getElementValue(elem_id) {
    return document.getElementById(elem_id).value;
}

class CreateExpedition extends Component {

    constructor(props) {
        super(props);
        this.state = {value: ''};
    }


    handleSubmit(event) {
        event.preventDefault();

        let date = new Date(getElementValue("valid_to"));
        let timestamp = date.getTime() / 1000;
        
        let expedition_data = {
            valid_to: timestamp,
            min_difficulty: parseInt(getElementValue("min_difficulty")),
            max_difficulty: parseInt(getElementValue("max_difficulty")),
            description: getElementValue("description"),
            location: getElementValue("location")
        }
     
        $.ajax({
            url: './api/expeditions',
            type: 'POST',
            data: JSON.stringify(expedition_data),
            contentType: 'application/json',
            dataType: 'json',
            success: function(result) {
                $.ajax({
                    url: './api/expedition/' + result.expedition.id + '/membership',
                    type: 'PUT',
                    dataType: 'json',
                });
                
                window.location.assign('/expeditions');
            }
        });
    }



    render() {
        return (
        <form onSubmit={this.handleSubmit}>

            <h2>Create expedition</h2>
            <div>
            <label><p>When does the expedition take place?</p>
                <input type="date" id="valid_to" name="valid_to" required />
            </label>
            </div>

            <div>
            <label><p>Select minimal expedition difficulty:</p>
                <select name="min_difficulty" id="min_difficulty" required >
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
            <label><p>Select maximum expedition difficulty:</p>
                <select name="max_difficulty" id="max_difficulty" required >
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
            <label><p>Insert location:</p>
                <input type="text" name="location" id="location" required />
            </label>
            </div>

            <div>
            <label><p>Insert description:</p>
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

export default CreateExpedition