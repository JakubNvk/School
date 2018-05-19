import flask
from flask_wtf import Form
from sqlalchemy.orm.exc import MultipleResultsFound, NoResultFound
from wtforms import fields
from wtforms.validators import (Email, InputRequired, ValidationError,
                                DataRequired, NumberRange)
from .validators import DateValidator

from .models import User


class LoginForm(Form):
    email = fields.StringField(validators=[InputRequired(), Email()])
    password = fields.StringField(validators=[InputRequired()])

    def validate_password(form, field):
        try:
            user = User.query.filter(User.email == form.email.data).one()
        except (MultipleResultsFound, NoResultFound):
            raise ValidationError("Invalid user")
        if user is None:
            raise ValidationError("Invalid user")
        if not user.is_valid_password(form.password.data):
            raise ValidationError("Invalid password")

        form.user = user


class RegistrationForm(Form):
    email = fields.StringField(validators=[InputRequired(), Email()])
    password = fields.StringField(validators=[InputRequired()])

    def validate_email(form, field):
        user = User.query.filter(User.email == field.data).first()
        if user is not None:
            raise ValidationError("A user with that email already exists.")


class ProfileForm(Form):
    age = fields.IntegerField(validators=[NumberRange(min=1, max=115)])
    skills = fields.IntegerField(validators=[NumberRange(min=1, max=10)])
    region = fields.StringField(validators=[DataRequired()])
    description = fields.StringField()


class ExpeditionForm(Form):
    valid_to = fields.StringField(validators=[DateValidator()])
    min_difficulty = fields.IntegerField(default=1,
                                         validators=[NumberRange(min=1, max=10)])
    max_difficulty = fields.IntegerField(default=10,
                                         validators=[NumberRange(min=1, max=10)])
    location = fields.StringField(validators=[DataRequired()])
    description = fields.StringField(validators=[DataRequired()])