mod database;
mod wrapper;
#[cfg(test)] pub mod test;

pub use database::{
    Database,
    Error
};
pub use wrapper::DatabaseWrapper;
