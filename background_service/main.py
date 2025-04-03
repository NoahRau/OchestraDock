from fastapi import FastAPI, Depends
from sqlalchemy.orm import Session
from database import SessionLocal, engine, Base
from models import BackgroundImage
from scheduler import start_scheduler
from datetime import date
from fastapi.middleware.cors import CORSMiddleware

origins = [
    "http://localhost:3000",
    "http://127.0.0.1:3000"
]
app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"]
)

from scheduler import fetch_and_store_image

Base.metadata.create_all(bind=engine)


def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

start_scheduler()

@app.get("/image/today")
def get_today_image(db: Session = Depends(get_db)):
    image = db.query(BackgroundImage).filter(BackgroundImage.date == date.today()).first()
    if not image:
        fetch_and_store_image()
        image = db.query(BackgroundImage).filter(BackgroundImage.date == date.today()).first()
    if image:
        return {
            "date": image.date,
            "url": image.image_url,
            "author": image.author,
            "description": image.description
        }
    return {"message": "No image for today yet."}