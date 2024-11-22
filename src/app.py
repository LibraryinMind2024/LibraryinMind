from flask import Flask, request, jsonify, render_template
from werkzeug.utils import secure_filename
import os
from dotenv import load_dotenv
import requests
import json
import time
import openai
import base64

app = Flask(__name__)
load_dotenv()

# 환경 변수 설정
openai.api_key = os.getenv("OPENAI_API_KEY")
ocr_secret_key = os.getenv("OCR_APII_KEY")
ocr_api_url = os.getenv("OCR_API_URL")
imgbb_api_key = os.getenv("IMGBB_API_KEY")  # ImgBB API 키 추가

def allowed_file(filename):
    """허용된 파일 확장자 확인"""
    ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg', 'gif'}
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

def upload_to_imgbb(file_stream):
    """파일 스트림을 ImgBB에 업로드하고 URL 반환"""
    try:
        imgbb_url = "https://api.imgbb.com/1/upload"
        file_content = file_stream.read()
        b64_image = base64.b64encode(file_content).decode('utf-8')
        payload = {
            "key": imgbb_api_key,
            "image": b64_image
        }
        response = requests.post(imgbb_url, data=payload)
        response_data = response.json()

        if response.status_code == 200 and response_data['success']:
            return response_data['data']['url']
        else:
            print(f"Error uploading to ImgBB: {response_data}")
            return None
    except Exception as e:
        print(f"Error uploading to ImgBB: {e}")
        return None

# 네이버 OCR 요청
def send_ocr_request(image_url):
    payload = {
        "version": "V2",
        "requestId": "1234",  # 고유 ID
        "timestamp": int(time.time() * 1000),
        "lang": "ko",
        "images": [
            {
                "format": "jpg",
                "name": "example_image",
                "url": image_url
            }
        ],
        "enableTableDetection": False
    }

    headers = {
        "Content-Type": "application/json",
        "X-OCR-SECRET": ocr_secret_key
    }

    try:
        response = requests.post(ocr_api_url, headers=headers, json=payload)

        if response.status_code == 200:
            ocr_results = response.json()
            all_texts = []
            for image_result in ocr_results.get('images', []):
                for field in image_result.get('fields', []):
                    text = field.get('inferText', '')
                    all_texts.append(text)
            return ' '.join(all_texts)
        else:
            print(f"OCR Error: {response.status_code} - {response.text}")
            return None
    except Exception as e:
        print(f"Error during OCR request: {str(e)}")
        return None


@app.route('/')
def home():
    return render_template('urlTest.html')

@app.route('/uploads', methods=['POST'])
def upload_and_process_image():
    if 'file' not in request.files:
        return jsonify({'error': 'No file part'}), 400

    file = request.files['file']
    if file.filename == '':
        return jsonify({'error': 'No selected file'}), 400

    if file and allowed_file(file.filename):
        # ImgBB에 업로드
        image_url = upload_to_imgbb(file)
        if not image_url:
            return jsonify({'error': 'Failed to upload to ImgBB'}), 500

        # 네이버 OCR 요청
        extracted_text = send_ocr_request(image_url)
        if extracted_text:
            return jsonify({'ocr_text': extracted_text, 'image_url': image_url}), 200
        else:
            return jsonify({'error': 'OCR request failed', 'image_url': image_url}), 500

    return jsonify({'error': 'Invalid file format'}), 400

if __name__ == '__main__':
    app.run(debug=True)