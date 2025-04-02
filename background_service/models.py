from sqlalchemy import Column, Integer, String, Date
from database import Base
import datetime

class BackgroundImage(Base):
    __tablename__ = "background_images"

    id = Column(Integer, primary_key=True, index=True)
    date = Column(Date, unique=True, default=datetime.date.today)
    image_url = Column(String, nullable=False)
    author = Column(String, nullable=True)
    description = Column(String, nullable=True)