import datetime
from random import SystemRandom

from backports.pbkdf2 import pbkdf2_hmac, compare_digest
from flask.ext.login import UserMixin
from sqlalchemy.ext.hybrid import hybrid_property

from project.data import db, CRUDMixin



class User(UserMixin, CRUDMixin, db.Model):
    __tablename__ = 'user'

    id = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String(50), unique=True)
    _password = db.Column(db.LargeBinary(120))
    _salt = db.Column(db.String(120))
    created_at = db.Column(db.DateTime, default=datetime.datetime.now)
    profile = db.relationship('Profile', backref='user')
    created_expeditions = db.relationship('Expedition')
    comments = db.relationship('Comment')
    expeditions = db.relationship('Expedition')
    received_msgs = db.relationship('Message')
    sent_msgs = db.relationship('Message')

    @hybrid_property
    def password(self):
        return self._password

    @password.setter
    def password(self, value):
        if self._salt is None:
            self._salt = bytes(SystemRandom().getrandbits(128))
        self._password = self._hash_password(value)

    def is_valid_password(self, password):
        new_hash = self._hash_password(password)
        return compare_digest(new_hash, self._password)

    def _hash_password(self, password):
        pwd = password.encode("utf-8")
        salt = bytes(self._salt)
        buff = pbkdf2_hmac("sha512", pwd, salt, iterations=100000)
        return bytes(buff)


class Profile(db.Model):
    __tablename__ = 'profile'

    id = db.Column(db.Integer, primary_key=True)
    user = db.Column(db.Integer, db.ForeignKey('user.id'))
    age = db.Column(db.Integer)
    skills = db.Column(db.Integer, default=0)
    region = db.Column(db.String)
    description = db.Column(db.String)


class Comment(db.Model):
    __tablename__ = 'comment'

    id = db.Column(db.Integer, primary_key=True)
    user = db.relationship('User')
    text = db.Column(db.String)
    expedition = db.relationship('Expedition')


class Message(db.Model):
    __tablename__ = 'message'

    id = db.Column(db.Integer, primary_key=True)
    recipient = db.relationship('User')
    sender = db.relationship('User')
    text = db.Column(db.String)
    created = db.Column(db.DateTime)



class Expedition(db.Model):
    __tablename__ = 'expedition'

    plus_year = datetime.datetime.now() + datetime.timedelta(days=365)
    
    id = db.Column(db.Integer, primary_key=True)
    valid_to = db.Column(db.DateTime, default=plus_year)
    creator = db.relationship('User')
    created_at = db.Column(db.DateTime, default=datetime.datetime.now)
    deleted_at = db.Column(db.DateTime, default=plus_year)
    min_difficulty = db.Column(db.Integer, default=0)
    max_difficulty = db.Column(db.Integer, default=10)
    description = db.Column(db.String)
    comments = db.relationship('Comment')
    locations = db.relationship('Location')
    members = db.relationship('User')


class Location(db.Model):
    __tablename__ = 'location'

    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String)
    expeditions = db.relationship('Expedition')