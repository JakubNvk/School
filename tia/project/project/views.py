from flask import Blueprint, flash, redirect, render_template, request, url_for
from flask.ext.login import current_user, login_required, login_user, logout_user

from .forms import LoginForm, RegistrationForm, ExpeditionForm
from .data import db, query_to_list
from .models import User

skialp = Blueprint('skialp', __name__)


@skialp.route('/')
def index():
    if not current_user.is_anonymous():
        return redirect(url_for('skialp.dashboard'))
    return render_template('index.html')


@skialp.route('/dashboard')
def dashboard():
    return render_template('dashboard.html')


@skialp.route('/login', methods=('GET', 'POST'))
def login():
    form = LoginForm()
    if form.validate_on_submit():
        login_user(form.user)
        flash('Logged in successfully.')
        return redirect(request.args.get('next') or url_for('skialp.index'))
    return render_template('login.html', form=form)


@skialp.route('/register', methods=('GET', 'POST'))
def register():
    form = RegistrationForm()
    if form.validate_on_submit():
        user = User.create(**form.data)
        login_user(user)
        return redirect(url_for('skialp.index'))
    return render_template('register.html', form=form)


@skialp.route('/logout')
@login_required
def logout():
    logout_user()
    return redirect(url_for('skialp.index'))