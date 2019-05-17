use std::time::Instant;

/// Representation of the Identity of some resource
#[derive(Debug)]
pub struct Identity<ID> {
    id: ID,
    version: String,
    created: Instant,
    updated: Instant,
}

/// Representation of the persisted state of some resource
#[derive(Debug)]
pub struct Model<ID, DATA> {
    identity: Identity<ID>,
    data: DATA,
}
