from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from adapters.repositories.sqlalchemy_repository import SQLAlchemyImageRepository
from adapters.sources.unsplash_adapter import UnsplashAdapter
from core.use_cases.fetch_image import get_today_image   # <- correct module


app = FastAPI()

# CORS configuration to allow frontend (e.g. http://localhost:3000) when running via docker‑compose
origins = [
    "http://localhost:3000",
    "http://127.0.0.1:3000",
    "http://frontend:3000",   # docker‑compose service name
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

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