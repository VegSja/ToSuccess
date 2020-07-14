from google.oauth2 import id_token
from google.auth.transport import requests
import os

class token_validation:

    def __init__(self, token_from_http):
        self.token = token_from_http
        self.validate()

    def validate(self):
        try:
            idinfo = id_token.verify_oauth2_token(self.token, requests.Request(), os.getenv('GOOGLE_CLIENT_ID'))

            #Get the user's Google Account ID from the decoded token
            userid = idinfo['sub']
            print("SUCCESS: Successfully validated token")    
        except ValueError:
            print("ERROR: Invalid token")
            pass