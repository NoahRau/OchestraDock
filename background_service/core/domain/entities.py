from dataclasses import dataclass
from datetime import date
from typing import Optional

@dataclass
class BackgroundImageEntity:
    date: date
    url: str
    author: Optional[str]
    description: Optional[str]