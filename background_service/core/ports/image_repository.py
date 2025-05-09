from abc import ABC, abstractmethod
from datetime import date
from core.domain.entities import BackgroundImageEntity

class ImageRepositoryPort(ABC):
    @abstractmethod
    def get_by_date(self, d: date) -> BackgroundImageEntity | None: ...
    @abstractmethod
    def save(self, image: BackgroundImageEntity) -> None: ...