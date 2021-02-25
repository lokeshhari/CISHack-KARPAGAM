from django.db import models

from django.urls import reverse
import uuid
from datetime import date
from django.contrib.auth.models import User

class Genre(models.Model):
    name = models.CharField(max_length=200, help_text='Enter a book genre (e.g. Science Fiction)')
    
    def __str__(self):
        return self.name

class Book(models.Model):
    title = models.CharField(max_length=200)
    author = models.ForeignKey('Author', on_delete=models.SET_NULL, null=True)
    langs= models.ForeignKey('Language',on_delete=models.SET_NULL,null=True)
    summary = models.TextField(max_length=1000, help_text='Enter a brief description of the book')
    isbn = models.CharField('ISBN', max_length=13, help_text='13 Character <a href="https://www.isbn-international.org/content/what-isbn">ISBN number</a>')
    genre = models.ManyToManyField(Genre, help_text='Select a genre for this book')
    file_url = models.URLField(max_length=1000,null=True)
    download_url=models.URLField(max_length=1000,null=True)
    class Meta:
        ordering = ['title']
    
    def __str__(self):
        return self.title
    
    def get_absolute_url(self):
        return reverse('book-detail', args=[str(self.id)])
    def display_genre(self):
        """Create a string for the Genre. This is required to display genre in Admin."""
        return ', '.join(genre.name for genre in self.genre.all()[:3])
    
    display_genre.short_description = 'Genre'

class Author(models.Model):
    first_name = models.CharField(max_length=100)
    last_name = models.CharField(max_length=100)
    date_of_birth = models.DateField(null=True, blank=True)
    date_of_death = models.DateField('Died', null=True, blank=True)

    class Meta:
        ordering = ['first_name']
        

    def get_absolute_url(self):
        return reverse('author-detail', args=[str(self.id)])

    def __str__(self):
        return f'{self.last_name}  {self.first_name}'

class Language(models.Model):
    lang=models.CharField(max_length=30)
    class meta:
        ordering=['lang']

    def __str__(self):
        return self.lang

class Request(models.Model):
    book_name=models.CharField(max_length=30)
    class meta:
        ordering=['book_name']

    def __str__(self):
        return self.book_name
    def get_absolute_url(self):
        return reverse('request-detail', args=[str(self.id)])

