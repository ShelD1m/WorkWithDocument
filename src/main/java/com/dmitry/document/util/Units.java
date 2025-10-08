package com.dmitry.document.util;

public final class Units {
    private Units() {}
    /** миллиметры в twips (1/1440 inch). 1 inch = 25.4 mm, 1440 twips/inch => 56.692913... twips/mm */
    public static int mmToTwips(double mm) {
        return (int)Math.round(mm * 56.6929133858);
    }
}
