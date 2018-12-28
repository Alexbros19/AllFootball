package com.alexbros.opidlubnyi.allfootball.models;

public class CampaignOdd extends Campaign {
    public static final int TYPE_PRE_MATCH = 0;
    public static final int TYPE_1_X_2 = 1;
    public static final int TYPE_NEXT_TO_SCORE = 25;

    public static final int TEXT_COLOR_NONE = -1;

    public int oddType = TYPE_PRE_MATCH;

    public String odd1String = "";
    public String odd2String = "";
    public String oddXString = "";

    public float odd1 = 0;
    public float odd2 = 0;
    public float oddX = 0;

    public int textColor = TEXT_COLOR_NONE;

    public boolean isLive = false;
    public long timestamp;
    public int prioritization;

    public String publisherImageName = "";

    //Over/Under 1.5
//    public String oddOver15String = "";
//    public String oddUnder15String = "";
//
//    public float oddOver15Float = 0;
//    public float oddUnder15Float = 0;
//
//    public String oddOver15URL = "";
//    public String oddUnder15URL = "";

    //Over/Under 2.5
//    public String oddOver25String = "";
//    public String oddUnder25String = "";
//
//    public float oddOver25Float = 0;
//    public float oddUnder25Float = 0;
//
//    public String oddOver25URL = "";
//    public String oddUnder25URL = "";

    //Over/Under 3.5
//    public String oddOver35String = "";
//    public String oddUnder35String = "";
//
//    public float oddOver35Float = 0;
//    public float oddUnder35Float = 0;
//
//    public String oddOver35URL = "";
//    public String oddUnder35URL = "";

    //Double Chance
//    public String odd1XString = "";
//    public String odd2XString = "";
//
//    public float odd1XFloat = 0;
//    public float odd2XFloat = 0;
//
//    public String odd1XURL = "";
//    public String odd2XURL = "";

    //Both to score
//    public String oddYesString = "";
//    public String oddNoString = "";
//
//    public float oddYesFloat = 0;
//    public float oddNoFloat = 0;
//
//    public String oddYesURL = "";
//    public String oddNoURL = "";

//    public boolean checkOddType(int type) {
//        switch (type) {
//            case 0:
//                return (odd1 != 0 || oddX != 0 || odd2 != 0);
//            case 1:
//                return (oddOver15Float != 0 || oddUnder15Float != 0);
//            case 2:
//                return (oddOver25Float != 0 || oddUnder25Float != 0);
//            case 3:
//                return (oddOver35Float != 0 || oddUnder35Float != 0);
//            case 4:
//                return  (odd1XFloat != 0 || odd2XFloat != 0);
//            case 5:
//                return (oddYesFloat != 0 || oddNoFloat != 0);
//            default:
//                return false;
//        }
//    }

// MOVE MODEL
//    public static void setOddButtonStyle(Context context, Button btn, String url) {
//        if (url.equals("")) {
//            btn.setBackgroundColor(Color.TRANSPARENT);
//            if (PreferenceHelper.isAppThemeWhite(context))
//                btn.setTextColor(Colors.textColorThird);
//        } else
//            btn.setOnClickListener(new OnOddBtnClickListener(url, context));
//    }
}
