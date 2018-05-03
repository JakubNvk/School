from flask import Blueprint, flash, Markup, redirect, render_template, url_for

from .forms import SiteForm, VisitForm
from .data import db, query_to_list
from .models import User

skialp = Blueprint("skialp", __name__)


@skialp.route("/")
def index():
    pass

@skialp.route('/login/', methods=('GET', 'POST'))
def login():
    form = LoginForm()
    if form.validate_on_submit():
        login_user(form.user)
        flash("Logged in successfully.")
        return redirect(request.args.get("next") or url_for("tracking.index"))
    return render_template('login.html', form=form)


@skialp.route('/register/', methods=('GET', 'POST'))
def register():
    form = RegistrationForm()
    if form.validate_on_submit():
        user = User.create(**form.data)
        login_user(user)
        return redirect(url_for('tracking.index'))
    return render_template('register.html', form=form)


@skialp.route('/logout/')
@login_required
def logout():
    logout_user()