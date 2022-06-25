import torch,time,numpy as np
from flask import Flask,jsonify
from transformers import BertTokenizerFast, BertForQuestionAnswering, Trainer, TrainingArguments
from flask_sqlalchemy import SQLAlchemy
app = Flask(__name__)
db = SQLAlchemy()
app.config['SQLALCHEMY_TRACK_MODIFICATIONS']= True
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///bigprojeks2.db'
class HISTORY(db.Model):
    id = db.Column(db.Integer, primary_key=True) # primary keys are required by SQLAlchemy
    nama = db.Column(db.String(100))
    konteks = db.Column(db.String(3000))
    pertanyaan = db.Column(db.String(1000))
    rank = db.Column(db.String(100))
    jawaban = db.Column(db.String(1000))
    score = db.Column(db.String(100))
    waktu_proses = db.Column(db.String(100))
db.init_app(app)  
device = "cuda" if torch.cuda.is_available() else "cpu" 
torch.device(device) 
modelCheckpoint = "indolem/indobert-base-uncased"
model = BertForQuestionAnswering.from_pretrained("model")
tokenizer = BertTokenizerFast.from_pretrained(modelCheckpoint)
start_time = time.time()
def bert_prediction(user,context,question):
  encodedData = tokenizer(question, context, padding=True, return_offsets_mapping=True, truncation="only_second", return_tensors="pt")
  offsetMapping = encodedData.pop("offset_mapping")[0]
  encodedData.to(device)
  model.to(device)

  model.eval() # IMPORTANT! Set the model as evaluation mode.
  with torch.no_grad(): # IMPORTANT! Do not computing gradient!
    outputs = model(encodedData["input_ids"], attention_mask=encodedData["attention_mask"]) # Feed forward. Without calculating loss.

  startLogits = outputs.start_logits[0].detach().cpu().numpy() # Getting logits, moving to CPU.
  endLogits = outputs.end_logits[0].detach().cpu().numpy() # Getting logits, moving to CPU.

  start_indexes = np.argsort(startLogits).tolist()
  end_indexes = np.argsort(endLogits).tolist()
  candidates = []
  for start_index in start_indexes:
    for end_index in end_indexes:
      if (
        start_index >= len(offsetMapping)
        or end_index >= len(offsetMapping)
        or offsetMapping[start_index] is None
        or offsetMapping[end_index] is None
      ):
        continue
      
      if end_index < start_index or end_index - start_index + 1 > 25:
        continue

      if start_index <= end_index:
        start_char = offsetMapping[start_index][0]
        end_char = offsetMapping[end_index][1]
        candidates.append({
          "score": startLogits[start_index] + endLogits[end_index],
          "text": context[start_char: end_char]
        })

  candidates = sorted(candidates, key=lambda x: x["score"], reverse=True)[:3]
  respon_model = []
  for i, candidate in enumerate(candidates):
      dictlogs = {}
      i=i+1
      rank = str(i)#convert number rank to string
      score = str(candidate['score'])#convert float32 to string
      dictlogs.update({"rank": rank,"jawaban": candidate['text'], "score":score,"waktu_proses":str(time.time() - start_time)})
      respon_model.append(dictlogs)
      history = HISTORY(nama=user,konteks=context,pertanyaan=question,rank= rank,jawaban=candidate['text'],score=score,waktu_proses=str(time.time() - start_time))
      db.session.add(history)
      db.session.commit()
  return jsonify(respon_model)