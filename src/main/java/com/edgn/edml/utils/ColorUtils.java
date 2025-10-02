package com.edgn.edml.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class ColorUtils {
    private static final Map<String, Integer> NAMED_COLORS = new HashMap<>();
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^#[0-9A-Fa-f]{3,8}$");

    //generated named colors through AIs databases
    static {
        // Transparent
        NAMED_COLORS.put("transparent", 0x00000000);

        // Whites
        NAMED_COLORS.put("aliceblue", 0xFFF0F8FF);
        NAMED_COLORS.put("antiquewhite", 0xFFFAEBD7);
        NAMED_COLORS.put("azure", 0xFFF0FFFF);
        NAMED_COLORS.put("beige", 0xFFF5F5DC);
        NAMED_COLORS.put("blanchedalmond", 0xFFFFEBCD);
        NAMED_COLORS.put("cornsilk", 0xFFFFF8DC);
        NAMED_COLORS.put("floralwhite", 0xFFFFFAF0);
        NAMED_COLORS.put("ghostwhite", 0xFFF8F8FF);
        NAMED_COLORS.put("honeydew", 0xFFF0FFF0);
        NAMED_COLORS.put("ivory", 0xFFFFFFF0);
        NAMED_COLORS.put("lavenderblush", 0xFFFFF0F5);
        NAMED_COLORS.put("lemonchiffon", 0xFFFFFACD);
        NAMED_COLORS.put("linen", 0xFFFAF0E6);
        NAMED_COLORS.put("mintcream", 0xFFF5FFFA);
        NAMED_COLORS.put("mistyrose", 0xFFFFE4E1);
        NAMED_COLORS.put("moccasin", 0xFFFFE4B5);
        NAMED_COLORS.put("navajowhite", 0xFFFFDEAD);
        NAMED_COLORS.put("oldlace", 0xFFFDF5E6);
        NAMED_COLORS.put("papayawhip", 0xFFFFEFD5);
        NAMED_COLORS.put("peachpuff", 0xFFFFDAB9);
        NAMED_COLORS.put("seashell", 0xFFFFF5EE);
        NAMED_COLORS.put("snow", 0xFFFFFAFA);
        NAMED_COLORS.put("white", 0xFFFFFFFF);
        NAMED_COLORS.put("whitesmoke", 0xFFF5F5F5);

        // Grays
        NAMED_COLORS.put("darkgray", 0xFFA9A9A9);
        NAMED_COLORS.put("darkgrey", 0xFFA9A9A9);
        NAMED_COLORS.put("darkslategray", 0xFF2F4F4F);
        NAMED_COLORS.put("darkslategrey", 0xFF2F4F4F);
        NAMED_COLORS.put("dimgray", 0xFF696969);
        NAMED_COLORS.put("dimgrey", 0xFF696969);
        NAMED_COLORS.put("gainsboro", 0xFFDCDCDC);
        NAMED_COLORS.put("gray", 0xFF808080);
        NAMED_COLORS.put("grey", 0xFF808080);
        NAMED_COLORS.put("lightgray", 0xFFD3D3D3);
        NAMED_COLORS.put("lightgrey", 0xFFD3D3D3);
        NAMED_COLORS.put("lightslategray", 0xFF778899);
        NAMED_COLORS.put("lightslategrey", 0xFF778899);
        NAMED_COLORS.put("silver", 0xFFC0C0C0);
        NAMED_COLORS.put("slategray", 0xFF708090);
        NAMED_COLORS.put("slategrey", 0xFF708090);

        // Blacks
        NAMED_COLORS.put("black", 0xFF000000);

        // Reds
        NAMED_COLORS.put("brown", 0xFFA52A2A);
        NAMED_COLORS.put("crimson", 0xFFDC143C);
        NAMED_COLORS.put("darkred", 0xFF8B0000);
        NAMED_COLORS.put("darksalmon", 0xFFE9967A);
        NAMED_COLORS.put("firebrick", 0xFFB22222);
        NAMED_COLORS.put("indianred", 0xFFCD5C5C);
        NAMED_COLORS.put("lightcoral", 0xFFF08080);
        NAMED_COLORS.put("lightsalmon", 0xFFFFA07A);
        NAMED_COLORS.put("maroon", 0xFF800000);
        NAMED_COLORS.put("red", 0xFFFF0000);
        NAMED_COLORS.put("rosybrown", 0xFFBC8F8F);
        NAMED_COLORS.put("salmon", 0xFFFA8072);

        // Pinks
        NAMED_COLORS.put("deeppink", 0xFFFF1493);
        NAMED_COLORS.put("hotpink", 0xFFFF69B4);
        NAMED_COLORS.put("lightpink", 0xFFFFB6C1);
        NAMED_COLORS.put("mediumvioletred", 0xFFC71585);
        NAMED_COLORS.put("palevioletred", 0xFFDB7093);
        NAMED_COLORS.put("pink", 0xFFFFC0CB);

        // Oranges
        NAMED_COLORS.put("coral", 0xFFFF7F50);
        NAMED_COLORS.put("darkorange", 0xFFFF8C00);
        NAMED_COLORS.put("orange", 0xFFFFA500);
        NAMED_COLORS.put("orangered", 0xFFFF4500);
        NAMED_COLORS.put("sandybrown", 0xFFF4A460);
        NAMED_COLORS.put("tomato", 0xFFFF6347);

        // Yellows
        NAMED_COLORS.put("darkkhaki", 0xFFBDB76B);
        NAMED_COLORS.put("gold", 0xFFFFD700);
        NAMED_COLORS.put("goldenrod", 0xFFDAA520);
        NAMED_COLORS.put("khaki", 0xFFF0E68C);
        NAMED_COLORS.put("lightgoldenrodyellow", 0xFFFAFAD2);
        NAMED_COLORS.put("lightyellow", 0xFFFFFFE0);
        NAMED_COLORS.put("palegoldenrod", 0xFFEEE8AA);
        NAMED_COLORS.put("yellow", 0xFFFFFF00);

        // Greens
        NAMED_COLORS.put("chartreuse", 0xFF7FFF00);
        NAMED_COLORS.put("darkgreen", 0xFF006400);
        NAMED_COLORS.put("darkolivegreen", 0xFF556B2F);
        NAMED_COLORS.put("darkseagreen", 0xFF8FBC8F);
        NAMED_COLORS.put("forestgreen", 0xFF228B22);
        NAMED_COLORS.put("green", 0xFF008000);
        NAMED_COLORS.put("greenyellow", 0xFFADFF2F);
        NAMED_COLORS.put("lawngreen", 0xFF7CFC00);
        NAMED_COLORS.put("lightgreen", 0xFF90EE90);
        NAMED_COLORS.put("lightseagreen", 0xFF20B2AA);
        NAMED_COLORS.put("lime", 0xFF00FF00);
        NAMED_COLORS.put("limegreen", 0xFF32CD32);
        NAMED_COLORS.put("mediumaquamarine", 0xFF66CDAA);
        NAMED_COLORS.put("mediumseagreen", 0xFF3CB371);
        NAMED_COLORS.put("mediumspringgreen", 0xFF00FA9A);
        NAMED_COLORS.put("olivedrab", 0xFF6B8E23);
        NAMED_COLORS.put("palegreen", 0xFF98FB98);
        NAMED_COLORS.put("seagreen", 0xFF2E8B57);
        NAMED_COLORS.put("springgreen", 0xFF00FF7F);
        NAMED_COLORS.put("yellowgreen", 0xFF9ACD32);

        // Cyans
        NAMED_COLORS.put("aqua", 0xFF00FFFF);
        NAMED_COLORS.put("aquamarine", 0xFF7FFFD4);
        NAMED_COLORS.put("cyan", 0xFF00FFFF);
        NAMED_COLORS.put("darkcyan", 0xFF008B8B);
        NAMED_COLORS.put("darkturquoise", 0xFF00CED1);
        NAMED_COLORS.put("lightcyan", 0xFFE0FFFF);
        NAMED_COLORS.put("mediumturquoise", 0xFF48D1CC);
        NAMED_COLORS.put("paleturquoise", 0xFFAFEEEE);
        NAMED_COLORS.put("teal", 0xFF008080);
        NAMED_COLORS.put("turquoise", 0xFF40E0D0);

        // Blues
        NAMED_COLORS.put("blue", 0xFF0000FF);
        NAMED_COLORS.put("cadetblue", 0xFF5F9EA0);
        NAMED_COLORS.put("cornflowerblue", 0xFF6495ED);
        NAMED_COLORS.put("darkblue", 0xFF00008B);
        NAMED_COLORS.put("darkslateblue", 0xFF483D8B);
        NAMED_COLORS.put("deepskyblue", 0xFF00BFFF);
        NAMED_COLORS.put("dodgerblue", 0xFF1E90FF);
        NAMED_COLORS.put("lightblue", 0xFFADD8E6);
        NAMED_COLORS.put("lightskyblue", 0xFF87CEFA);
        NAMED_COLORS.put("lightsteelblue", 0xFFB0C4DE);
        NAMED_COLORS.put("mediumblue", 0xFF0000CD);
        NAMED_COLORS.put("mediumslateblue", 0xFF7B68EE);
        NAMED_COLORS.put("midnightblue", 0xFF191970);
        NAMED_COLORS.put("navy", 0xFF000080);
        NAMED_COLORS.put("powderblue", 0xFFB0E0E6);
        NAMED_COLORS.put("royalblue", 0xFF4169E1);
        NAMED_COLORS.put("skyblue", 0xFF87CEEB);
        NAMED_COLORS.put("slateblue", 0xFF6A5ACD);
        NAMED_COLORS.put("steelblue", 0xFF4682B4);

        // Purples, Violets, and Magentas
        NAMED_COLORS.put("blueviolet", 0xFF8A2BE2);
        NAMED_COLORS.put("darkmagenta", 0xFF8B008B);
        NAMED_COLORS.put("darkorchid", 0xFF9932CC);
        NAMED_COLORS.put("darkviolet", 0xFF9400D3);
        NAMED_COLORS.put("fuchsia", 0xFFFF00FF);
        NAMED_COLORS.put("indigo", 0xFF4B0082);
        NAMED_COLORS.put("lavender", 0xFFE6E6FA);
        NAMED_COLORS.put("magenta", 0xFFFF00FF);
        NAMED_COLORS.put("mediumorchid", 0xFFBA55D3);
        NAMED_COLORS.put("mediumpurple", 0xFF9370DB);
        NAMED_COLORS.put("orchid", 0xFFDA70D6);
        NAMED_COLORS.put("plum", 0xFFDDA0DD);
        NAMED_COLORS.put("purple", 0xFF800080);
        NAMED_COLORS.put("rebeccapurple", 0xFF663399);
        NAMED_COLORS.put("thistle", 0xFFD8BFD8);
        NAMED_COLORS.put("violet", 0xFFEE82EE);

        // Browns
        NAMED_COLORS.put("bisque", 0xFFFFE4C4);
        NAMED_COLORS.put("burlywood", 0xFFDEB887);
        NAMED_COLORS.put("chocolate", 0xFFD2691E);
        NAMED_COLORS.put("darkgoldenrod", 0xFFB8860B);
        NAMED_COLORS.put("olive", 0xFF808000);
        NAMED_COLORS.put("peru", 0xFFCD853F);
        NAMED_COLORS.put("saddlebrown", 0xFF8B4513);
        NAMED_COLORS.put("sienna", 0xFFA0522D);
        NAMED_COLORS.put("tan", 0xFFD2B48C);
        NAMED_COLORS.put("wheat", 0xFFF5DEB3);

        // X11 Extended Colors (over 250 additional colors)
        NAMED_COLORS.put("snow1", 0xFFFFFAFA);
        NAMED_COLORS.put("snow2", 0xFFEEE9E9);
        NAMED_COLORS.put("snow3", 0xFFCDC9C9);
        NAMED_COLORS.put("snow4", 0xFF8B8989);
        NAMED_COLORS.put("antiquewhite1", 0xFFFFEFDB);
        NAMED_COLORS.put("antiquewhite2", 0xFFEEDFCC);
        NAMED_COLORS.put("antiquewhite3", 0xFFCDC0B0);
        NAMED_COLORS.put("antiquewhite4", 0xFF8B8378);
        NAMED_COLORS.put("bisque1", 0xFFFFE4C4);
        NAMED_COLORS.put("bisque2", 0xFFEED5B7);
        NAMED_COLORS.put("bisque3", 0xFFCDB79E);
        NAMED_COLORS.put("bisque4", 0xFF8B7D6B);
        NAMED_COLORS.put("peachpuff1", 0xFFFFDAB9);
        NAMED_COLORS.put("peachpuff2", 0xFFEECBAD);
        NAMED_COLORS.put("peachpuff3", 0xFFCDAF95);
        NAMED_COLORS.put("peachpuff4", 0xFF8B7765);
        NAMED_COLORS.put("navajowhite1", 0xFFFFDEAD);
        NAMED_COLORS.put("navajowhite2", 0xFFEECFA1);
        NAMED_COLORS.put("navajowhite3", 0xFFCDB38B);
        NAMED_COLORS.put("navajowhite4", 0xFF8B795E);
        NAMED_COLORS.put("lemonchiffon1", 0xFFFFFACD);
        NAMED_COLORS.put("lemonchiffon2", 0xFFEEE9BF);
        NAMED_COLORS.put("lemonchiffon3", 0xFFCDC9A5);
        NAMED_COLORS.put("lemonchiffon4", 0xFF8B8970);
        NAMED_COLORS.put("cornsilk1", 0xFFFFF8DC);
        NAMED_COLORS.put("cornsilk2", 0xFFEEE8CD);
        NAMED_COLORS.put("cornsilk3", 0xFFCDC8B1);
        NAMED_COLORS.put("cornsilk4", 0xFF8B8878);
        NAMED_COLORS.put("ivory1", 0xFFFFFFF0);
        NAMED_COLORS.put("ivory2", 0xFFEEEEE0);
        NAMED_COLORS.put("ivory3", 0xFFCDCDC1);
        NAMED_COLORS.put("ivory4", 0xFF8B8B83);
        NAMED_COLORS.put("honeydew1", 0xFFF0FFF0);
        NAMED_COLORS.put("honeydew2", 0xFFE0EEE0);
        NAMED_COLORS.put("honeydew3", 0xFFC1CDC1);
        NAMED_COLORS.put("honeydew4", 0xFF838B83);
        NAMED_COLORS.put("lavenderblush1", 0xFFFFF0F5);
        NAMED_COLORS.put("lavenderblush2", 0xFFEEE0E5);
        NAMED_COLORS.put("lavenderblush3", 0xFFCDC1C5);
        NAMED_COLORS.put("lavenderblush4", 0xFF8B8386);
        NAMED_COLORS.put("mistyrose1", 0xFFFFE4E1);
        NAMED_COLORS.put("mistyrose2", 0xFFEED5D2);
        NAMED_COLORS.put("mistyrose3", 0xFFCDB7B5);
        NAMED_COLORS.put("mistyrose4", 0xFF8B7D7B);
        NAMED_COLORS.put("azure1", 0xFFF0FFFF);
        NAMED_COLORS.put("azure2", 0xFFE0EEEE);
        NAMED_COLORS.put("azure3", 0xFFC1CDCD);
        NAMED_COLORS.put("azure4", 0xFF838B8B);
        NAMED_COLORS.put("slateblue1", 0xFF836FFF);
        NAMED_COLORS.put("slateblue2", 0xFF7A67EE);
        NAMED_COLORS.put("slateblue3", 0xFF6959CD);
        NAMED_COLORS.put("slateblue4", 0xFF473C8B);
        NAMED_COLORS.put("royalblue1", 0xFF4876FF);
        NAMED_COLORS.put("royalblue2", 0xFF436EEE);
        NAMED_COLORS.put("royalblue3", 0xFF3A5FCD);
        NAMED_COLORS.put("royalblue4", 0xFF27408B);
        NAMED_COLORS.put("dodgerblue1", 0xFF1E90FF);
        NAMED_COLORS.put("dodgerblue2", 0xFF1C86EE);
        NAMED_COLORS.put("dodgerblue3", 0xFF1874CD);
        NAMED_COLORS.put("dodgerblue4", 0xFF104E8B);
        NAMED_COLORS.put("steelblue1", 0xFF63B8FF);
        NAMED_COLORS.put("steelblue2", 0xFF5CACEE);
        NAMED_COLORS.put("steelblue3", 0xFF4F94CD);
        NAMED_COLORS.put("steelblue4", 0xFF36648B);
        NAMED_COLORS.put("deepskyblue1", 0xFF00BFFF);
        NAMED_COLORS.put("deepskyblue2", 0xFF00B2EE);
        NAMED_COLORS.put("deepskyblue3", 0xFF009ACD);
        NAMED_COLORS.put("deepskyblue4", 0xFF00688B);
        NAMED_COLORS.put("skyblue1", 0xFF87CEFF);
        NAMED_COLORS.put("skyblue2", 0xFF7EC0EE);
        NAMED_COLORS.put("skyblue3", 0xFF6CA6CD);
        NAMED_COLORS.put("skyblue4", 0xFF4A708B);
        NAMED_COLORS.put("lightskyblue1", 0xFFB0E2FF);
        NAMED_COLORS.put("lightskyblue2", 0xFFA4D3EE);
        NAMED_COLORS.put("lightskyblue3", 0xFF8DB6CD);
        NAMED_COLORS.put("lightskyblue4", 0xFF607B8B);
        NAMED_COLORS.put("slategray1", 0xFFC6E2FF);
        NAMED_COLORS.put("slategray2", 0xFFB9D3EE);
        NAMED_COLORS.put("slategray3", 0xFF9FB6CD);
        NAMED_COLORS.put("slategray4", 0xFF6C7B8B);
        NAMED_COLORS.put("lightsteelblue1", 0xFFCAE1FF);
        NAMED_COLORS.put("lightsteelblue2", 0xFFBCD2EE);
        NAMED_COLORS.put("lightsteelblue3", 0xFFA2B5CD);
        NAMED_COLORS.put("lightsteelblue4", 0xFF6E7B8B);
        NAMED_COLORS.put("lightblue1", 0xFFBFEFFF);
        NAMED_COLORS.put("lightblue2", 0xFFB2DFEE);
        NAMED_COLORS.put("lightblue3", 0xFF9AC0CD);
        NAMED_COLORS.put("lightblue4", 0xFF68838B);
        NAMED_COLORS.put("lightcyan1", 0xFFE0FFFF);
        NAMED_COLORS.put("lightcyan2", 0xFFD1EEEE);
        NAMED_COLORS.put("lightcyan3", 0xFFB4CDCD);
        NAMED_COLORS.put("lightcyan4", 0xFF7A8B8B);
        NAMED_COLORS.put("paleturquoise1", 0xFFBBFFFF);
        NAMED_COLORS.put("paleturquoise2", 0xFFAEEEE);
        NAMED_COLORS.put("paleturquoise3", 0xFF96CDCD);
        NAMED_COLORS.put("paleturquoise4", 0xFF668B8B);
        NAMED_COLORS.put("cadetblue1", 0xFF98F5FF);
        NAMED_COLORS.put("cadetblue2", 0xFF8EE5EE);
        NAMED_COLORS.put("cadetblue3", 0xFF7AC5CD);
        NAMED_COLORS.put("cadetblue4", 0xFF53868B);
        NAMED_COLORS.put("turquoise1", 0xFF00F5FF);
        NAMED_COLORS.put("turquoise2", 0xFF00E5EE);
        NAMED_COLORS.put("turquoise3", 0xFF00C5CD);
        NAMED_COLORS.put("turquoise4", 0xFF00868B);
        NAMED_COLORS.put("cyan1", 0xFF00FFFF);
        NAMED_COLORS.put("cyan2", 0xFF00EEEE);
        NAMED_COLORS.put("cyan3", 0xFF00CDCD);
        NAMED_COLORS.put("cyan4", 0xFF008B8B);
        NAMED_COLORS.put("darkslategray1", 0xFF97FFFF);
        NAMED_COLORS.put("darkslategray2", 0xFF8DEEEE);
        NAMED_COLORS.put("darkslategray3", 0xFF79CDCD);
        NAMED_COLORS.put("darkslategray4", 0xFF528B8B);
        NAMED_COLORS.put("aquamarine1", 0xFF7FFFD4);
        NAMED_COLORS.put("aquamarine2", 0xFF76EEC6);
        NAMED_COLORS.put("aquamarine3", 0xFF66CDAA);
        NAMED_COLORS.put("aquamarine4", 0xFF458B74);
        NAMED_COLORS.put("darkseagreen1", 0xFFC1FFC1);
        NAMED_COLORS.put("darkseagreen2", 0xFFB4EEB4);
        NAMED_COLORS.put("darkseagreen3", 0xFF9BCD9B);
        NAMED_COLORS.put("darkseagreen4", 0xFF698B69);
        NAMED_COLORS.put("seagreen1", 0xFF54FF9F);
        NAMED_COLORS.put("seagreen2", 0xFF4EEE94);
        NAMED_COLORS.put("seagreen3", 0xFF43CD80);
        NAMED_COLORS.put("seagreen4", 0xFF2E8B57);
        NAMED_COLORS.put("palegreen1", 0xFF9AFF9A);
        NAMED_COLORS.put("palegreen2", 0xFF90EE90);
        NAMED_COLORS.put("palegreen3", 0xFF7CCD7C);
        NAMED_COLORS.put("palegreen4", 0xFF548B54);
        NAMED_COLORS.put("springgreen1", 0xFF00FF7F);
        NAMED_COLORS.put("springgreen2", 0xFF00EE76);
        NAMED_COLORS.put("springgreen3", 0xFF00CD66);
        NAMED_COLORS.put("springgreen4", 0xFF008B45);
        NAMED_COLORS.put("green1", 0xFF00FF00);
        NAMED_COLORS.put("green2", 0xFF00EE00);
        NAMED_COLORS.put("green3", 0xFF00CD00);
        NAMED_COLORS.put("green4", 0xFF008B00);
        NAMED_COLORS.put("chartreuse1", 0xFF7FFF00);
        NAMED_COLORS.put("chartreuse2", 0xFF76EE00);
        NAMED_COLORS.put("chartreuse3", 0xFF66CD00);
        NAMED_COLORS.put("chartreuse4", 0xFF458B00);
        NAMED_COLORS.put("olivedrab1", 0xFFC0FF3E);
        NAMED_COLORS.put("olivedrab2", 0xFFB3EE3A);
        NAMED_COLORS.put("olivedrab3", 0xFF9ACD32);
        NAMED_COLORS.put("olivedrab4", 0xFF698B22);
        NAMED_COLORS.put("darkolivegreen1", 0xFFCAFF70);
        NAMED_COLORS.put("darkolivegreen2", 0xFFBCEE68);
        NAMED_COLORS.put("darkolivegreen3", 0xFFA2CD5A);
        NAMED_COLORS.put("darkolivegreen4", 0xFF6E8B3D);
        NAMED_COLORS.put("khaki1", 0xFFFFF68F);
        NAMED_COLORS.put("khaki2", 0xFFEEE685);
        NAMED_COLORS.put("khaki3", 0xFFCDC673);
        NAMED_COLORS.put("khaki4", 0xFF8B864E);
        NAMED_COLORS.put("lightgoldenrod1", 0xFFFFEC8B);
        NAMED_COLORS.put("lightgoldenrod2", 0xFFEEDC82);
        NAMED_COLORS.put("lightgoldenrod3", 0xFFCDBE70);
        NAMED_COLORS.put("lightgoldenrod4", 0xFF8B814C);
        NAMED_COLORS.put("lightyellow1", 0xFFFFFFE0);
        NAMED_COLORS.put("lightyellow2", 0xFFEEEED1);
        NAMED_COLORS.put("lightyellow3", 0xFFCDCDB4);
        NAMED_COLORS.put("lightyellow4", 0xFF8B8B7A);
        NAMED_COLORS.put("yellow1", 0xFFFFFF00);
        NAMED_COLORS.put("yellow2", 0xFFEEEE00);
        NAMED_COLORS.put("yellow3", 0xFFCDCD00);
        NAMED_COLORS.put("yellow4", 0xFF8B8B00);
        NAMED_COLORS.put("gold1", 0xFFFFD700);
        NAMED_COLORS.put("gold2", 0xFFEEC900);
        NAMED_COLORS.put("gold3", 0xFFCDAD00);
        NAMED_COLORS.put("gold4", 0xFF8B7500);
        NAMED_COLORS.put("goldenrod1", 0xFFFFC125);
        NAMED_COLORS.put("goldenrod2", 0xFFEEB422);
        NAMED_COLORS.put("goldenrod3", 0xFFCD9B1D);
        NAMED_COLORS.put("goldenrod4", 0xFF8B6914);
        NAMED_COLORS.put("darkgoldenrod1", 0xFFFFB90F);
        NAMED_COLORS.put("darkgoldenrod2", 0xFFEEAD0E);
        NAMED_COLORS.put("darkgoldenrod3", 0xFFCD950C);
        NAMED_COLORS.put("darkgoldenrod4", 0xFF8B6508);
        NAMED_COLORS.put("rosybrown1", 0xFFFFC1C1);
        NAMED_COLORS.put("rosybrown2", 0xFFEEB4B4);
        NAMED_COLORS.put("rosybrown3", 0xFFCD9B9B);
        NAMED_COLORS.put("rosybrown4", 0xFF8B6969);
        NAMED_COLORS.put("indianred1", 0xFFFF6A6A);
        NAMED_COLORS.put("indianred2", 0xFFEE6363);
        NAMED_COLORS.put("indianred3", 0xFFCD5555);
        NAMED_COLORS.put("indianred4", 0xFF8B3A3A);
        NAMED_COLORS.put("sienna1", 0xFFFF8247);
        NAMED_COLORS.put("sienna2", 0xFFEE7942);
        NAMED_COLORS.put("sienna3", 0xFFCD6839);
        NAMED_COLORS.put("sienna4", 0xFF8B4726);
        NAMED_COLORS.put("salmon1", 0xFFFF8C69);
        NAMED_COLORS.put("salmon2", 0xFFEE8262);
        NAMED_COLORS.put("salmon3", 0xFFCD7054);
        NAMED_COLORS.put("salmon4", 0xFF8B4C39);
        NAMED_COLORS.put("orangered1", 0xFFFF4500);
        NAMED_COLORS.put("orangered2", 0xFFEE4000);
        NAMED_COLORS.put("orangered3", 0xFFCD3700);
        NAMED_COLORS.put("orangered4", 0xFF8B2500);
        NAMED_COLORS.put("red1", 0xFFFF0000);
        NAMED_COLORS.put("red2", 0xFFEE0000);
        NAMED_COLORS.put("red3", 0xFFCD0000);
        NAMED_COLORS.put("red4", 0xFF8B0000);
        NAMED_COLORS.put("deeppink1", 0xFFFF1493);
        NAMED_COLORS.put("deeppink2", 0xFFEE1289);
        NAMED_COLORS.put("deeppink3", 0xFFCD1076);
        NAMED_COLORS.put("deeppink4", 0xFF8B0A50);
        NAMED_COLORS.put("hotpink1", 0xFFFF6EB4);
        NAMED_COLORS.put("hotpink2", 0xFFEE6AA7);
        NAMED_COLORS.put("hotpink3", 0xFFCD6090);
        NAMED_COLORS.put("hotpink4", 0xFF8B3A62);
        NAMED_COLORS.put("pink1", 0xFFFFB5C5);
        NAMED_COLORS.put("pink2", 0xFFEEA9B8);
        NAMED_COLORS.put("pink3", 0xFFCD919E);
        NAMED_COLORS.put("pink4", 0xFF8B636C);
        NAMED_COLORS.put("lightpink1", 0xFFFFAEB9);
        NAMED_COLORS.put("lightpink2", 0xFFEEA2AD);
        NAMED_COLORS.put("lightpink3", 0xFFCD8C95);
        NAMED_COLORS.put("lightpink4", 0xFF8B5F65);
        NAMED_COLORS.put("palevioletred1", 0xFFFF82AB);
        NAMED_COLORS.put("palevioletred2", 0xFFEE799F);
        NAMED_COLORS.put("palevioletred3", 0xFFCD6889);
        NAMED_COLORS.put("palevioletred4", 0xFF8B475D);
        NAMED_COLORS.put("maroon1", 0xFFFF34B3);
        NAMED_COLORS.put("maroon2", 0xFFEE30A7);
        NAMED_COLORS.put("maroon3", 0xFFCD2990);
        NAMED_COLORS.put("maroon4", 0xFF8B1C62);
        NAMED_COLORS.put("violetred1", 0xFFFF3E96);
        NAMED_COLORS.put("violetred2", 0xFFEE3A8C);
        NAMED_COLORS.put("violetred3", 0xFFCD3278);
        NAMED_COLORS.put("violetred4", 0xFF8B2252);
        NAMED_COLORS.put("magenta1", 0xFFFF00FF);
        NAMED_COLORS.put("magenta2", 0xFFEE00EE);
        NAMED_COLORS.put("magenta3", 0xFFCD00CD);
        NAMED_COLORS.put("magenta4", 0xFF8B008B);
        NAMED_COLORS.put("orchid1", 0xFFFF83FA);
        NAMED_COLORS.put("orchid2", 0xFFEE7AE9);
        NAMED_COLORS.put("orchid3", 0xFFCD69C9);
        NAMED_COLORS.put("orchid4", 0xFF8B4789);
        NAMED_COLORS.put("plum1", 0xFFFFBBFF);
        NAMED_COLORS.put("plum2", 0xFFEEAEFF);
        NAMED_COLORS.put("plum3", 0xFFCD96CD);
        NAMED_COLORS.put("plum4", 0xFF8B668B);
        NAMED_COLORS.put("mediumorchid1", 0xFFE066FF);
        NAMED_COLORS.put("mediumorchid2", 0xFFD15FEE);
        NAMED_COLORS.put("mediumorchid3", 0xFFB452CD);
        NAMED_COLORS.put("mediumorchid4", 0xFF7A378B);
        NAMED_COLORS.put("darkorchid1", 0xFFBF3EFF);
        NAMED_COLORS.put("darkorchid2", 0xFFB23AEE);
        NAMED_COLORS.put("darkorchid3", 0xFF9A32CD);
        NAMED_COLORS.put("darkorchid4", 0xFF68228B);
        NAMED_COLORS.put("purple1", 0xFF9B30FF);
        NAMED_COLORS.put("purple2", 0xFF912CEE);
        NAMED_COLORS.put("purple3", 0xFF7D26CD);
        NAMED_COLORS.put("purple4", 0xFF551A8B);
        NAMED_COLORS.put("mediumpurple1", 0xFFAB82FF);
        NAMED_COLORS.put("mediumpurple2", 0xFF9F79EE);
        NAMED_COLORS.put("mediumpurple3", 0xFF8968CD);
        NAMED_COLORS.put("mediumpurple4", 0xFF5D478B);
        NAMED_COLORS.put("thistle1", 0xFFFFE1FF);
        NAMED_COLORS.put("thistle2", 0xFFEED2EE);
        NAMED_COLORS.put("thistle3", 0xFFCDB5CD);
        NAMED_COLORS.put("thistle4", 0xFF8B7B8B);
    }


    private ColorUtils() {}

    public static int parseColor(String colorStr) {
        if (colorStr == null || colorStr.isBlank()) {
            return 0xFF666666;
        }
        String color = colorStr.toLowerCase().trim();
        if (color.startsWith("#")) {
            return parseHexColor(color);
        }
        return NAMED_COLORS.getOrDefault(color, 0xFF666666);
    }

    public static int parseHexColor(String hex) {
        try {
            int len = hex.length();
            int alpha = 0xFF;
            int rgb;
            if (len == 4) { // #RGB
                rgb = Integer.parseInt(hex.substring(1), 16);
                rgb = ((rgb & 0xF00) << 8) | ((rgb & 0x0F0) << 4) | (rgb & 0x00F);
                rgb |= (rgb << 4);
            } else if (len == 5) { // #RGBA
                rgb = Integer.parseInt(hex.substring(1, 4), 16);
                rgb = ((rgb & 0xF00) << 8) | ((rgb & 0x0F0) << 4) | (rgb & 0x00F);
                rgb |= (rgb << 4);
                alpha = Integer.parseInt(hex.substring(4), 16) * 0x11;
            } else if (len == 7) { // #RRGGBB
                rgb = Integer.parseInt(hex.substring(1), 16);
            } else if (len == 9) { // #RRGGBBAA
                rgb = Integer.parseInt(hex.substring(1, 7), 16);
                alpha = Integer.parseInt(hex.substring(7), 16);
            } else {
                return 0xFF666666;
            }
            return (Math.clamp(alpha, 0, 0xFF) << 24) | (rgb & 0xFFFFFF);
        } catch (Exception e) {
            return 0xFF666666;
        }
    }

    public static boolean isNamedColor(String value) {
        return NAMED_COLORS.containsKey(value.toLowerCase().trim());
    }

    public static boolean isHexColor(String value) {
        return HEX_COLOR_PATTERN.matcher(value).matches();
    }

    public static void registerNamedColor(String name, int color) {
        NAMED_COLORS.put(name.toLowerCase().trim(), color);
    }
}
