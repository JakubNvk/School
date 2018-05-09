from flask import Flask

from .auth import login_manager
from .data import db
from .views import skialp

app = Flask(__name__)
app.config.from_object('config')


@app.context_processor
def provide_constants():
    return {"constants": {"ver": 1}}

db.init_app(app)

login_manager.init_app(app)

app.register_blueprint(skialp)