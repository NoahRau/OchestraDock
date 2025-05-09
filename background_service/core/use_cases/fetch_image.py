from datetime import date
from core.ports.image_repository import ImageRepositoryPort
from core.ports.image_source import ImageSourcePort
from core.domain.entities import BackgroundImageEntity

def get_today_image(repo: ImageRepositoryPort,
                    source: ImageSourcePort) -> BackgroundImageEntity:
    today = date.today()

    cached = repo.get_by_date(today)
    if cached:
        return cached

    # fetch + persist
    image = source.fetch_random_image()
    image.date = today            # inject today's date
    repo.save(image)
    return image