"""
WSGI config for IITG_Speech_Lab project.

It exposes the WSGI callable as a module-level variable named ``application``.

For more information on this file, see
https://docs.djangoproject.com/en/2.2/howto/deployment/wsgi/
"""

import os

from django.core.wsgi import get_wsgi_application

os.environ.setdefault('DJANGO_SETTINGS_MODULE', 'IITG_Speech_Lab.settings')

application = get_wsgi_application()

http_proxy = "http://shiva170101063:hZev4pLy@202.141.80.24:3128"
https_proxy = "http://shiva170101063:hZev4pLy@202.141.80.24:3128"

os.environ['http_proxy'] = http_proxy
os.environ['https_proxy'] = https_proxy