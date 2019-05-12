use actix_web::{dev::Body, http::header::HeaderName, http::header::HeaderValue, HttpResponse};
use assert_json_diff::assert_json_eq;
use serde_json::{from_str, Value};

///Check if the given HTTP Response object has the expected content type header
pub fn assert_content_type(expected: &'static str, actual: &HttpResponse) {
    assert_eq!(
        Some(&HeaderValue::from_static(expected)),
        actual
            .headers()
            .get(HeaderName::from_static("content-type"))
    );
}

pub fn assert_body_json(expected: Value, actual: &HttpResponse) {
    let actual_body = actual.body().as_ref().expect("No body was present");

    let parsed_actual = match actual_body {
        Body::None => panic!("Body not present"),
        Body::Empty => panic!("Body present but empty"),
        Body::Bytes(bytes) => {
            let parsed_string = std::str::from_utf8(bytes).unwrap();
            from_str(parsed_string).unwrap()
        }
        Body::Message(_) => panic!("Unsupported type: Message"),
    };
    assert_json_eq!(expected, parsed_actual);
}
