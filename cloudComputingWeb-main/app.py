import os
import requests
from flask import Flask, flash, request, render_template, redirect, make_response

ENV_VAR_TRANSCRIBE_SERVER ="http://34.76.212.17:3000"
# ENV_VAR_TRANSCRIBE_SERVER = 'http://127.0.0.1:5001'
# UPLOAD_FOLDER = os.path.join('static', 'videos')
# DOWNLOAD_FOLDER = os.path.join('static', 'videos')
app = Flask(__name__, static_url_path='/static/')
# app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
app.secret_key = "abc"


class Tripel:
    def __init__(self, name: str, id: str, status: str, link:str):
        self.name = name
        self.id = id
        self.status = status
        self.link = link

    def __repr__(self):
        return f'Entry(name={self.name}, id={self.id}, status={self.id}, link={self.link})'


@app.route('/')
def upload_form():
    cok = request.cookies.items()
    files = []
    for i in cok:
        if i[0] != 'session':
            status = requests.get(ENV_VAR_TRANSCRIBE_SERVER + '/status/'+i[1])
            percentage = status.content
            if(percentage != "queued" ):
                percentage = str(status.content) + "%"
                
            files.append(Tripel(i[0], i[1], str(percentage), ENV_VAR_TRANSCRIBE_SERVER + '/video/'+i[1]))
    resp = make_response(render_template('Home.html', files=files))
    return resp


@app.route('/upload', methods=['POST'])
def upload():
    print('uploading to ' + ENV_VAR_TRANSCRIBE_SERVER + "/upload")
    video = request.files['file']
    if video:
        print('file uploaded, saving...')
        response = requests.post(ENV_VAR_TRANSCRIBE_SERVER + "/upload", files={'video': video})
        resp = redirect('/')
        resp.set_cookie(key=video.filename, value=response.text)
        flash('uploaded successfully id:' + response.text)
        return resp
    flash('Nothing uploaded')
    return redirect('/')


@app.route('/uploadapi', methods=['POST'])
def uploadapi():
    video = request.files['file']
    if video:
        response = requests.post(ENV_VAR_TRANSCRIBE_SERVER, data={'file': video})
        return response.text
    return 'Nothing uploaded'


@app.route('/status/<id>')
def status(id):
    print("status "  + id	)
    resp = requests.get(ENV_VAR_TRANSCRIBE_SERVER + '/status/' + id)
    return resp.text


""" @app.route('/upload1', methods=['POST'])
def upload1():
    video = request.files['file']
    if video:
        filename = secure_filename(video.filename)
        path = os.path.join(app.config['UPLOAD_FOLDER'], filename)
        video.save(path)
        flash('new file upload: '+ path)
        resp = redirect('/')
        resp.set_cookie(key=filename, value='local')
        return resp
    flash('Nothing uploaded')
    return redirect('/')
    
    
@app.route('/video/<filename>')
def download(filename):
    return send_from_directory(DOWNLOAD_FOLDER, filename)
"""

if __name__ == '__main__':
    app.run(host='0.0.0.0' , port=80)
