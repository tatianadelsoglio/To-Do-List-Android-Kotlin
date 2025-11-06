from django.contrib import admin

from .models import Task

@admin.register(Task)
class TaskAdmin(admin.ModelAdmin):
    list_display = ('id', 'titulo', 'descripcion', 'estado', 'created_at')
    list_filter = ('estado',)  
    search_fields = ('titulo', 'descripcion') 