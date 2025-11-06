from django.urls import path
from .views import TaskListCreateAPIView, TaskRetrieveUpdateDestroyAPIView

urlpatterns = [
    path('tasks/', TaskListCreateAPIView.as_view(), name='tasks-list-create'),
    path('tasks/<int:pk>', TaskRetrieveUpdateDestroyAPIView.as_view(), name='task-detail'),
]
