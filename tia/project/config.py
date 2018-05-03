from os.path import abspath, dirname, join

_cwd = dirname(abspath(__file__))

SECRET_KEY = 'flask-secret-key'
SQLALCHEMY_DATABASE_URI = 'sqlite:///' + join(_cwd, 'skialp.db')
SQLALCHEMY_ECHO = True