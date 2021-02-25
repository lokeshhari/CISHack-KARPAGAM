from django.contrib import admin

from .models import Author, Genre, Book,Language,Request

class BooksInline(admin.TabularInline):
    model = Book
    
class AuthorAdmin(admin.ModelAdmin):
    list_display = ('last_name', 'first_name', 'date_of_birth', 'date_of_death')
    fields = ['first_name', 'last_name', ('date_of_birth', 'date_of_death')]
    inlines=[BooksInline]

admin.site.register(Author, AuthorAdmin)

class BookAdmin(admin.ModelAdmin):
    list_display = ('title', 'author', 'display_genre','isbn')


admin.site.register(Book,BookAdmin)

admin.site.register(Genre)
admin.site.register(Request)
admin.site.register(Language)
