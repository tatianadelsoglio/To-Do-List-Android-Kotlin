from django.db import models

class Task(models.Model):
    titulo = models.CharField(max_length=200)
    descripcion = models.TextField(blank=True)
    estado = models.BooleanField(default=False)
    created_at = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.titulo
