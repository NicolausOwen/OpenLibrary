package com.kelompok5.openlibrary.utils;

import androidx.room.TypeConverter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Converters {
    @TypeConverter
    public static List<String> fromString(String value) {
        return value == null ? Collections.emptyList() : Arrays.asList(value.split(","));
    }

    @TypeConverter
    public static String fromList(List<String> list) {
        return list == null ? "" : String.join(",", list);
    }
}
