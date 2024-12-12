from flask import Flask, request, jsonify, render_template
from flask_cors import CORS
from werkzeug.utils import secure_filename
import os
from dotenv import load_dotenv
import requests
import json
import time
import openai
import base64
import traceback
import re
import uuid
import subprocess

# Flask 애플리케이션 초기화
app = Flask(__name__, static_folder="static", template_folder="templates")
CORS(app)
load_dotenv()


# 환경 변수 설정
openai.api_key = os.getenv("OPENAI_API_KEY")
ocr_secret_key = os.getenv("OCR_APII_KEY")
ocr_api_url = os.getenv("OCR_API_URL")
imgbb_api_key = os.getenv("IMGBB_API_KEY")  # ImgBB API 키 추가

# 선택에 따라 다른 저장된 JSON 파일 로드
option_to_file = {
    "어린이 전체": ("entire_titles.json", "entire_detailed.json"),
    "어린이 문학": ("literature_titles.json", "literature_detailed.json"),
    "어린이 교양": ("refinement_titles.json", "refinement_detailed.json"),
    "어린이 만화": ("cartoon_titles.json", "cartoon_detailed.json")
}


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
        'requestId': str(uuid.uuid4()),
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
            return ''.join(all_texts)
        else:
            print(f"OCR Error: {response.status_code} - {response.text}")
            return None
    except Exception as e:
        print(f"Error during OCR request: {str(e)}")
        return None


def clean_title(title):
    """부제목 제거 및 특수문자 제거"""
    if not title:
        return ""
    # ':' 이후 부제목 제거
    title = title.split(':')[0]
    # 특수문자와 공백 제거, 소문자로 변환
    return re.sub(r"[^a-zA-Z0-9가-힣]", "", title).strip().lower()


def recommend_books(diary_text, prompt_setting, book_title, book_details):
    """GPT를 사용하여 도서 추천"""
    try:
        response = openai.ChatCompletion.create(
            model="gpt-3.5-turbo",
            messages=[
                {"role": "system", "content": "어린이에게 적합한 도서를 추천하는 도서 추천 시스템입니다."},
                {"role": "user",
                 "content": f"{prompt_setting}. 다음 일기와 책 목록 중 적합한 책 꼭 세 권을 추천해줘:\n\n일기: {diary_text}\n\n책 목록: {book_title}"
                 }
            ],
            max_tokens=500
        )
        recommendation = response.choices[0].message['content'].strip()

        # 추천된 책 제목 리스트 추출
        book_list = []
        for line in recommendation.split('\n'):
            if line.strip().startswith(("1.", "2.", "3.")):
                title = line.split(".", 1)[1].split(":")[0].strip()
                title = title.replace("**", "")  # ** 제거
                book_list.append(title)

        # 매칭된 책 정보
        matched_books = []
        for book in book_details:
            for title in book_list:
                json_title = clean_title(book["title"])
                gpt_title = clean_title(title)

                if json_title == gpt_title:
                    matched_books.append(book)

        # 매칭된 도서가 3권 미만일 경우 추가 도서 선택
        while len(matched_books) < 3 and len(book_details) > len(matched_books):
            for book in book_details:
                if book not in matched_books:
                    matched_books.append(book)
                if len(matched_books) >= 3:
                    break

        return {"recommendation": recommendation, "book_list": book_list, "matched_books": matched_books}
    except Exception as e:
        print("=== 오류 발생 ===")
        print(traceback.format_exc())
        return {"error": str(e)}


# HTML 렌더링
@app.route('/')
def home():
    return render_template('urlTest.html')


# 파일 업로드 처리
@app.route('/uploads', methods=['POST'])
def upload_and_process_image():
    try:
        # 1. 업로드된 파일이 있는지 확인
        if 'file' not in request.files:
            print("Error: No file part in request")
            return jsonify({'error': 'No file part'}), 400

        file = request.files['file']
        if file.filename == '':
            print("Error: No file selected")
            return jsonify({'error': 'No selected file'}), 400

        # 선택된 옵션에 따라 파일 경로 설정
        search_option = request.form.get('search-option')
        if not search_option or search_option not in option_to_file:
            print("Error: Invalid search option")
            return jsonify({'error': 'Invalid search option'}), 400

        titles_file, detailed_file = option_to_file[search_option]

        # 책 목록 및 세부 정보 로드
        if os.path.exists(titles_file) and os.path.exists(detailed_file):
            with open(titles_file, 'r', encoding='utf-8') as f:
                book_title = json.load(f)
                print(book_title)
            with open(detailed_file, 'r', encoding='utf-8') as f:
                book_details = json.load(f)
                print(book_details)
        else:
            print("설마 여기?")
            print("Error: Book files not founddddddd")
            return jsonify({'error': 'Book files not founddddddd'}), 400

        # 2. 파일 업로드 처리
        if file and allowed_file(file.filename):
            print(f"File received: {file.filename}")
            image_url = upload_to_imgbb(file)
            if not image_url:
                print("Error: Failed to upload image to ImgBB")
                return jsonify({'error': 'Failed to upload to ImgBB'}), 500

            print(f"Image URL: {image_url}")
            extracted_text = send_ocr_request(image_url)
            if not extracted_text:
                print("Error: OCR request failed")
                return jsonify({'error': 'OCR request failed', 'image_url': image_url}), 500

            print(f"Extracted Text: {extracted_text}")
            prompt_setting = "일기를 분석해서 그걸 바탕으로 적합한 책 꼭 세가지를 추천해줘"
            recommendation_result = recommend_books(extracted_text, prompt_setting, book_title, book_details)
            print(f"Recommendation Result: {recommendation_result}")

            if 'error' in recommendation_result:
                print("Error in GPT API:", recommendation_result['error'])
                return jsonify({'error': 'GPT API failed', 'details': recommendation_result['error']}), 500

            return jsonify({
                'ocr_text': extracted_text,
                'image_url': image_url,  # 클라이언트에 이미지 URL 반환
                'recommendation_result': recommendation_result
            }), 200

        print("Error: Invalid file format")
        return jsonify({'error': 'Invalid file format'}), 400
    except Exception as e:
        print("Unexpected Error:", str(e))
        return jsonify({'error': 'Server error', 'details': str(e)}), 500


@app.route('/test_gpt', methods=['GET'])
def test_gpt():
    # 테스트용 일기 텍스트
    diary_text = "오늘 학교에서 친구들과 재미있는 놀이를 했어요."
    # 테스트용 프롬프트
    prompt_setting = "일기를 분석해서 적합한 책 세 가지를 추천해줘, 일기를 분석한 내용을 한줄 정도 그리고 책 3권 제목만 표시해줘"
    # 예시로 '어린이 전체' 옵션 사용
    titles_file, detailed_file = option_to_file['어린이 전체']
    book_title = []
    book_details = []
    if os.path.exists(titles_file) and os.path.exists(detailed_file):
        with open(titles_file, 'r', encoding='utf-8') as f:
            book_title = json.load(f)
        with open(detailed_file, 'r', encoding='utf-8') as f:
            book_details = json.load(f)
    result = recommend_books(diary_text, prompt_setting, book_title, book_details)
    # 결과 반환
    return jsonify(result), 200


print("Python script is running!")
# 기존의 코드 추가

if __name__ == '__main__':
    # Flask 서버를 백그라운드에서 실행
    subprocess.Popen(['nohup', 'python3', '/home/ec2-user/.ssh/LibraryinMind_backend/src/main/resources/app.py', '&'], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    print("Flask server is running in the background.")
    # Flask 서버가 실행되도록 대기하지 않고, 다른 코드 계속 실행
    app.run(port=5000, debug=True)
