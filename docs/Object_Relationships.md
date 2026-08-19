# Object Relationships

| Relationship | Type | Why |
|---|---|---|
| Rider → Ride | Association | A `Ride` holds a reference to the `Rider` who requested it, but the `Rider` doesn't own the `Ride`'s lifecycle — the rider exists independently before, during, and after the ride, and can have many rides over time. Deleting a `Ride` shouldn't delete the `Rider`. |
| Driver → Ride | Association | Same reasoning as above: a `Ride` references the assigned `Driver`, but the driver's own lifecycle (registered, available, working other rides) is completely independent of any one `Ride`. The reference on `Ride` even starts out null, which wouldn't make sense for a stronger relationship. |
| Ride → FareReceipt | Composition | A `FareReceipt` only makes sense in the context of the specific `Ride` it was generated for (it's constructed with that ride's id and amount) and has no independent lifecycle before the ride completes. It's a value produced *by* completing a ride, not an entity that could reasonably be attached to a different ride later. |
| RideService → Strategies | Composition | `RideService` holds its `RideMatchingStrategy` and `FareStrategy` as required constructor dependencies — a `RideService` cannot exist in a valid state without them, and they're scoped to that one `RideService` instance for its whole lifetime. (This is composition in the object-oriented-design sense, i.e. `RideService` is built around the strategies it holds — not the stricter UML sense of the part dying when the whole does, since strategy objects are stateless and shared/reused freely. It's still the correct classification for "composition over inheritance" purposes: RideService gets its matching/pricing *behavior* by holding another object, not by extending one.) |

## Why these aren't inheritance relationships

None of `Rider`, `Driver`, `Ride`, or `FareReceipt` extend one another or share a common base class — they're four independent entities that reference each other, which is the correct shape here since none of them is a specialization of another (a `Driver` is not a kind of `Rider`, etc.). The only place inheritance appears at all is the `exception/` hierarchy (`RideWiseException` as a common unchecked base), which is a legitimate use of inheritance — the five concrete exceptions genuinely *are* `RideWiseException`s, satisfying LSP, versus `Ride` which merely *has* a `Rider`.
