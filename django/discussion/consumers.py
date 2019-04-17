from course import views

import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import ntplib

from asgiref.sync import async_to_sync
from channels.generic.websocket import WebsocketConsumer
import json

# cred = credentials.Certificate("iitg-speech-lab-firebase-adminsdk-ggn1f-2f757184a1.json")
cred = credentials.Certificate({
    "type": "service_account",
    "project_id": "iitg-speech-lab",
    "private_key_id": "2f757184a13945f894a474a1f7f0ed15547d0781",
    "private_key": "-----BEGIN PRIVATE KEY-----\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCnGJ6a0ncoPF0w\nilyrOHSMNuRM7XK620f3/euPvayvIxC/Yad9CHUbieQiwnIJTknJv5T5aXZ2Brq8\nxr0tkxkjNnhzn8VYS2QjrWkgVHP80WNs/S4kpZp7E4G+kgzCVxi5Mu0Qd1onyiL9\nvY4X5I9PrP06foAl18FPDRSxKi7mBJf4enQcp6fvSASDbAKMoOlU4u1NVMbi50Nx\nUqJC0Hq1TogyKgFgDbSOSEdFjRHk53+G5Ved4QlTIEJCws6NHcLaek6YPyz80vp3\nB61+u4IvDsGEcITbkq1ayKwhCNlTG4A5ksxETOKAOC55njRjrVtjinxXXjqkrioG\n+CxIatGrAgMBAAECggEAAK/nuxBBgC9bXL886VFWnVr+bliNn7oWHi1zoggwJRo6\nT+cpZqi5vo6/Gut8x5AEWqmIhcwKuiqF6w/QKFdSA6SOMz+FcrsAoursIz9lqLT9\nuS2DWpA5xebLIkr8dXIhPmW4ttgezUoWAcAdTPjaJAQ8mFh702wDNf2CR8Y6IiUB\n/OECTrCGI6ED+5xvE8JevRE8HLksOv5crwKBoEa/yb65uDmVnFgHfISS3TkdRmpW\nFAHpPZ/1zsbgU3v4cXSQJsj5xuEk/vv5tamuvJtfyJ9MdEd2WDjN1zw2fpl/Fhgy\nC9/hED02Lu/lmLRNoU9IyUFSNAWVToWR6waobmAZ8QKBgQDaSvjCJfd0jDbd6nHX\nmuLIYU5vwEqWDO8lZljH8JBX9DDvXEzqKdRQS6kqdYu3hsKpIlJzDvqShVemfAda\ntdXNQXrqt8pPH4IVZVYj4raAIT4nXqGJDl4Mnayk/eM6YVhJQMIr/yW5EPfssHjz\nxZ0avYn2I0YBfMupn4JB12FZEQKBgQDD9bPc/uw1JDnj+9i9/xdsDbpQ8e8pjbA7\nIwSlzs6PhjVg1oZUjIhlKleBshqG/DKeBZfhrKGo7HsX3yXpOZcs1iVT3a50TW6h\n+CX/H3nzVXRjdEFUcWX2qwKjGagOeNtNtAR0dkluovHbkxiWAHXNMnyG0+HEN6zj\nlAi3lwCe+wKBgFSBr5mhjxGccmUorJe2C1NdcDsM6xL5wN7upzIH7ClQjF0tk00X\nkmzfTYb1aHhNADDv65FFXDW6zzrRSxuPx0wlrEsPiY9l+DsGNvm/e71QoTomhUyE\ntl4V8E8TRpNEOiRpoIHdzaG+cuw7SSe9+drvQ2h5MVHEGSf6azfIBJSxAoGBALrt\nQmnpcwEuUVq8/wAeugUFA1n7rxyAYD/JI8HXCQu4BmsduH4moGWAgoDhmJRzNwWu\naDeKKZuuGa2n284idab7kBf0O1oOEx7GS9iV+gq41ZGZcEhQ8+bdMmLLMpi7iNcS\nhb1iqKG1JelC5A0S20ymgEtNCuvWAEIHEFmw3ZLJAoGAVfK3ovsR3e0PptoJ27WS\neoFm1Fh2iXovrtOE23EONuakCy9aLmc9QfBq3TG+OJdKZXD+pe4xtRVvtipnbryU\nAzYz9k4p3cJHnnQwtHDakeaXvQXO960uM1C3VHn+XAVNd8tO3sUNyenpZzJPwcX2\no4Lsy7nKljoEGxCgS2I94mI=\n-----END PRIVATE KEY-----\n",
    "client_email": "firebase-adminsdk-ggn1f@iitg-speech-lab.iam.gserviceaccount.com",
    "client_id": "110868864917664596078",
    "auth_uri": "https://accounts.google.com/o/oauth2/auth",
    "token_uri": "https://oauth2.googleapis.com/token",
    "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
    "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-ggn1f%40iitg-speech-lab.iam.gserviceaccount.com"
})
# firebase_admin.initialize_app(cred)
db = firestore.client()

# -------------------------------------------------------------------------------------------------------------------------------
#Receive and sending messages
class ChatConsumer(WebsocketConsumer):
    def connect(self):
        # print('Not yet opened', open)
        self.room_name = self.scope['url_route']['kwargs']['CourseGroupID']
        # print(self.room_name)
        self.room_group_name = 'chat_%s' % self.room_name
        # print(self.channel_name)
        # Join room group
        async_to_sync(self.channel_layer.group_add)(
            self.room_group_name,
            self.channel_name
        )

        #Retrieve messages from Firebase
        CourseID = self.scope['url_route']['kwargs']['CourseID']
        CourseGroupID = self.scope['url_route']['kwargs']['CourseGroupID']
        doc_ref = db.collection(u'Courses').document(CourseID).collection(u'CourseGroup').document(CourseGroupID).collection(u'Messages').order_by(u'PostTime')

        docs = list(doc_ref.get())

        self.accept()

        for i in range(len(docs)):
            id = docs[i].id
            doc = docs[i]
            doc = doc.to_dict()
            if (doc['IsPoll'] == False):
                self.send(text_data=json.dumps({
                    'IsPoll': doc['IsPoll'],
                    'messageHead' : doc['MessageHead'],
                    'message': doc['MessageBody'],
                    'Author':doc['Author'],
                    'MessageID': id,
                    'IsReply': False,
                    'ShowReply': False,
                }))
            else:
                self.send(text_data=json.dumps({
                    'IsPoll': doc['IsPoll'],
                    'PollQues': doc['PollQues'],
                    'Author':doc['Author'],
                    'MessageID': id,
                    'IsReply': False,
                    'ShowReply': False,
                }))


    def disconnect(self, close_code):
        # Leave room group
        async_to_sync(self.channel_layer.group_discard)(
            self.room_group_name,
            self.channel_name
        )

    # Receive message from WebSocket
    def receive(self, text_data):
        # print("Jeronemo")
        text_data_json = json.loads(text_data)
        IsPoll = text_data_json['IsPoll']
        CourseID = text_data_json['CourseID']
        CourseGroupID = text_data_json['CourseGroupID']
        MessageID = ''
        # IsReply = ''
        # message = ''
        # messageHead = ''
        # ReplyBody = ''
        if (IsPoll == False):
            client = ntplib.NTPClient()
            response = client.request('pool.ntp.org')
            MessageID = str(response.tx_time)
            ShowReply = text_data_json['ShowReply']
            if (ShowReply == False):
                IsReply = text_data_json['IsReply']
                message = text_data_json['message']
                messageHead = text_data_json['messageHead']
                ReplyBody = text_data_json['ReplyBody']
                if not IsReply:
                    client = ntplib.NTPClient()
                    response = client.request('pool.ntp.org')
                    MessageID = str(response.tx_time)
                    doc_ref = db.collection(u'Courses').document(CourseID).collection(u'CourseGroup').document(CourseGroupID).collection(u'Messages').document(MessageID).set({'Author' : curAuthor,'MessageHead' : messageHead, 'MessageBody' : message,'IsPoll': False,'PostTime':firestore.SERVER_TIMESTAMP})

                else:
                    MessageID = text_data_json['MessageID']
                    doc_ref = db.collection(u'Courses').document(CourseID).collection(u'CourseGroup').document(CourseGroupID).collection(u'Messages').document(MessageID).collection(u'Replies').add({'Author' : curAuthor,'MessageID' : MessageID, 'PostTime' : firestore.SERVER_TIMESTAMP, 'ReplyBody': ReplyBody})

                # Send message to room group
                async_to_sync(self.channel_layer.group_send)(
                    self.room_group_name,
                    {
                        'type': 'chat_message',
                        'ShowReply': False,
                        'Author': curAuthor,
                        'IsReply': IsReply,
                        'messageHead' : messageHead,
                        'message': message,
                        'ReplyBody': ReplyBody,
                        'MessageID': MessageID,
                    }
                )
            else:
                MessageID = text_data_json['MessageID']
                doc_ref = db.collection(u'Courses').document(CourseID).collection(u'CourseGroup').document(CourseGroupID).collection(u'Messages').document(MessageID).collection(u'Replies').order_by(u'PostTime')
                replies = list(doc_ref.get())
                for i in range(len(replies)):
                    replies[i] = replies[i].to_dict()
                    replies[i]['PostTime']=str(replies[i]['PostTime'])
                    print(replies[i])
                # Send reply to user in group
                text_data=json.dumps({
                    'IsPoll': False,
                    'ShowReply': True,
                    'IsReply': True,
                    'Replies': replies,
                })
                # print(text_data)
                self.send(text_data)
        else:
            ShowReply = text_data_json['ShowReply']
            if (ShowReply == False):
                IsReply = text_data_json['IsReply']
                if (IsReply == False):
                    PollQues = text_data_json['PollQues']
                    # PollOptNum = text_data_json['PollOptNum']
                    PollOpt = []
                    for i in range(len(text_data_json['PollOptions'])):
                        PollOpt.append(text_data_json['PollOptions'][i])
                    DBPoll = {
                        'Author': curAuthor,
                        'PollQues': PollQues,
                        'PollOpt': PollOpt,
                        'IsPoll': True,
                        'PostTime':firestore.SERVER_TIMESTAMP,
                    }
                    client = ntplib.NTPClient()
                    response = client.request('pool.ntp.org')
                    MessageID = str(response.tx_time)
                    doc_ref = db.collection(u'Courses').document(CourseID).collection(u'CourseGroup').document(CourseGroupID).collection(u'Messages').document(MessageID).set(DBPoll)

                    async_to_sync(self.channel_layer.group_send)(
                    self.room_group_name,
                    {
                        'type': 'poll_message',
                        'Author': curAuthor,
                        'PollQues': PollQues,
                        'PollOpt': PollOpt,
                        'IsReply': IsReply,
                        'MessageID': MessageID,
                    })
                else:
                    UserOpt = int(text_data_json['UserOpt'])
                    MessageID = text_data_json['MessageID']
                    doc_ref = db.collection(u'Courses').document(CourseID).collection(u'CourseGroup').document(CourseGroupID).collection(u'Messages').document(MessageID).collection(u'Replies').document(ucurAuthor)
                    # print(UserOpt)
                    DBPollUpdate = {
                        'MessageID': MessageID,
                        'ReplyBody': UserOpt,
                        'PostTime':firestore.SERVER_TIMESTAMP,
                        'Author': curAuthor,
                    }
                    doc_ref.set(DBPollUpdate)

            else:
                MessageID = text_data_json['MessageID']
                doc_ref = db.collection(u'Courses').document(CourseID).collection(u'CourseGroup').document(CourseGroupID).collection(u'Messages').document(MessageID)
                Poll = doc_ref.get().to_dict()
                PollOpt = Poll['PollOpt']
                PollCnt = []
                for i in range(len(PollOpt)):
                    PollCnt.append(0);

                rep_ref = db.collection(u'Courses').document(CourseID).collection(u'CourseGroup').document(CourseGroupID).collection(u'Messages').document(MessageID).collection(u'Replies')
                replies = list(rep_ref.get())

                userOpt = -1
                for i in range(len(replies)):
                    replies[i] = replies[i].to_dict()
                    PollCnt[replies[i]['ReplyBody']] = PollCnt[replies[i]['ReplyBody']] + 1
                    if (replies[i]['Author'] == curAuthor):
                        userOpt = replies[i]['ReplyBody']

                text_data=json.dumps({
                    'IsPoll': True,
                    'ShowReply': True,
                    'IsReply': True,
                    'PollOpt': PollOpt,
                    'PollCnt': PollCnt,
                    'UserOpt': userOpt,
                    'MessageID': MessageID,
                })
                self.send(text_data)


    # Receive poll from room group
    def poll_message(self, event):

        Author=event['Author']
        PollQues = event['PollQues']
        PollOpt = event['PollOpt']
        IsReply = event['IsReply']
        MessageID = event['MessageID']
        # Send message to WebSocket
        self.send(text_data=json.dumps({
            'ShowReply': False,
            'Author':Author,
            'IsReply': IsReply,
            'IsPoll': True,
            'PollQues': PollQues,
            'PollOpt': PollOpt,
            'MessageID': MessageID,
        }))

    # Receive message from room group
    def chat_message(self, event):
        message = event['message']
        messageHead = event['messageHead']
        IsReply = event['IsReply']
        ReplyBody = event['ReplyBody']
        MessageID = event['MessageID']
        Author=event['Author']
        # Send message to WebSocket
        self.send(text_data=json.dumps({
            'ShowReply': False,
            'Author':Author,
            'IsReply': IsReply,
            'IsPoll': False,
            'messageHead' : messageHead,
            'message': message,
            'ReplyBody': ReplyBody,
            'MessageID': MessageID,
        }))
