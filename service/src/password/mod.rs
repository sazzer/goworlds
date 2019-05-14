mod hash;
mod check;

pub use hash::{
    Error,
    hash_password
};

pub use check::check_password;
