from apscheduler.schedulers.background import BackgroundScheduler
from datetime import date
from unsplash import fetch_random_image
from database import SessionLocal
from models import BackgroundImage

def fetch_and_store_image():
    db = SessionLocal()
    today = date.today()
    exists = db.query(BackgroundImage).filter(BackgroundImage.date == today).first()
    if exists:
        db.close()
        return

    try:
        image = fetch_random_image()
        entry = BackgroundImage(
            date=today,
            image_url=image["image_url"],
            author=image["author"],
            description=image["description"]
        )
        db.add(entry)
        db.commit()
    except Exception as e:
        print(f"Error fetching image: {e}")
    finally:
        db.close()

def start_scheduler():
    scheduler = BackgroundScheduler()
    scheduler.add_job(fetch_and_store_image, "cron", hour=0, minute=0)
    scheduler.start()