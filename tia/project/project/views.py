import datetime

from flask import Blueprint, flash, redirect, render_template, request, url_for
from flask.ext.login import current_user, login_required, login_user, logout_user

from .forms import LoginForm, RegistrationForm, ExpeditionForm, ProfileForm
from .data import db, query_to_list
from .models import User, Profile

skialp = Blueprint('skialp', __name__)


@skialp.route('/')
def index():
    if not current_user.is_anonymous():
        return redirect(url_for('skialp.dashboard'))
    return render_template('index.html')


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
        user.profile.append(Profile.create())
        user.save()
        login_user(user)
        return redirect(url_for('skialp.index'))
    return render_template('register.html', form=form)


@skialp.route('/logout')
@login_required
def logout():
    logout_user()
    return redirect(url_for('skialp.index'))


@skialp.route('/dashboard')
def dashboard():
    return render_template('dashboard.html')


@skialp.route('/profile')
def profile():
    return render_template('profile.html')


@skialp.route('/edit_profile')
def edit_profile():
    form = ProfileForm()
    return render_template('edit_profile.html', form=form)


@skialp.route('/expeditions')
def expeditions():
    return render_template('expeditions.html')


@skialp.route('/create_expedition')
def create_expedition():
    form = ExpeditionForm()
    return render_template('create_expedition.html', form=form)