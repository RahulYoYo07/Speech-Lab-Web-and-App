import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

# Use a service account
cred = credentials.Certificate('iitg-speech-lab-firebase-adminsdk-ggn1f-2f757184a1.json')
firebase_admin.initialize_app(cred)

db = firestore.client()

doc_ref = db.collection(u'rooms').document(u'cs222')
# path = "users/lace";
ref = db.collection(u'users').document(u'lace')
ref2 = db.collection(u'users').document(u'elace')
doc_ref.set({
    u'size': 50,
    u'mem1': ref2,
    u'mem2': ref
})

try:
    doc = doc_ref.get().to_dict()
    #dict = doc.to_dict()
    mm = doc['mem2'].get()
    #print(dict['mem2'])
    print(u'Document data: {}'.format(mm.to_dict()))
except google.cloud.exceptions.NotFound:
    print(u'No such document!')
