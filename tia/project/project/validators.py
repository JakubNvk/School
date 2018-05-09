import re
import datetime
from wtforms import validators


class DateValidator:
    def __init__(self, date=None, empty_msg=None, message=None, val_err_msg=None):
        self.date = date

        if not empty_msg:
            empty_msg = 'Insert your birthdate.'
        self.empty_msg = empty_msg

        if not message:
            message = 'Correct format is DD.MM.RRRR'
        self.message = message

        if not val_err_msg:
            val_err_msg = 'Nonexistent date.'
        self.val_err_msg = val_err_msg

    def __call__(self, form, field):
        date_reg = re.compile(r'^(\d?\d)\.(\d?\d)\.(\d\d\d\d)$')
        mo = date_reg.search(field.data)

        if not field.data:
            raise validators.ValidationError(self.empty_msg)

        try:
            mo.group(0)
        except AttributeError:
            raise validators.ValidationError(self.message)

        try:
            datetime.datetime.strptime(mo.group(0), '%d.%m.%Y')
        except ValueError:
            raise validators.ValidationError(self.val_err_msg)

