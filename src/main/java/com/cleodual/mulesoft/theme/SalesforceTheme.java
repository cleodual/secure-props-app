package com.cleodual.mulesoft.theme;

import java.awt.Color;

/**
 * Salesforce modern design theme with Lightning Design System colors.
 * Provides centralized color palette, typography, and spacing constants.
 */
public class SalesforceTheme {
    // ==================== PRIMARY COLORS ====================
    // Salesforce brand colors
    public static final Color BRAND_BLUE = new Color(0, 112, 210);           // #0070D2
    public static final Color BRAND_BLUE_LIGHT = new Color(0, 161, 223);    // #00A1DF
    public static final Color BRAND_BLUE_DARK = new Color(0, 45, 117);      // #002D75

    // ==================== BACKGROUND COLORS ====================
    public static final Color BG_PRIMARY = new Color(15, 16, 18);           // Deep navy background
    public static final Color BG_SECONDARY = new Color(23, 25, 29);         // Slightly lighter navy
    public static final Color BG_SURFACE = new Color(31, 33, 37);           // Surface level
    public static final Color BG_SURFACE_ALT = new Color(40, 43, 48);       // Alternative surface
    public static final Color BG_HOVER = new Color(50, 54, 60);             // Hover state

    // ==================== EDITOR COLORS ====================
    public static final Color EDITOR_BG = new Color(19, 20, 24);            // Editor background
    public static final Color OUTPUT_BG = new Color(25, 32, 30);            // Output background (slight teal tint)

    // ==================== TEXT COLORS ====================
    public static final Color TEXT_PRIMARY = new Color(235, 237, 241);      // Primary text (white-ish)
    public static final Color TEXT_SECONDARY = new Color(169, 172, 178);    // Secondary text (gray)
    public static final Color TEXT_TERTIARY = new Color(128, 132, 139);     // Tertiary text (darker gray)
    public static final Color TEXT_MUTED = new Color(102, 105, 112);        // Muted text

    // ==================== ACCENT COLORS ====================
    public static final Color ACCENT_SUCCESS = new Color(75, 192, 128);     // Green for success
    public static final Color ACCENT_ERROR = new Color(235, 85, 85);        // Red for errors
    public static final Color ACCENT_WARNING = new Color(255, 171, 0);      // Orange for warnings
    public static final Color ACCENT_INFO = new Color(0, 161, 223);         // Light blue for info

    // ==================== BORDER & DIVIDER COLORS ====================
    public static final Color BORDER_PRIMARY = new Color(73, 77, 84);       // Primary borders
    public static final Color BORDER_SECONDARY = new Color(57, 60, 65);     // Secondary borders
    public static final Color DIVIDER = new Color(50, 53, 59);              // Divider lines

    // ==================== INTERACTIVE ELEMENT COLORS ====================
    public static final Color INPUT_BG = new Color(24, 26, 31);             // Input field background
    public static final Color INPUT_BORDER = new Color(76, 81, 89);         // Input field border
    public static final Color INPUT_BORDER_FOCUS = BRAND_BLUE;              // Input focus border
    public static final Color BUTTON_BG_PRIMARY = BRAND_BLUE;               // Primary button
    public static final Color BUTTON_BG_SECONDARY = new Color(62, 66, 72);  // Secondary button
    public static final Color BUTTON_TEXT = TEXT_PRIMARY;                   // Button text

    // ==================== TYPOGRAPHY ====================
    public static final String FONT_FAMILY_SANS = "Segoe UI";
    public static final String FONT_FAMILY_MONO = "Consolas";

    // Font sizes
    public static final int FONT_SIZE_XL = 26;       // Titles
    public static final int FONT_SIZE_LG = 15;       // Panel headers
    public static final int FONT_SIZE_BASE = 13;     // Regular text
    public static final int FONT_SIZE_SM = 12;       // Small text
    public static final int FONT_SIZE_XS = 11;       // Extra small

    // Font weights (using Font constants)
    // BOLD = 1, PLAIN = 0, ITALIC = 2

    // ==================== SPACING ====================
    public static final int SPACING_XS = 4;
    public static final int SPACING_SM = 8;
    public static final int SPACING_MD = 12;
    public static final int SPACING_LG = 14;
    public static final int SPACING_XL = 16;
    public static final int SPACING_2XL = 20;

    // ==================== SIZING ====================
    public static final int BUTTON_HEIGHT_PRIMARY = 42;
    public static final int BUTTON_HEIGHT_SECONDARY = 40;
    public static final int BUTTON_HEIGHT_COMPACT = 36;
    public static final int BUTTON_WIDTH_COMPACT = 84;
    public static final int INPUT_HEIGHT = 34;
    public static final int COMPONENT_MIN_WIDTH = 1180;
    public static final int COMPONENT_MIN_HEIGHT = 760;

    // ==================== BORDER RADIUS ====================
    public static final int BORDER_RADIUS = 8;
    public static final int BORDER_RADIUS_SM = 4;

    // ==================== SHADOWS & EFFECTS ====================
    public static final int SHADOW_BLUR = 12;
    public static final int SHADOW_OFFSET = 2;

    // ==================== OPACITY ====================
    public static final float OPACITY_DISABLED = 0.5f;
    public static final float OPACITY_HOVER = 0.9f;
    public static final float OPACITY_ACTIVE = 1.0f;
}

