package com.github.denisura.mordor.data.database;


import net.simonvt.schematic.annotation.Check;
import net.simonvt.schematic.annotation.ConflictResolutionType;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;


public interface ProfileColumns {

    String CREDIT_SCORE_BUCKET_VERY_HIGH = "VeryHigh";
    String CREDIT_SCORE_BUCKET_HIGH = "High";
    String CREDIT_SCORE_BUCKET_LOW = "Low";

    String LOAN_TO_VALUE_BUCKET_VERY_HIGH = "VeryHigh";
    String LOAN_TO_VALUE_BUCKET_HIGH = "High";
    String LOAN_TO_VALUE_BUCKET_NORMAL = "Normal";

    String PROGRAM_FIXED_30_YEAR = "Fixed30Year";
    String PROGRAM_FIXED_20_YEAR = "Fixed20Year";
    String PROGRAM_FIXED_15_YEAR = "Fixed15Year";
    String PROGRAM_ARM5 = "ARM5";
    String PROGRAM_ARM7 = "ARM7";

    String LOAN_CATEGORY_PURCHASE = "Purchase";
    String LOAN_CATEGORY_REFINANCE = "Refinance";

    String TREND_UP = "up";
    String TREND_SAME = "same";
    String TREND_DOWN = "down";

    @DataType(INTEGER)
    @PrimaryKey(onConflict = ConflictResolutionType.REPLACE)
    String _ID = "_id";

    @DataType(TEXT)
    @NotNull
    @Check(ProfileColumns.CREDIT_SCORE_BUCKET + " IN ('"
            + ProfileColumns.CREDIT_SCORE_BUCKET_VERY_HIGH + "', '"
            + ProfileColumns.CREDIT_SCORE_BUCKET_HIGH + "', '"
            + ProfileColumns.CREDIT_SCORE_BUCKET_LOW + "')")
    String CREDIT_SCORE_BUCKET = "credit_score_bucket";

    @DataType(TEXT)
    @NotNull
    @Check(ProfileColumns.LOAN_TO_VALUE_BUCKET + " IN ('"
            + ProfileColumns.LOAN_TO_VALUE_BUCKET_VERY_HIGH + "', '"
            + ProfileColumns.LOAN_TO_VALUE_BUCKET_HIGH + "', '"
            + ProfileColumns.LOAN_TO_VALUE_BUCKET_NORMAL + "')")
    String LOAN_TO_VALUE_BUCKET = "loan_to_value_bucket";

    @DataType(TEXT)
    @NotNull
    @Check(ProfileColumns.PROGRAM + " IN ('"
            + ProfileColumns.PROGRAM_FIXED_30_YEAR + "', '"
            + ProfileColumns.PROGRAM_FIXED_20_YEAR + "', '"
            + ProfileColumns.PROGRAM_FIXED_15_YEAR + "', '"
            + ProfileColumns.PROGRAM_ARM5 + "', '"
            + ProfileColumns.PROGRAM_ARM7 + "')")
    String PROGRAM = "program";

    @DataType(TEXT)
    @NotNull
    String STATE = "state";


    @DataType(TEXT)
    @NotNull
    @Check(ProfileColumns.LOAN_CATEGORY + " IN ('"
            + ProfileColumns.LOAN_CATEGORY_PURCHASE + "', '"
            + ProfileColumns.LOAN_CATEGORY_REFINANCE + "')")
    String LOAN_CATEGORY = "loan_category";

    @DataType(TEXT)
    @Check(ProfileColumns.TREND + " IN ('"
            + ProfileColumns.TREND_UP + "', '"
            + ProfileColumns.TREND_SAME + "', '"
            + ProfileColumns.TREND_DOWN + "')")
    String TREND = "trend";

    @DataType(REAL)
    String CURRENT_RATE = "current_rate";
}
