use std::time::Instant;

/// Representation of the Identity of some resource
pub struct Identity<ID> {
    id: ID,
    version: String,
    created: Instant,
    updated: Instant,
}

/// Representation of the persisted state of some resource
pub struct Model<ID, DATA> {
    identity: Identity<ID>,
    data: DATA,
}
