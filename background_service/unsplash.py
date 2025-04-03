import os
import requests
from dotenv import load_dotenv

load_dotenv()
UNSPLASH_ACCESS_KEY = os.getenv("UNSPLASH_ACCESS_KEY")

def fetch_random_image():
    url = "https://api.unsplash.com/photos/random"
    headers = {"Authorization": f"Client-ID {UNSPLASH_ACCESS_KEY}"}
    params = {"query": "nature", "orientation": "portrait"}

    response = requests.get(url, headers=headers, params=params)
    response.raise_for_status()

    data = response.json()
    return {
        "image_url": data["urls"]["full"],
        "author": data["user"]["name"],
        "description": data.get("description") or data.get("alt_description")
    }