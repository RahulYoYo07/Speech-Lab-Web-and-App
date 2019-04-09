from django.http import HttpResponse
from django.shortcuts import render
from django.utils.safestring import mark_safe
import json

def course_group(request, CourseID, CourseGroupID):
    return render(request, 'discussion/group.html', {
        'CourseGroupID_json': mark_safe(json.dumps(CourseGroupID)),
        'CourseID_json': mark_safe(json.dumps(CourseID)),
    })

def group(request, CourseID, AssignmentID, GroupID):
    pass
