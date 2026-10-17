# Uploaded home package integration

Uses the user's `NepaliTimePass_Home_Package(1).zip` artwork and SetupScreen implementation, adapted on main aa056f7. All 14 compressed WebP assets are preserved. Four existing categories remain selectable; `cat_other` and six additional gang badges are spare assets, not new enum values or persisted emblem choices.

The existing callback signature, editable team names/colors, language switching, movie library, timer settings and game transitions remain intact. Added responsive stacked team cards for compact phones/large fonts, 48dp color targets, accessible category/duration/language state, scrollable dialogs and short interaction animations. Sticky Start remains outside the content scroll.

Also restores home/icon bitmap resources already referenced by main, executable Gradle wrapper, and the standard generated Android debug key in place of a missing repository debug.keystore. Existing timer, poster and scoring code is unchanged.

Device checks remain necessary for keyboard/insets, TalkBack, very large fonts, and physical audio/notification behavior.

## Compact setup and final score follow-up

Standard portrait phones now use a compact single-page layout: four category cards in one row, a duration dropdown, compact team cards that open name/color editors, and a library shortcut. The previous editable callback contract is retained. Small heights, landscape and accessibility font scaling use a scrolling fallback instead of clipping controls. Keyboard/inset padding protects the main action.

The final score screen uses a new generated, text-free `winner_stage.webp` background inspired by the supplied image(7).png. Emblems, gang names, counts, crown, winner/draw labels and actions remain native and bilingual. Replays/setup actions are pinned; match history expands on demand. Existing one-shot confetti finishes after 1.4 seconds; no idle animation loop is added. Test coverage includes team-two wins, ties, history expansion and compact setup controls.

Dialog widths are explicitly responsive (92% of the available width, capped at 560dp), avoiding platform-width oscillation under Robolectric's explicit phone-size qualifiers while preserving native dialog semantics. See Robolectric issue #8460: https://github.com/robolectric/robolectric/issues/8460. No tests are skipped or timeouts extended.

## Victory and handoff follow-up

The up-next screen reuses the illustrated gang badges, with a 136dp active badge, compact scores, and a pinned one-tap reveal/start action. The winner uses localized transparent gold brush title art with a red underline, native live names/scores, and a single 1.6-second localized badge burst that pauses outside the foreground. No continuous celebration animation is used.

Title art generated with the built-in image generation tool. Prompt: transparent mobile game title, exact WINNER! / विजेता!, gold foil brush lettering with red paint underline, no scene or other text. Optimized WebPs: app/src/main/res/drawable-nodpi/winner_title_en.webp and winner_title_ne.webp.
