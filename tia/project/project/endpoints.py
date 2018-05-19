from flask import abort, request
from flask.json import jsonify, loads
from flask_restful import Resource
from flask.ext.login import current_user, login_required

from sqlalchemy import and_, not_

from .data import db
from .models import *


class ProfileList(Resource):
    @login_required
    def get(self):
        ''' Get list of all profiles. '''
        try:
            profiles = Profile.query.all()

            return jsonify(profiles=[i.serialize for i in profiles])
        except Exception as e:
            print e
            abort(500)

    @login_required
    def post(self):
        ''' Add new profile. '''
        
        try:
            profile_data = loads(request.data)
            profile = Profile(**profile_data)
            current_user.profile.append(profile)
            current_user.save()

            return jsonify(profile=profile.serialize)
        except Exception as e:
            print e
            abort(500)


class ProfileDetail(Resource):
    @login_required
    def get(self):
        ''' Get user profile. '''

        user = current_user

        try:
            profile = Profile.query.filter(Profile.user_id == user.id).one()
            return jsonify(profile=profile.serialize)
        except Exception as e:
            print e
            abort(404)

    @login_required
    def patch(self):
        ''' Edit user profile. '''

        user = current_user

        try:
            profile = Profile.query.filter(Profile.user_id == user.id).one()
            if profile.user.id != user.id:
                abort(403)

            profile_data = loads(request.data)
            profile = profile.update(**profile_data)

            return jsonify(profile=profile.serialize)
        except Exception as e:
            print e
            abort(500)

    @login_required
    def delete(self):
        ''' Delete user profile. '''

        user = current_user

        try:
            profile = Profile.query.filter(Profile.user_id == user.id).one()
            if profile.user.id != user.id:
                abort(403)
            profile.delete()

            return 200
        except Exception as e:
            abort(500)


class ExpeditionList(Resource):
    @login_required
    def get(self):
        ''' Get list of all expeditions. '''
        try:
            expeditions = Expedition.query.all()

            return jsonify(expeditions=[i.serialize for i in expeditions])
        except Exception as e:
            print e
            abort(500)


    @login_required
    def post(self):
        ''' Add new expeditions. '''

        try:
            expedition_data = loads(request.data)
            expedition = Expedition.create(**expedition_data)
            
            return jsonify(expedition=expedition.serialize)
        except Exception as e:
            raise e
            abort(500)


class ExpeditionDetail(Resource):
    @login_required
    def get(self, id):
        ''' Get expedition info. '''
        
        try:
            expedition = Expedition.query.filter(Expedition.id == id).one()
            
            return jsonify(expedition=expedition.serialize)
        except Exception as e:
            abort(404)

    @login_required
    def patch(self, id):
        ''' Edit expedition info. '''

        try:
            expedition = Expedition.query.filter(and_(Expedition.id == id)).one()
            expedition_data = loads(request.data)
            expedition = expedition.update(**expedition_data)

            return jsonify(expedition=expedition.serialize)
        except Exception as e:
            abort(500)

    @login_required
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
    @login_required
    def get(self, id):
        ''' Get expedition members. '''

        try:
            expedition = Expedition.query.filter(Expedition.id == id).one()

            return jsonify(id=id, members=[i.serialize for i in expedition.members])
        except Exception as e:
            abort(404)

    @login_required
    def put(self, id):
        ''' Add user to expedition. '''

        user = current_user
        try:
            expedition = Expedition.query.filter(and_(Expedition.id == id,
                                                      Expedition.members.any(User.id==user.id))).first()
            if expedition is None:
                expedition = Expedition.query.filter(Expedition.id == id).one()
                expedition.members.append(user)
                expedition.save()

            return jsonify(expedition=expedition.serialize)
        except Exception as e:
            print e
            abort(500)


    @login_required
    def delete(self, id):
        ''' Delete user from expedition. '''
        
        user = current_user
        try:
            expedition = Expedition.query.filter(Expedition.id == id).one()
            members = expedition.members

            for i in range(len(members)):
                if members[i].id == user.id:
                    members.remove(members[i])

            expedition.members = members
            expedition.save()

            return jsonify(expedition=expedition.serialize)
        except Exception as e:
            abort(500)