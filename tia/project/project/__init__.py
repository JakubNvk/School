from flask import Flask
from flask.ext.restful import Api

from .auth import login_manager
from .data import db
from .views import skialp

from .endpoints import ProfileList, ProfileDetail, ExpeditionList, ExpeditionDetail, ExpeditionMembership

app = Flask(__name__)
api = Api(app)
app.config.from_object('config')

# API resource routing
api.add_resource(ProfileList, '/api/profiles')
api.add_resource(ProfileDetail, '/api/profile')
api.add_resource(ExpeditionList, '/api/expeditions')
api.add_resource(ExpeditionDetail, '/api/expedition/<int:id>')
api.add_resource(ExpeditionMembership, '/api/expedition/<int:id>/membership')


@app.context_processor
def provide_constants():
    return {"constants": {"ver": 1}}

db.init_app(app)

login_manager.init_app(app)

app.register_blueprint(skialp)