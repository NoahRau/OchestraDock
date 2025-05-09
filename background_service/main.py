from fastapi import FastAPI
from adapters.repositories.sqlalchemy_repository import SQLAlchemyImageRepository
from adapters.sources.unsplash_adapter import UnsplashAdapter
from core.use_cases.fetch_image import get_today_image   # <- correct module

app = FastAPI()

# Instantiate ports/adapters once
repo = SQLAlchemyImageRepository()
source = UnsplashAdapter()

# ---------- HTTP Route ----------
@app.get("/image/today", summary="Get today’s background image")
def image_today():
    img = get_today_image(repo, source)
    return {
        "date": img.date.isoformat(),
        "url": img.url,
        "author": img.author,
        "description": img.description,
    }

# ---------- optional: preload at startup ----------
@app.on_event("startup")
async def preload_today():
    get_today_image(repo, source)