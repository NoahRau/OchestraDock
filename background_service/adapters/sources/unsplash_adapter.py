import os, requests
from dotenv import load_dotenv
from core.domain.entities import BackgroundImageEntity
from core.ports.image_source import ImageSourcePort

load_dotenv()
KEY = os.getenv("UNSPLASH_ACCESS_KEY")

class UnsplashAdapter(ImageSourcePort):
    def fetch_random_image(self) -> BackgroundImageEntity:
        resp = requests.get(
            "https://api.unsplash.com/photos/random",
            headers={"Authorization": f"Client-ID {KEY}"},
            params={"query": "nature", "orientation": "portrait"},
            timeout=10
        )
        resp.raise_for_status()
        data = resp.json()
        return BackgroundImageEntity(
            date=None,                                 # wird hinterher gesetzt
            url=data["urls"]["full"],
            author=data["user"]["name"],
            description=data.get("description") or data.get("alt_description")
        )