from datetime import date
from core.domain.entities import BackgroundImageEntity
from core.ports.image_repository import ImageRepositoryPort
from database import SessionLocal
from models import BackgroundImage

class SQLAlchemyImageRepository(ImageRepositoryPort):
    def get_by_date(self, d: date):
        db = SessionLocal()
        try:
            row = db.query(BackgroundImage).filter(BackgroundImage.date == d).first()
            if not row:
                return None
            return BackgroundImageEntity(
                date=row.date,
                url=row.image_url,
                author=row.author,
                description=row.description,
            )
        finally:
            db.close()

    def save(self, image: BackgroundImageEntity):
        db = SessionLocal()
        try:
            row = BackgroundImage(
                date=image.date,
                image_url=image.url,
                author=image.author,
                description=image.description
            )
            db.add(row)
            db.commit()
        finally:
            db.close()