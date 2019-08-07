CREATE INDEX worlds_name_idx ON worlds USING GIN (to_tsvector('english', name));
CREATE INDEX worlds_description_idx ON worlds USING GIN (to_tsvector('english', description));
