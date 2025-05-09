from abc import ABC, abstractmethod
from core.domain.entities import BackgroundImageEntity

class ImageSourcePort(ABC):
    @abstractmethod
    def fetch_random_image(self) -> BackgroundImageEntity: ...