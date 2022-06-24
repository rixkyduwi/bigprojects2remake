import random,string
from flask import Flask,jsonify, request
from bert import bert_prediction
from werkzeug.security import check_password_hash,generate_password_hash
from flask_restful import Resource, Api
from flask_sqlalchemy import SQLAlchemy
from flask_httpauth import HTTPTokenAuth
auth = HTTPTokenAuth(scheme='Bearer')
db = SQLAlchemy()
app = Flask(__name__)
api = Api(app)
app.config['SQLALCHEMY_TRACK_MODIFICATIONS']= True
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///bigprojeks2.db'
class ADMIN(db.Model):
    id = db.Column(db.Integer, primary_key=True) # primary keys are required by SQLAlchemy
    email = db.Column(db.String(100), unique=True)
    password = db.Column(db.String(100))
    name = db.Column(db.String(1000))
    token = db.Column(db.String(1000))
class HISTORY(db.Model):
    score = db.Column(db.String(100))
    rank = db.Column(db.String(100))
    jawaban = db.Column(db.String(1000))
    nama = db.Column(db.String(1000),primary_key=True)
    waktu_proses = db.Column(db.String(1000))
db.init_app(app)
@auth.verify_token
def verify_token(token):
    user=ADMIN.query.filter_by(token=token).first() 
    return user.name
class apisignup(Resource):
    def post(self):
        email = request.json['email']
        name = request.json['name']
        password = request.json['password']
        admin = ADMIN(email=email,name=name,password=generate_password_hash(password),token= '')
        db.session.add(admin)
        db.session.commit()
        return jsonify({"msg" : "registrasi sukses","status":200})
class apilogin(Resource):
    def post(self):
        email = request.json['email']
        password = request.json['password']
        j=15
        user= ADMIN.query.filter_by(email=email).first()
        if not user or not check_password_hash(user.password, password):
            return jsonify({"msg":"login gagal","url": "/login","status":200})
        token = ''.join(random.choices(string.ascii_uppercase + string.digits, k = j))
        user.token= token
        db.session.commit()
        return jsonify({"msg":"login sukses, hai "+user.name,"token":token,"url": "/dashboard", "status":200})
class apipredict(Resource):
    @auth.login_required
    def post(self):
        context = request.json['konteks']
        question = request.json['pertanyaan']
        prediction = bert_prediction(context,question)
        print(prediction)
        return prediction
class apihistory(Resource):
    @auth.login_required
    def get(self):
        context = request.json['konteks']
        question = request.json['pertanyaan']
        prediction = bert_prediction(context,question)
        print(self.name)
        history = HISTORY(nama=self.name,rank= prediction['rank'],jawaban=prediction['jawaban'],score=prediction['score'],waktu_proses=prediction['waktu_proses'])
        db.session.add(history)
        db.session.commit()
        print(prediction)
        return prediction
api.add_resource(apisignup, '/api/v1/users/create', methods=['POST'])
api.add_resource(apilogin, '/api/v1/users/login', methods=['POST'])
api.add_resource(apipredict, '/api/v1/model/rankanswer', methods=['POST'])
api.add_resource(apihistory, '/api/v1/users/history', methods=['GET'])
if __name__ == "__main__":
    app.run(port=5000,debug=True)