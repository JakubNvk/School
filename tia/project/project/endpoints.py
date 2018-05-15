from flask import abort, request
from flask.json import jsonify, loads
from flask_restful import Resource
from flask.ext.login import current_user

from sqlalchemy import and_

from .data import db


class ProfileList(Resource):
    def get(self):
        ''' Get list of all profiles. '''
        try:
            profiles = Profile.query.all()

            return jsonify(profiles)
        except Exception as e:
            abort(500)

    def post(self):
        ''' Add new profile. '''
        
        try:
            profile_data = loads(request.data)
            profile = Profile.create(**profile_data)
            
            return jsonify(profile)
        except Exception as e:
            abort(500)


class ProfileDetail(Resource):
    def get(self, id):
        ''' Get user profile. '''

        try:
            profile = Profile.query.filter(Profile.id == id).one()
            
            return jsonify(profile)
        except Exception as e:
            abort(404)

    def patch(self, id):
        ''' Edit user profile. '''

        user = current_user

        try:
            profile = Profile.query.filter(Profile.id == id).one()
            if profile.user.id != user.id:
                abort(403)

            profile_data = loads(request.data)
            profile = profile.update(**profile_data)

            return jsonify(profile)
        except Exception as e:
            abort(500)

    def delete(self, id):
        ''' Delete user profile. '''

        user = current_user

        try:
            profile = Profile.query.filter(Profile.id == id).one()
            if profile.user.id != user.id:
                abort(403)
            profile.delete()

            return 200
        except Exception as e:
            abort(500)


class ExpeditionList(Resource):
    def get(self):
        ''' Get list of all expeditions. '''
        try:
            expeditions = Expedition.query.all()

            return jsonify(profiles)
        except Exception as e:
            abort(500)


    def post(self):
        ''' Add new expeditions. '''

        try:
            expedition_data = loads(request.data)
            expedition = Expedition.create(**expedition_data)
            
            return jsonify(expedition)
        except Exception as e:
            abort(500)


class ExpeditionDetail(Resource):
    def get(self, id):
        ''' Get expedition info. '''
        
        try:
            expedition = Expedition.query.filter(Expedition.id == id).one()
            
            return jsonify(expedition)
        except Exception as e:
            abort(404)

    def patch(self, id):
        ''' Edit expedition info. '''

        try:
            expedition = Expedition.query.filter(and_(Expedition.id == id)).one()
            expedition_data = loads(request.data)
            expedition = expedition.update(**expedition_data)

            return jsonify(expedition)
        except Exception as e:
            abort(500)

    def delete(self, id):
        ''' Delete expedition info. '''

        user = current_user

        try:
            expedition = Expedition.query.filter(Expedition.id == id).one()
            expedition.delete()

            return 200
        except Exception as e:
            abort(500)


class ExpeditionMembership(Resource):
    def get(self, id):
        ''' Get expedition members. '''

        try:
            expedition = Expedition.query().filter(Expedition.id == id).one()

            return jsonify(id=id, members=expedition.members)
        except Exception as e:
            abort(404)

    def put(self, id):
        ''' Add user to expedition. '''

        user = current_user
        try:
            expedition = Expedition.query().filter(and_(Expedition.id == id,
                                                        Expedition.members.any(user))).one()
            expedition.members.append(user)

            return 200
        except Exception as e:
            abort(500)


    def delete(self, id):
        ''' Delete user from expedition. '''
        
        user = current_user
        try:
            expedition = Expedition.query().filter(Expedition.id == id).one()
            members = expedition.members

            for i in range(len(members)):
                if members[i].id == user.id:
                    members.remove(members[i])

            return 200
        except Exception as e:
            abort(500)