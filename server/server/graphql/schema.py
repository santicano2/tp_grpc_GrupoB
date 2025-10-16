import strawberry
from typing import List, Optional
from datetime import datetime
from collections import defaultdict
from server.storage import db, Donation, User, SavedFilter, Event
from server.auth import can_manage_inventory
import json
@strawberry.type
class SavedFilterType:
    id: int
    name: str
    type: str
    filters_json: str
    created_at: str
    updated_at: str

@strawberry.input
class SavedFilterInput:
    name: str
    filters_json: str  # JSON string


@strawberry.type
class DonationSummary:
    category: str
    deleted: bool
    total_quantity: int

@strawberry.type
class DonationDetail:
    id: int
    category: str
    description: str
    quantity: int
    deleted: bool
    created_at: str
    created_by: str
    updated_at: Optional[str]
    updated_by: Optional[str]

@strawberry.type
class DonationReport:
    summary: List[DonationSummary]
    details: List[DonationDetail]

# === EVENT PARTICIPATION REPORT TYPES ===
@strawberry.type
class EventParticipationDetail:
    event_id: int
    event_name: str
    user: str
    participation_date: str  # ISO

@strawberry.type
class EventParticipationSummary:
    year: int
    month: int
    total_events: int
    total_participations: int

@strawberry.type
class EventParticipationReport:
    summary: List[EventParticipationSummary]
    details: List[EventParticipationDetail]

@strawberry.input
class EventParticipationReportFilter:
    user: Optional[str] = None
    date_from: Optional[str] = None  # ISO
    date_to: Optional[str] = None

@strawberry.input
class DonationReportFilter:
    category: Optional[str] = None
    deleted: Optional[bool] = None
    date_from: Optional[str] = None  # ISO format
    date_to: Optional[str] = None

def filter_donations(donations, f: DonationReportFilter):
    filtered = []
    for d in donations:
        if f.category and d.category != f.category:
            continue
        if f.deleted is not None and d.deleted != f.deleted:
            continue
        if f.date_from:
            try:
                if d.created_at < f.date_from:
                    continue
            except Exception:
                pass
        if f.date_to:
            try:
                if d.created_at > f.date_to:
                    continue
            except Exception:
                pass
        filtered.append(d)
    return filtered

@strawberry.type
class Query:

    @strawberry.field
    def event_participation_report(self, info, actor_username: str, filter: Optional[EventParticipationReportFilter] = None) -> EventParticipationReport:
        # Autorización: solo PRESIDENTE o VOCAL pueden ver el informe
        user: User = db.find_user_by_login(actor_username)
        if not user or not user.active or not can_manage_inventory(user.role):
            raise Exception("No autorizado: solo PRESIDENTE o VOCAL pueden acceder al informe de eventos.")

        # Obtener eventos y participaciones
        events = db.events.values()
        details = []
        for event in events:
            event_date = event.when_iso[:10]  # YYYY-MM-DD
            for member in event.members:
                # Filtros
                if filter:
                    if filter.user and member != filter.user:
                        continue
                    if filter.date_from and event_date < filter.date_from:
                        continue
                    if filter.date_to and event_date > filter.date_to:
                        continue
                details.append(EventParticipationDetail(
                    event_id=event.id,
                    event_name=event.name,
                    user=member,
                    participation_date=event.when_iso
                ))

        # Agrupar por año-mes
        summary_dict = defaultdict(lambda: {"events": set(), "participations": 0})
        for d in details:
            dt = datetime.fromisoformat(d.participation_date[:10])
            key = (dt.year, dt.month)
            summary_dict[key]["events"].add(d.event_id)
            summary_dict[key]["participations"] += 1
        summary = [
            EventParticipationSummary(
                year=year, month=month,
                total_events=len(data["events"]),
                total_participations=data["participations"]
            )
            for (year, month), data in sorted(summary_dict.items())
        ]
        return EventParticipationReport(summary=summary, details=details)

    @strawberry.field
    def list_saved_filters(self, info, actor_username: str, tipo: str = "DONACIONES") -> list[SavedFilterType]:
        user: User = db.find_user_by_login(actor_username)
        if not user or not user.active:
            raise Exception("No autorizado")
        filters = db.list_filters(user.id, tipo)
        return [SavedFilterType(
            id=f.id, name=f.name, type=f.type, filters_json=f.filters_json,
            created_at=f.created_at, updated_at=f.updated_at
        ) for f in filters]

    @strawberry.field
    def donation_report(self, info, actor_username: str, filter: Optional[DonationReportFilter] = None) -> DonationReport:
        # solo PRESIDENTE o VOCAL pueden ver el informe
        user: User = db.find_user_by_login(actor_username)
        if not user or not user.active or not can_manage_inventory(user.role):
            raise Exception("No autorizado: solo PRESIDENTE o VOCAL pueden acceder al informe de donaciones.")
        donations = list(db.donations.values())
        if filter:
            donations = filter_donations(donations, filter)
        # Agrupar por categoria y eliminado
        summary_dict = {}
        for d in donations:
            key = (d.category, d.deleted)
            if key not in summary_dict:
                summary_dict[key] = 0
            summary_dict[key] += d.quantity
        summary = [DonationSummary(category=k[0], deleted=k[1], total_quantity=v) for k, v in summary_dict.items()]
        details = [DonationDetail(
            id=d.id, category=d.category, description=d.description, quantity=d.quantity, deleted=d.deleted,
            created_at=d.created_at, created_by=d.created_by, updated_at=d.updated_at, updated_by=d.updated_by
        ) for d in donations]
        return DonationReport(summary=summary, details=details)

@strawberry.type
class Mutation:
    @strawberry.mutation
    def save_filter(self, info, actor_username: str, input: SavedFilterInput, tipo: str = "DONACIONES") -> SavedFilterType:
        user: User = db.find_user_by_login(actor_username)
        if not user or not user.active:
            raise Exception("No autorizado")
        f = db.save_filter(user.id, input.name, tipo, json.loads(input.filters_json))
        return SavedFilterType(
            id=f.id, name=f.name, type=f.type, filters_json=f.filters_json,
            created_at=f.created_at, updated_at=f.updated_at
        )

    @strawberry.mutation
    def update_filter(self, info, actor_username: str, filter_id: int, input: SavedFilterInput) -> SavedFilterType:
        user: User = db.find_user_by_login(actor_username)
        if not user or not user.active:
            raise Exception("No autorizado")
        f = db.update_filter(filter_id, input.name, json.loads(input.filters_json))
        return SavedFilterType(
            id=f.id, name=f.name, type=f.type, filters_json=f.filters_json,
            created_at=f.created_at, updated_at=f.updated_at
        )

    @strawberry.mutation
    def delete_filter(self, info, actor_username: str, filter_id: int) -> bool:
        user: User = db.find_user_by_login(actor_username)
        if not user or not user.active:
            raise Exception("No autorizado")
        return db.delete_filter(filter_id)

schema = strawberry.Schema(query=Query, mutation=Mutation)
