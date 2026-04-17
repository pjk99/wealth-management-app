from http.client import HTTPException

from fastapi import FastAPI
from pydantic import BaseModel
from google import genai
from dotenv import load_dotenv
import json
import os


load_dotenv()
API_KEY = os.getenv("GEMINI_API_KEY")

app = FastAPI()

@app.get("/health")
def health():
    return {"status": "AI service running"}


client = genai.Client(api_key=API_KEY)


class HeaderRequest(BaseModel):
    headers: list[str]


def map_headers_with_gemini(headers):
    prompt = f"""
You are a data normalization assistant.

Map these Excel headers to standard fields:

FIELDS:
- household_name
- name
- account_type
- account_number
- custodian
- email
- phone
- net_worth
- income

Return ONLY valid JSON like:
{{ "Header Name": "mapped_field" }}

Headers:
{headers}
"""

    response = client.models.generate_content(
        model="models/gemini-2.5-flash",
        contents=prompt,
        config={
            "response_mime_type": "application/json",
            "temperature": 0
        }
    )

    text = response.text.strip()

    # fallback cleanup (rarely needed if mime_type works)
    text = text.replace("```json", "").replace("```", "").strip()

    try:
        return json.loads(text)
    except json.JSONDecodeError:
        return {"error": "Invalid JSON from model", "raw": text}


@app.post("/map-headers")
def map_headers_api(request: HeaderRequest):
    try:
        result = map_headers_with_gemini(request.headers)
        return {"mapped_headers": result}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))