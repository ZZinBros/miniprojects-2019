package com.woowacourse.zzinbros.common;

import org.apache.commons.text.translate.AggregateTranslator;
import org.apache.commons.text.translate.CharSequenceTranslator;
import org.apache.commons.text.translate.EntityArrays;
import org.apache.commons.text.translate.LookupTranslator;

public class EscapedCharacters {
    private static final CharSequenceTranslator ALL_ESCAPE = new AggregateTranslator(
            new LookupTranslator(EntityArrays.BASIC_ESCAPE),
            new LookupTranslator(EntityArrays.APOS_ESCAPE),
            new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE),
            new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE),
            new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE)
    );

    public static String of(String rawText) {
        return ALL_ESCAPE.translate(rawText);
    }
}
