# Uploaded home package integration

Uses the user's `NepaliTimePass_Home_Package(1).zip` artwork and SetupScreen implementation, adapted on main aa056f7. All 14 compressed WebP assets are preserved. Four existing categories remain selectable; `cat_other` and six additional gang badges are spare assets, not new enum values or persisted emblem choices.

The existing callback signature, editable team names/colors, language switching, movie library, timer settings and game transitions remain intact. Added responsive stacked team cards for compact phones/large fonts, 48dp color targets, accessible category/duration/language state, scrollable dialogs and short interaction animations. Sticky Start remains outside the content scroll.

Also restores home/icon bitmap resources already referenced by main, executable Gradle wrapper, and the standard generated Android debug key in place of a missing repository debug.keystore. Existing timer, poster and scoring code is unchanged.

Device checks remain necessary for keyboard/insets, TalkBack, very large fonts, and physical audio/notification behavior.
