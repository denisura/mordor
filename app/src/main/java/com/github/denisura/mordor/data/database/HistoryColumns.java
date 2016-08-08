package com.github.denisura.mordor.data.database;


import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;


public interface HistoryColumns {

    @DataType(INTEGER)
    @NotNull
    String PROFILE_ID = "profile_id";


    @DataType(REAL)
    @NotNull
    String RATE = "rate";


    @DataType(INTEGER)
    @NotNull
    String TIME = "time";
}
