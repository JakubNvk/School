from flask import Flask
from flask_restful import Api

from .auth import login_manager
from .data import db
from .views import skialp

from .endpoints import MyExpeditions

app = Flask(__name__)
api = Api(app)
app.config.from_object('config')

api.add_resource(MyExpeditions, '/my_expeditions/<string:myexp_id>')

@app.context_processor
def provide_constants():
    return {"constants": {"ver": 1}}

db.init_app(app)

login_manager.init_app(app)

app.register_blueprint(skialp)