import datetime

from django import forms
from django.core.exceptions import ValidationError
from django.utils.translation import ugettext_lazy as _
from django.forms import ModelForm
from finalyear.models import Author,Book,Request
from django.contrib.auth.forms import UserCreationForm
from django.contrib.auth.models import User
from django import forms
from django.core.exceptions import ValidationError

class CustomUserCreationForm(forms.Form):
    username = forms.CharField(label='Enter Username', min_length=4, max_length=150)
    email = forms.EmailField(label='Enter email')
    password1 = forms.CharField(label='Enter password', widget=forms.PasswordInput)
    password2 = forms.CharField(label='Confirm password', widget=forms.PasswordInput)

    def clean_username(self):
        username = self.cleaned_data['username'].lower()
        r = User.objects.filter(username=username)
        if r.count():
            raise  ValidationError("Username already exists")
        c=0
        for a in username:
            if (a.isspace()):
                c=1
        if c:
            raise ValidationError("Username shouldnot contain space")
        return username

    def clean_email(self):
        email = self.cleaned_data['email'].lower()
        r = User.objects.filter(email=email)
        if r.count():
            raise  ValidationError("Email already exists")
        return email

    def clean_password2(self):
        SpecialSym=['@','#','&','₹','_']
        password1 = self.cleaned_data.get('password1')
        password2 = self.cleaned_data.get('password2')


        if not any(char.isdigit() for char in password2):
            raise ValidationError('Password should have at least one numeric characters') 

        if not any(char.isupper() for char in password2):
            raise ValidationError('Password should have at least one uppercase letter')

        if not any(char.islower() for char in password2):
            raise ValidationError('Password should have at least one lowercase letter')

        if not any(char in SpecialSym for char in password2):
            raise ValidationError('Password should have at least one of the symbols $@#') 

       
        if password1 and password2 and password1 != password2:
            raise ValidationError("Password doesn't match")

        return password2

    def save(self, commit=True):
        user = User.objects.create_user(
            self.cleaned_data['username'],
            self.cleaned_data['email'],
            self.cleaned_data['password1']
        )
        return user

    
