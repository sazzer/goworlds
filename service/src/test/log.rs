use log::LevelFilter;
use log4rs::{
    append::console::ConsoleAppender,
    config::{Appender, Config, Root},
};
use std::sync::Once;

static START: Once = Once::new();

pub fn setup() {
    START.call_once(|| {
        let stdout = ConsoleAppender::builder().build();

        let config = Config::builder()
            .appender(Appender::builder().build("stdout", Box::new(stdout)))
            .build(Root::builder().appender("stdout").build(LevelFilter::Debug))
            .unwrap();

        log4rs::init_config(config).unwrap();
    });
}
