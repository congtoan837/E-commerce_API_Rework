DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_proc
        WHERE proname = 'remove_accent'
    ) THEN
        CREATE OR REPLACE FUNCTION remove_accent(text)
        RETURNS text AS $$
        BEGIN
            RETURN translate($1,
            'áàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵđ',
            'aaaaaaaaaaaaaaaaaeeeeeeeeeeeiiiiiooooooooooooooooouuuuuuuuuuuyyyyyd');
        END;
        $$ LANGUAGE plpgsql;
    END IF;
END $$;
