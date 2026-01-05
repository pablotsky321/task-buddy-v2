CREATE OR REPLACE FUNCTION set_updated_at()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_updated_at_task
    BEFORE UPDATE ON task
    FOR EACH ROW
EXECUTE FUNCTION set_updated_at();

